/*
 * author: Clément Levallois
 */
package net.clementlevallois.umigonfamily.umigon.decision;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsImmediatelyPrecededBySubjectiveTerm;
import net.clementlevallois.umigon.heuristics.tools.LoaderOfLexiconsAndConditionalExpressions;
import net.clementlevallois.umigon.model.Category;
import net.clementlevallois.umigon.model.classification.Decision;
import net.clementlevallois.umigon.model.classification.Document;
import net.clementlevallois.umigon.model.NGram;
import net.clementlevallois.umigon.model.classification.BooleanCondition;
import net.clementlevallois.umigon.model.classification.ResultOneHeuristics;

/**
 *
 * @author LEVALLOIS
 */
public class WhenTextContainsAModerator {

    Set<String> moderatorsForward;
    Set<String> moderatorsBackward;
    LoaderOfLexiconsAndConditionalExpressions lexiconsAndTheirConditionalExpressions;

    public WhenTextContainsAModerator(Set<String> moderatorsForward, Set<String> moderatorsBackward, LoaderOfLexiconsAndConditionalExpressions lexiconsAndTheirConditionalExpressions) {
        this.moderatorsForward = moderatorsForward;
        this.moderatorsBackward = moderatorsBackward;
        this.lexiconsAndTheirConditionalExpressions = lexiconsAndTheirConditionalExpressions;
    }

