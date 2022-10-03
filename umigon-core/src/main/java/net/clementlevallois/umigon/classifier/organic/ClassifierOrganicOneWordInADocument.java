///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.clementlevallois.umigon.classifier.organic;
//
////import Admin.Parameters;
//import java.util.HashSet;
//import net.clementlevallois.umigon.model.TermWithConditionalExpressions;
//import net.clementlevallois.umigon.heuristics.tools.LoaderOfLexiconsAndConditionalExpressions;
//
//import net.clementlevallois.umigon.model.Document;
//import java.util.List;
//import java.util.Queue;
//import net.clementlevallois.umigon.classifier.commonOps.TextCleaningOperations;
//import net.clementlevallois.umigon.model.Categories.Category;
//import net.clementlevallois.umigon.model.CategoryAndIndex;
//import net.clementlevallois.umigon.semantics.resources.Semantics;
//import net.clementlevallois.utils.StatusCleaner;
//
//public class ClassifierOrganicOneWordInADocument {
//
//    Semantics semantics;
//    LoaderOfLexiconsAndConditionalExpressions heuristics;
//
//    public ClassifierOrganicOneWordInADocument(Semantics semantics) throws Exception {
//        this.semantics = semantics;
//        heuristics = semantics.getLexiconsAndTheirConditionalExpressions();
//
//    }
//
//    public String call(String term, String text) {
//
//        
//        if (!text.toLowerCase().contains(term.toLowerCase())){
//            return "text does not contain this term";
//        }
//
//        Document document = new Document();
//        List<CategoryAndIndex> categoriesIdentified;
//
//        TermWithConditionalExpressions heuristic;
//        StatusCleaner cleaner = new StatusCleaner();
//
//        
//        String termStripped = TextCleaningOperations.stripText(cleaner, semantics.getRepeatedCharactersRemover(), term);
//        String textStripped = TextCleaningOperations.stripText(cleaner, semantics.getRepeatedCharactersRemover(), text);
//
//        String termLowerCase = term.toLowerCase();
//        String termLowerCaseStripped = termStripped.toLowerCase();
//
//        String textStrippedLowercase = textStripped.toLowerCase();
//        
//        int indexTermStrippedLowercase = textStripped.toLowerCase().indexOf(termLowerCaseStripped);
//
//
//        // now looking at a single term in this doc: what is its sentiment?
//        heuristic = heuristics.getMapH9().get(termLowerCase);
//        if (heuristic != null) {
//            categoriesIdentified = semantics.getTermLevelHeuristics().checkFeatures(heuristic, text, textStripped, term, indexTermStrippedLowercase, false);
//            document.addSeveralCategories(categoriesIdentified);
//        } else if (!termLowerCaseStripped.equals(termLowerCase)) {
//                document = TextCleaningOperations.checkTermHeuristicsOnNGrams(
//                        termLowerCaseStripped, text, textStripped,
//                        textStrippedLowercase, document, heuristics, heuristics.getMapH9(),
//                        new HashSet(), semantics);
//        }
//
//
//        // PREPARING THE OUTPUT
//        Queue<Category> categories = document.getListCategories();
//        if (categories.contains(Category._611) || categories.contains(Category._61)) {
//            return Category._61.toString();
//        } else {
//            return Category._10.toString();
//        }
//    }
//}
