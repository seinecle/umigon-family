package net.clementlevallois.umigon.explain.tests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import net.clementlevallois.umigon.classifier.resources.Semantics;
import net.clementlevallois.umigon.classifier.sentiment.ClassifierSentimentOneDocument;
import net.clementlevallois.umigon.model.Document;
import net.clementlevallois.umigon.explain.controller.UmigonExplain;
import net.clementlevallois.umigon.explain.parameters.HtmlSettings;

/**
 *
 * @author LEVALLOIS
 */
public class ControllerForTestsSentiment {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        HtmlSettings htmlSettings = new HtmlSettings();
        
        Semantics semanticsEN = new Semantics();
        semanticsEN.loader("en");

        Semantics semanticsFR = new Semantics();
        semanticsFR.loader("fr");

        Semantics semanticsES = new Semantics();
        semanticsES.loader("es");


        ClassifierSentimentOneDocument classifierOneDocumentFR = new ClassifierSentimentOneDocument(semanticsFR);
        Document docFR;
        Document resultFR;
        Document docES;
        Document resultES;
        Document resultEN;
        Document docEN;
        String markers;

        ClassifierSentimentOneDocument classifierOneDocumentEN = new ClassifierSentimentOneDocument(semanticsEN);
        ClassifierSentimentOneDocument classifierOneDocumentES = new ClassifierSentimentOneDocument(semanticsES);

