/*
 * author: Clément Levallois
 */
package net.clementlevallois.umigon.explain.controller;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Optional;
import net.clementlevallois.umigon.model.Category;
import net.clementlevallois.umigon.model.Category.CategoryEnum;
import net.clementlevallois.umigon.model.classification.Decision;
import net.clementlevallois.umigon.model.classification.Document;
import net.clementlevallois.umigon.model.classification.ResultOneHeuristics;
import net.clementlevallois.umigon.explain.parameters.HtmlSettings;

/**
 *
 * @author LEVALLOIS
 */
public class UmigonExplain {

    private static String PATHLOCALE;

    public static void main(String[] args) {
        ResourceBundle localeBundle = getLocaleBundle("en");
        System.out.println("language: " + localeBundle.getLocale().toLanguageTag());
    }

    public static ResourceBundle getLocaleBundle(String languageTag) {
        ResourceBundle rb = null;
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
				String userName = Optional.ofNullable(System.getenv("USERNAME"))
                                  .orElse("Unknown User");
				if (userName.equals("clevallois")){
					userName = userName + ".GOBELINS";				
				}
                PATHLOCALE = "C:\\Users\\" + userName + "\\open\\nocode-app-functions\\umigon-static-files\\src\\main\\resources\\net\\clementlevallois\\umigon\\explanation\\i18n";
            } else {
                PATHLOCALE = "/home/waouh/nocodeapp-web/umigon-static-files/src/main/resources/net/clementlevallois/umigon/explanation/i18n";
            }
            File file = new File(PATHLOCALE);
            URL[] urls = {file.toURI().toURL()};
            URLClassLoader loader = new URLClassLoader(urls);
            rb = ResourceBundle.getBundle("text", Locale.forLanguageTag(languageTag), loader);
            if (rb == null) {
                rb = ResourceBundle.getBundle("text", Locale.forLanguageTag("en"), loader);
            }
            loader.close();
            return rb;
        } catch (MalformedURLException ex) {
            System.out.println("error in retrieving bundle");
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println("could not close the class loader");
            System.out.println(ex.getMessage());
        }
        return rb;
    }

    public static String getSentimentPlainText(Document doc, String locale) {
        if (!doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._11).isEmpty()) {
            return UmigonExplain.getLocaleBundle(locale).getString("sentiment.ispositive");
        } else if (!doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._12).isEmpty()) {
            return UmigonExplain.getLocaleBundle(locale).getString("sentiment.isnegative");
        } else {
            return UmigonExplain.getLocaleBundle(locale).getString("sentiment.isneutral");
        }
    }

    public static String getOrganicPlainText(Document doc, String locale) {
        if (!doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._61).isEmpty()) {
            return UmigonExplain.getLocaleBundle(locale).getString("organic.iscorporate");
        } else if (!doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._611).isEmpty()) {
            return UmigonExplain.getLocaleBundle(locale).getString("organic.iscorporate");
        } else {
            return UmigonExplain.getLocaleBundle(locale).getString("organic.isneutral");
        }
    }

    public static Document enrichDocWithPlainTextSentimentResults(Document doc, String locale) {
        if (!doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._11).isEmpty()) {
            doc.setCategoryLocalizedPlainText(UmigonExplain.getLocaleBundle(locale).getString("sentiment.ispositive"));
            doc.setCategoryCode("_11");
        } else if (!doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._12).isEmpty()) {
            doc.setCategoryLocalizedPlainText(UmigonExplain.getLocaleBundle(locale).getString("sentiment.isnegative"));
            doc.setCategoryCode("_12");
        } else {
            doc.setCategoryLocalizedPlainText(UmigonExplain.getLocaleBundle(locale).getString("sentiment.isneutral"));
            doc.setCategoryCode("_10");
        }
        return doc;
    }

    public static Document enrichDocWithPlainTextOrganicResults(Document doc, String locale) {
        if (!doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._61).isEmpty()) {
            doc.setCategoryLocalizedPlainText(UmigonExplain.getLocaleBundle(locale).getString("organic.iscorporate"));
            doc.setCategoryCode("_61");
        } else if (!doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._611).isEmpty()) {
            doc.setCategoryLocalizedPlainText(UmigonExplain.getLocaleBundle(locale).getString("organic.iscorporate"));
            doc.setCategoryCode("_611");
        } else {
            doc.setCategoryLocalizedPlainText(UmigonExplain.getLocaleBundle(locale).getString("organic.isneutral"));
            doc.setCategoryCode("_10");
        }
        return doc;
    }

    public static JsonObjectBuilder getSentimentJsonObject(Document doc, String locale) {
        ResourceBundle bundle = UmigonExplain.getLocaleBundle(locale);
        JsonObjectBuilder job = Json.createObjectBuilder();

        if (!doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._11).isEmpty()) {
            return job.add("sentiment", bundle.getString("sentiment.ispositive"));
        } else if (!doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._12).isEmpty()) {
            return job.add("sentiment", bundle.getString("sentiment.isnegative"));
        } else {
            return job.add("sentiment", bundle.getString("sentiment.isneutral"));
        }
    }

    public static JsonObjectBuilder getOrganicJsonObject(Document doc, String locale) {
        ResourceBundle bundle = UmigonExplain.getLocaleBundle(locale);
        JsonObjectBuilder job = Json.createObjectBuilder();

        if (!doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._61).isEmpty()) {
            return job.add("organic", bundle.getString("organic.iscorporate"));
        } else if (!doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._611).isEmpty()) {
            return job.add("organic", bundle.getString("organic.iscorporate"));
        } else {
            return job.add("organic", bundle.getString("organic.isneutral"));
        }
    }

    public static String getSentimentPlainText(CategoryEnum categoryEnum, String locale) {
        ResourceBundle bundle = UmigonExplain.getLocaleBundle(locale);
        switch (categoryEnum) {
            case _11:
                return bundle.getString("sentiment.ispositive");
            case _12:
                return bundle.getString("sentiment.isnegative");
            default:
                return bundle.getString("sentiment.isneutral");
        }
    }

    public static String getOrganicPlainText(CategoryEnum categoryEnum, String locale) {
        ResourceBundle bundle = UmigonExplain.getLocaleBundle(locale);
        switch (categoryEnum) {
            case _61:
                return bundle.getString("organic.iscorporate");
            case _611:
                return bundle.getString("organic.iscorporate");
            default:
                return bundle.getString("organic.isneutral");
        }
    }

    public static JsonObjectBuilder getSentimentJsonObject(CategoryEnum categoryEnum, String locale) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        ResourceBundle bundle = UmigonExplain.getLocaleBundle(locale);
        switch (categoryEnum) {
            case _11:
                return job.add("sentiment", bundle.getString("sentiment.ispositive"));
            case _12:
                return job.add("sentiment", bundle.getString("sentiment.isnegative"));
            default:
                return job.add("sentiment", bundle.getString("sentiment.isneutral"));
        }
    }

    public static JsonObjectBuilder getOrganicJsonObject(CategoryEnum categoryEnum, String locale) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        ResourceBundle bundle = UmigonExplain.getLocaleBundle(locale);
        switch (categoryEnum) {
            case _61:
                return job.add("organic", bundle.getString("organic.iscorporate"));
            case _611:
                return job.add("organic", bundle.getString("organic.iscorporate"));
            default:
                return job.add("sentiment", bundle.getString("organic.isneutral"));
        }
    }

    public static String getExplanationsOfDecisionsPlainText(Document doc, String languageTag) {
        ResourceBundle bundle = UmigonExplain.getLocaleBundle(languageTag);
        StringBuilder sb = new StringBuilder();
        List<Decision> decisions = doc.getDecisions();
        if (decisions == null || decisions.isEmpty()) {
            return "";
        }
        sb.append("\n");
        if (decisions.size() == 1) {
            sb.append(bundle.getString("statement.a_decision_has_been_made"));
        } else {
            sb.append(bundle.getString("statement.a_number_of_decisions_have_been_made"));
        }
        sb.append(":\n");
        for (Decision decision : decisions) {
            sb.append(ExplanationOneDecision.getExplanationOneDecisionPlainText(decision, languageTag));
            if (!sb.toString().endsWith(".")) {
                sb.append(".");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String getExplanationsOfDecisionsHtml(Document doc, String languageTag, HtmlSettings htmlSettings) {
        ResourceBundle bundle = UmigonExplain.getLocaleBundle(languageTag);
        StringBuilder sb = new StringBuilder();
        List<Decision> decisions = doc.getDecisions();
        if (decisions == null || decisions.isEmpty()) {
            return "";
        }
        sb.append("<p>");
        if (decisions.size() == 1) {
            sb.append(bundle.getString("statement.a_decision_has_been_made"));
        } else {
            sb.append(bundle.getString("statement.a_number_of_decisions_have_been_made"));
        }
        sb.append(":</p>");
        sb.append("\n");

        sb.append("<ul>");
        sb.append("\n");
        for (Decision decision : decisions) {
            sb.append("<li>");
            sb.append("\n");
            sb.append(ExplanationOneDecision.getExplanationOneDecisionHtml(decision, languageTag, htmlSettings));
            if (!sb.toString().endsWith(".")) {
                sb.append(".");
            }
            sb.append("</li>");
            sb.append("\n");
        }
        sb.append("</ul>");
        sb.append("\n");
        return sb.toString();
    }

    public static JsonObjectBuilder getExplanationsOfDecisionsJsonObject(Document doc, String languageTag) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        List<Decision> decisions = doc.getDecisions();
        if (decisions == null || decisions.isEmpty()) {
            return job;
        }
        int i = 1;
        for (Decision decision : decisions) {
            JsonObjectBuilder explanationOneDecisionJsonObject = ExplanationOneDecision.getExplanationOneDecisionJsonObject(decision, languageTag);
            job.add("decision #" + String.valueOf(i++), explanationOneDecisionJsonObject);
        }
        return job;
    }

    public static String getExplanationOfHeuristicResultsPlainText(Document doc, String languageTag) {
        ResourceBundle bundle = UmigonExplain.getLocaleBundle(languageTag);
        StringBuilder sb = new StringBuilder();
        sb.append("text: ").append(doc.getText());
        sb.append("\n").append("\n");
        for (Decision decision : doc.getDecisions()) {
            if (decision.getDecisionType().equals(Decision.DecisionType.ADD)) {
                for (ResultOneHeuristics oneHeuristics : decision.getListOfHeuristicsImpacted()) {
                    doc.addOneHeuristicsResult(oneHeuristics);
                }
            }
        }
        Set<ResultOneHeuristics> allHeuristicsResultsForPositive = doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._11);
        Set<ResultOneHeuristics> allHeuristicsResultsForNegative = doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._12);
        List<ResultOneHeuristics> resultsHeuristics = new ArrayList();
        List<ResultOneHeuristics> deletedHeuristicsFollowingDecisions = new ArrayList();
        for (Decision decision : doc.getDecisions()) {
            deletedHeuristicsFollowingDecisions.addAll(decision.getListOfHeuristicsImpacted());
        }
        if (!allHeuristicsResultsForPositive.isEmpty()) {
            sb.append(bundle.getString("sentiment.ispositive"));
            resultsHeuristics.addAll(allHeuristicsResultsForPositive);
        } else if (!allHeuristicsResultsForNegative.isEmpty()) {
            sb.append(bundle.getString("sentiment.isnegative"));
            resultsHeuristics.addAll(allHeuristicsResultsForNegative);
        } else {
            sb.append(bundle.getString("sentiment.isneutral"));
            resultsHeuristics.addAll(doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._10));
        }
        resultsHeuristics.addAll(deletedHeuristicsFollowingDecisions);
        sb.append(ExaminingAllResultsHeuristics.goThroughAllResultsHeuristicsPlainText(resultsHeuristics, languageTag));
        return sb.toString();
    }

    public static String getExplanationOfHeuristicOrganicResultsPlainText(Document doc, String languageTag) {
        ResourceBundle bundle = UmigonExplain.getLocaleBundle(languageTag);
        StringBuilder sb = new StringBuilder();
        sb.append("text: ").append(doc.getText());
        sb.append("\n").append("\n");
        for (Decision decision : doc.getDecisions()) {
            if (decision.getDecisionType().equals(Decision.DecisionType.ADD)) {
                for (ResultOneHeuristics oneHeuristics : decision.getListOfHeuristicsImpacted()) {
                    doc.addOneHeuristicsResult(oneHeuristics);
                }
            }
        }
        Set<ResultOneHeuristics> allHeuristicsResultsForOrganic = doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._61);
        allHeuristicsResultsForOrganic.addAll(doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._611));
        List<ResultOneHeuristics> resultsHeuristics = new ArrayList();
        List<ResultOneHeuristics> deletedHeuristicsFollowingDecisions = new ArrayList();
        for (Decision decision : doc.getDecisions()) {
            deletedHeuristicsFollowingDecisions.addAll(decision.getListOfHeuristicsImpacted());
        }
        if (!allHeuristicsResultsForOrganic.isEmpty()) {
            sb.append(bundle.getString("organic.iscorporate"));
            resultsHeuristics.addAll(allHeuristicsResultsForOrganic);
        } else {
            sb.append(bundle.getString("organic.isneutral"));
            resultsHeuristics.addAll(doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._10));
        }
        resultsHeuristics.addAll(deletedHeuristicsFollowingDecisions);
        sb.append(ExaminingAllResultsHeuristics.goThroughAllResultsHeuristicsPlainText(resultsHeuristics, languageTag));
        return sb.toString();
    }

    public static String getExplanationOfHeuristicResultsHtml(Document doc, String languageTag, HtmlSettings htmlSettings, Boolean withoutContactAndTextTitle) {
        ResourceBundle bundle = UmigonExplain.getLocaleBundle(languageTag);
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("\n");
        sb.append("<html>");
        sb.append("\n");
        sb.append("<head>");
        sb.append("\n");
        String cssToAddToHead = HtmlHighlighter.generateCssStyles(doc);
        sb.append(cssToAddToHead);
        sb.append("</head>");
        sb.append("\n");
        sb.append("<body>");
        sb.append("\n");
        if (!withoutContactAndTextTitle) {
            sb.append("<p>");
            sb.append(bundle.getString("message.contact"));
            sb.append("</p>");
            sb.append("\n");
            sb.append("<br/>");
            sb.append("\n");
            sb.append("<br/>");
            sb.append("\n");
            sb.append("<p><strong>").append(bundle.getString("message.text")).append(":</strong></p>");
            sb.append("\n");
            sb.append("<br/>");
            sb.append("\n");
        }
        sb.append("<p>");
        String underlinedOriginalSentence = HtmlHighlighter.underline(doc);
        sb.append(underlinedOriginalSentence);
        sb.append("</p>");
        sb.append("\n");
        sb.append("<br/>");
        sb.append("\n");
        Set<ResultOneHeuristics> allHeuristicsResultsForPositive = doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._11);
        Set<ResultOneHeuristics> allHeuristicsResultsForNegative = doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._12);
        Collection<ResultOneHeuristics> resultsHeuristics = new ArrayList();
        List<ResultOneHeuristics> deletedHeuristicsFollowingDecisions = new ArrayList();
        for (Decision decision : doc.getDecisions()) {
            deletedHeuristicsFollowingDecisions.addAll(decision.getListOfHeuristicsImpacted());
        }
        sb.append("<p>");
        sb.append("\n");
        if (!allHeuristicsResultsForPositive.isEmpty()) {
            sb.append("<span style=\"color:")
                    .append(htmlSettings.getPositiveTermColor())
                    .append("\">");
            sb.append(bundle.getString("sentiment.ispositive"));
            sb.append("</span>");
            resultsHeuristics.addAll(allHeuristicsResultsForPositive);
        } else if (!allHeuristicsResultsForNegative.isEmpty()) {
            sb.append("<span style=\"color:")
                    .append(htmlSettings.getNegativeTermColor())
                    .append("\">");
            sb.append(bundle.getString("sentiment.isnegative"));
            sb.append("</span>");
            resultsHeuristics.addAll(allHeuristicsResultsForNegative);
        } else {
            sb.append(bundle.getString("sentiment.isneutral"));
            resultsHeuristics = doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._10);
        }
        resultsHeuristics.addAll(deletedHeuristicsFollowingDecisions);
        String explanationsHeuristics = ExaminingAllResultsHeuristics.goThroughAllResultsHeuristicsHtml(resultsHeuristics, languageTag, new HtmlSettings());
        sb.append(explanationsHeuristics);
        sb.append("</p>");
        String explanationsOfDecisionsHtml = getExplanationsOfDecisionsHtml(doc, languageTag, htmlSettings);
        sb.append(explanationsOfDecisionsHtml);
        sb.append("</body>");
        sb.append("\n");
        sb.append("</html>");
        sb.append("\n");
        return sb.toString();
    }

    public static String getExplanationOfHeuristicOrganicResultsHtml(Document doc, String languageTag, HtmlSettings htmlSettings, Boolean withoutContactAndTextTitle) {
        ResourceBundle bundle = UmigonExplain.getLocaleBundle(languageTag);
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("\n");
        sb.append("<html>");
        sb.append("\n");
        sb.append("<head>");
        sb.append("\n");
        String cssToAddToHead = HtmlHighlighter.generateCssStyles(doc);
        sb.append(cssToAddToHead);
        sb.append("</head>");
        sb.append("\n");
        sb.append("<body>");
        sb.append("\n");
        if (!withoutContactAndTextTitle) {
            sb.append("<p>");
            sb.append(bundle.getString("message.contact"));
            sb.append("</p>");
            sb.append("\n");
            sb.append("<br/>");
            sb.append("\n");
            sb.append("<br/>");
            sb.append("\n");
            sb.append("<p><strong>").append(bundle.getString("message.text")).append(":</strong></p>");
            sb.append("\n");
            sb.append("<br/>");
            sb.append("\n");
        }
        sb.append("<p>");
        String underlinedOriginalSentence = HtmlHighlighter.underline(doc);
        sb.append(underlinedOriginalSentence);
        sb.append("</p>");
        sb.append("\n");
        sb.append("<br/>");
        sb.append("\n");
        Set<ResultOneHeuristics> allHeuristicsResultsForOrganic = doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._61);
        allHeuristicsResultsForOrganic.addAll(doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._611));
        Collection<ResultOneHeuristics> resultsHeuristics = new ArrayList();
        List<ResultOneHeuristics> deletedHeuristicsFollowingDecisions = new ArrayList();
        for (Decision decision : doc.getDecisions()) {
            deletedHeuristicsFollowingDecisions.addAll(decision.getListOfHeuristicsImpacted());
        }
        sb.append("<p>");
        sb.append("\n");
        if (!allHeuristicsResultsForOrganic.isEmpty()) {
            sb.append("<span style=\"color:")
                    .append(htmlSettings.getPositiveTermColor())
                    .append("\">");
            sb.append(bundle.getString("organic.iscorporate"));
            sb.append("</span>");
            resultsHeuristics.addAll(allHeuristicsResultsForOrganic);
        } else {
            sb.append(bundle.getString("organic.isneutral"));
            resultsHeuristics = doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._10);
        }
        resultsHeuristics.addAll(deletedHeuristicsFollowingDecisions);
        String explanationsHeuristics = ExaminingAllResultsHeuristics.goThroughAllResultsHeuristicsHtml(resultsHeuristics, languageTag, new HtmlSettings());
        sb.append(explanationsHeuristics);
        sb.append("</p>");
        String explanationsOfDecisionsHtml = getExplanationsOfDecisionsHtml(doc, languageTag, htmlSettings);
        sb.append(explanationsOfDecisionsHtml);
        sb.append("</body>");
        sb.append("\n");
        sb.append("</html>");
        sb.append("\n");
        return sb.toString();
    }

    public static JsonObjectBuilder getExplanationOfHeuristicResultsJson(Document doc, String languageTag) {
        ResourceBundle bundle = UmigonExplain.getLocaleBundle(languageTag);
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("text", doc.getText());
        Set<ResultOneHeuristics> allHeuristicsResultsForPositive = doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._11);
        Set<ResultOneHeuristics> allHeuristicsResultsForNegative = doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._12);
        List<ResultOneHeuristics> deletedHeuristicsFollowingDecisions = new ArrayList();
        for (Decision decision : doc.getDecisions()) {
            deletedHeuristicsFollowingDecisions.addAll(decision.getListOfHeuristicsImpacted());
        }
        Collection<ResultOneHeuristics> resultsHeuristics = new ArrayList();
        if (!allHeuristicsResultsForPositive.isEmpty()) {
            job.add("sentiment", bundle.getString("sentiment.ispositive"));
            resultsHeuristics.addAll(allHeuristicsResultsForPositive);
        } else if (!allHeuristicsResultsForNegative.isEmpty()) {
            job.add("sentiment", bundle.getString("sentiment.isnegative"));
            resultsHeuristics.addAll(allHeuristicsResultsForNegative);
        } else {
            job.add("sentiment", bundle.getString("sentiment.isneutral"));
            resultsHeuristics = doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._10);
        }
        resultsHeuristics.addAll(deletedHeuristicsFollowingDecisions);
        job.add("explanation heuristics", ExaminingAllResultsHeuristics.goThroughAllResultsHeuristicsJsonObject(resultsHeuristics, languageTag));
        return job;
    }

    public static JsonObjectBuilder getExplanationOfHeuristicOrganicResultsJson(Document doc, String languageTag) {
        ResourceBundle bundle = UmigonExplain.getLocaleBundle(languageTag);
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("text", doc.getText());

        // 61 and 61 are categories for commercial / sponsored speak
        Set<ResultOneHeuristics> allHeuristicsResultsForCommercial = doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._61);
        allHeuristicsResultsForCommercial.addAll(doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._611));

        List<ResultOneHeuristics> deletedHeuristicsFollowingDecisions = new ArrayList();
        for (Decision decision : doc.getDecisions()) {
            deletedHeuristicsFollowingDecisions.addAll(decision.getListOfHeuristicsImpacted());
        }
        Collection<ResultOneHeuristics> resultsHeuristics = new ArrayList();
        if (!allHeuristicsResultsForCommercial.isEmpty()) {
            job.add("organic", bundle.getString("organic.iscorporate"));
            resultsHeuristics.addAll(allHeuristicsResultsForCommercial);
        } else {
            job.add("organic", bundle.getString("organic.isneutral"));
            resultsHeuristics = doc.getAllHeuristicsResultsForOneCategory(Category.CategoryEnum._10);
        }
        resultsHeuristics.addAll(deletedHeuristicsFollowingDecisions);
        job.add("explanation heuristics", ExaminingAllResultsHeuristics.goThroughAllResultsHeuristicsJsonObject(resultsHeuristics, languageTag));
        return job;
    }
}
