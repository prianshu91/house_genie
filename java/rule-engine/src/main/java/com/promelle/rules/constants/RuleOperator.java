package com.promelle.rules.constants;

/**
 * This enum holds  rule operators
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public enum RuleOperator {
    EQUALS("="),
    NOT_EQUALS("!="),
    LESS_THAN("<"),
    LESS_THAN_EQUALS("<="),
    GREATER_THAN(">"),
    GREATER_THAN_EQUALS(">="),
    LIKE("LIKE"),
    NOT_LIKE("NOT LIKE"),
    IN("IN"),
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL"),
    NOT_IN("NOT IN");

    private String opertaor;

    private RuleOperator(String operator) {
        this.opertaor = operator;
    }

    public String getOpertaor() {
        return opertaor;
    }

    public static RuleOperator getRuleOperator(String str) {
        if (contains(str)) {
            return valueOf(str);
        }
        return null;
    }

    public static boolean contains(String str) {
        for (RuleOperator ruleOperator : RuleOperator.values()) {
            if (ruleOperator.name().equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

}