        docEN = new Document();
        docEN.setText("I don’t think he really did a great job");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        markers = UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultEN, "fr");
        System.out.println("semantic markers found: " + markers);
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultEN, "fr"));        

        docEN = new Document();
        docEN.setText("I don't think #Macron is very smart");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        markers = UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultEN, "fr");
        System.out.println("semantic markers found: " + markers);
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultEN, "fr"));        

        docEN = new Document();
        docEN.setText("#absurd");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        markers = UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultEN, "fr");
        System.out.println("semantic markers found: " + markers);
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultEN, "fr"));        

        docEN = new Document();
        docEN.setText("I didn't know. Thank you.");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        markers = UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultEN, "fr");
        System.out.println("semantic markers found: " + markers);
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultEN, "fr"));        

        docEN = new Document();
        docEN.setText("Good compromise in the forced absence of the field test");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        markers = UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultEN, "fr");
        System.out.println("semantic markers found: " + markers);
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultEN, "fr"));        

        docEN = new Document();
        docEN.setText("he didn't really do a great job");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        markers = UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultEN, "fr");
        System.out.println("semantic markers found: " + markers);
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultEN, "fr"));        

        docEN = new Document();
        docEN.setText("Why on earth is the price of the new iPhone so high?!!!!");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        markers = UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultEN, "fr");
        System.out.println("semantic markers found: " + markers);
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultEN, "fr"));        

        docES = new Document();
        docES.setText("Odio");
        resultES = classifierOneDocumentES.call(docES);
        System.out.println("test: " + docES.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultES, "fr"));
        markers = UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultES, "fr");
        System.out.println("semantic markers found: " + markers);
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultES, "fr"));        
        
        docEN = new Document();
        docEN.setText("very informative and friendly. i did not feel judged at all");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        markers = UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultEN, "fr");
        System.out.println("semantic markers found: " + markers);
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultEN, "fr"));        
        
        docFR = new Document();
        docFR.setText("D’habitude je n’aime pas trop la charcuterie mais j’ai beaucoup apprécie la cuisine des bouchons lyonnais 😊");
        resultFR = classifierOneDocumentFR.call(docFR);
        System.out.println("test: " + docFR.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultFR, "fr"));
        markers = UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultFR, "fr");
        System.out.println("semantic markers found: " + markers);
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultFR, "fr"));

        docEN = new Document();
        docEN.setText("KAMALA 2020!!!!!!");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        markers = UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultEN, "fr");
        System.out.println("semantic markers found: " + markers);
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultEN, "fr"));

        docEN = new Document();
        docEN.setText("Nuts have properties)");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        markers = UmigonExplain.getExplanationOfHeuristicResultsHtml(resultEN, "fr", htmlSettings, false);
        System.out.println("semantic markers found: " + markers);
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsHtml(resultEN, "fr", htmlSettings));

        docEN = new Document();
        docEN.setText("😊😊😊😊😊😊");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        markers = UmigonExplain.getExplanationOfHeuristicResultsHtml(resultEN, "fr", htmlSettings, false);
        System.out.println("semantic markers found: " + markers);
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsHtml(resultEN, "fr", htmlSettings));

        docEN = new Document();
        docEN.setText("The staff was not welcoming the breakfast was practically insufcient and after 9am they did not");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        markers = UmigonExplain.getExplanationOfHeuristicResultsHtml(resultEN, "fr", htmlSettings, false);
        System.out.println("semantic markers found: " + markers);
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsHtml(resultEN, "fr", htmlSettings));

        System.out.println("expected: negative tone");
        System.out.println("-----------");
        
        docEN = new Document();
        docEN.setText("this is baaaad :-)");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsHtml(resultEN, "fr", htmlSettings, false));
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsHtml(resultEN, "fr", htmlSettings));

        System.out.println("expected: positive tone");
        System.out.println("-----------");
        
        docEN.setText("nocode is not horrible");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsHtml(resultEN, "fr", htmlSettings, false));
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsHtml(resultEN, "fr", htmlSettings));

        System.out.println("expected: neutral tone");
        System.out.println("-----------");
        
        docEN = new Document();
        docEN.setText("I am fond of nocode functions");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultEN, "fr"));
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultEN, "fr"));
        System.out.println("expected: positive tone");
        System.out.println("-----------");

        docEN = new Document();
        docEN.setText("I am not glad of this");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultEN, "fr"));
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultEN, "fr"));
        System.out.println("expected: negative tone");
        System.out.println("-----------");

        docEN = new Document();
        docEN.setText("I am fond of coding but I prefer nocode functions");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultEN, "fr"));
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultEN, "fr"));
        System.out.println("expected: neutral tone");
        System.out.println("-----------");

        docEN = new Document();
        docEN.setText("Men and women, our defenders! You are brilliantly defending the country from one of the most powerful countries in the world.");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultEN, "fr"));
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultEN, "fr"));
        System.out.println("expected: positive tone");
        System.out.println("-----------");

        docEN = new Document();
        docEN.setText("According to preliminary data, unfortunately, we have lost 137 of our heroes today - our citizens. 10 of them are officers. 316 are wounded");
        resultEN = classifierOneDocumentEN.call(docEN);
        System.out.println("test: " + docEN.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultEN, "fr"));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultEN, "fr"));
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultEN, "fr"));
        System.out.println("expected: negative tone");
        System.out.println("-----------");        
        
        docFR = new Document();
        docFR.setText("J'exprime une opinion sur cet élu Marcel sauras tu la deviner #Marceldémission");
        resultFR = classifierOneDocumentFR.call(docFR);
        System.out.println("test: " + docFR.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultFR, "fr"));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultFR, "fr"));
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultFR, "fr"));
        System.out.println("expected: negative tone");
        System.out.println("-----------");
        
        
        docFR = new Document();
        docFR.setText("j'en ai marre de la situation");
        resultFR = classifierOneDocumentFR.call(docFR);
        System.out.println("test: " + docFR.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultFR, "fr"));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultFR, "fr"));
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultFR, "fr"));
        System.out.println("expected: negative tone");
        System.out.println("-----------");

        
        docFR = new Document();
        docFR.setText("#Elections #Elections2020#Municipales #Municipales2020ElectionsMunicipales ElectionsMunicipales2020https://t.co/Lr9rGG71M4");
        resultFR = classifierOneDocumentFR.call(docFR);
        System.out.println("test: " + docFR.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultFR, "fr"));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultFR, "fr"));
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultFR, "fr"));
        System.out.println("expected: neutral tone");
        System.out.println("-----------");

        docFR = new Document();
        docFR.setText("Il est gentil, pour être poli");
        resultFR = classifierOneDocumentFR.call(docFR);
        System.out.println("test: " + docFR.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultFR, "fr"));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsHtml(resultFR, "fr", htmlSettings, false));
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsHtml(resultFR, "fr", htmlSettings));
        System.out.println("expected: negative tone");
        System.out.println("-----------");

        docFR = new Document();
        docFR.setText("La meuf qui hurle dans le bus parce qu' on s' est assis à côté d' elle... 😒");
        resultFR = classifierOneDocumentFR.call(docFR);
        System.out.println("test: " + docFR.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultFR, "fr"));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultFR, "fr"));
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultFR, "fr"));
        System.out.println("expected: negative tone");
        System.out.println("-----------");

        docFR = new Document();
        docFR.setText("Miss France comme chaque année! Tant qu'ils mettrons ce programme CONTRE le téléthon!");
        resultFR = classifierOneDocumentFR.call(docFR);
        System.out.println("test: " + docFR.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultFR, "fr"));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultFR, "fr"));
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultFR, "fr"));
        System.out.println("expected: negative tone");
        System.out.println("-----------");

        docFR = new Document();
        docFR.setText("Miss France comme chaque année! Tant qu'ils mettrons ce programme contre le téléthon!");
        resultFR = classifierOneDocumentFR.call(docFR);
        System.out.println("test: " + docFR.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultFR, "fr"));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultFR, "fr"));
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultFR, "fr"));
        System.out.println("expected: neutral tone");
        System.out.println("-----------");

        docFR = new Document();
        docFR.setText("Nocode c'est tendance :)");
        resultFR = classifierOneDocumentFR.call(docFR);
        System.out.println("test: " + docFR.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultFR, "fr"));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsHtml(resultFR, "fr", htmlSettings, false));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultFR, "fr"));
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultFR, "fr"));
        System.out.println("expected: negative tone");
        System.out.println("-----------");

        docFR = new Document();
        docFR.setText("Il y a de vraies burnes");
        resultFR = classifierOneDocumentFR.call(docFR);
        System.out.println("test: " + docFR.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultFR, "fr"));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultFR, "fr"));
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultFR, "fr"));
        System.out.println("expected: negative tone");
        System.out.println("-----------");

        docFR = new Document();
        docFR.setText("Il a de vraies burnes");
        resultFR = classifierOneDocumentFR.call(docFR);
        System.out.println("test: " + docFR.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultFR, "fr"));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultFR, "fr"));
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultFR, "fr"));
        System.out.println("expected: positive tone");
        System.out.println("-----------");


        docFR = new Document();
        docFR.setText("@Marieadelaided3 Bonsoir. C'est toujours un plaisir de lire vos messages et critiques. Mais une nouvelle fois, pouvons nous faire quelque chose pour vous ?");
        resultFR = classifierOneDocumentFR.call(docFR);
        System.out.println("test: " + docFR.getText());
        System.out.println("result: " + UmigonExplain.getSentimentPlainText(resultFR, "fr"));
        System.out.println("semantic markers found: " + UmigonExplain.getExplanationOfHeuristicResultsPlainText(resultFR, "fr"));
        System.out.println("decisions made: " + UmigonExplain.getExplanationsOfDecisionsPlainText(resultFR, "fr"));
        System.out.println("expected: positive tone");
        System.out.println("-----------");


        
//        ClassifierSentimentOneWordInADocument oneWordInADocument = new ClassifierSentimentOneWordInADocument(semanticsEN);
//        String resultString = oneWordInADocument.call("horrible", "nocode is not horrible :-)");
//        System.out.println("test one word \"horrible\" in: nocode is not horrible :-)");
//        System.out.println("result: " + resultString);
//        System.out.println("expected: neutral tone");
    }

}
