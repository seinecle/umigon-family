/*
 * author: Clément Levallois
 */
package net.clementlevallois.umigon.tokenizer.controller;

import com.vdurmont.emoji.EmojiParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import net.clementlevallois.umigon.model.Emoji;
import net.clementlevallois.umigon.model.NonWord;
import net.clementlevallois.umigon.model.PatternOfInterest;
import net.clementlevallois.umigon.model.Punctuation;
import net.clementlevallois.umigon.model.Term;
import net.clementlevallois.umigon.model.TextFragment;
import net.clementlevallois.umigon.model.WhiteSpace;
import net.clementlevallois.utils.RepeatedCharactersRemover;
import net.clementlevallois.utils.TextCleaningOps;

/**
 *
 * @author LEVALLOIS
 */
public class UmigonTokenizer {

    public static void main(String[] args) throws IOException {

        String text = "I love chocolate";
//        String text = "I can't *wait*  to see this performance! 𝄠\nI will l@@@ve it :-) 😀😀😀 😀 :((( ";

//        String text = "I love chocolate :-), really (especially with coffee!)";
//        String text = "nocode is the new thing :) 🤔";
        System.out.println("text: " + text);
        System.out.println("");
        Set<String> languageSpecificLexicon = new HashSet();
        UmigonTokenizer controller = new UmigonTokenizer();
        List<TextFragment> textFragments = UmigonTokenizer.tokenize(text, languageSpecificLexicon);
        String beautiffiedPrint = controller.printTextFragments(textFragments);
        System.out.println(beautiffiedPrint);
    }

    private enum CurrentFragment {
        CURR_FRAGMENT_IS_WHITE_SPACE, CURR_FRAGMENT_IS_PUNCT, CURR_FRAGMENT_IS_NON_WORD, CURR_FRAGMENT_IS_TERM, CURR_FRAGMENT_IS_NOT_STARTED
    }

