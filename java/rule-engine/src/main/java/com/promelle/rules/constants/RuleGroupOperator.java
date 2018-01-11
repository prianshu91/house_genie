package com.promelle.rules.constants;

/**
 * This enum holds rule group operators
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public enum RuleGroupOperator {
    AND, OR;

    public static RuleGroupOperator getRuleGroupOperator(String str) {
        if (contains(str)) {
            return valueOf(str);
        }
        return null;
    }

    public static boolean contains(String str) {
        for (RuleGroupOperator operator : RuleGroupOperator.values()) {
            if (operator.name().equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

}
