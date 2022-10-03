/*
 * author: Clément Levallois
 */
package net.clementlevallois.umigon.explain.controller;

import java.util.ArrayList;
import java.util.List;
import net.clementlevallois.umigon.model.Category;
import net.clementlevallois.umigon.model.Category.CategoryEnum;
import net.clementlevallois.umigon.model.Decision;
import net.clementlevallois.umigon.model.Document;
import net.clementlevallois.umigon.model.Hashtag;
import net.clementlevallois.umigon.model.NGram;
import net.clementlevallois.umigon.model.NonWord;
import net.clementlevallois.umigon.model.ResultOneHeuristics;
import net.clementlevallois.umigon.model.Term;
import net.clementlevallois.umigon.model.TextFragment;

/**
 *
 * @author LEVALLOIS
 */
public class HtmlHighlighter {

    public static String underline(Document doc) {
        StringBuilder sb = new StringBuilder();

        List<ResultOneHeuristics> resultsOfHeuristicsIncludingRemovedOnes = new ArrayList(doc.getResultsOfHeuristics());
        List<Decision> sentimentDecisions = doc.getSentimentDecisions();
        for (Decision decision : sentimentDecisions) {
            for (ResultOneHeuristics oneHeuristics : decision.getListOfHeuristicsImpacted()) {
                resultsOfHeuristicsIncludingRemovedOnes.add(oneHeuristics);
            }
        }

        List<TextFragment> allTextFragments = doc.getAllTextFragments();
        for (TextFragment tf : allTextFragments) {
            StringBuilder tfStringBuilder = new StringBuilder();
            tfStringBuilder.append(tf.getOriginalForm());
            for (ResultOneHeuristics resultOneHeuristics : resultsOfHeuristicsIncludingRemovedOnes) {
                TextFragment textFragmentInvestigated = resultOneHeuristics.getTextFragmentInvestigated();
                Category.CategoryEnum categoryEnum = resultOneHeuristics.getCategoryEnum();
                if (tf instanceof Term && textFragmentInvestigated instanceof NGram) {
                    NGram ngram = (NGram) textFragmentInvestigated;
                    for (Term term : ngram.getTerms()) {
                        if (term.getOriginalForm().equals(tf.getOriginalForm()) && term.getIndexCardinal() == tf.getIndexCardinal()) {
                            if (categoryEnum.equals(CategoryEnum._11)) {
                                tfStringBuilder.insert(0, "<span class=\"user\" data-uid=\"001\">");
                                tfStringBuilder.append("</span>");
                            }
                            if (categoryEnum.equals(CategoryEnum._12)) {
                                tfStringBuilder.insert(0, "<span class=\"user\" data-uid=\"002\">");
                                tfStringBuilder.append("</span>");
                            }
                        }
                    }
                } else if (tf instanceof NonWord && textFragmentInvestigated instanceof NonWord) {
                    NonWord nonWord = (NonWord) textFragmentInvestigated;

                    if (nonWord.getOriginalForm().equals(tf.getOriginalForm()) && nonWord.getIndexCardinal() == tf.getIndexCardinal()) {
                        if (categoryEnum.equals(CategoryEnum._11)) {
                            tfStringBuilder.insert(0, "<span class=\"user\" data-uid=\"001\">");
                            tfStringBuilder.append("</span>");
                        }
                        if (categoryEnum.equals(CategoryEnum._12)) {
                            tfStringBuilder.insert(0, "<span class=\"user\" data-uid=\"002\">");
                            tfStringBuilder.append("</span>");
                        }
                    }
                } else if (tf instanceof Hashtag && textFragmentInvestigated instanceof Hashtag) {
                    Hashtag hashtag = (Hashtag) textFragmentInvestigated;

                    if (hashtag.getOriginalForm().equals(tf.getOriginalForm()) && hashtag.getIndexCardinal() == tf.getIndexCardinal()) {
                        if (categoryEnum.equals(CategoryEnum._11)) {
                            tfStringBuilder.insert(0, "<span class=\"user\" data-uid=\"001\">");
                            tfStringBuilder.append("</span>");
                        }
                        if (categoryEnum.equals(CategoryEnum._12)) {
                            tfStringBuilder.insert(0, "<span class=\"user\" data-uid=\"002\">");
                            tfStringBuilder.append("</span>");
                        }
                    }
                }
            }
            sb.append(tfStringBuilder.toString());
        }

        return sb.toString();
    }

    public static String generateCssStyles(Document doc) {
        StringBuilder sb = new StringBuilder();
        sb.append("<style>");
        sb.append("\n");
        sb.append("span.user { border-bottom:1px solid; display:inline-block; padding-bottom:1px; }");
        sb.append("\n");
        sb.append("span[data-uid='001'] { border-bottom-color:blue; }");
        sb.append("\n");
        sb.append("span[data-uid='002'] { border-bottom-color:red; }");
        sb.append("\n");
        sb.append("span[data-uid='003'] { border-bottom-color:orange; }");
        sb.append("\n");
        sb.append("</style>");
        sb.append("\n");
        return sb.toString();
    }
}
