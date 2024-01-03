/*
 * author: Clément Levallois
 */
package net.clementlevallois.umigon.explain.controller;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import net.clementlevallois.umigon.model.classification.BooleanCondition;
import net.clementlevallois.umigon.model.classification.ResultOneHeuristics;
import net.clementlevallois.umigon.model.TypeOfTextFragment.TypeOfTextFragmentEnum;
import net.clementlevallois.umigon.explain.parameters.HtmlSettings;

/**
 *
 * @author LEVALLOIS
 */
public class ExplanationOneHeuristics {

    public static String getOneHeuristicsResultsPlainText(ResultOneHeuristics resultOneHeuristics, String languageTag) {
        StringBuilder sb = new StringBuilder();
        List<BooleanCondition> booleanConditions = resultOneHeuristics.getBooleanConditions();
        // because we don't care to know about conditions led to no category
        Collection<BooleanCondition> nonEmptyBooleanConditions = booleanConditions.stream().filter(x -> !x.getBooleanConditionEnum().equals(BooleanCondition.BooleanConditionEnum.none)).collect(Collectors.toList());

        if (!resultOneHeuristics.getTextFragmentInvestigated().getOriginalForm().isBlank()) {
            sb.append(getTokenWasMatched(resultOneHeuristics.getTextFragmentInvestigated().getTypeOfTextFragmentEnum(), languageTag));
            sb.append(": \"");

            sb.append(resultOneHeuristics.getTextFragmentInvestigated().getOriginalForm());
        }
        if (nonEmptyBooleanConditions.isEmpty()) {
            return sb.append("\". ").toString();
        } else {
            if (!resultOneHeuristics.getTextFragmentInvestigated().getOriginalForm().isBlank()) {
                sb.append("\", ");
                sb.append(getAndANumberOfConditionsWereMatched(nonEmptyBooleanConditions.size(), languageTag));
            }else{
                sb.append(getANumberOfConditionsWereMatched(nonEmptyBooleanConditions.size(), languageTag));
            }
            sb.append(":\n");
        }

        int i = 1;
        for (BooleanCondition booleanCondition : nonEmptyBooleanConditions) {
            sb.append("\t\t").append(String.valueOf(i++)).append(") ");
            sb.append(ExplanationOneBooleanCondition.getExplanationOneBooleanConditonPlainText(booleanCondition, languageTag));
            sb.append("\n");
        }
        if (sb.toString().endsWith(", \n")) {
            sb = new StringBuilder(sb.substring(0, sb.length() - 3)).append("\n");
        }
        return sb.toString();
    }

    public static String getOneHeuristicsResultsHtml(ResultOneHeuristics resultOneHeuristics, String languageTag, HtmlSettings htmlSettings) {
        StringBuilder sb = new StringBuilder();
        List<BooleanCondition> booleanConditions = resultOneHeuristics.getBooleanConditions();
        // because we don't care to know about conditions that needed to NOT be fulfilled
        Collection<BooleanCondition> nonFlippedBooleanConditions = booleanConditions.stream().filter(x -> !x.getBooleanConditionEnum().equals(BooleanCondition.BooleanConditionEnum.none)).collect(Collectors.toList());

        TypeOfTextFragmentEnum ttf = resultOneHeuristics.getTextFragmentInvestigated().getTypeOfTextFragmentEnum();

        String tokenWasMatched = getTokenWasMatched(ttf, languageTag);
        sb.append(tokenWasMatched);
        sb.append(": ");
        sb.append("\"");
        sb.append("<span style=\"color:")
                .append(htmlSettings.getTermColorBasedOnSentiment(resultOneHeuristics.getCategoryEnum()))
                .append("\">");
        sb.append(resultOneHeuristics.getTextFragmentInvestigated().getOriginalForm());
        sb.append("</span>");
        sb.append("\"");
        if (nonFlippedBooleanConditions.isEmpty()) {
            return sb.append(". ").toString();
        } else {
            sb.append(", ");
            sb.append(getAndANumberOfConditionsWereMatched(nonFlippedBooleanConditions.size(), languageTag));
            sb.append(":");
            sb.append("<br/>");
            sb.append("\n");
        }

        sb.append("<ul>");
        sb.append("\n");
        for (BooleanCondition booleanCondition : nonFlippedBooleanConditions) {
            sb.append("<li>");
            sb.append("\n");
            sb.append(ExplanationOneBooleanCondition.getExplanationOneBooleanConditonHtml(booleanCondition, languageTag));
            sb.append("\n");
            sb.append("</li>");
            sb.append("\n");
        }
        sb.append("</ul>");
        sb.append("\n");
        return sb.toString();
    }

