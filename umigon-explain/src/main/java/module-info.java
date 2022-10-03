/*
 * author: ClÃ©ment Levallois
 */

module net.clementlevallois.umigon.explain {
    
    requires net.clementlevallois.umigon.model;
    requires net.clementlevallois.umigon.core;
    requires jakarta.json.bind;
    requires org.eclipse.parsson;


    exports net.clementlevallois.umigon.explain.controller;
    exports net.clementlevallois.umigon.explain.parameters;
}