    public static List<TextFragment> tokenize(String text, Set<String> languageSpecificLexicon) throws IOException {
        PatternOfInterestChecker poiChecker = new PatternOfInterestChecker();
        poiChecker.loadPatternsOfInterest();
        List<TextFragment> textFragments = new ArrayList();

        boolean textFragmentStarted = false;
        boolean isCurrCodePointWhiteSpace = false;
        boolean isCurrCodePointTerm = false;
        boolean isCurrCodePointEmoji = false;
        boolean isCurrCodPointPunctuation = false;

        boolean dontStartNewFragment = false;
        CurrentFragment currFragment = CurrentFragment.CURR_FRAGMENT_IS_NOT_STARTED;

        WhiteSpace whiteSpace = null;
        Term term = null;
        Punctuation punctuation = null;
        NonWord nonWord = null;
        Emoji emoji = null;
        int i = 0;

        int[] codePoints = text.codePoints().toArray();

        for (int codePoint : codePoints) {
            dontStartNewFragment = false;
            String stringOfCodePoint = Character.toString(codePoint);
            if (stringOfCodePoint.equals(")")) {
                System.out.println("stop there is a )");
            }

            //check if this is a punctuation mark
            isCurrCodPointPunctuation = Pattern.matches("[\\p{Punct}\\p{IsPunctuation}]", stringOfCodePoint);

            // check if this is an emoji
            List<String> extractEmojis = EmojiParser.extractEmojis(stringOfCodePoint);
            isCurrCodePointEmoji = !extractEmojis.isEmpty();

            // check if this is a whitespace
            isCurrCodePointWhiteSpace = stringOfCodePoint.isBlank();

            switch (currFragment) {
                case CURR_FRAGMENT_IS_WHITE_SPACE -> {
                    if (isCurrCodePointWhiteSpace) {
                        whiteSpace.addStringToOriginalForm(stringOfCodePoint);
                        if (stringOfCodePoint.equals("\n")) {
                            whiteSpace.setSentenceOrLineBreak(Boolean.TRUE);
                        }
                    } else {
                        textFragments.add(whiteSpace);
                        textFragmentStarted = false;
                    }
                }

                case CURR_FRAGMENT_IS_TERM -> {
                    String originalForm = term.getOriginalForm();

                    if ((isCurrCodePointWhiteSpace) || (isCurrCodPointPunctuation && !originalForm.startsWith("http"))) {
                        String cleanedForm = RepeatedCharactersRemover.repeatedCharacters(originalForm, languageSpecificLexicon);
                        String cleanedAndStrippedForm = TextCleaningOps.flattenToAscii(cleanedForm);
                        term.setCleanedForm(cleanedForm);
                        term.setCleanedAndStrippedForm(cleanedAndStrippedForm);

                        // some term fragments can be onomatopaes (wooow) or texto speak (lol, rofl)
                        // we call them 'non words' (for lack of better name)
                        // they must be detected to be attributed a specific type (different from "term"):
                        PatternOfInterest returnsMatchOrNot = poiChecker.returnsMatchOrNot(term.getCleanedAndStrippedForm());
                        if (returnsMatchOrNot.getMatched()) {
                            nonWord = new NonWord();
                            nonWord.setIndexCardinal(term.getIndexCardinal());
                            nonWord.setIndexOrdinal(term.getIndexOrdinal());
                            nonWord.setOriginalForm(term.getOriginalForm());
                            nonWord.setTypeOfTextFragmentEnum(returnsMatchOrNot.getTypeOfTextFragmentEnum());
                            nonWord.setPoi(returnsMatchOrNot);
                            textFragments.add(nonWord);
                        } else {
                            textFragments.add(term);
                        }
                        textFragmentStarted = false;
                    } else if (!isCurrCodePointEmoji) {
                        term.addStringToOriginalForm(stringOfCodePoint);
                    } else if (isCurrCodePointEmoji) {
                        textFragments.add(term);
                        textFragmentStarted = false;
                    }
                }

                case CURR_FRAGMENT_IS_NON_WORD -> {
                    if ((isCurrCodePointWhiteSpace || isCurrCodePointEmoji)) {
                        textFragments.add(nonWord);
                        textFragmentStarted = false;
                        nonWord = new NonWord();
                    } else {
                        String currNonWordWithNewChar = nonWord.getOriginalForm() + stringOfCodePoint;
                        PatternOfInterest returnsMatchOrNot = poiChecker.returnsMatchOrNot(currNonWordWithNewChar);
                        if (returnsMatchOrNot.getMatched()) {
                            nonWord.setOriginalForm(currNonWordWithNewChar);
                            nonWord.setPoi(returnsMatchOrNot);
                            currFragment = CurrentFragment.CURR_FRAGMENT_IS_NON_WORD;
                        } else {
                            textFragments.add(nonWord);
                            textFragmentStarted = false;
                            dontStartNewFragment = true;
                            nonWord = new NonWord();
                        }
                    }
                }

                case CURR_FRAGMENT_IS_PUNCT -> {
                    PatternOfInterest poi = poiChecker.returnsMatchOrNot(punctuation.getOriginalForm());
                    if (poi.getMatched()) {
                        nonWord = punctuation.toNonWord(poi, punctuation.getOriginalForm());
                        textFragments.add(nonWord);
                        textFragmentStarted = false;
                        break;
                    }
                    if (isCurrCodPointPunctuation) {
                        String currPunctWithNewChar = punctuation.getOriginalForm() + stringOfCodePoint;
                        if (currPunctWithNewChar.codePoints().toArray().length > 1) {
                            poi = poiChecker.returnsMatchOrNot(currPunctWithNewChar);
                            if (poi.getMatched()) {
                                nonWord = punctuation.toNonWord(poi, currPunctWithNewChar);
                                currFragment = CurrentFragment.CURR_FRAGMENT_IS_NON_WORD;
                                break;
                            } else {
                                punctuation.addStringToOriginalForm(stringOfCodePoint);
                            }
                        }
                    } else {
                        int[] codePointsPunct = punctuation.getOriginalForm().codePoints().toArray();
                        currFragment = CurrentFragment.CURR_FRAGMENT_IS_PUNCT;
                        for (int codePointPunct : codePointsPunct) {
                            String punct = Character.toString(codePointPunct);
                            punctuation = new Punctuation();
                            punctuation.setIndexCardinal(i);
                            punctuation.setIndexOrdinal(textFragments.size());
                            punctuation.addStringToOriginalForm(punct);
                            textFragments.add(punctuation);
                        }
                        textFragmentStarted = false;
                    }
                }
            }

            if (!textFragmentStarted & !dontStartNewFragment) {
                if (isCurrCodePointWhiteSpace) {
                    textFragmentStarted = true;
                    whiteSpace = new WhiteSpace();
                    whiteSpace.setIndexCardinal(i);
                    whiteSpace.setIndexOrdinal(textFragments.size());
                    currFragment = CurrentFragment.CURR_FRAGMENT_IS_WHITE_SPACE;
                    whiteSpace.addStringToOriginalForm(stringOfCodePoint);
                    if (stringOfCodePoint.equals("\n")) {
                        whiteSpace.setSentenceOrLineBreak(Boolean.TRUE);
                    }
                } else if (!isCurrCodePointEmoji & !isCurrCodPointPunctuation) {
                    textFragmentStarted = true;
                    term = new Term();
                    currFragment = CurrentFragment.CURR_FRAGMENT_IS_TERM;
                    term.setIndexCardinal(i);
                    term.setIndexOrdinal(textFragments.size());
                    term.addStringToOriginalForm(stringOfCodePoint);
                } else if (isCurrCodPointPunctuation) {
                    textFragmentStarted = true;
                    punctuation = new Punctuation();
                    currFragment = CurrentFragment.CURR_FRAGMENT_IS_PUNCT;
                    punctuation.setIndexCardinal(i);
                    punctuation.setIndexOrdinal(textFragments.size());
                    punctuation.addStringToOriginalForm(stringOfCodePoint);
                } else if (isCurrCodePointEmoji) {
                    emoji = new Emoji();
                    emoji.setIndexCardinal(i);
                    emoji.setIndexOrdinal(textFragments.size());
                    emoji.addStringToOriginalForm(stringOfCodePoint);
                    emoji.setSemiColonForm(EmojiParser.parseToAliases(stringOfCodePoint));
                    textFragments.add(emoji);
                    textFragmentStarted = false;
                    currFragment = CurrentFragment.CURR_FRAGMENT_IS_NOT_STARTED;
                }
            }
            i++;
            if (i == codePoints.length) {

                if (currFragment == CurrentFragment.CURR_FRAGMENT_IS_WHITE_SPACE) {
                    textFragments.add(whiteSpace);
                }

                if (currFragment == CurrentFragment.CURR_FRAGMENT_IS_NON_WORD) {
                    textFragments.add(nonWord);
                }

                if (currFragment == CurrentFragment.CURR_FRAGMENT_IS_TERM) {
                    textFragments.add(term);
                }

                if (currFragment == CurrentFragment.CURR_FRAGMENT_IS_PUNCT) {
                    PatternOfInterest returnsMatchOrNot = poiChecker.returnsMatchOrNot(punctuation.getOriginalForm());
                    if (returnsMatchOrNot.getMatched()) {
                        nonWord = new NonWord();
                        nonWord.setIndexCardinal(punctuation.getIndexCardinal());
                        nonWord.setIndexOrdinal(punctuation.getIndexOrdinal());
                        nonWord.setOriginalForm(punctuation.getOriginalForm());
                        nonWord.setTypeOfTextFragmentEnum(returnsMatchOrNot.getTypeOfTextFragmentEnum());
                        nonWord.setPoi(returnsMatchOrNot);
                        textFragments.add(nonWord);
                    } else {
                        int[] codePointsPunct = punctuation.getOriginalForm().codePoints().toArray();
                        for (int codePointPunct : codePointsPunct) {
                            String punct = Character.toString(codePointPunct);
                            punctuation = new Punctuation();
                            punctuation.setIndexCardinal(i);
                            punctuation.setIndexOrdinal(textFragments.size());
                            punctuation.addStringToOriginalForm(punct);
                            textFragments.add(punctuation);
                        }
                    }

                }

//                if (!isCurrCodePointWhiteSpace & !isCurrCodePointEmoji) {
//                    if (!isCurrCodPointPunctuation) {
//                        String originalForm = term.getOriginalForm();
//                        term.setOriginalForm(originalForm);
//                        String cleanedForm = RepeatedCharactersRemover.repeatedCharacters(originalForm, languageSpecificLexicon);
//                        String cleanedAndStrippedForm = TextCleaningOps.flattenToAscii(cleanedForm);
//                        term.setCleanedForm(cleanedForm);
//                        term.setCleanedAndStrippedForm(cleanedAndStrippedForm);
//                        textFragments.add(term);
//                    } else {
//                        if (term.getOriginalForm().codePoints().toArray().length > 1) {
//                            PatternOfInterest returnsMatchOrNot = poiChecker.returnsMatchOrNot(term.getOriginalForm());
//                            if (returnsMatchOrNot.getMatched()) {
//                                nonWord = new NonWord();
//                                nonWord.setIndexCardinal(term.getIndexCardinal());
//                                nonWord.setIndexOrdinal(term.getIndexOrdinal());
//                                nonWord.setOriginalForm(term.getOriginalForm());
//                                nonWord.setTypeOfTextFragmentEnum(returnsMatchOrNot.getTypeOfTextFragmentEnum());
//                                nonWord.setPoi(returnsMatchOrNot);
//                                textFragments.add(nonWord);
//                            } else if (punctuation != null) {
//                                int[] codePointsPunct = punctuation.getOriginalForm().codePoints().toArray();
//                                for (int codePointPunct : codePointsPunct) {
//                                    String punct = Character.toString(codePointPunct);
//                                    punctuation = new Punctuation();
//                                    currFragment = CurrentFragment.CURR_FRAGMENT_IS_PUNCT;
//                                    punctuation.setIndexCardinal(i);
//                                    punctuation.setIndexOrdinal(textFragments.size());
//                                    punctuation.addStringToOriginalForm(punct);
//                                    textFragments.add(punctuation);
//                                }
//                            }
//                        } else {
//                            if (!term.getOriginalForm().isEmpty()) {
//                                textFragments.add(term);
//                            }
//                        }
//
//                    }
//                }
            }
        }

        return textFragments;

    }

    private String printTextFragments(List<TextFragment> textFragments) {
        StringBuilder sb = new StringBuilder();
        for (TextFragment text : textFragments) {
            sb.append("text fragment: ").append(text.getOriginalForm());
            sb.append(" (type: ").append(text.getTypeOfTextFragmentEnum()).append(")");
            sb.append("\n");
        }
        return sb.toString();
    }

}
