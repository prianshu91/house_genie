package com.promelle.rules.dto;

import com.promelle.rules.constants.RuleOperator;

/**
 * This class is responsible for holding a single rule.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class SimpleRule extends Rule {
	private static final long serialVersionUID = 5024616691726268074L;
	private Object actual;
    private RuleOperator operator;
    private Object expected;

    public SimpleRule() {
        // TODO Auto-generated constructor stub
    }

    public SimpleRule(Object actual, RuleOperator operator, Object expected) {
        this.actual = actual;
        this.operator = operator;
        this.expected = expected;
    }

    public SimpleRule(SimpleRule rule) {
        setId(rule.getId());
        this.actual = rule.getActual();
        this.operator = rule.getOperator();
        this.expected = rule.getExpected();
    }

    public Object getActual() {
        return actual;
    }

    public void setActual(Object actual) {
        this.actual = actual;
    }

    public RuleOperator getOperator() {
        return operator;
    }

    public void setOperator(RuleOperator operator) {
        this.operator = operator;
    }

    public Object getExpected() {
        return expected;
    }

    public void setExpected(Object expected) {
        this.expected = expected;
    }

}
