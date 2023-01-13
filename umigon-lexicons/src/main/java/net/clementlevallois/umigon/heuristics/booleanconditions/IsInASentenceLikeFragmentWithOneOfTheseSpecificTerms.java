/*
 * author: Cl�ment Levallois
 */
package net.clementlevallois.umigon.heuristics.booleanconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.clementlevallois.umigon.model.BooleanCondition;
import net.clementlevallois.umigon.model.NGram;
import static net.clementlevallois.umigon.model.BooleanCondition.BooleanConditionEnum.isInASentenceLikeFragmentWithOneOfTheseSpecificTerms;

/**
 *
 * @author LEVALLOIS
 */
public class IsInASentenceLikeFragmentWithOneOfTheseSpecificTerms {

    public static BooleanCondition check(boolean stripped, NGram ngram, List<NGram> ngrams, Set<String> keywords) {
        BooleanCondition booleanCondition = new BooleanCondition(isInASentenceLikeFragmentWithOneOfTheseSpecificTerms);
        booleanCondition.setAssociatedKeywords(keywords);
        List<NGram> nGramsThatMatched = new ArrayList();
        for (NGram ngramLoop : ngrams) {
            if (keywords.contains(ngramLoop.getCleanedAndStrippedNgramIfCondition(stripped))) {
                nGramsThatMatched.add(ngramLoop);
            }
        }
        if (nGramsThatMatched.isEmpty()) {
            return booleanCondition;
        } else {
            booleanCondition.setTokenInvestigatedGetsMatched(Boolean.TRUE);
            booleanCondition.setTextFragmentMatched(ngram);
            booleanCondition.setAssociatedKeywordMatchedAsTextFragment(nGramsThatMatched);
            return booleanCondition;
        }
    }
}
