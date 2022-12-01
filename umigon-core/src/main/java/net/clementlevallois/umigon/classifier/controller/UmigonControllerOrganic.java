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
import net.clementlevallois.umigon.classifier.organic.ClassifierOrganicOneDocument;
import net.clementlevallois.umigon.classifier.resources.Semantics;
import net.clementlevallois.umigon.model.Document;

/**
 *
 * @author LEVALLOIS
 */
public class UmigonControllerOrganic {

    boolean initCompleted = false;
    ClassifierOrganicOneDocument classifierMachineForPriorInitialization;
    Semantics semanticsFR = new Semantics();
    Semantics semanticsEN = new Semantics();

    public static void main(String args[]) throws URISyntaxException, Exception {
        Document document = new UmigonControllerOrganic().runSingleLine("C'est vraiment bien nul", "fr");
        System.out.println("language du texte: " + document.getLanguage());

    }

    public UmigonControllerOrganic() {
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
            initCompleted = true;
        }
    }

    public List<Document> runMultipleLinesOneLanguage(List<String> stringsToBeProcessed, String lang) throws Exception {
        ClassifierOrganicOneDocument classifierMachine = null;
        List<Document> list = new ArrayList();
        if (lang.equals("fr")) {
            classifierMachine = new ClassifierOrganicOneDocument(semanticsFR);
        }
        if (lang.equals("en")) {
            classifierMachine = new ClassifierOrganicOneDocument(semanticsEN);
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
        ClassifierOrganicOneDocument machineFR = new ClassifierOrganicOneDocument(semanticsFR);
        ClassifierOrganicOneDocument machineEN = new ClassifierOrganicOneDocument(semanticsEN);
        for (Map.Entry<String, String> entry : stringsWithLangs.entrySet()) {
            Document document = new Document(entry.getKey());
            if (entry.getValue().equals("fr")) {
                document = machineFR.call(document);
            }
            if (entry.getValue().equals("en")) {
                document = machineEN.call(document);
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
        ClassifierOrganicOneDocument classifierMachine = null;
        if (lang.equals("fr")) {
            classifierMachine = new ClassifierOrganicOneDocument(semanticsFR);
        }
        if (lang.equals("en")) {
            classifierMachine = new ClassifierOrganicOneDocument(semanticsEN);
        }
        Document document = new Document(stringToBeProcessed);
        document = classifierMachine.call(document);
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
            classifierMachineForPriorInitialization = new ClassifierOrganicOneDocument(semanticsFR);
        }
        if (lang.equals("en")) {
            classifierMachineForPriorInitialization = new ClassifierOrganicOneDocument(semanticsEN);
        }
    }

}
