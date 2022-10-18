/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.clementlevallois.umigon.classifier.organic;

//import Admin.Parameters;
import java.io.IOException;
import java.util.ArrayList;
import net.clementlevallois.umigon.model.TermWithConditionalExpressions;
import net.clementlevallois.umigon.heuristics.tools.LoaderOfLexiconsAndConditionalExpressions;

import net.clementlevallois.umigon.model.Document;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;
import net.clementlevallois.umigon.heuristics.catalog.IsNegationInAllCaps;
import net.clementlevallois.umigon.heuristics.catalog.IsQuestionMarkAtEndOfText;
import net.clementlevallois.umigon.heuristics.tools.EmojisHeuristicsandResourcesLoader;
import net.clementlevallois.umigon.heuristics.tools.HashtagLevelHeuristicsVerifier;
import net.clementlevallois.umigon.heuristics.tools.TermLevelHeuristicsVerifier;
import net.clementlevallois.umigon.model.BooleanCondition;
import net.clementlevallois.umigon.model.Category;
import net.clementlevallois.umigon.model.Category.CategoryEnum;
import net.clementlevallois.umigon.model.NGram;
import net.clementlevallois.umigon.model.NonWord;
import net.clementlevallois.umigon.semantics.resources.Semantics;
import net.clementlevallois.umigon.model.ResultOneHeuristics;
import net.clementlevallois.umigon.model.SentenceLike;
import net.clementlevallois.umigon.model.Term;
import net.clementlevallois.umigon.model.Text;
import net.clementlevallois.umigon.model.TextFragment;
import net.clementlevallois.umigon.model.TypeOfTextFragment;
import net.clementlevallois.umigon.ngram.ops.SentenceLikeFragmentsDetector;
import net.clementlevallois.umigon.ngram.ops.NGramFinderBisForTextFragments;
import net.clementlevallois.umigon.tokenizer.controller.UmigonTokenizer;
import net.clementlevallois.umigonfamily.umigon.decision.SentimentDecisionMaker;

public class ClassifierOrganicOneDocument {

    Semantics semantics;
    LoaderOfLexiconsAndConditionalExpressions lexiconsAndTheirConditionalExpressions;

    public ClassifierOrganicOneDocument(Semantics semantics) {
        this.semantics = semantics;
        lexiconsAndTheirConditionalExpressions = semantics.getLexiconsAndTheirConditionalExpressions();
    }

