/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.clementlevallois.umigon.heuristics.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsHashtagNegativeSentiment;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsHashtagPositiveSentiment;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsInHashtag;
import net.clementlevallois.umigon.heuristics.lexicons.multilingual.PlaceHolderMULTI;
import net.clementlevallois.umigon.model.BooleanCondition;
import net.clementlevallois.umigon.model.Category;
import net.clementlevallois.umigon.model.NGram;
import net.clementlevallois.umigon.model.Punctuation;
import net.clementlevallois.umigon.model.ResultOneHeuristics;
import net.clementlevallois.umigon.model.TermWithConditionalExpressions;

/**
 *
 * @author LEVALLOIS
 */
public class PunctuationValenceVerifier {

    private static Set<String> punctuationSignsWithPositiveValence;
    private static Set<String> punctuationSignsWithNegativeValence;

    public static void load() {
        if (punctuationSignsWithPositiveValence != null && punctuationSignsWithNegativeValence != null) {
            return;
        }
        punctuationSignsWithPositiveValence = new HashSet();
        punctuationSignsWithNegativeValence = new HashSet();

        try ( // we load the punctuation signs and their valence
                InputStream inputStream = PlaceHolderMULTI.class.getResourceAsStream("punctuation_signs.txt")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            List<String> patternsOfInterestAsTSV = br.lines().collect(Collectors.toList());
            for (String patternOfInterestAsTSV : patternsOfInterestAsTSV) {
                String[] elements = patternOfInterestAsTSV.split("\t");
                if (elements.length < 2) {
                    System.out.println("error in an punctuatiin line, too short:");
                    System.out.println(patternOfInterestAsTSV);
                }
                switch (elements[1]) {

                    case "positive":
                        punctuationSignsWithPositiveValence.add(elements[0]);
                        break;
                    case "negative":
                        punctuationSignsWithNegativeValence.add(elements[0]);
                        break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static List<ResultOneHeuristics> check(Punctuation punctuation) {
        List<ResultOneHeuristics> resultsHeuristics = new ArrayList();

        if (punctuationSignsWithPositiveValence.contains(punctuation.getOriginalForm())) {
            ResultOneHeuristics resultOneHeuristics = new ResultOneHeuristics(Category.CategoryEnum._11, punctuation);
            resultsHeuristics.add(resultOneHeuristics);
        }
        if (punctuationSignsWithNegativeValence.contains(punctuation.getOriginalForm())) {
            ResultOneHeuristics resultOneHeuristics = new ResultOneHeuristics(Category.CategoryEnum._12, punctuation);
            resultsHeuristics.add(resultOneHeuristics);
        }
        return resultsHeuristics;

    }

}
