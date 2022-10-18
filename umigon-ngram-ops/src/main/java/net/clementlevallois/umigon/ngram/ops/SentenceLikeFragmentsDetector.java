/*
 * author: Clément Levallois
 */
package net.clementlevallois.umigon.ngram.ops;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.clementlevallois.umigon.model.NGram;
import net.clementlevallois.umigon.model.SentenceLike;
import net.clementlevallois.umigon.model.Term;
import net.clementlevallois.umigon.model.TextFragment;
import net.clementlevallois.umigon.model.TypeOfTextFragment;
import net.clementlevallois.umigon.tokenizer.controller.UmigonTokenizer;

/**
 *
 * @author LEVALLOIS
 */
public class SentenceLikeFragmentsDetector {

    public static void main(String args[]) throws IOException {
        String example = "La meuf qui hurle dans le bus parce qu' on s' est assis à côté d' elle... 😒";
//        String example = "Je vais super bien :-), vraiment vous êtes des champions (même toi!)";
        Set<String> languageSpecificLexicon = new HashSet();
        List<TextFragment> allTextFragments = UmigonTokenizer.tokenize(example, languageSpecificLexicon);
        List<SentenceLike> sentenceLikeFragments = returnSentenceLikeFragments(allTextFragments);
        for (SentenceLike sentenceLike : sentenceLikeFragments) {
            for (TextFragment textFragment : sentenceLike.getNgrams()) {
                System.out.print(textFragment.getOriginalForm());
                System.out.print(" ");
            }
            System.out.println("");
        }
    }

    public static List<SentenceLike> returnSentenceLikeFragments(List<TextFragment> textFragments) {
        List<SentenceLike> listOfSentenceLikeFragments = new ArrayList();
        List<NGram> listOfNGrams = new ArrayList();
        Iterator<TextFragment> it = textFragments.iterator();
        SentenceLike sentenceLike = new SentenceLike();
        sentenceLike.setIndexCardinal(0);
        sentenceLike.setIndexOrdinal(0);

        while (it.hasNext()) {
            TextFragment nextTextFragment = it.next();
//            if (nextTextFragment.getString().equals("burnes")){
//                System.out.println("stop");
//            }
            sentenceLike.getTextFragments().add(nextTextFragment);
            
            TypeOfTextFragment.TypeOfTextFragmentEnum typeOfTextFragment = nextTextFragment.getTypeOfTextFragmentEnum();
//            if (typeOfTextFragment == null){
//                System.out.println("stop");
//            }
            switch (typeOfTextFragment) {
                case TERM:
                    if (sentenceLike.getNgrams().isEmpty()) {
                        sentenceLike.setIndexCardinal(nextTextFragment.getIndexCardinal());
                    }
                    nextTextFragment.setIndexOrdinal(listOfNGrams.size());
                    Term term = (Term) nextTextFragment;
                    NGram ngram = new NGram();
                    ngram.setIndexCardinal(term.getIndexCardinal());
                    ngram.setIndexOrdinal(term.getIndexOrdinal());
                    ngram.setIndexOrdinalInSentence(term.getIndexOrdinalInSentence());
                    ngram.setIndexCardinalInSentence(term.getIndexCardinalInSentence());
                    ngram.getTerms().add(term);
                    StringBuilder sb = new StringBuilder();
                    for (Term termLoop : ngram.getTerms()) {
                        sb.append(termLoop.getOriginalForm());
                        sb.append(" ");
                    }
                    ngram.setOriginalForm(sb.toString().trim());

                    listOfNGrams.add(ngram);
                    break;
                case PUNCTUATION:
                    String s = nextTextFragment.getOriginalForm();
                    if (!sentenceLike.getNgrams().isEmpty() && s.contains(",") || s.contains("(") || s.contains(")") || s.contains("\"") || s.contains("«") || s.contains("»") || s.contains("“") || s.contains("”") || s.contains("„")) {
                        sentenceLike.getNgrams().addAll(listOfNGrams);
                        sentenceLike.setIndexOrdinal(listOfSentenceLikeFragments.size());
                        listOfSentenceLikeFragments.add(sentenceLike);
                        sentenceLike = new SentenceLike();
                        listOfNGrams = new ArrayList();
                    }
                    break;
                default:
                    //do nothing
                    break;
            }
        }
        sentenceLike.getNgrams().addAll(listOfNGrams);
        if (!sentenceLike.getNgrams().isEmpty()) {
            listOfSentenceLikeFragments.add(sentenceLike);
        }
        return listOfSentenceLikeFragments;
    }
}
