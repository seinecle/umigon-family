/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.clementlevallois.umigon.classifier.delight;

import net.clementlevallois.umigon.classifier.resources.Semantics;
import net.clementlevallois.umigon.model.Document;

/**
 *
 * @author LEVALLOIS
 */
public class ControllerForTestsDelight {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        Semantics semantics = new Semantics();
        semantics.loader("en");
        ClassifierDelightOneDocument oneWord = new ClassifierDelightOneDocument(semantics);
        Document doc = new Document();
        doc.setText("nocode is an awesome thing :)");
        Document result = oneWord.call(doc);
        System.out.println("result: " + result.getExplanationSentimentHtml());
        System.out.println("expected result: " + "[positive tone, delight]");
    }

}
