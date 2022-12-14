# Umigon
A family of modules for essential NLP tasks and sentiment analysis, done well.

# The tokenizer
Why another tokenizer?? **Because splitting on whitespaces is not enough.**

Consider this sentence:

> I can't *wait*  to see this performance! 𝄠
> I will l@@@ve it :-) 😀😀😀 😀 :((( 

Will be tokeinzed as:

text fragment: I (type: TERM)

text fragment:   (type: WHITE_SPACE)

text fragment: can (type: TERM)

text fragment: ' (type: PUNCTUATION)

text fragment: t (type: TERM)

text fragment:   (type: WHITE_SPACE)

text fragment: * (type: PUNCTUATION)

text fragment: wait (type: TERM)

text fragment: * (type: PUNCTUATION)

text fragment:    (type: WHITE_SPACE)

text fragment: to (type: TERM)

text fragment:   (type: WHITE_SPACE)

text fragment: see (type: TERM)

text fragment:   (type: WHITE_SPACE)

text fragment: this (type: TERM)

text fragment:   (type: WHITE_SPACE)

text fragment: performance (type: TERM)

text fragment: ! (type: PUNCTUATION)

text fragment:   (type: WHITE_SPACE)

text fragment: 𝄠 (type: TERM)

text fragment:  (type: WHITE_SPACE)

text fragment: I (type: TERM)

text fragment:   (type: WHITE_SPACE)

text fragment: will (type: TERM)

text fragment:   (type: WHITE_SPACE)

text fragment: l (type: TERM)

text fragment: @ (type: PUNCTUATION)

text fragment: @ (type: PUNCTUATION)

text fragment: @ (type: PUNCTUATION)

text fragment: ve (type: TERM)

text fragment:   (type: WHITE_SPACE)

text fragment: it (type: TERM)

text fragment:   (type: WHITE_SPACE)

text fragment: :-) (type: EMOTICON_IN_ASCII)

text fragment:   (type: WHITE_SPACE)

text fragment: 😀 (type: EMOJI)

text fragment: 😀 (type: EMOJI)

text fragment: 😀 (type: EMOJI)

text fragment:   (type: WHITE_SPACE)

text fragment: 😀 (type: EMOJI)

text fragment:   (type: WHITE_SPACE)

text fragment: :((( (type: EMOTICON_IN_ASCII)

text fragment:   (type: WHITE_SPACE)

## Improvements needed
In the example above, we would expect "l@@@ve" and "can't" to be tokenized as single tokens, not to be decomposed in sub-fragments. This should be relatively easily obtained by detecting fragments that end and finish with letters. This is on my todo list.


## Principles followed
- the least dependencies possible:
   * a Utils module which has no further dependency
   * the emoji-java library, which has a single dependency on org.json
   * the umigon-model module which is the dependency-free data structure for the Umigon modules

- each module should serve a function in a stand-alone fashion: you can use it independently from other modules.

## Docs
- see the [umigon-docs](https://github.com/seinecle/umigon-family/tree/main/umigon-docs) module to see how the tokenizer fits in the broader family of Umigon modules
- or check the readme in each module 

## How to cite it
"Clement Levallois. 2013. Umigon: sentiment analysis for tweets based on terms lists and heuristics. In Second Joint Conference on Lexical and Computational Semantics (*SEM), Volume 2: Proceedings of the Seventh International Workshop on Semantic Evaluation (SemEval 2013), pages 414–417, Atlanta, Georgia, USA. Association for Computational Linguistics." 

## Web app
The tokenizer and all Umigon modules are implemented in [Nocodefunctions](https://nocodefunctions.com), a free web app for nocode data analysis.

## Contact
[Clément Levallois](https://twitter.com/seinecle)

## License
See [license.md](LICENCE.md)