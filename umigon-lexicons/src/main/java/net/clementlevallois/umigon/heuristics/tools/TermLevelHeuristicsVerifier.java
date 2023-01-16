/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.clementlevallois.umigon.heuristics.tools;

import net.clementlevallois.umigon.model.BooleanCondition;
import net.clementlevallois.umigon.model.TermWithConditionalExpressions;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsAllCaps;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsFirstLetterCapitalized;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsImmediatelyFollowedByANegation;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsImmediatelyFollowedByAnOpinion;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsImmediatelyFollowedBySpecificTerm;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsImmediatelyPrecededByANegation;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsImmediatelyPrecededBySpecificTerm;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsFirstTermOfText;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsFollowedByANegativeOpinion;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsFollowedByAPositiveOpinion;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsFollowedBySpecificTerm;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsHashtagPositiveSentiment;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsHashtagStart;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsImmediatelyFollowedByANegativeOpinion;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsImmediatelyFollowedByAPositiveOpinion;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsImmediatelyPrecededByPositive;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsInASentenceLikeFragmentWithOneOfTheseSpecificTerms;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsInHashtag;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsInSegmentEndingInExclamation;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsLastNGramOfSegment;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsPrecededByOpinion;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsPrecededByPositive;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsPrecededBySpecificTerm;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsPrecededByStrongWord;
import net.clementlevallois.umigon.heuristics.booleanconditions.IsPrecededBySubjectiveTerm;
import net.clementlevallois.umigon.model.Category;
import net.clementlevallois.umigon.model.Category.CategoryEnum;
import net.clementlevallois.umigon.model.NGram;
import net.clementlevallois.umigon.model.ResultOneHeuristics;
import net.clementlevallois.umigon.model.SentenceLike;
import net.clementlevallois.umigon.model.TextFragment;

/*
 Copyright 2008-2013 Clement Levallois
 Authors : Clement Levallois <clementlevallois@gmail.com>
 Website : http://www.clementlevallois.net


 DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.

 Copyright 2013 Clement Levallois. All rights reserved.

 The contents of this file are subject to the terms of either the GNU
 General Public License Version 3 only ("GPL") or the Common
 Development and Distribution License("CDDL") (collectively, the
 "License"). You may not use this file except in compliance with the
 License. You can obtain a copy of the License at
 http://gephi.org/about/legal/license-notice/
 or /cddl-1.0.txt and /gpl-3.0.txt. See the License for the
 specific language governing permissions and limitations under the
 License.  When distributing the software, include this License Header
 Notice in each file and include the License files at
 /cddl-1.0.txt and /gpl-3.0.txt. If applicable, add the following below the
 License Header, with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"

 If you wish your version of this file to be governed by only the CDDL
 or only the GPL Version 3, indicate your decision by adding
 "[Contributor] elects to include this software in this distribution
 under the [CDDL or GPL Version 3] license." If you do not indicate a
 single choice of license, a recipient has the option to distribute
 your version of this file under either the CDDL, the GPL Version 3 or
 to extend the choice of license to its licensees as provided above.
 However, if you add GPL Version 3 code and therefore, elected the GPL
 Version 3 license, then the option applies only if the new code is
 made subject to such option by the copyright holder.

 Contributor(s): Clement Levallois

 */
public class TermLevelHeuristicsVerifier {

