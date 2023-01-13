/*
 * author: Cl�ment Levallois
 */
package net.clementlevallois.umigon.heuristics.booleanconditions;

import java.util.ArrayList;
import java.util.List;
import net.clementlevallois.umigon.heuristics.tools.LoaderOfLexiconsAndConditionalExpressions;
import net.clementlevallois.umigon.heuristics.tools.TextFragmentOps;
import net.clementlevallois.umigon.model.BooleanCondition;
import static net.clementlevallois.umigon.model.BooleanCondition.BooleanConditionEnum.isImmediatelyPrecededByANegation;
import net.clementlevallois.umigon.model.NGram;

/**
 *
 * @author LEVALLOIS
 */
public class IsImmediatelyPrecededByANegation {

    public static BooleanCondition check(boolean stripped, List<NGram> textFragmentsThatAreNGrams, NGram ngram, LoaderOfLexiconsAndConditionalExpressions lexiconsAndTheirConditionalExpressions) {
        BooleanCondition booleanCondition = new BooleanCondition(isImmediatelyPrecededByANegation);

        List<NGram> ngramsFoundAtIndexMinusOne = TextFragmentOps.getNGramsAtRelativeOrdinalIndex(textFragmentsThatAreNGrams, ngram, -1);
        List<NGram> ngramsFoundAtIndexMinusTwo = TextFragmentOps.getNGramsAtRelativeOrdinalIndex(textFragmentsThatAreNGrams, ngram, -2);
        
        // risky to search negations at index minus 3
        
        // it will be wrong in the case of "not bad but ok" -> will output that "ok" is immediately preceded by "not", which is incorrect
        
        // however it is useful in the case of "I don't think they are great" because "don't think" is in our lexicon of negations and it is at index minus 3 of "great"
        
        
        List<NGram> ngramsFoundAtIndexMinusThree = TextFragmentOps.getNGramsAtRelativeOrdinalIndex(textFragmentsThatAreNGrams, ngram, -3);

        List<NGram> allNgramsFound = new ArrayList();
        allNgramsFound.addAll(ngramsFoundAtIndexMinusOne);
        allNgramsFound.addAll(ngramsFoundAtIndexMinusTwo);
        allNgramsFound.addAll(ngramsFoundAtIndexMinusThree);

        List<NGram> nGramsThatMatchedANegation = TextFragmentOps.checkIfListOfNgramsMatchStringsFromCollection(stripped, allNgramsFound, lexiconsAndTheirConditionalExpressions.getSetNegations());
        booleanCondition.setTokenInvestigatedGetsMatched(!nGramsThatMatchedANegation.isEmpty());
        if (!nGramsThatMatchedANegation.isEmpty()) {
            booleanCondition.setAssociatedKeywordMatchedAsTextFragment(nGramsThatMatchedANegation);
            booleanCondition.setTextFragmentMatched(ngram);
        }
        return booleanCondition;
    }

}
