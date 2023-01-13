/*
 * author: Cl�ment Levallois
 */
package net.clementlevallois.umigon.heuristics.booleanconditions;

import java.util.List;
import net.clementlevallois.umigon.heuristics.tools.TextFragmentOps;
import net.clementlevallois.umigon.model.BooleanCondition;
import net.clementlevallois.umigon.model.NGram;
import static net.clementlevallois.umigon.model.BooleanCondition.BooleanConditionEnum.isLastNGramOfSegment;

/**
 *
 * @author LEVALLOIS
 */
public class IsLastNGramOfSegment {

        public static BooleanCondition check(boolean stripped, List<NGram> textFragmentsThatAreNGrams, NGram ngram) {
        BooleanCondition booleanCondition = new BooleanCondition(isLastNGramOfSegment);
        List<NGram> nGramsAfterAnOrdinalIndex = TextFragmentOps.getNGramsAfterAnOrdinalIndex(textFragmentsThatAreNGrams, ngram);
        if (nGramsAfterAnOrdinalIndex.isEmpty()) {
            booleanCondition.setTextFragmentMatched(ngram);
            booleanCondition.setTokenInvestigatedGetsMatched(true);
        }
        return booleanCondition;
    }
    
}