    public static ResultOneHeuristics checkHeuristicsOnOneNGram(NGram ngramParam, SentenceLike sentenceLike, TermWithConditionalExpressions termWithConditionalExpressions, LoaderOfLexiconsAndConditionalExpressions lexiconsAndConditionalExpressions, boolean stripped, Set<String> stopWords) {
        String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String termFromLexicon = termWithConditionalExpressions.getTerm();
        List<BooleanCondition> booleanConditions = termWithConditionalExpressions.getMapFeatures();
        String rule = termWithConditionalExpressions.getRule();

        List<NGram> nGramsInSentence = sentenceLike.getNgrams();
        List<TextFragment> textFragmentsInSentence = sentenceLike.getTextFragments();

//        if (termFromLexicon.equals("like")) {
//            System.out.println("like has been found");
//        }
        InterpreterOfConditionalExpressions interpreter = new InterpreterOfConditionalExpressions();
        Category cat;

        Map<String, Boolean> conditions = new HashMap();
        ResultOneHeuristics resultOneHeuristics = new ResultOneHeuristics(ngramParam);
        if (booleanConditions.isEmpty()) {
            try {
                cat = new Category(rule);
                BooleanCondition booleanCondition = new BooleanCondition(BooleanCondition.BooleanConditionEnum.none);
                booleanCondition.setTokenInvestigatedGetsMatched(Boolean.TRUE);
                resultOneHeuristics.setCategoryEnum(cat.getCategoryEnum());
                resultOneHeuristics.getBooleanConditions().add(booleanCondition);
                return resultOneHeuristics;
            } catch (IllegalArgumentException wrongCode) {
                System.out.println("rule was misspelled or just wrong:");
                System.out.println(rule);
                System.out.println("for term:");
                System.out.println(ngramParam.getCleanedNgram());
                return resultOneHeuristics;
            }
        }

        BooleanCondition.BooleanConditionEnum conditionEnum;
        Set<String> associatedKeywords;

        int numberOfExaminationsOfBooleanConditions = 1;
//        String nGramStripped = ngramParam.getCleanedAndStrippedNgramIfCondition(true);
//        String nGramNonStripped = ngramParam.getCleanedAndStrippedNgramIfCondition(false);
//        if (!nGramStripped.equals(nGramNonStripped)) {
//            numberOfExaminationsOfBooleanConditions = 2;
//        }

        for (int i = 1; i <= numberOfExaminationsOfBooleanConditions; i++) {
            /*
            a little trick to achieve the following effect:
            - the first loop will examine the NON stripped version of the ngram
            - the second loop will examine the stripped version of the ngram
            - when there is just one loop, only the NON stripped version of the ngram is examined            
             */
            int count = 0;

            for (BooleanCondition booleanCondition : booleanConditions) {

                conditionEnum = booleanCondition.getBooleanConditionEnum();
                associatedKeywords = booleanCondition.getAssociatedKeywords(stripped);

                boolean opposite = false;
                if (booleanCondition.getFlipped()) {
                    opposite = true;
                }

                switch (conditionEnum) {

                    case isImmediatelyPrecededByANegation:
                        booleanCondition = IsImmediatelyPrecededByANegation.check(stripped, nGramsInSentence, ngramParam, lexiconsAndConditionalExpressions, stopWords);
                        break;

                    case isImmediatelyFollowedByANegation:
                        booleanCondition = IsImmediatelyFollowedByANegation.check(stripped, nGramsInSentence, ngramParam, lexiconsAndConditionalExpressions);
                        break;

                    case isImmediatelyPrecededBySpecificTerm:
                        booleanCondition = IsImmediatelyPrecededBySpecificTerm.check(stripped, nGramsInSentence, ngramParam, associatedKeywords);
                        break;

                    case isImmediatelyFollowedBySpecificTerm:
                        booleanCondition = IsImmediatelyFollowedBySpecificTerm.check(stripped, nGramsInSentence, ngramParam, associatedKeywords);
                        break;

                    case isImmediatelyFollowedByAnOpinion:
                        booleanCondition = IsImmediatelyFollowedByAnOpinion.check(stripped, nGramsInSentence, ngramParam, lexiconsAndConditionalExpressions);
                        break;

                    case isPrecededBySubjectiveTerm:
                        booleanCondition = IsPrecededBySubjectiveTerm.check(stripped, nGramsInSentence, ngramParam, lexiconsAndConditionalExpressions);
                        break;

                    case isFirstTermOfText:
                        booleanCondition = IsFirstTermOfText.check(ngramParam.getCleanedAndStrippedNgram(), nGramsInSentence.get(0).getCleanedAndStrippedNgram());
                        break;

                    case isFollowedByAPositiveOpinion:
                        booleanCondition = IsFollowedByAPositiveOpinion.check(stripped, nGramsInSentence, ngramParam, lexiconsAndConditionalExpressions);
                        break;

                    case isFollowedByANegativeOpinion:
                        booleanCondition = IsFollowedByANegativeOpinion.check(stripped, nGramsInSentence, ngramParam, lexiconsAndConditionalExpressions);
                        break;

                    case isImmediatelyFollowedByAPositiveOpinion:
                        booleanCondition = IsImmediatelyFollowedByAPositiveOpinion.check(stripped, nGramsInSentence, ngramParam, lexiconsAndConditionalExpressions);
                        break;

                    case isImmediatelyFollowedByANegativeOpinion:
                        booleanCondition = IsImmediatelyFollowedByANegativeOpinion.check(stripped, nGramsInSentence, ngramParam, lexiconsAndConditionalExpressions);
                        break;

                    case isFollowedBySpecificTerm:
                        booleanCondition = IsFollowedBySpecificTerm.check(stripped, nGramsInSentence, ngramParam, associatedKeywords);
                        break;

                    case isLastNGramOfSegment:
                        booleanCondition = IsLastNGramOfSegment.check(stripped, nGramsInSentence, ngramParam);
                        break;

                    case isInSegmentEndingWithExclamation:
                        booleanCondition = IsInSegmentEndingInExclamation.check(stripped, textFragmentsInSentence, ngramParam);
                        break;

                    case isInASentenceLikeFragmentWithOneOfTheseSpecificTerms:
                        booleanCondition = IsInASentenceLikeFragmentWithOneOfTheseSpecificTerms.check(stripped, ngramParam, nGramsInSentence, associatedKeywords);
                        break;

                    case isHashtagStart:
                        booleanCondition = IsHashtagStart.check(stripped, ngramParam, termFromLexicon);
                        break;

                    case isInHashtag:
                        booleanCondition = IsInHashtag.check(stripped, ngramParam, lexiconsAndConditionalExpressions);
                        break;

                    case isHashtagPositiveSentiment:
                        booleanCondition = IsHashtagPositiveSentiment.check(stripped, ngramParam, lexiconsAndConditionalExpressions);
                        break;

                    case isHashtagNegativeSentiment:
                        booleanCondition = IsHashtagPositiveSentiment.check(stripped, ngramParam, lexiconsAndConditionalExpressions);
                        break;

                    case isPrecededBySpecificTerm:
                        booleanCondition = IsPrecededBySpecificTerm.check(stripped, nGramsInSentence, ngramParam, associatedKeywords);
                        break;

                    case isQuestionMarkAtEndOfText:
                        break;

                    case isAllCaps:
                        booleanCondition = IsAllCaps.check(ngramParam);
                        break;

                    case isPrecededByStrongWord:
                        booleanCondition = IsPrecededByStrongWord.check(stripped, nGramsInSentence, ngramParam, lexiconsAndConditionalExpressions);
                        break;

                    case isImmediatelyPrecededByPositive:
                        booleanCondition = IsImmediatelyPrecededByPositive.check(stripped, nGramsInSentence, ngramParam, lexiconsAndConditionalExpressions);
                        break;

                    case isPrecededByPositive:
                        booleanCondition = IsPrecededByPositive.check(stripped, nGramsInSentence, ngramParam, lexiconsAndConditionalExpressions);
                        break;

                    case isPrecededByOpinion:
                        booleanCondition = IsPrecededByOpinion.check(stripped, nGramsInSentence, ngramParam, lexiconsAndConditionalExpressions);
                        break;

                    case isFirstLetterCapitalized:
                        booleanCondition = IsFirstLetterCapitalized.check(ngramParam.getCleanedNgram());
                        break;
                }
//                        booleanCondition = IsQuestionMarkAtEndOfText.check(text);

                boolean booleanOutcomeAfterPossibleInversion;
                if (opposite) {
                    booleanCondition.setFlipped(Boolean.TRUE);
                    booleanOutcomeAfterPossibleInversion = !booleanCondition.getTokenInvestigatedGetsMatched();
                } else {
                    booleanOutcomeAfterPossibleInversion = booleanCondition.getTokenInvestigatedGetsMatched();
                }
                if (booleanCondition.getTokenInvestigatedGetsMatched() == null) {
                    System.out.println("stop: a boolean expression didnt get a match result in Term Heuristics Verifier");
                }
                conditions.put(ALPHABET.substring(count, (count + 1)), booleanOutcomeAfterPossibleInversion);
                count++;
                resultOneHeuristics.getBooleanConditions().add(booleanCondition);
            }
        }

        String result = interpreter.interprete(rule, conditions);
        if (result != null && !result.isBlank()) {
            if (result.equals("-1")) {
                System.out.println("problem with this rule:");
                System.out.println("rule: " + rule);
                System.out.println("term: " + ngramParam.getCleanedNgram());
                System.out.println("text: " + "");
            }
            try {
                cat = new Category(result);
                resultOneHeuristics.setCategoryEnum(cat.getCategoryEnum());
                return resultOneHeuristics;
            } catch (IllegalArgumentException wrongCode) {
                System.out.println("outcome was misspelled or just wrong, after evaluating the heuristics:");
                System.out.println(result);
                return new ResultOneHeuristics(CategoryEnum._10, ngramParam);
            }
        }
        return new ResultOneHeuristics(ngramParam);
    }

//    public boolean isImmediatelyFollowedByVerbPastTense() {
//        try {
//            String temp = text.substring(text.indexOf(term) + term.length()).trim();
//            boolean pastTense;
//            String[] nextTerms = temp.split(" ");
//            if (nextTerms.length > 0) {
//                temp = nextTerms[0].trim();
//                pastTense = temp.endsWith("ed");
//                if (pastTense) {
//                    return true;
//                }
//                pastTense = temp.endsWith("ought") & !temp.startsWith("ought");
//                return pastTense;
//            } else {
//                return false;
//            }
//        } catch (StringIndexOutOfBoundsException e) {
//            System.out.println(e.getMessage());
//            System.out.println("status was: " + text);
//            System.out.println("term was: " + term);
//            return false;
//        }
//    }
}
