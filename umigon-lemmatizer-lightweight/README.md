### What?
A lemmatizer for English and French.

### How to use it?

- call it directly in your code, to lemmatize a sentence:
```java
Lemmatizer lemmatizer = new Lemmatizer("en");
String result = lemmatizer.sentenceLemmatizer("Students are in Paris");
// result: "Student are in Paris"
```

- or lemmatize a single term:
```java
Lemmatizer lemmatizer = new Lemmatizer("en");
String result = lemmatizer.lemmatize("Students");
// result: "Student"
```


### Pros
- light weight: one single file

### What for?
Excellent lemmatizers already exist for French and English. To excel, they use large scale models (hundreds of megabytes per language) or require extensive pre-processing on the text to be lemmatized (such a part of speech tagging).

This lemmatizer, in contrast, is lightweight: it is a single file. The results are not perfect but accurate enough when the goal is to remove plural forms and other common variations before counting the most frequent terms in a text.

### Origin?
This function is developed by Clement Levallois, in support of academic work published [in various places](https://scholar.google.fr/citations?user=r0R0vekAAAAJ&hl=en). It is now used in support of [a web app providing free text analysis for non coders](https://nocodefunctions.com).

### Contributions
Your contributions are very welcome.

### License
Apache v2
