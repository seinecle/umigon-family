module net.clementlevallois.umigon.core {
    requires net.clementlevallois.utils;
    requires net.clementlevallois.stopwords;
    requires net.clementlevallois.umigon.ngram.ops;
    requires net.clementlevallois.umigon.decision;
    requires net.clementlevallois.umigon.tokenizer;
    requires transitive net.clementlevallois.umigon.model;
    requires net.clementlevallois.umigon.heuristics;
    requires emoji.java;
    
    exports net.clementlevallois.umigon.classifier.controller;
    exports net.clementlevallois.umigon.classifier.sentiment;
    exports net.clementlevallois.umigon.classifier.delight;
    exports net.clementlevallois.umigon.classifier.organic;
    exports net.clementlevallois.umigon.classifier.resources;
}
