/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.clementlevallois.umigon.classifier.controller;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.clementlevallois.umigon.classifier.resources.Semantics;
import net.clementlevallois.umigon.classifier.sentiment.ClassifierSentimentOneDocument;
import net.clementlevallois.umigon.model.Document;

/**
 *
 * @author LEVALLOIS
 */
public class UmigonController {

    boolean initCompleted = false;
    ClassifierSentimentOneDocument classifierMachineForPriorInitialization;
    Semantics semanticsFR = new Semantics();
    Semantics semanticsEN = new Semantics();
    Semantics semanticsES = new Semantics();
    ClassifierSentimentOneDocument machineFR;
    ClassifierSentimentOneDocument machineEN;
    ClassifierSentimentOneDocument machineES;

    public static void main(String args[]) throws URISyntaxException, Exception {
        Document document = new UmigonController().runSingleLine("rappelez vous que si agnes buzin au lieu de mettre des mots dans les aéroports pendant 1 mois avait pris des mesures on serait pas passé de 0% de bridage à 100% en 3j mais on aurait �t� � 50% depuis 15 jours. bien + vivable #macron20h #covid19france #castaner #confinementtotal", "fr");
        System.out.println("language du texte: " + document.getLanguage());
        System.out.println("sentiment du texte: " + document.getExplanationSentimentHtml());

    }

    public UmigonController() {
        try {
            init();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * history of versions: 1.0: start using the maven repo from
     * https://github.com/optimaize/language-detector
     */
    /**
     * @param stringToBeProcessed the string to be processed
     * @return a Document, which is an object containing much info on the
     * processed String
     * @throws java.net.URISyntaxException
     */
    private void init() throws Exception {
        if (!initCompleted) {
            semanticsFR.loader("fr");
            semanticsEN.loader("en");
            semanticsEN.loader("es");
            machineFR = new ClassifierSentimentOneDocument(semanticsFR);
            machineEN = new ClassifierSentimentOneDocument(semanticsEN);
            machineES = new ClassifierSentimentOneDocument(semanticsES);
            initCompleted = true;
        }
    }

    public List<Document> runMultipleLinesOneLanguage(List<String> stringsToBeProcessed, String lang) throws Exception {
        ClassifierSentimentOneDocument classifierMachine = null;
        List<Document> list = new ArrayList();
        if (lang.equals("fr")) {
            classifierMachine = new ClassifierSentimentOneDocument(semanticsFR);
        }
        if (lang.equals("en")) {
            classifierMachine = new ClassifierSentimentOneDocument(semanticsEN);
        }
        if (lang.equals("es")) {
            classifierMachine = new ClassifierSentimentOneDocument(semanticsES);
        }
        for (String stringToBeProcessed : stringsToBeProcessed) {
            Document document = new Document(stringToBeProcessed);
            document = classifierMachine.call(document);
            list.add(document);
        }
        return list;

    }

    public List<Document> runMultipleLinesMultipleLanguages(Map<String, String> stringsWithLangs) throws Exception {
        List<Document> list = new ArrayList();
        for (Map.Entry<String, String> entry : stringsWithLangs.entrySet()) {
            Document document = new Document(entry.getKey());
            if (entry.getValue().equals("fr")) {
                document = machineFR.call(document);
            }
            if (entry.getValue().equals("en")) {
                document = machineEN.call(document);
            }
            if (entry.getValue().equals("es")) {
                document = machineES.call(document);
            }
            list.add(document);
        }
        return list;

    }

    public Document runSingleLine(String stringToBeProcessed, String lang) throws URISyntaxException, Exception {
        if (stringToBeProcessed == null || stringToBeProcessed.isBlank()) {
            Document document = new Document(stringToBeProcessed);
            return document;
        }
            Document document = new Document(stringToBeProcessed);
        if (lang.equals("fr")) {
            document = machineFR.call(document);
            return document;
        }
        if (lang.equals("en")) {
            document = machineEN.call(document);
            return document;
        }
        if (lang.equals("es")) {
            document = machineES.call(document);
            return document;
        }
        return document;
    }

    public Document runSingleLineAfterClassifierInitialization(String stringToBeProcessed) throws URISyntaxException, Exception {
        if (stringToBeProcessed == null || stringToBeProcessed.isBlank()) {
            Document document = new Document(stringToBeProcessed);
            return document;
        }
        Document document = new Document(stringToBeProcessed);
        document = classifierMachineForPriorInitialization.call(document);
        return document;
    }

    public void initializeClassifier(String lang) throws Exception {
        if (lang.equals("fr")) {
            classifierMachineForPriorInitialization = new ClassifierSentimentOneDocument(semanticsFR);
        }
        if (lang.equals("en")) {
            classifierMachineForPriorInitialization = new ClassifierSentimentOneDocument(semanticsEN);
        }
        if (lang.equals("es")) {
            classifierMachineForPriorInitialization = new ClassifierSentimentOneDocument(semanticsES);
        }
    }

    public Semantics getSemanticsFR() {
        return semanticsFR;
    }

    public Semantics getSemanticsEN() {
        return semanticsEN;
    }
    
    public Semantics getSemanticsES() {
        return semanticsES;
    }
    
    

}
