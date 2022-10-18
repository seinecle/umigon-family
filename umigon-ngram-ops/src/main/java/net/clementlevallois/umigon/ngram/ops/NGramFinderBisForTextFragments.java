package net.clementlevallois.umigon.ngram.ops;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import net.clementlevallois.umigon.model.NGram;
import net.clementlevallois.umigon.model.SentenceLike;
import net.clementlevallois.umigon.model.Term;
import net.clementlevallois.umigon.model.TextFragment;
import net.clementlevallois.umigon.model.TypeOfTextFragment;
import net.clementlevallois.umigon.tokenizer.controller.UmigonTokenizer;

/**
 *
 * @author LEVALLOIS adapted from https://stackoverflow.com/a/13977401/798502
 */
public class NGramFinderBisForTextFragments {

    public static void main(String[] args) throws IOException {
        String example = "Nuts have properties)";
//        String example = "J'aime, vraiment vous êtes des champions (même toi!) http://momo";
        Set<String> languageSpecificLexicon = new HashSet();
        List<TextFragment> allTextFragments = UmigonTokenizer.tokenize(example, languageSpecificLexicon);
        List<SentenceLike> listOfSentenceLike = SentenceLikeFragmentsDetector.returnSentenceLikeFragments(allTextFragments);
        int countSentenceLikeFragments = 1;
        for (SentenceLike sentenceLikeFragment : listOfSentenceLike) {
            System.out.println("sentence like fragment #" + countSentenceLikeFragments++);
            List<NGram> generateNgramsUpto = NGramFinderBisForTextFragments.generateNgramsUpto(sentenceLikeFragment.getNgrams(), 5);
            for (TextFragment textFragment : generateNgramsUpto) {
                if (textFragment.getTypeOfTextFragmentEnum() == null) {
                    System.out.println("stop null fragment type");
                }
                if (textFragment.getTypeOfTextFragmentEnum().equals(TypeOfTextFragment.TypeOfTextFragmentEnum.TERM)) {
                    System.out.println(textFragment.getOriginalForm());
                }
                if (textFragment.getTypeOfTextFragmentEnum().equals(TypeOfTextFragment.TypeOfTextFragmentEnum.NGRAM)) {
                    NGram ngram = (NGram) textFragment;
                    List<Term> terms = ngram.getTerms();
                    for (Term term : terms) {
                        System.out.print(term.getOriginalForm());
                        System.out.print(' ');
                    }
                    System.out.println("");
                }
            }
        }
    }

    public static List<NGram> generateNgramsUpto(List<NGram> ngrams, int maxGramSize) {

        List<NGram> textFragmentsAugmentedWithNGrams = new ArrayList();
        int ngramSize;
        NGram ngram;

        ListIterator<NGram> it = ngrams.listIterator();
        while (it.hasNext()) {
            NGram word = it.next();
            if (!(word instanceof NGram)) {
                System.out.println("alert a non ngram detected in method generateNgramsUpto");
                System.out.println("textFragment was: " + word.getOriginalForm());
                continue;
            }

            //1- add the word itself
            textFragmentsAugmentedWithNGrams.add(word);

            //2- open a new NGram
            ngram = new NGram();
            ngram.getTerms().add(word.getTerms().get(0));
            ngramSize = 1;

            // call to 'previous()' to stay on the same term on the next iteration forward
            it.previous();

            //2- insert prevs of the word and add those too
            while (it.hasPrevious() && ngramSize < maxGramSize) {
                ngram.getTerms().add(0, it.previous().getTerms().get(0));
                NGram newNgram = new NGram();
                newNgram.getTerms().addAll(ngram.getTerms());
                newNgram.setIndexCardinal(newNgram.getTerms().get(0).getIndexCardinal());
                newNgram.setIndexOrdinal(newNgram.getTerms().get(0).getIndexOrdinal());
                newNgram.setIndexCardinalInSentence(newNgram.getTerms().get(0).getIndexCardinalInSentence());
                newNgram.setIndexOrdinalInSentence(newNgram.getTerms().get(0).getIndexOrdinalInSentence());
                StringBuilder sb = new StringBuilder();
                for (Term term : newNgram.getTerms()) {
                    sb.append(term.getOriginalForm());
                    sb.append(" ");
                }
                newNgram.setOriginalForm(sb.toString().trim());

                textFragmentsAugmentedWithNGrams.add(newNgram);
                ngramSize++;
            }

            //go back to initial position + 1
            while (ngramSize > 0) {
                ngramSize--;
                it.next();
            }
        }
        return textFragmentsAugmentedWithNGrams;
    }
}
