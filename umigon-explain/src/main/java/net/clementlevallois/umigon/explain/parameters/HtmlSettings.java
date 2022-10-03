/*
 * author: Clément Levallois
 */
package net.clementlevallois.umigon.explain.parameters;

import net.clementlevallois.umigon.model.Category.CategoryEnum;

/**
 *
 * @author LEVALLOIS
 */
public class HtmlSettings {

    private String positiveTermColor = "#0000FF";  // blue
    private String negativeTermColor = "#FF0000"; // red
    private String moderatorTermColor = "#ff7f00"; // orange
    private String negationTermColor = "#ff7f00";  // orange

    public String getPositiveTermColor() {
        return positiveTermColor;
    }

    public String getTermColorBasedOnSentiment(CategoryEnum categoryEnum) {
        switch (categoryEnum) {
            case _11:
                return positiveTermColor;
            case _111:
                return positiveTermColor;
            case _12:
                return negativeTermColor;
            default:
                return "#000000";
        }
    }

    public void setPositiveTermColor(String positiveTermColor) {
        this.positiveTermColor = positiveTermColor;
    }

    public String getNegativeTermColor() {
        return negativeTermColor;
    }

    public void setNegativeTermColor(String negativeTermColor) {
        this.negativeTermColor = negativeTermColor;
    }

    public String getModeratorTermColor() {
        return moderatorTermColor;
    }

    public void setModeratorTermColor(String moderatorTermColor) {
        this.moderatorTermColor = moderatorTermColor;
    }

    public String getNegationTermColor() {
        return negationTermColor;
    }

    public void setNegationTermColor(String negationTermColor) {
        this.negationTermColor = negationTermColor;
    }

}
