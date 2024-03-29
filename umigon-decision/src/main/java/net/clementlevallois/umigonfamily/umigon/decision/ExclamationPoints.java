/*
 * author: Clément Levallois
 */
package net.clementlevallois.umigonfamily.umigon.decision;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.clementlevallois.umigon.model.Category;
import net.clementlevallois.umigon.model.Category.CategoryEnum;
import net.clementlevallois.umigon.model.classification.Decision;
import net.clementlevallois.umigon.model.classification.Document;
import net.clementlevallois.umigon.model.classification.ResultOneHeuristics;
import net.clementlevallois.umigon.model.TextFragment;

/**
 *
 * @author LEVALLOIS
 */
public class ExclamationPoints {

    Document document;

    public ExclamationPoints(Document document) {
        this.document = document;
    }

    public Document checkIt() {

        /*
        The idea is to capture positive sentiments like:
        
        BIDEN 2020!!!!
        
        Which we want to capture by catching:
        - short sentences
        - ending with at least 2 exclamations points
        - and with no sentiment yet detected in them
        if so, add a positive sentiment
         */
        List<TextFragment> allTextFragments = document.getAllTextFragments();
        if (allTextFragments.size() < 3) {
            return document;
        }
        Set<ResultOneHeuristics> indicesPos = document.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._11);
        Set<ResultOneHeuristics> indicesNeg = document.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._12);
        if (indicesPos.isEmpty() & indicesNeg.isEmpty()) {

            TextFragment lastTF = allTextFragments.get(allTextFragments.size() - 1);
            TextFragment beforeLastTF = allTextFragments.get(allTextFragments.size() - 2);
            boolean questionMarkFollowedByExclamatioMark = false;
            TextFragment beforeCurrent = null;
            for (TextFragment tf: allTextFragments){
                if (tf.getOriginalForm().equals("?")){
                    beforeCurrent = tf;
                }else{
                    beforeCurrent =null;
                }
                if (beforeCurrent != null && tf.getOriginalForm().equals("!")){
                    questionMarkFollowedByExclamatioMark = true;
                    break;
                }
            }
            
            if (questionMarkFollowedByExclamatioMark){
                // negative strong reaction detected
                Decision decision = new Decision();
                decision.setDecisionMotive(Decision.DecisionMotive.QUESTION_MARK_FOLLOWED_BY_EXCLAMATION_MARKS);
                decision.setDecisionType(Decision.DecisionType.ADD);
                decision.setTextFragmentInvolvedInDecision(beforeCurrent);                
                ResultOneHeuristics oneHeuristics = new ResultOneHeuristics(CategoryEnum._12, beforeCurrent);
                ResultOneHeuristics secondHeuristics = new ResultOneHeuristics(CategoryEnum._22, beforeCurrent);
                List<ResultOneHeuristics> heuristics = new ArrayList();
                heuristics.add(oneHeuristics);
                heuristics.add(secondHeuristics);
                decision.setListOfHeuristicsImpacted(heuristics);
                document.getDecisions().add(decision);
                document.getResultsOfHeuristics().addAll(heuristics);
                
            }
            
           
            if (!questionMarkFollowedByExclamatioMark & lastTF.getOriginalForm().equals("!") && beforeLastTF.getOriginalForm().equals("!")) {
                // positive enthusiasm detected
                Decision decision = new Decision();
                decision.setDecisionMotive(Decision.DecisionMotive.EXCLAMATION_MARKS_ENDING_SHORT_NEUTRAL_SENTENCES);
                decision.setDecisionType(Decision.DecisionType.ADD);
                decision.setTextFragmentInvolvedInDecision(lastTF);
                
                ResultOneHeuristics oneHeuristics = new ResultOneHeuristics(CategoryEnum._11, lastTF);
                ResultOneHeuristics secondHeuristics = new ResultOneHeuristics(CategoryEnum._22, lastTF);
                List<ResultOneHeuristics> heuristics = new ArrayList();
                heuristics.add(oneHeuristics);
                heuristics.add(secondHeuristics);
                decision.setListOfHeuristicsImpacted(heuristics);
                document.getDecisions().add(decision);
                document.getResultsOfHeuristics().addAll(heuristics);
            }
        }
        return document;
    }

}
