/*
 * author: Clément Levallois
 */
package net.clementlevallois.umigonfamily.umigon.decision;

import java.util.Set;
import net.clementlevallois.umigon.heuristics.tools.LoaderOfLexiconsAndConditionalExpressions;
import net.clementlevallois.umigon.model.SentenceLike;
import net.clementlevallois.umigon.model.classification.Document;

/**
 *
 * @author LEVALLOIS
 */
public class SentimentDecisionMaker {

    Document document;
    SentenceLike sentence;
    Set<String> negations;
    Set<String> moderatorsBackward;
    Set<String> moderatorsForward;
    Set<String> markersOfIrony;
    Set<String> intenseWords;
    Set<String> subjectiveTerms;
    LoaderOfLexiconsAndConditionalExpressions lexiconsAndTheirConditionalExpressions;

    public SentimentDecisionMaker(Document document, LoaderOfLexiconsAndConditionalExpressions lexiconsAndTheirConditionalExpressions) {
        this.document = document;
        this.negations = lexiconsAndTheirConditionalExpressions.getSetNegations();
        this.moderatorsBackward = lexiconsAndTheirConditionalExpressions.getSetModeratorsBackward();
        this.moderatorsForward = lexiconsAndTheirConditionalExpressions.getSetModeratorsForward();
        this.markersOfIrony = lexiconsAndTheirConditionalExpressions.getSetIronicallyPositive();
        this.intenseWords = lexiconsAndTheirConditionalExpressions.getSetStrong();
        this.subjectiveTerms = lexiconsAndTheirConditionalExpressions.getSetSubjective();
        this.lexiconsAndTheirConditionalExpressions = lexiconsAndTheirConditionalExpressions;
    }

    public SentimentDecisionMaker(SentenceLike sentence, LoaderOfLexiconsAndConditionalExpressions lexiconsAndTheirConditionalExpressions) {
        this.intenseWords = lexiconsAndTheirConditionalExpressions.getSetStrong();
        this.subjectiveTerms = lexiconsAndTheirConditionalExpressions.getSetSubjective();
        this.sentence = sentence;
    }

    public boolean skipSentimentEvaluation() {
        QuestionMarkAtTheEnd check = new QuestionMarkAtTheEnd(sentence, intenseWords, subjectiveTerms);
        return check.skipSentimentEvaluation();
    }

    public void doCheckOnNegations() {
        WhenTextContainsPositiveNegativeAndNegation negationsCheck = new WhenTextContainsPositiveNegativeAndNegation(document, negations);
        document = negationsCheck.containsANegationAndAPositiveAndNegativeSentiment();
    }

    public void doCheckOnModerators() {
        WhenTextContainsAModerator moderatorsCheck = new WhenTextContainsAModerator(moderatorsForward, moderatorsBackward, lexiconsAndTheirConditionalExpressions);
        document = moderatorsCheck.containsAModeratorForward(document);
        document = moderatorsCheck.containsAModeratorBackward(document);
    }

    public void doCheckOnSarcasm() {
        IsThereATraceOfSarcasm sarcasmCheck = new IsThereATraceOfSarcasm(document, markersOfIrony);
        document = sarcasmCheck.checkForIrony();
        document = sarcasmCheck.checkForAntiPhrase();
    }

    public void doCheckOnWinnerTakesAll() {
        WinnerTakesAllChecker winnerTakesAllChecker = new WinnerTakesAllChecker(document);
        document = winnerTakesAllChecker.considerStrongSigns();
    }

    public void finalAdjudication() {
        FinalDecisionMaker finalDecision = new FinalDecisionMaker(document);
        document = finalDecision.takeIt();
    }

    public Document getDocument() {
        return document;
    }

    public void doCheckOnExclamationPoints() {
        ExclamationPoints checkExclamationPoints = new ExclamationPoints(document);
        document = checkExclamationPoints.checkIt();
    }
}
