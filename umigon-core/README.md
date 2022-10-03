# Umigon-core

The classification engine for sentiment analysis. The basic operations are:
- decompose the text into n-grams
- create a version of the n-gram which is stripped from accents etc.
- for each of these two versions of the ngrams: if the ngram belongs to a pre-established lexicon, apply a heuristic for this term.
- the lexicons are listed in a repo umigon-heuristics also to be found on github.

# Key features

* Multilingual. French and English are covered and new languages will be added.

* Enables state of the art sentiment analysis in French and English.

# To do
The denominations (what is a heuristic? a conditional expression?) are not super clear and consistent. It should get straightened up.

# License
Apache v2 license. These data and code can be used for non commercial *and commercial* purposes. Just make sure to include the Apache v2 license and the copyright notice.

# Credit
For academic use, cite this reference:

Levallois, Clement. “Umigon: Sentiment analysis on Tweets based on terms lists and heuristics”. Proceedings of the 7th International Workshop on Semantic Evaluation (SemEval), 2013, Atlanta, Georgia


# Contact
Clement Levallois, @seinecle on Twitter or https://clementlevallois.net
