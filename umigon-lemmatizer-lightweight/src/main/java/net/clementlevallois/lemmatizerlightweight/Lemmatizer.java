/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.clementlevallois.lemmatizerlightweight;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author LEVALLOIS
 */
public class Lemmatizer {

    private String[] noLemmaEN = new String[]{"access", "accumbens", "addresses", "afterwards", "always", "amazing", "approaches", "analyses", "biases", "businesses", "ceiling", "classes", "crises", "daunting", "discusses", "economics", "elsevier", "ethics", "focuses", "fries", "goes", "humanities", "hypotheses", "inches", "lies", "losses", "marketing", "morning", "news", "outlier", "outstanding", "physics", "politics", "premises", "processes", "red", "rigged", "ries", "series", "sometimes", "something", "species", "spring", "status", "ted", "themselves", "neural processes", "united", "wales", "witnesses"};
    private String[] noLemmaFR = new String[]{"accès", "alors", "apres", "après", "aupres", "auprès", "Calvados", "concours", "corps", "cours", "dans", "discours", "divers", "etes", "êtes", "ethos", "éthos", "gens", "gros", "lors", "outils", "pays", "parcours", "pres", "près", "proces", "procès", "propos", "puis", "sans", "secours", "sens", "sommes", "succès", "succes", "temps", "toujours", "travers", "très", "tres", "univers", "viens", "vos"};
    private String[] noLemma = new String[]{"analytics", "accumbens", "aws", "bayes", "business", "charles", "ects", "cnrs", "cowles", "deep learning", "developer", "ethos", "faas", "forbes", "iaas", "james", "keynes", "koopmans", "nhs", "paas", "paris", "programming", "reactjs", "saas", "siemens", "sanders", "ted", "vuejs", "united states"};

    private Set<String> noLemmaSet;
    private final String lang;

    public static void main(String[] args) throws Exception {
        Lemmatizer l = new Lemmatizer("en");
        l.lemmatize("gets");
    }

    public Lemmatizer(String lang) throws Exception {
        switch (lang) {
            case "en":
                noLemmaSet = new HashSet(Arrays.asList(noLemmaEN));
                break;
            case "fr":
                noLemmaSet = new HashSet(Arrays.asList(noLemmaFR));
                break;
            default:
                noLemmaSet = new HashSet();
                break;
        }
        noLemmaSet.addAll(Arrays.asList(noLemma));
        this.lang = lang;
    }

    public String lemmatize(String term) {

        if (noLemmaSet.contains(term)) {
            return term;
        }

        if ((term.endsWith("s") | term.endsWith("s'"))
                && !term.endsWith("us")
                && !term.endsWith("as")
                && !term.endsWith("ss")
                && !term.endsWith("sses")
                && !term.endsWith("ies")
                && !noLemmaSet.contains(term)
                && !term.endsWith("is")) {
            if (term.endsWith("s")) {
                term = term.substring(0, term.length() - 1);
            }
            if (term.endsWith("s'")) {
                term = term.substring(0, term.length() - 2);
            }

        } else if (term.endsWith("'")) {
            term = term.substring(0, term.length() - 1);
        }

        if (lang.equals("en")) {
            if (term.endsWith("sses")) {
                term = term.substring(0, term.length() - 2);
            }
            if (term.endsWith("ies")) {
                term = term.substring(0, term.length() - 3) + "y";
            }
            else if (term.endsWith("'s")) {
                term = term.substring(0, term.length() - 2);
            } else if (term.endsWith("ed")) {
                if (term.endsWith("rred")) {
                    term = term.substring(0, term.length() - 3);
                } else if (term.endsWith("lked")
                        || term.endsWith("cked")
                        || term.endsWith("pted")
                        || term.endsWith("ssed")
                        || term.endsWith("lled")
                        || term.endsWith("iased")
                        || (term.endsWith("red") && (!term.endsWith("ired")& !term.endsWith("ured")))
                        || term.endsWith("aired")
                        || term.endsWith("used")
                        || term.endsWith("ned")
                        || (term.endsWith("ded") & !term.endsWith("ided"))
                        || (term.endsWith("ted") & !term.endsWith("ated"))) {
                    term = term.substring(0, term.length() - 2);
                } else if (term.endsWith("ied")) {
                    term = term.substring(0, term.length() - 3) + "y";
                } else if (term.endsWith("eed")) {
                    // do nothing (as in: exceed, proceed)
                } else {
                    // purchased -> purchase
                    term = term.substring(0, term.length() - 1);
                }
            } else if (term.endsWith("'s")) {
                term = term.substring(0, term.length() - 2);
            } else if (term.endsWith("ing")) {
                if (term.endsWith("king") && !term.equals("king")) {
                    term = term.substring(0, term.length() - 3) + "e";
                } else if (term.endsWith("ging") && !term.endsWith("gging")) {
                    term = term.substring(0, term.length() - 3) + "e";
                } else if (term.endsWith("sing")
                        || term.endsWith("zing")
                        || term.endsWith("cing")
                        || term.endsWith("ving")
                        || term.endsWith("ring")) {
                    term = term.substring(0, term.length() - 3) + "e";
                } else if (term.length() > 2) {
                    term = term.substring(0, term.length() - 3);
                    //running has become runn. Should become run.
                    int size = term.length();
                    if (size > 1) {
                        String lastTwoLetters = term.substring(size - 2, size);
                        if (lastTwoLetters.length() > 1 && lastTwoLetters.charAt(0) == lastTwoLetters.charAt(1)) {
                            term = term.substring(0, term.length() - 1);
                        }
                        size = term.length();
                        if (size > 1) {
                            //voting has become vot. Should become vote. Same for any word ending in at or ot (not plant, suspect, ...). Yet, it will miscorrect "pivot"
                            lastTwoLetters = term.substring(size - 2, size);
                            if (lastTwoLetters.equals("at") || lastTwoLetters.equals("ot") || lastTwoLetters.equals("id")) {
                                term = term + "e";
                            }
                        }
                    }
                }
            } else if (term.endsWith("ier")) {
                term = term.substring(0, term.length() - 3) + "y";
            }
        }
        if (lang.equals("fr")) {
            if (term.endsWith("ies") || term.endsWith("sses")) {
                term = term.substring(0, term.length() - 1);
            }
            if (term.endsWith("ère")) {
                term = term.substring(0, term.length() - 3) + "er";
            }
            if (term.endsWith("rions")) {
                term = term.substring(0, term.length() - 5);
            }
            // more work to do on French conjuguaisons obviously - doable! Get in touch via Github issues.
        }
        return term.trim();

    }

    public String sentenceLemmatizer(String sentence) {

        String[] terms = sentence.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String term : terms) {
            sb.append(lemmatize(term));
            sb.append(" ");
        }
        return sb.toString().trim();
    }

}
