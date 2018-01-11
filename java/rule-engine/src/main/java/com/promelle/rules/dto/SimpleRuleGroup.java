package com.promelle.rules.dto;

import java.util.List;

import com.promelle.rules.constants.RuleGroupOperator;

/**
 * This class is responsible for holding a group of rules.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class SimpleRuleGroup extends Rule {
	private static final long serialVersionUID = 3571433750104717090L;
	private List<Rule> rules;
    private RuleGroupOperator operator;

    public SimpleRuleGroup() {
        this.operator = RuleGroupOperator.AND;
    }

    public SimpleRuleGroup(String name, List<Rule> rules, RuleGroupOperator operator) {
        this.rules = rules;
        this.operator = operator;
    }

    public SimpleRuleGroup(SimpleRuleGroup ruleGroup) {
        setId(ruleGroup.getId());
        this.rules = ruleGroup.getRules();
        this.operator = ruleGroup.getOperator();
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public RuleGroupOperator getOperator() {
        return operator;
    }

    public void setOperator(RuleGroupOperator operator) {
        this.operator = operator;
    }

}
