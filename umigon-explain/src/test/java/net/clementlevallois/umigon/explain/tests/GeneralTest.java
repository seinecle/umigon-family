/*
 * Copyright Clement Levallois 2021-2023. License Attribution 4.0 Intertnational (CC BY 4.0)
 */
package net.clementlevallois.umigon.explain.tests;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import net.clementlevallois.umigon.classifier.resources.Semantics;
import net.clementlevallois.umigon.classifier.sentiment.ClassifierSentimentOneDocument;
import net.clementlevallois.umigon.explain.controller.UmigonExplain;
import net.clementlevallois.umigon.explain.parameters.HtmlSettings;
import net.clementlevallois.umigon.model.classification.Document;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author LEVALLOIS
 */
public class GeneralTest {

    @Test
    public void testSentiment() throws IOException {

        ResourceBundle localeBundle = UmigonExplain.getLocaleBundle("en");

        Assert.assertNotNull(localeBundle);

        HtmlSettings htmlSettings = new HtmlSettings();

        Semantics semanticsEN = new Semantics();
        semanticsEN.loader("en");

        Semantics semanticsFR = new Semantics();
        semanticsFR.loader("fr");

        Semantics semanticsES = new Semantics();
        semanticsES.loader("es");

        Document resultAnalysis;
        Document document;
        String markers;

        ClassifierSentimentOneDocument classifierOneDocumentFR = new ClassifierSentimentOneDocument(semanticsFR);
        ClassifierSentimentOneDocument classifierOneDocumentEN = new ClassifierSentimentOneDocument(semanticsEN);
        ClassifierSentimentOneDocument classifierOneDocumentES = new ClassifierSentimentOneDocument(semanticsES);

        ClassLoader classLoader = getClass().getClassLoader();

        System.out.println("test positif English texts:");
        File fileWithExamples = new File(classLoader.getResource("en-pos.txt").getFile());
        List<String> lines = Files.readAllLines(fileWithExamples.toPath());
        for (String line : lines) {
            document = new Document();
            document.setText(line);
            resultAnalysis = classifierOneDocumentEN.call(document);
            markers = UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultAnalysis, "fr");
            System.out.println("full explanation: " + markers);
            System.out.println("decisions: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultAnalysis, "fr"));
            Assert.assertEquals("sentiment positif", UmigonExplain.getSentimentPlainText(resultAnalysis, "fr"));
        }
        System.out.println("------------ *********** ----------------");

        System.out.println("test negative English texts:");
        fileWithExamples = new File(classLoader.getResource("en-neg.txt").getFile());
        lines = Files.readAllLines(fileWithExamples.toPath());
        for (String line : lines) {
            document = new Document();
            document.setText(line);
            resultAnalysis = classifierOneDocumentEN.call(document);
            markers = UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultAnalysis, "fr");
            System.out.println("full explanation: " + markers);
            System.out.println("decisions: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultAnalysis, "fr"));
            Assert.assertEquals("sentiment négatif", UmigonExplain.getSentimentPlainText(resultAnalysis, "fr"));
        }
        System.out.println("------------ *********** ----------------");

        System.out.println("test neutral English texts:");
        fileWithExamples = new File(classLoader.getResource("en-neutral.txt").getFile());
        lines = Files.readAllLines(fileWithExamples.toPath());
        for (String line : lines) {
            document = new Document();
            document.setText(line);
            resultAnalysis = classifierOneDocumentEN.call(document);
            markers = UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultAnalysis, "fr");
            System.out.println("full explanation: " + markers);
            System.out.println("decisions: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultAnalysis, "fr"));
            Assert.assertEquals("sentiment neutre", UmigonExplain.getSentimentPlainText(resultAnalysis, "fr"));
        }
        System.out.println("------------ *********** ----------------");

        System.out.println("test neutral French texts:");
        fileWithExamples = new File(classLoader.getResource("fr-neutral.txt").getFile());
        lines = Files.readAllLines(fileWithExamples.toPath());
        for (String line : lines) {
            document = new Document();
            document.setText(line);
            resultAnalysis = classifierOneDocumentFR.call(document);
            markers = UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultAnalysis, "fr");
            System.out.println("full explanation: " + markers);
            System.out.println("decisions: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultAnalysis, "fr"));
            Assert.assertEquals("sentiment neutre", UmigonExplain.getSentimentPlainText(resultAnalysis, "fr"));
        }
        System.out.println("------------ *********** ----------------");

        System.out.println("test positive French texts:");
        fileWithExamples = new File(classLoader.getResource("fr-pos.txt").getFile());
        lines = Files.readAllLines(fileWithExamples.toPath());
        for (String line : lines) {
            document = new Document();
            document.setText(line);
            resultAnalysis = classifierOneDocumentFR.call(document);
            markers = UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultAnalysis, "fr");
            System.out.println("full explanation: " + markers);
            System.out.println("decisions: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultAnalysis, "fr"));
            Assert.assertEquals("sentiment positif", UmigonExplain.getSentimentPlainText(resultAnalysis, "fr"));
        }
        System.out.println("------------ *********** ----------------");

        System.out.println("test negative French texts:");
        fileWithExamples = new File(classLoader.getResource("fr-neg.txt").getFile());
        lines = Files.readAllLines(fileWithExamples.toPath());
        for (String line : lines) {
            document = new Document();
            document.setText(line);
            resultAnalysis = classifierOneDocumentFR.call(document);
            markers = UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultAnalysis, "fr");
            System.out.println("full explanation: " + markers);
            System.out.println("decisions: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultAnalysis, "fr"));
            Assert.assertEquals("sentiment négatif", UmigonExplain.getSentimentPlainText(resultAnalysis, "fr"));
        }
        System.out.println("------------ *********** ----------------");

    }

}
