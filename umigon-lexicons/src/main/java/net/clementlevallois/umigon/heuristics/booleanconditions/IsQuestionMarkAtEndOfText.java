/*
 * author: Cl�ment Levallois
 */
package net.clementlevallois.umigon.heuristics.booleanconditions;

import java.util.ArrayList;
import java.util.List;
import net.clementlevallois.umigon.model.BooleanCondition;
import static net.clementlevallois.umigon.model.BooleanCondition.BooleanConditionEnum.isQuestionMarkAtEndOfText;
import net.clementlevallois.umigon.model.NGram;
import net.clementlevallois.umigon.model.TextFragment;
import net.clementlevallois.umigon.model.TypeOfTextFragment;

/**
 *
 * @author LEVALLOIS
 */
public class IsQuestionMarkAtEndOfText {

    public static BooleanCondition check(List<TextFragment> textFragments) {
        BooleanCondition booleanCondition = new BooleanCondition(isQuestionMarkAtEndOfText);

        List<TextFragment> workingList = new ArrayList();
        workingList.addAll(textFragments);
        
        if (workingList.isEmpty()){
            return booleanCondition;
        }

        int indexLastOne = workingList.size() - 1;
        
        TextFragment lastOne = workingList.get(indexLastOne);
        boolean checksComplete = false;
        while (!checksComplete) {
            if (lastOne.getTypeOfTextFragmentEnum().equals(TypeOfTextFragment.TypeOfTextFragmentEnum.NGRAM)) {
                NGram curr = (NGram) lastOne;
                if (curr.getCleanedNgram().contains("/")) {
                    workingList.remove(workingList.size() - 1);
                } else {
                    checksComplete = true;
                }
            } else {
                checksComplete = true;
            }
        }
        lastOne = workingList.get(workingList.size() - 1);
        if (lastOne.getTypeOfTextFragmentEnum().equals(TypeOfTextFragment.TypeOfTextFragmentEnum.PUNCTUATION)) {
            if (lastOne.getOriginalForm().equals("?")) {
                booleanCondition.setTokenInvestigatedGetsMatched(Boolean.TRUE);
                booleanCondition.setTextFragmentMatched(lastOne);
            }
        }
        return booleanCondition;

    }
}
