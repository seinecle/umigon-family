/*
 * author: Clément Levallois
 */
package net.clementlevallois.umigonfamily.umigon.decision;

import java.util.List;
import java.util.Set;
import net.clementlevallois.umigon.model.SentenceLike;
import net.clementlevallois.umigon.model.TextFragment;

/**
 *
 * @author LEVALLOIS
 */
public class QuestionMarkAtTheEnd {

    private final SentenceLike sentence;
    private Set<String> intenseWords;
    private Set<String> subjectiveTerms;

    public QuestionMarkAtTheEnd(SentenceLike sentence, Set<String> intenseWords, Set<String> subjectiveTerms) {
        this.sentence = sentence;
        this.intenseWords = intenseWords;
        this.subjectiveTerms = subjectiveTerms;
    }

    public boolean skipSentimentEvaluation() {
        /*
        The idea is to classify sentences ending in question marks as neutral in sentiment
         */
        List<TextFragment> allTextFragments = sentence.getTextFragments();
        if (allTextFragments.size() < 3) {
            return Boolean.FALSE;
        }
        if (!allTextFragments.get(allTextFragments.size() - 1).getOriginalForm().contains("?")) {
            return Boolean.FALSE;
        }

        /**
         * This check is for the following case:
         *
         * Why is he sad? -> NEUTRAL Why are you such a jerk? -> NEGATIVE
         * partially because of the intensity term 'such a' Is the house
         * beautiful? -> NEUTRAL How come the house is so beautiful? -> POSITIVE
         * partially because of the intensity term 'so'
         *
         * So the rules applied here is: dismiss any sentiment in a question,
         * except if there is a marker of intensity or a marker of subjectivity
         *
         */
        for (TextFragment tf : allTextFragments) {
            if (intenseWords.contains(tf.getOriginalForm().toLowerCase()) || subjectiveTerms.contains(tf.getOriginalForm().toLowerCase())) {
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
    }

}
