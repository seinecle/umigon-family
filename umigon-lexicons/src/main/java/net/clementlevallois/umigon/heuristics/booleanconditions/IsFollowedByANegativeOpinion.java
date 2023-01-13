/*
 * author: Cl�ment Levallois
 */
package net.clementlevallois.umigon.heuristics.booleanconditions;

import java.util.List;
import net.clementlevallois.umigon.heuristics.tools.LoaderOfLexiconsAndConditionalExpressions;
import net.clementlevallois.umigon.heuristics.tools.TextFragmentOps;
import net.clementlevallois.umigon.model.BooleanCondition;
import static net.clementlevallois.umigon.model.BooleanCondition.BooleanConditionEnum.isFollowedByANegativeOpinion;
import net.clementlevallois.umigon.model.NGram;

/**
 *
 * @author LEVALLOIS
 */
public class IsFollowedByANegativeOpinion {

    public static BooleanCondition check(boolean stripped, List<NGram> textFragmentsThatAreNGrams, NGram ngram, LoaderOfLexiconsAndConditionalExpressions lexiconsAndTheirConditionalExpressions) {
        BooleanCondition booleanCondition = new BooleanCondition(isFollowedByANegativeOpinion);

        List<NGram> nGramsAfterAnOrdinalIndex = TextFragmentOps.getNGramsAfterAnOrdinalIndex(textFragmentsThatAreNGrams, ngram);
        List<NGram> nGramsThatMatchedNegativeOpinion = TextFragmentOps.checkIfListOfNgramsMatchStringsFromCollection(stripped, nGramsAfterAnOrdinalIndex, lexiconsAndTheirConditionalExpressions.getMapH2().keySet());
        booleanCondition.setTokenInvestigatedGetsMatched(!nGramsThatMatchedNegativeOpinion.isEmpty());
        if (!nGramsThatMatchedNegativeOpinion.isEmpty()) {
            booleanCondition.setAssociatedKeywordMatchedAsTextFragment(nGramsThatMatchedNegativeOpinion);
            booleanCondition.setTextFragmentMatched(ngram);
        }
        return booleanCondition;
    }
}
