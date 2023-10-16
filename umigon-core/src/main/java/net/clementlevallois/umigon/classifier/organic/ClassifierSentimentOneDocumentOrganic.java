/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.clementlevallois.umigon.classifier.organic;

//import Admin.Parameters;
import java.io.IOException;
import java.util.ArrayList;
import net.clementlevallois.umigon.model.classification.TermWithConditionalExpressions;
import net.clementlevallois.umigon.heuristics.tools.LoaderOfLexiconsAndConditionalExpressions;

import net.clementlevallois.umigon.model.classification.Document;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.clementlevallois.umigon.heuristics.tools.HashtagLevelHeuristicsVerifier;
import net.clementlevallois.umigon.heuristics.tools.TermLevelHeuristicsVerifier;
import net.clementlevallois.umigon.model.NGram;
import net.clementlevallois.umigon.classifier.resources.Semantics;
import net.clementlevallois.umigon.model.classification.ResultOneHeuristics;
import net.clementlevallois.umigon.model.SentenceLike;
import net.clementlevallois.umigon.model.Term;
import net.clementlevallois.umigon.model.Text;
import net.clementlevallois.umigon.model.TextFragment;
import net.clementlevallois.umigon.model.TypeOfTextFragment;
import net.clementlevallois.umigon.ngram.ops.SentenceLikeFragmentsDetector;
import net.clementlevallois.umigon.ngram.ops.NGramFinderBisForTextFragments;
import net.clementlevallois.umigon.tokenizer.controller.UmigonTokenizer;

public class ClassifierSentimentOneDocumentOrganic {

    Semantics semantics;
    LoaderOfLexiconsAndConditionalExpressions lexiconsAndTheirConditionalExpressions;

    public ClassifierSentimentOneDocumentOrganic(Semantics semantics) {
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

        Set<NGram> alreadyExaminedNGramInOrganic = new HashSet();

        Text text = new Text();

        text.setOriginalForm(document.getText());

        document.setLanguage(semantics.getLang());

        List<TextFragment> allTextFragments = UmigonTokenizer.tokenize(document.getText(), semantics.getLexiconsAndTheirConditionalExpressions().getLexiconsWithoutTheirConditionalExpressions());
        List<NGram> ngrams = new ArrayList();
        SentenceLikeFragmentsDetector sentenceDetector = new SentenceLikeFragmentsDetector();
        List<SentenceLike> sentenceLikeFragments = sentenceDetector.returnSentenceLikeFragments(allTextFragments);
        for (SentenceLike sentenceLikeFragment : sentenceLikeFragments) {
            List<NGram> generateNgramsUpto = NGramFinderBisForTextFragments.generateNgramsUpto(sentenceLikeFragment.getNgrams(), 5);
            ngrams.addAll(generateNgramsUpto);
            sentenceLikeFragment.setNgrams(generateNgramsUpto);
        }
        document.setAllTextFragments(allTextFragments);
        document.setNgrams(ngrams);

        List<NGram> textFragmentsThatAreHashTag = new ArrayList();

        // iterating on the list of text fragments
        // a hashtag is a text fragment immediately preceded by a text fragment which is a punctuation sign equal to "#"
        boolean nextTextFragmentIsHashtag = false;
        for (TextFragment textFragment : allTextFragments) {
            if (nextTextFragmentIsHashtag && textFragment.getTypeOfTextFragmentEnum().equals(TypeOfTextFragment.TypeOfTextFragmentEnum.TERM)) {
                Term hashtag = (Term) textFragment;
                textFragmentsThatAreHashTag.add(hashtag.toNgram());
            }
            nextTextFragmentIsHashtag = textFragment.getOriginalForm().equals("#");

        }

        List<ResultOneHeuristics> runningHashTagOps = HashtagLevelHeuristicsVerifier.checkSentiment(lexiconsAndTheirConditionalExpressions, textFragmentsThatAreHashTag);
        resultsHeuristics.addAll(runningHashTagOps);
        alreadyExaminedNGramInOrganic.addAll(textFragmentsThatAreHashTag);

        for (SentenceLike sentenceLikeFragment : sentenceLikeFragments) {

            for (NGram ngram : sentenceLikeFragment.getNgrams()) {

                // skipping the ngram if it is a stopword, EXCEPT if this is a sentiment related stopword
                if (ngram.getCleanedNgram().isBlank() || semantics.getStopwordsWithoutSentimentRelevance().contains(ngram.getCleanedNgram().toLowerCase())) {
                    continue;
                }

                /*
            
                ----- 3 -----
            Checking if the ngram matches a heuristic for ORGANIC TERMS
            
            (mapH9 lists corporate speak)
            
                 */
                if (alreadyExaminedNGramInOrganic.contains(ngram)) {
                    continue;
                }

                boolean stripped = false;
                termAndItsConditionalExpressions = lexiconsAndTheirConditionalExpressions.getMapH9().get(ngram.getCleanedNgram().toLowerCase());
                if (termAndItsConditionalExpressions == null) {
                    termAndItsConditionalExpressions = lexiconsAndTheirConditionalExpressions.getMapH9().get(ngram.getCleanedAndStrippedNgram().toLowerCase());
                    stripped = true;
                }

                if (termAndItsConditionalExpressions != null) {
                    ResultOneHeuristics resultOneHeuristics = TermLevelHeuristicsVerifier.checkHeuristicsOnOneNGram(ngram, sentenceLikeFragment, termAndItsConditionalExpressions, lexiconsAndTheirConditionalExpressions, stripped, semantics.getStopwordsWithoutSentimentRelevance());
                    resultsHeuristics.add(resultOneHeuristics);
                    alreadyExaminedNGramInOrganic.add(ngram);
                }
            }
        }
        // adding all the results of the heuristics to the Document object
        document.getResultsOfHeuristics().addAll(resultsHeuristics);

        return document;

    }
}