    public Document containsAModeratorForward(Document document) {
        List<NGram> ngrams = document.getNgrams();
        NGram moderator = null;

        String precedingNGramAsString = "";
        for (NGram ngram : ngrams) {
            String ngramAsString = ngram.getCleanedNgram().toLowerCase();
            if (moderatorsForward.contains(ngramAsString)) {
                moderator = ngram;
                String moderatorAsString = ngram.getCleanedAndStrippedNgram().toLowerCase();
                // because "if" is a moderator, but "as if" and "even if" is not one.
                if (moderatorAsString.equals("if ") && (precedingNGramAsString.equals("as") || precedingNGramAsString.equals("even"))) {
                    moderator = null;
                }
            }
            precedingNGramAsString = ngramAsString;
        }
        if (moderator != null) {
            for (NGram ngram : ngrams) {
                if (ngram.getIndexCardinal() > moderator.getIndexCardinal()) {
                    String ngramAsString = ngram.getCleanedNgram().toLowerCase();
                    if (ngramAsString.contains("as well") || ngramAsString.contains("too") || ngramAsString.contains("aussi") || ngramAsString.contains("tambien")) {
                        moderator = null;
                        break;
                    }
                }
            }
        }
        if (moderator == null) {
            return document;
        }
        
        
        /*
        
        The following check is the following:
        
        A. I don't think this is great
        A. They don't think this is great
        
        -> "think" is a moderator. Should it cancel the positive sentiment effect of "great"?
        -> A. the moderator is preceded by a subjective term, so the moderator has no cancellation effect on the sentiment
        -> B. the moderator is NOT preceded by a subjective term, so the moderator should cancel the effect on the sentiment
        
        */
        
        boolean strippedText = false;
        BooleanCondition checkOnSubjectiveTermBefore = IsImmediatelyPrecededBySubjectiveTerm.check(strippedText, document.getNgrams(), moderator, lexiconsAndTheirConditionalExpressions);
        if (checkOnSubjectiveTermBefore.getTextFragmentMatched()!= null){
            return document;
        }

        Set<ResultOneHeuristics> indexesPos = document.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._11);
        Set<ResultOneHeuristics> indexesNeg = document.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._12);

        if (indexesPos.isEmpty() & indexesNeg.isEmpty()) {
            return document;
        }
        int indexCardinalModerator;
        int index = Integer.MAX_VALUE;
        int indexNegFirst = Integer.MAX_VALUE;
        int indexPosLast = -1;
        int indexNegLast = -1;

        Iterator<ResultOneHeuristics> iterator;

        iterator = indexesPos.iterator();
        while (iterator.hasNext()) {
            Integer currIndex = iterator.next().getTextFragmentInvestigated().getIndexCardinal();
            if (currIndex < index) {
                index = currIndex;
            }
            if (currIndex > indexPosLast) {
                indexPosLast = currIndex;
            }
        }
        iterator = indexesNeg.iterator();
        while (iterator.hasNext()) {
            Integer currIndex = iterator.next().getTextFragmentInvestigated().getIndexCardinal();
            if (currIndex < indexNegFirst) {
                indexNegFirst = currIndex;
            }
            if (currIndex > indexNegLast) {
                indexNegLast = currIndex;
            }
        }

        Decision decision;
        indexCardinalModerator = moderator.getIndexCardinal();
        for (ResultOneHeuristics entry : indexesPos) {
            int indexLoop = entry.getTextFragmentInvestigated().getIndexCardinal();
            if ((indexLoop > indexCardinalModerator)) {
                document.getResultsOfHeuristics().remove(entry);
                decision = new Decision();
                decision.setDecisionMotive(Decision.DecisionMotive.MODERATOR_THEN_POSITIVE_TERM);
                List<ResultOneHeuristics> heuristics = new ArrayList();
                heuristics.add(entry);
                decision.setListOfHeuristicsImpacted(heuristics);
                decision.setDecisionType(Decision.DecisionType.REMOVE);
                decision.setTextFragmentInvolvedInDecision(moderator);
                document.getDecisions().add(decision);
            }
        }

        for (ResultOneHeuristics entry : indexesNeg) {
            int indexLoop = entry.getTextFragmentInvestigated().getIndexCardinal();
            if ((indexLoop > indexCardinalModerator)) {
                document.getResultsOfHeuristics().remove(entry);
                decision = new Decision();
                decision.setDecisionMotive(Decision.DecisionMotive.MODERATOR_THEN_NEGATIVE_TERM);
                List<ResultOneHeuristics> heuristics = new ArrayList();
                heuristics.add(entry);
                decision.setListOfHeuristicsImpacted(heuristics);
                decision.setDecisionType(Decision.DecisionType.REMOVE);
                decision.setTextFragmentInvolvedInDecision(moderator);
                document.getDecisions().add(decision);
            }
        }
        return document;
    }

    public Document containsAModeratorBackward(Document document) {
        List<NGram> ngrams = document.getNgrams();
        NGram moderator = null;

        for (NGram ngram : ngrams) {
            String ngramAsString = ngram.getCleanedNgram().toLowerCase();
            if (moderatorsBackward.contains(ngramAsString)) {
                moderator = ngram;
            }
        }
        if (moderator == null) {
            return document;
        }

        Set<ResultOneHeuristics> indexesPos = document.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._11);
        Set<ResultOneHeuristics> indexesNeg = document.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._12);

        if (indexesPos.isEmpty() & indexesNeg.isEmpty()) {
            return document;
        }
        int indexCardinalModerator;
        int indexSegmentLikeFragmentOfModerator;
        int index = Integer.MAX_VALUE;
        int indexNegFirst = Integer.MAX_VALUE;
        int indexPosLast = -1;
        int indexNegLast = -1;

        Iterator<ResultOneHeuristics> iterator;

        iterator = indexesPos.iterator();
        while (iterator.hasNext()) {
            Integer currIndex = iterator.next().getTextFragmentInvestigated().getIndexCardinal();
            if (currIndex < index) {
                index = currIndex;
            }
            if (currIndex > indexPosLast) {
                indexPosLast = currIndex;
            }
        }
        iterator = indexesNeg.iterator();
        while (iterator.hasNext()) {
            Integer currIndex = iterator.next().getTextFragmentInvestigated().getIndexCardinal();
            if (currIndex < indexNegFirst) {
                indexNegFirst = currIndex;
            }
            if (currIndex > indexNegLast) {
                indexNegLast = currIndex;
            }
        }

        Decision decision;
        indexCardinalModerator = moderator.getIndexCardinal();
        indexSegmentLikeFragmentOfModerator = moderator.getSentenceLikeFragmentIndex();

        for (ResultOneHeuristics entry : indexesPos) {
            int indexOfPositiveTextFragment = entry.getTextFragmentInvestigated().getIndexCardinal();
            int indexSegmentLikeFragmentOfPositiveTextFragment = entry.getTextFragmentInvestigated().getSentenceLikeFragmentIndex();
            boolean isOpinionInSameOrPrecedingTextFragment = indexSegmentLikeFragmentOfPositiveTextFragment == indexSegmentLikeFragmentOfModerator | indexSegmentLikeFragmentOfPositiveTextFragment == (indexSegmentLikeFragmentOfModerator - 1);
            if ((indexOfPositiveTextFragment < indexCardinalModerator) && isOpinionInSameOrPrecedingTextFragment) {
                document.getResultsOfHeuristics().remove(entry);
                decision = new Decision();
                decision.setDecisionMotive(Decision.DecisionMotive.POSITIVE_TERM_THEN_MODERATOR);
                List<ResultOneHeuristics> heuristics = new ArrayList();
                heuristics.add(entry);
                decision.setListOfHeuristicsImpacted(heuristics);
                decision.setDecisionType(Decision.DecisionType.REMOVE);
                decision.setTextFragmentInvolvedInDecision(moderator);
                document.getDecisions().add(decision);
            }
        }

        for (ResultOneHeuristics entry : indexesNeg) {
            int indexOfNegativeTextFragment = entry.getTextFragmentInvestigated().getIndexCardinal();
            int indexSegmentLikeFragmentOfNegativeTextFragment = entry.getTextFragmentInvestigated().getSentenceLikeFragmentIndex();
            boolean isOpinionInSameOrPrecedingTextFragment = indexSegmentLikeFragmentOfNegativeTextFragment == indexSegmentLikeFragmentOfModerator | indexSegmentLikeFragmentOfNegativeTextFragment == (indexSegmentLikeFragmentOfModerator - 1);
            if ((indexOfNegativeTextFragment < indexCardinalModerator) && isOpinionInSameOrPrecedingTextFragment) {
                document.getResultsOfHeuristics().remove(entry);
                decision = new Decision();
                decision.setDecisionMotive(Decision.DecisionMotive.NEGATIVE_TERM_THEN_MODERATOR);
                List<ResultOneHeuristics> heuristics = new ArrayList();
                heuristics.add(entry);
                decision.setListOfHeuristicsImpacted(heuristics);
                decision.setDecisionType(Decision.DecisionType.REMOVE);
                decision.setTextFragmentInvolvedInDecision(moderator);
                document.getDecisions().add(decision);
            }
        }
        return document;
    }
}
