///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.clementlevallois.umigon.classifier.sentiment;
//
////import Admin.Parameters;
//import java.util.HashSet;
//import net.clementlevallois.umigon.model.TermWithConditionalExpressions;
//import net.clementlevallois.umigon.heuristics.tools.LoaderOfLexiconsAndConditionalExpressions;
//
//import net.clementlevallois.umigon.model.Document;
//import java.util.List;
//import java.util.Set;
//import net.clementlevallois.ngramops.NGramFinder;
//import net.clementlevallois.umigon.classifier.commonOps.TextCleaningOperations;
//import net.clementlevallois.umigon.model.Categories.Category;
//import net.clementlevallois.umigon.semantics.resources.Semantics;
//import net.clementlevallois.umigon.model.CategoryAndIndex;
//import net.clementlevallois.utils.StatusCleaner;
//import org.apache.commons.lang3.StringUtils;
//
//public class ClassifierSentimentOneWordInADocument {
//
//    Semantics semantics;
//    LoaderOfLexiconsAndConditionalExpressions heuristics;
//
//    public ClassifierSentimentOneWordInADocument(Semantics semantics) {
//        this.semantics = semantics;
//        heuristics = semantics.getLexiconsAndTheirConditionalExpressions();
//    }
//
//    public String call(String term, String text) {
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
//        heuristic = heuristics.getMapH1().get(termLowerCase);
//        if (heuristic != null) {
//            categoriesIdentified = (semantics.getTermLevelHeuristics().checkFeatures(heuristic, text, textStripped, term, indexTermStrippedLowercase, false));
//            document.addSeveralCategories(categoriesIdentified);
//            if (!categoriesIdentified.isEmpty() && categoriesIdentified.get(0).getCategory().equals(new Category("11"))) {
//                document.addTermToPositive(term);
//            }
//        } else if (!termLowerCaseStripped.equals(termLowerCase)) {
//                document = TextCleaningOperations.checkTermHeuristicsOnNGrams(
//                        termLowerCaseStripped, text, textStripped,
//                        textStrippedLowercase, document, heuristics, heuristics.getMapH1(),
//                        new HashSet(), semantics);
//        }
//
//        heuristic = heuristics.getMapH2().get(termLowerCase);
//        if (heuristic != null) {
//            categoriesIdentified = semantics.getTermLevelHeuristics().checkFeatures(heuristic, text, textStripped, term, indexTermStrippedLowercase, false);
//            document.addSeveralCategories(categoriesIdentified);
//            if (!categoriesIdentified.isEmpty() && categoriesIdentified.get(0).getCategory().equals(new Category("12"))) {
//                document.addTermToNegative(term);
//            }
//        } else if (!termLowerCaseStripped.equals(termLowerCase)) {
//                document = TextCleaningOperations.checkTermHeuristicsOnNGrams(
//                        termLowerCaseStripped, text, textStripped,
//                        textStrippedLowercase, document, heuristics, heuristics.getMapH2(),
//                        new HashSet(), semantics); 
//        }
//
//        return document.getSentiment().toString();
//
//    }
//}
