/*
 * author: Clément Levallois
 */
package net.clementlevallois.umigon.explain.controller;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import java.util.Locale;
import java.util.Set;
import net.clementlevallois.umigon.model.classification.BooleanCondition;
import net.clementlevallois.umigon.model.NGram;

/**
 *
 * @author LEVALLOIS
 */
public class ExplanationOneBooleanCondition {

    public static String getExplanationOneBooleanConditonPlainText(BooleanCondition booleanCondition, String languageTag) {
        StringBuilder sb = new StringBuilder();
        sb.append(getConditionalExpressionName(booleanCondition.getTokenInvestigatedGetsMatched(), booleanCondition.getBooleanConditionEnum(), booleanCondition.getFlipped(), languageTag).toLowerCase(Locale.forLanguageTag(languageTag)));
        if (!booleanCondition.getAssociatedKeywordMatchedAsNGrams().isEmpty()) {
            Set<NGram> associatedKeywordMatchedAsNGrams = booleanCondition.getAssociatedKeywordMatchedAsNGrams();
            sb.append(" (\"");
            for (NGram associatedMatchedNGram : associatedKeywordMatchedAsNGrams) {
                sb.append(associatedMatchedNGram.getCleanedNgram()).append(", ");
            }
            if (sb.toString().endsWith(", ")) {
                sb = new StringBuilder(sb.toString().substring(0, sb.toString().length() - 2));
            }
            sb.append("\")");
        }
        sb.append(", ");
        return sb.toString();
    }

    public static String getExplanationOneBooleanConditonHtml(BooleanCondition booleanCondition, String languageTag) {
        StringBuilder sb = new StringBuilder();
        sb.append(getConditionalExpressionName(booleanCondition.getTokenInvestigatedGetsMatched(), booleanCondition.getBooleanConditionEnum(), booleanCondition.getFlipped(), languageTag).toLowerCase(Locale.forLanguageTag(languageTag)));
        if (!booleanCondition.getAssociatedKeywordMatchedAsNGrams().isEmpty()) {
            Set<NGram> associatedKeywordMatchedAsNGrams = booleanCondition.getAssociatedKeywordMatchedAsNGrams();
            sb.append(" (\"");
            for (NGram associatedMatchedNGram : associatedKeywordMatchedAsNGrams) {
                sb.append(associatedMatchedNGram.getCleanedNgram()).append(", ");
            }
            if (sb.toString().endsWith(", ")) {
                sb = new StringBuilder(sb.toString().substring(0, sb.toString().length() - 2));
            }
            sb.append("\")");
        } else if (!booleanCondition.getAssociatedKeywords(false).isEmpty()) {
            Set<String> associatedKeywordsNotMatched = booleanCondition.getAssociatedKeywords(false);
            sb.append(" (\"");
            for (String associatedKeywordNotMatched : associatedKeywordsNotMatched) {
                sb.append(associatedKeywordNotMatched).append(", ");
            }
            if (sb.toString().endsWith(", ")) {
                sb = new StringBuilder(sb.toString().substring(0, sb.toString().length() - 2));
            }
            sb.append("\")");
        }
        sb.append("\n");
        return sb.toString();
    }

    public static JsonObjectBuilder getExplanationOneBooleanConditonJsonObject(BooleanCondition booleanCondition, String languageTag) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("conditional expression", getConditionalExpressionName(booleanCondition.getTokenInvestigatedGetsMatched(), booleanCondition.getBooleanConditionEnum(), booleanCondition.getFlipped(), languageTag).toLowerCase(Locale.forLanguageTag(languageTag)));
        Set<NGram> associatedKeywordMatchedAsNGrams = booleanCondition.getAssociatedKeywordMatchedAsNGrams();
        StringBuilder sb = new StringBuilder();
        for (NGram associatedMatchedNGram : associatedKeywordMatchedAsNGrams) {
            sb.append(associatedMatchedNGram.getCleanedNgram()).append(", ");
        }
        if (sb.toString().endsWith(", ")) {
            sb = new StringBuilder(sb.toString().substring(0, sb.toString().length() - 2));
        }
        job.add("keyword matched", sb.toString());
        return job;
    }

    private static String getConditionalExpressionName(Boolean matched, BooleanCondition.BooleanConditionEnum condition, boolean flipped, String languageTag) {
        if (matched) {
            return UmigonExplain.getLocaleBundle(languageTag).getString("condition.name." + condition.name());
        } else {
            return UmigonExplain.getLocaleBundle(languageTag).getString("condition.name.not." + condition.name());
        }
    }
}