    public Document call(Document document) throws IOException {
        List<ResultOneHeuristics> resultsHeuristics = new ArrayList();

        //not classifying docs that are null or empty
        if (document == null || document.getText() == null || document.getText().isBlank()) {
            return document;
        }

        TermWithConditionalExpressions termAndItsConditionalExpressions;

        Set<NGram> alreadyExaminedNGramInPositive = new HashSet();

        Text text = new Text();

        text.setOriginalForm(document.getText());

//        if (document.getText().contains("tendance")) {
//            System.out.println("stop tendance in text");
//        }
        document.setLanguage(semantics.getLang());

        List<TextFragment> allTextFragments = UmigonTokenizer.tokenize(document.getText(), semantics.getLexiconsAndTheirConditionalExpressions().getLexiconsWithoutTheirConditionalExpressions());
        List<NGram> ngrams = new ArrayList();
        List<SentenceLike> listOfSentenceLikeFragments = SentenceLikeFragmentsDetector.returnSentenceLikeFragments(allTextFragments);
        for (SentenceLike sentenceLikeFragment : listOfSentenceLikeFragments) {
            List<NGram> generateNgramsUpto = NGramFinderBisForTextFragments.generateNgramsUpto(sentenceLikeFragment.getNgrams(), 5);
            ngrams.addAll(generateNgramsUpto);
            sentenceLikeFragment.setNgrams(ngrams);
        }
        document.setAllTextFragments(allTextFragments);
        document.setNgrams(ngrams);

        List<NGram> textFragmentsThatAreHashTag = new ArrayList();

        // iterating on the list of text fragments
        // a hashtag is a text fragment immediately preceded by a text fragment which is a punctuation sign equal to "#"
        boolean nextTextFragmentIsHashtag = false;
        for (TextFragment textFragment : allTextFragments) {
            if (textFragment.getTypeOfTextFragmentEnum().equals(TypeOfTextFragment.TypeOfTextFragmentEnum.PUNCTUATION)) {
                nextTextFragmentIsHashtag = textFragment.getOriginalForm().equals("#");
            }
            if (nextTextFragmentIsHashtag && textFragment.getTypeOfTextFragmentEnum().equals(TypeOfTextFragment.TypeOfTextFragmentEnum.TERM)) {
                Term hashtag = (Term) textFragment;
                textFragmentsThatAreHashTag.add(hashtag.toNgram());
            }
        }

        List<ResultOneHeuristics> runningHashTagOps = HashtagLevelHeuristicsVerifier.check(lexiconsAndTheirConditionalExpressions, textFragmentsThatAreHashTag);
        resultsHeuristics.addAll(runningHashTagOps);
        alreadyExaminedNGramInPositive.addAll(textFragmentsThatAreHashTag);

        // checking onomatopaes, texto speak and emoticons in ascii ("non words")
        for (TextFragment textFragment : ngrams) {
            if (textFragment instanceof NonWord) {
                List<Category> categories = ((NonWord) textFragment).getPoi().getCategories();
                for (Category cat : categories) {
                    ResultOneHeuristics resultOneHeuristics = new ResultOneHeuristics(cat.getCategoryEnum(), textFragment);
                    resultsHeuristics.add(resultOneHeuristics);
                }
            }
        }

        // emojis ?
        List<ResultOneHeuristics> containsAffectiveEmojis = EmojisHeuristicsandResourcesLoader.containsAffectiveEmojis(allTextFragments);
        resultsHeuristics.addAll(containsAffectiveEmojis);

        // does the text contain a negation in CAPS?
        for (NGram ngram : ngrams) {
            BooleanCondition booleanCondition = IsNegationInAllCaps.check(ngram, semantics.getSetNegations());
            if (booleanCondition.getTokenInvestigatedGetsMatched()) {
                ResultOneHeuristics resultOneHeuristics = new ResultOneHeuristics(CategoryEnum._12, ngram);
                resultOneHeuristics.getBooleanConditions().add(booleanCondition);
                resultsHeuristics.add(resultOneHeuristics);
            }
        }

        for (SentenceLike sentenceLikeFragment : listOfSentenceLikeFragments) {

            List<NGram> ngramsInSentence = sentenceLikeFragment.getNgrams().stream().filter(x -> x instanceof NGram).map(NGram.class::cast).collect(toList());
            sentenceLikeFragment.setNgrams(ngramsInSentence);

            for (NGram ngram : sentenceLikeFragment.getNgrams()) {

                // skipping the ngram if it is a stopword, EXCEPT if this is a sentiment related stopword
                if (ngram.getCleanedNgram().isBlank() || semantics.getStopwordsWithoutSentimentRelevance().contains(ngram.getCleanedNgram().toLowerCase())) {
                    continue;
                }

                /*
            
                ----- 3 -----
            Checking if the ngram matches a heuristic for PROMOTED TERMS
            
            (mapH9 lists promoted terms and their rules)
            
                 */
                if (alreadyExaminedNGramInPositive.contains(ngram)) {
                    continue;
                }

//            if (ngram.getCleanedAndStrippedNgram().equals("burnes")) {
//                System.out.println("stop for word before checking positive heuristics");
//            }
                boolean stripped = false;
                termAndItsConditionalExpressions = lexiconsAndTheirConditionalExpressions.getMapH9().get(ngram.getCleanedNgram().toLowerCase());
                if (termAndItsConditionalExpressions == null) {
                    termAndItsConditionalExpressions = lexiconsAndTheirConditionalExpressions.getMapH9().get(ngram.getCleanedAndStrippedNgram().toLowerCase());
                    stripped = true;
                }

                if (termAndItsConditionalExpressions != null) {
                    ResultOneHeuristics resultOneHeuristics = TermLevelHeuristicsVerifier.checkHeuristicsOnOneNGram(ngram, sentenceLikeFragment, termAndItsConditionalExpressions, lexiconsAndTheirConditionalExpressions, stripped);
                    resultsHeuristics.add(resultOneHeuristics);
                    alreadyExaminedNGramInPositive.add(ngram);
                }

            }
        }
        // adding all the results of the heuristics to the Document object
        document.getResultsOfHeuristics().addAll(resultsHeuristics);

        /*
            
                ----- 5 -----
        
                Adjucating the final sentiment based on the results of all the heuristics
            
         */
        SentimentDecisionMaker sentimentDecisionMaker = new SentimentDecisionMaker(document, semantics.getSetNegations(), semantics.getSetModerators(), semantics.getSetIronicTerms());

        sentimentDecisionMaker.doCheckOnNegations();
        // if the text is a question, classify it as neutral.
        // we might of course miss ironic intent, but questions are too hard to decipher
        BooleanCondition bc = IsQuestionMarkAtEndOfText.check(allTextFragments);
        if (bc.getTokenInvestigatedGetsMatched()) {
            ResultOneHeuristics resultOneHeuristics = new ResultOneHeuristics(CategoryEnum._40, bc.getTextFragmentMatched());
            resultOneHeuristics.getBooleanConditions().add(bc);
            resultsHeuristics.add(resultOneHeuristics);
        }

        sentimentDecisionMaker.doCheckOnModerators();

        sentimentDecisionMaker.doCheckOnSarcasm();

        sentimentDecisionMaker.doCheckOnWinnerTakesAll();

        sentimentDecisionMaker.finalAdjudication();

        return sentimentDecisionMaker.getDocument();

    }
}
