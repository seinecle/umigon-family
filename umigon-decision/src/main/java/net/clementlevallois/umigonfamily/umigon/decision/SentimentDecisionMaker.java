/*
 * author: Clément Levallois
 */
package net.clementlevallois.umigonfamily.umigon.decision;

import java.util.Set;
import net.clementlevallois.umigon.model.Document;

/**
 *
 * @author LEVALLOIS
 */
public class SentimentDecisionMaker {

    Document document;
    Set<String> negations;
    Set<String> moderators;
    Set<String> markersOfIrony;

    public SentimentDecisionMaker(Document document, Set<String> negations, Set<String> moderators, Set<String> markersOfIrony) {
        this.document = document;
        this.negations = negations;
        this.moderators = moderators;
        this.markersOfIrony = markersOfIrony;
    }

    public void doCheckOnNegations() {
        WhenTextContainsPositiveNegativeAndNegation negationsCheck = new WhenTextContainsPositiveNegativeAndNegation(document, negations);
        document = negationsCheck.containsANegationAndAPositiveAndNegativeSentiment();
    }

    public void doCheckOnModerators() {
        WhenTextContainsAModerator moderatorsCheck = new WhenTextContainsAModerator(document, moderators);
        document = moderatorsCheck.containsAModerator();
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
