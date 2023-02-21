/*
 * author: Cl�ment Levallois
 */
package net.clementlevallois.umigon.heuristics.booleanconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.clementlevallois.umigon.heuristics.tools.LoaderOfLexiconsAndConditionalExpressions;
import net.clementlevallois.umigon.heuristics.tools.TextFragmentOps;
import net.clementlevallois.umigon.model.BooleanCondition;
import static net.clementlevallois.umigon.model.BooleanCondition.BooleanConditionEnum.isImmediatelyPrecededByNegativePriorAssociation;
import net.clementlevallois.umigon.model.NGram;

/**
 *
 * @author LEVALLOIS
 */
public class IsImmediatelyPrecededByPriorPositiveAssociation {

    public static BooleanCondition check(boolean stripped, List<NGram> textFragmentsThatAreNGrams, NGram ngram, LoaderOfLexiconsAndConditionalExpressions lexiconsAndTheirConditionalExpressions, Set<String> stopwords) {
        BooleanCondition booleanCondition = new BooleanCondition(isImmediatelyPrecededByNegativePriorAssociation);

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
        
        List<NGram> nGramsThatMatchedAStrongTerm = TextFragmentOps.checkIfListOfNgramsMatchStringsFromCollection(stripped, allNgramsFound, lexiconsAndTheirConditionalExpressions.getSetStrong());

        // if the terms preceding the term under examination contain a strong word ("really", "very" ...) we should search for a negator one more step before
        if (!nGramsThatMatchedAStrongTerm.isEmpty()) {
            List<NGram> ngramsFoundAtIndexMinusFour = TextFragmentOps.getNGramsAtRelativeOrdinalIndex(textFragmentsThatAreNGrams, ngram, -4);
            allNgramsFound.addAll(ngramsFoundAtIndexMinusFour);
        }

        // if one of the terms preceding the term under examination is a stopword, then add a fifth step
        int numberOfStopWords = 0;
        for (NGram ngramFound: allNgramsFound){
            if (stopwords.contains(ngramFound.getCleanedAndStrippedNgram())){
                numberOfStopWords++;
            }
        }
        if (numberOfStopWords !=0) {
            int stepsBack = Math.max(-4 - numberOfStopWords, -6);
            List<NGram> ngramsFoundAtIndexMinusFive = TextFragmentOps.getNGramsAtRelativeOrdinalIndex(textFragmentsThatAreNGrams, ngram, stepsBack);
            allNgramsFound.addAll(ngramsFoundAtIndexMinusFive);
        }

        List<NGram> nGramsThatMatchedANegation = TextFragmentOps.checkIfListOfNgramsMatchStringsFromCollection(stripped, allNgramsFound, lexiconsAndTheirConditionalExpressions.getSetNegativePriorAssociations());
        booleanCondition.setTokenInvestigatedGetsMatched(!nGramsThatMatchedANegation.isEmpty());
        if (!nGramsThatMatchedANegation.isEmpty()) {
            booleanCondition.setAssociatedKeywordMatchedAsTextFragment(nGramsThatMatchedANegation);
            booleanCondition.setTextFragmentMatched(ngram);
        }
        return booleanCondition;
    }

}
