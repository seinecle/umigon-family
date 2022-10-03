///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.clementlevallois.umigon.classifier.organic;
//
//import net.clementlevallois.umigon.semantics.resources.Semantics;
//import net.clementlevallois.umigon.model.Document;
//
///**
// *
// * @author LEVALLOIS
// */
//public class ControllerForTestsOrganic {
//
//    /**
//     * @param args the command line arguments
//     */
//    public static void main(String[] args) throws Exception {
//
//        Semantics semanticsEN = new Semantics();
//        semanticsEN.loader("en");
//        Semantics semanticsFR = new Semantics();
//        semanticsFR.loader("fr");
//
//        ClassifierOrganicOneDocument oneDocument = new ClassifierOrganicOneDocument(semanticsEN);
//        Document doc = new Document();
//        doc.setText("BUY now, nocode is not horrible");
//        Document result = oneDocument.call(doc);
//        System.out.println("result: " + result.getExplanationSentiment());
//        System.out.println("expected: promoted");
//
//        System.out.println("-------------");
//
//        oneDocument = new ClassifierOrganicOneDocument(semanticsFR);
//        doc = new Document();
//        doc.setText("Cédez à vos envies beaut� sur http://loreal-paris.fr parce que vous le valez bien ");
//        result = oneDocument.call(doc);
//        System.out.println("result: " + result.getExplanationSentiment());
//        System.out.println("expected: promoted");
//
//        System.out.println("-------------");
//
//        ClassifierOrganicOneWordInADocument oneWordInADocument = new ClassifierOrganicOneWordInADocument(semanticsEN);
//        String resultString = oneWordInADocument.call("BUY", "BUY now, nocode is not horrible");
//        System.out.println("result: " + resultString);
//        System.out.println("expected: commercial tone / promoted");
//
//        System.out.println("-------------");
//
//    }
//}
