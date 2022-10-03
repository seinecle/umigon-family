# Documentation for Umigon

Umigon is a free application for sentiment analysis available as a [web app and as an API here](https://nocodefunctions.com/umigon/sentiment_analysis_tool.html).

This repository contains the documentation for Umigon.

# Overview of the architecture

![a description of the sub-projects composing Umigon](https://docs.google.com/drawings/d/e/2PACX-1vQDDpnqjIxHu0u0_Xu0ozKdANNlYBLvdIVEy3ESIoQU8lBpswpZQmR1uVzP6QKERl8L0_N0nVnhPXYw/pub?w=627&h=465)

① [umigon-model](https://github.com/seinecle/umigon-model): because definitions are important. umigon-model describes a data model that allows to manipulate precisely different parts of a text: words, puncutation, emojis, emoticons, hashtags and more.

 ② [umigon-tokenizer](https://github.com/seinecle/umigon-tokenizer): once we have these essential definitions, we can tokenize a text. This means breaking a text into meaningful segments, such as words but not only.

 ③ [umigon-ngram-ops](https://github.com/seinecle/umigon-ngram-ops): then, we identify the ngrams in the text. An ngram is a fragment of text composed of several words. We know "United" and "States" are in the text, but thanks to this step we will also have "United States".

 ④ [umigon-heuristics](https://github.com/seinecle/umigon-heuristics). A heuristic is a rule that will be applied whenever a word is found in the text. This repo includes a catalog of heuristics and procedures to evaluate them. For instance, a heuristics says that "AWFUL" carries a negative sentiment (it is a bit more complicated but you get the idea).

 ⑤ [umigon-core](https://github.com/seinecle/umigon-core). This repo is called "core" because this is where the projects 1, 2, 3 and 4 get leveraged to analyze the sentiment of a text. The text is tokenized, ngrams are added to it, and then heuristics are applied to each fragment. We get the sentiment of the text.

 ⑥ [umigon-decision](https://github.com/seinecle/umigon-decision). Sometimes, several conflictual sentiments are found in a text, even in the short texts that Umigon is specializing in (tweets, social media posts or comments...). This repo applies a series of decisions to arbitrate and arrive at a final, single sentiment.

 ⑦ [umigon-explain](https://github.com/seinecle/umigon-explain). In the preceding steps (2 to 6), all the  procedures that were applied and all the information that got extracted are carefully stored according to the data model (1). This repo performs a translation from the data model to a natural language humans (end users) can understand and appreciate. We cover 107 different languages and 3 different formats (plain text, html, json).

⑧ [umigon-docs](https://github.com/seinecle/umigon-docs). The repo containing the documentation for Umigon, such as this document.
