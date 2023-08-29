/*
 * author: Clément Levallois
 */
package net.clementlevallois.umigonfamily.umigon.decision;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.clementlevallois.umigon.model.Category;
import net.clementlevallois.umigon.model.classification.Decision;
import net.clementlevallois.umigon.model.classification.Document;
import net.clementlevallois.umigon.model.classification.ResultOneHeuristics;
import net.clementlevallois.umigon.model.TextFragment;
import net.clementlevallois.umigon.model.TypeOfTextFragment.TypeOfTextFragmentEnum;

/**
 *
 * @author LEVALLOIS
 */
public class QuestionMarkAtTheEnd {

    private Document document;

    public QuestionMarkAtTheEnd(Document document) {
        this.document = document;
    }

    public Document checkIt() {

        /*
        The idea is to classify sentences ending in question marks as neutral in sentiment
         */
        List<TextFragment> allTextFragments = document.getAllTextFragments();
        if (allTextFragments.size() < 3) {
            return document;
        }
        Set<ResultOneHeuristics> questionMarks = document.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._40);
        if (!questionMarks.isEmpty()) {
            ResultOneHeuristics heuristics = questionMarks.iterator().next();
            int indexOrdinalQuestionMark = heuristics.getTextFragmentInvestigated().getIndexOrdinal();
            if (indexOrdinalQuestionMark + 1 < allTextFragments.size()) {
                TextFragment nextTF = allTextFragments.get(indexOrdinalQuestionMark + 1);
                if (nextTF != null && nextTF.getTypeOfTextFragmentEnum().equals(TypeOfTextFragmentEnum.PUNCTUATION) && !nextTF.getOriginalForm().contains("?")) {
                    // then the question mark is followed by other punctuation signs, like "!!!" for instance
                    // no removal of existing heuristics should take place
                    return document;
                }
            }
            Decision decision = new Decision();
            decision.setDecisionMotive(Decision.DecisionMotive.ENDING_IN_QUESTION_MARK);
            decision.setDecisionType(Decision.DecisionType.REMOVE);
            decision.setTextFragmentInvolvedInDecision(heuristics.getTextFragmentInvestigated());
            List<ResultOneHeuristics> heuristicsToRemove = new ArrayList();
            for (ResultOneHeuristics one : document.getResultsOfHeuristics()) {
                if (!one.getCategoryEnum().equals(Category.CategoryEnum._40)) {
                    heuristicsToRemove.add(one);
                }
            }
            decision.setListOfHeuristicsImpacted(heuristicsToRemove);
            document.getDecisions().add(decision);
            document.getResultsOfHeuristics().removeAll(heuristicsToRemove);

        }
        return document;
    }

}
