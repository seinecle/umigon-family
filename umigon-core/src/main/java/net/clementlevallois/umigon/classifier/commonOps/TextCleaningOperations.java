/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.clementlevallois.umigon.classifier.commonOps;

import net.clementlevallois.utils.TextCleaningOps;

/**
 *
 * @author LEVALLOIS
 */
public class TextCleaningOperations {

    public static String cleanText(String text) {
        //removing content in double quotes and other cleaning ops
        text = TextCleaningOps.removeTermsBetweenQuotes(text);
        text = TextCleaningOps.removeUrls(text);
        text = TextCleaningOps.normalizeApostrophs(text);
        text = TextCleaningOps.removeNullChars(text);
        text = text.replaceAll(" +", " ");
        return text.trim();
    }

    public static String stripText(String text) {
        text = TextCleaningOps.removePunctuationSigns(text);
        text = TextCleaningOps.flattenToAscii(text);
        text = text.replaceAll(" +", " ");
        return text;
    }

}
