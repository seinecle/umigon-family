/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.clementlevallois.umigon.classifier.resources;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.clementlevallois.stopwords.Stopwords;
import net.clementlevallois.umigon.heuristics.tools.EmojisHeuristicsandResourcesLoader;
import net.clementlevallois.umigon.heuristics.tools.LoaderOfLexiconsAndConditionalExpressions;
import net.clementlevallois.umigon.heuristics.tools.PunctuationValenceVerifier;

/**
 *
 * @author LEVALLOIS
 */
public class Semantics {

    private Set<String> stopwords;
    private Set<String> stopwordsWithoutSentimentRelevance;
    private Set<String> setIronicTerms;
    private Set<String> setNegations;
    private Set<String> setModerators;
    private String lang;
    private Set<String> supportedLanguages = new HashSet();
    private LoaderOfLexiconsAndConditionalExpressions lexiconsAndTheirConditionalExpressions;

    public void loader(String lang) {
        try {
            this.lang = lang;
            this.supportedLanguages.add("en");
            this.supportedLanguages.add("fr");
            this.supportedLanguages.add("es");
            if (lang == null || !supportedLanguages.contains(lang)) {
                this.lang = "en";
            }
            Map<String, Set<String>> stopWords = Stopwords.getStopWords(this.lang);
            stopwords = (Set<String>) stopWords.get("long");
            lexiconsAndTheirConditionalExpressions = new LoaderOfLexiconsAndConditionalExpressions(this.lang);
            lexiconsAndTheirConditionalExpressions.load();
            setIronicTerms = lexiconsAndTheirConditionalExpressions.getSetIronicallyPositive();
            setNegations = lexiconsAndTheirConditionalExpressions.getSetNegations();
            setModerators = lexiconsAndTheirConditionalExpressions.getSetModeratorsForward();
            EmojisHeuristicsandResourcesLoader.load();
            PunctuationValenceVerifier.load();
            stopwordsWithoutSentimentRelevance = new HashSet();
            stopwordsWithoutSentimentRelevance.addAll(stopwords);
            stopwordsWithoutSentimentRelevance.removeAll(lexiconsAndTheirConditionalExpressions.getLexiconsWithoutTheirConditionalExpressions());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Set<String> getStopwords() {
        return stopwords;
    }

    public void setStopwords(Set<String> stopwords) {
        this.stopwords = stopwords;
    }

    public Set<String> getStopwordsWithoutSentimentRelevance() {
        return stopwordsWithoutSentimentRelevance;
    }

    public void setStopwordsWithoutSentimentRelevance(Set<String> stopwordsWithoutSentimentRelevance) {
        this.stopwordsWithoutSentimentRelevance = stopwordsWithoutSentimentRelevance;
    }

    public Set<String> getSetIronicTerms() {
        return setIronicTerms;
    }

    public void setSetIronicTerms(Set<String> setIronicTerms) {
        this.setIronicTerms = setIronicTerms;
    }

    public Set<String> getSetNegations() {
        return setNegations;
    }

    public void setSetNegations(Set<String> setNegations) {
        this.setNegations = setNegations;
    }

    public Set<String> getSetModerators() {
        return setModerators;
    }

    public void setSetModerators(Set<String> setModerators) {
        this.setModerators = setModerators;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Set<String> getSupportedLanguages() {
        return supportedLanguages;
    }

    public void setSupportedLanguages(Set<String> supportedLanguages) {
        this.supportedLanguages = supportedLanguages;
    }

    public LoaderOfLexiconsAndConditionalExpressions getLexiconsAndTheirConditionalExpressions() {
        return lexiconsAndTheirConditionalExpressions;
    }

    public void setLexiconsAndTheirConditionalExpressions(LoaderOfLexiconsAndConditionalExpressions lexiconsAndTheirConditionalExpressions) {
        this.lexiconsAndTheirConditionalExpressions = lexiconsAndTheirConditionalExpressions;
    }

}