    public static JsonObjectBuilder getOneHeuristicsResultsJsonObject(ResultOneHeuristics resultOneHeuristics, String languageTag) {
        JsonObjectBuilder job = Json.createObjectBuilder();

        List<BooleanCondition> booleanConditions = resultOneHeuristics.getBooleanConditions();
        // because we don't care to know about conditions that needed to NOT be fulfilled
        Collection<BooleanCondition> nonFlippedBooleanConditions = booleanConditions.stream().filter(x -> !x.getBooleanConditionEnum().equals(BooleanCondition.BooleanConditionEnum.none)).collect(Collectors.toList());

        job.add("type of token matched", resultOneHeuristics.getTextFragmentInvestigated().getTypeOfTextFragmentEnum().toString());

        job.add("token matched", resultOneHeuristics.getTextFragmentInvestigated().getOriginalForm());
        if (nonFlippedBooleanConditions.isEmpty()) {
            return job;
        }
        int i = 1;
        for (BooleanCondition booleanCondition : nonFlippedBooleanConditions) {
            job.add("boolean expression #" + String.valueOf(i++), ExplanationOneBooleanCondition.getExplanationOneBooleanConditonJsonObject(booleanCondition, languageTag));
        }
        return job;
    }

    public static String getTermWasMatched(String languageTag) {
        return UmigonExplain.getLocaleBundle(languageTag).getString("statement.term_was_matched");
    }

    public static String getAndANumberOfConditionsWereMatched(int numberOfConditions, String languageTag) {
        switch (numberOfConditions) {
            case 1:
                return UmigonExplain.getLocaleBundle(languageTag).getString("statement.and_one_condition_was_met");
            case 2:
                return UmigonExplain.getLocaleBundle(languageTag).getString("statement.and_two_conditions_were_met");
            case 3:
                return UmigonExplain.getLocaleBundle(languageTag).getString("statement.and_three_conditions_were_met");
            default:
                return UmigonExplain.getLocaleBundle(languageTag).getString("statement.and_these_conditions_were_met");
        }
    }

    public static String getANumberOfConditionsWereMatched(int numberOfConditions, String languageTag) {
        switch (numberOfConditions) {
            case 1:
                return UmigonExplain.getLocaleBundle(languageTag).getString("statement.one_condition_was_met");
            case 2:
                return UmigonExplain.getLocaleBundle(languageTag).getString("statement.two_conditions_were_met");
            case 3:
                return UmigonExplain.getLocaleBundle(languageTag).getString("statement.three_conditions_were_met");
            default:
                return UmigonExplain.getLocaleBundle(languageTag).getString("statement.these_conditions_were_met");
        }
    }

    public static String getTokenWasMatched(TypeOfTextFragmentEnum typeOfTokenEnum, String languageTag) {
        switch (typeOfTokenEnum) {
            case NGRAM:
                return UmigonExplain.getLocaleBundle(languageTag).getString("statement.term_was_matched");
            case EMOJI:
                return UmigonExplain.getLocaleBundle(languageTag).getString("statement.emoji_was_matched");
            case ONOMATOPAE:
                return UmigonExplain.getLocaleBundle(languageTag).getString("statement.onomatopae_was_matched");
            case HASHTAG:
                return UmigonExplain.getLocaleBundle(languageTag).getString("statement.hashtag_was_matched");
            case EMOTICON_IN_ASCII:
                return UmigonExplain.getLocaleBundle(languageTag).getString("statement.emoticon_in_ascii_was_matched");
            default:
                return UmigonExplain.getLocaleBundle(languageTag).getString("statement.term_was_matched");
        }
    }

    public static String getEmojiWasMatched(String languageTag) {
        return UmigonExplain.getLocaleBundle(languageTag).getString("statement.emoji_was_matched");
    }

    public static String getOnomatopaeWasMatched(String languageTag) {
        return UmigonExplain.getLocaleBundle(languageTag).getString("statement.onomatopae_was_matched");
    }

    public static String getHashtagWasMatched(String languageTag) {
        return UmigonExplain.getLocaleBundle(languageTag).getString("statement.hashtag_was_matched");
    }
}
