/*
 * author: Clément Levallois
 */
package net.clementlevallois.umigonfamily.umigon.decision;

import java.util.Set;
import net.clementlevallois.umigon.heuristics.tools.LoaderOfLexiconsAndConditionalExpressions;
import net.clementlevallois.umigon.model.classification.Document;

/**
 *
 * @author LEVALLOIS
 */
public class SentimentDecisionMaker {

    Document document;
    LoaderOfLexiconsAndConditionalExpressions lexiconsAndTheirConditionalExpressions;
    Set<String> negations;
    Set<String> moderatorsBackward;
    Set<String> moderatorsForward;
    Set<String> markersOfIrony;

    public SentimentDecisionMaker(Document document, LoaderOfLexiconsAndConditionalExpressions lexiconsAndTheirConditionalExpressions) {
        this.document = document;
        this.lexiconsAndTheirConditionalExpressions = lexiconsAndTheirConditionalExpressions;
        this.negations = lexiconsAndTheirConditionalExpressions.getSetNegations();
        this.moderatorsBackward = lexiconsAndTheirConditionalExpressions.getSetModeratorsBackward();
        this.moderatorsForward = lexiconsAndTheirConditionalExpressions.getSetModeratorsForward();
        this.markersOfIrony = lexiconsAndTheirConditionalExpressions.getSetIronicallyPositive();
    }

    public void doCheckQuestionMark() {
        QuestionMarkAtTheEnd check = new QuestionMarkAtTheEnd(document);
        document = check.checkIt();
    }

    public void doCheckOnNegations() {
        WhenTextContainsPositiveNegativeAndNegation negationsCheck = new WhenTextContainsPositiveNegativeAndNegation(document, negations);
        document = negationsCheck.containsANegationAndAPositiveAndNegativeSentiment();
    }

    public void doCheckOnModerators() {
        WhenTextContainsAModerator moderatorsCheck = new WhenTextContainsAModerator(document, moderatorsForward, moderatorsBackward);
        document = moderatorsCheck.containsAModeratorForward();
        document = moderatorsCheck.containsAModeratorBackward();
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
