/*
 * author: Cl�ment Levallois
 */
package net.clementlevallois.umigon.heuristics.booleanconditions;

import java.util.List;
import net.clementlevallois.umigon.heuristics.tools.TextFragmentOps;
import net.clementlevallois.umigon.model.BooleanCondition;
import static net.clementlevallois.umigon.model.BooleanCondition.BooleanConditionEnum.isInSegmentEndingWithExclamation;
import net.clementlevallois.umigon.model.NGram;
import net.clementlevallois.umigon.model.TextFragment;

/**
 *
 * @author LEVALLOIS
 */
public class IsInSegmentEndingInExclamation {

    public static BooleanCondition check(boolean stripped, List<TextFragment> textFragments, NGram ngram) {
        BooleanCondition booleanCondition = new BooleanCondition(isInSegmentEndingWithExclamation);
        List<TextFragment> textFragmentsAfterAnOrdinalIndex = TextFragmentOps.getTextFragmentsAfterAnNgram(textFragments, ngram);
        if (textFragmentsAfterAnOrdinalIndex.isEmpty()) {
            booleanCondition.setTokenInvestigatedGetsMatched(false);
            return booleanCondition;
        } else {
            int indexLastTF = -1;
            TextFragment lastTF = null;
            for (TextFragment tf : textFragmentsAfterAnOrdinalIndex) {
                if (tf.getIndexCardinal() > indexLastTF) {
                    indexLastTF = tf.getIndexCardinal();
                    lastTF = tf;
                }
            }
            if (lastTF != null && lastTF.getOriginalForm().contains("!")) {
                booleanCondition.setTokenInvestigatedGetsMatched(true);
                booleanCondition.setTextFragmentMatched(lastTF);
                return booleanCondition;
            } else {
                booleanCondition.setTokenInvestigatedGetsMatched(false);
                return booleanCondition;
            }
        }
    }

}
