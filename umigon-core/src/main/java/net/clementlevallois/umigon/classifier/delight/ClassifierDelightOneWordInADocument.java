///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.clementlevallois.umigon.classifier.delight;
//
////import Admin.Parameters;
//import java.util.HashSet;
//import net.clementlevallois.umigon.model.TermWithConditionalExpressions;
//import net.clementlevallois.umigon.heuristics.tools.LoaderOfLexiconsAndConditionalExpressions;
//
//import net.clementlevallois.umigon.model.Document;
//import java.util.List;
//import java.util.Map;
//import net.clementlevallois.umigon.classifier.commonOps.TextCleaningOperations;
//import net.clementlevallois.umigon.heuristics.tools.TermLevelHeuristicsVerifier;
//import net.clementlevallois.umigon.model.Category;
//import net.clementlevallois.umigon.model.Category.CategoryEnum;
//import net.clementlevallois.umigon.model.ResultOneHeuristics;
//import net.clementlevallois.umigon.model.Term;
//import net.clementlevallois.umigon.model.Text;
//import net.clementlevallois.umigon.semantics.resources.Semantics;
//
//public class ClassifierDelightOneWordInADocument {
//
//    Semantics semantics;
//    LoaderOfLexiconsAndConditionalExpressions heuristics;
//
//    public ClassifierDelightOneWordInADocument(Semantics semantics) throws Exception {
//        this.semantics = semantics;
//        heuristics = semantics.getLexiconsAndTheirConditionalExpressions();
//
//    }
//
//    public String call(String term, String text) {
//
//        if (!text.toLowerCase().contains(term.toLowerCase())){
//            return "text does not contain this term";
//        }
//
//        Document document = new Document();
//
//        TermWithConditionalExpressions heuristic;
//
//        
//        String termCleaned = TextCleaningOperations.stripText(term);
//        String termStripped = TextCleaningOperations.stripText(termCleaned);
//
//        String textCleaned = TextCleaningOperations.cleanText(text);
//        String textStripped = TextCleaningOperations.stripText(textCleaned);
//        
//        Term termObject = new Term();
//        termObject.setString(termCleaned);
//        termObject.setCleanedAndStrippedForm(termStripped);
//        
//        Text textObject = new Text();
//        textObject.setOriginalForm(text);
//        textObject.setCleanedForm(textCleaned);
//        textObject.setStrippedForm(textStripped);
//        
//        
//
////        String textStrippedLowercase = textStripped.toLowerCase();
////        
////        int indexTermStrippedLowercase = textStripped.toLowerCase().indexOf(termLowerCaseStripped);
//
//
//        // now looking at a single term in this doc: what is its sentiment?
//        heuristic = heuristics.getMapH17().get(termObject.getOriginalFormLowercase());
//        if (heuristic != null) {
//            List<ResultOneHeuristics> results = TermLevelHeuristicsVerifier.checkHeuristicsOnNGrams(false, termObject, textObject, heuristics.getMapH17(), new HashSet(), heuristics);
//            document.getResultsOfHeuristics().addAll(results);
//        } else if (!termObject.getStrippedFormLowercase().equals(termObject.getOriginalFormLowercase())) {
//            List<ResultOneHeuristics> results = TermLevelHeuristicsVerifier.checkHeuristicsOnNGrams(true, termObject, textObject, heuristics.getMapH17(), new HashSet(), heuristics);
//            document.getResultsOfHeuristics().addAll(results);
//        }
//
//        // PREPARING THE OUTPUT
//        Map<Integer, ResultOneHeuristics> allHeuristicsResultsForOneCategory = document.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._17);
//        if (!allHeuristicsResultsForOneCategory.isEmpty()) {
//            return CategoryEnum._17.toString();
//        } else {
//            return CategoryEnum._10.toString();
//        }
//
//    }
//}
