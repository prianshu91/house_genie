package com.promelle.rules.engine.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.promelle.constants.Punctuation;
import com.promelle.rules.constants.RuleGroupOperator;
import com.promelle.rules.constants.RuleOperator;
import com.promelle.rules.dto.Rule;
import com.promelle.rules.dto.SimpleRule;
import com.promelle.rules.dto.SimpleRuleGroup;
import com.promelle.rules.engine.RuleEngine;
import com.promelle.utils.JsonUtils;

/**
 * This class is responsible for providing a rule based engine.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public class DefaultRuleEngine implements RuleEngine<Rule> {

    /**
     * This function is responsible for parsing custom rule node.
     * 
     * @param customRuleNode
     * @return customRule
     */
    public Rule parseCustomRuleNode(JsonNode customRuleNode) {
        SimpleRule customRule = new SimpleRule();
        if (customRuleNode != null && customRuleNode.size() > 0) {
            String actual = JsonUtils.getStringValue(customRuleNode, NAME, Punctuation.EMPTY.toString());
            String expected = JsonUtils.getStringValue(customRuleNode, VALUE, Punctuation.EMPTY.toString());
            String type = JsonUtils.getStringValue(customRuleNode, TYPE, Punctuation.EMPTY.toString());
            RuleOperator ruleOperator = RuleOperator.getRuleOperator(JsonUtils.getStringValue(customRuleNode, OPERATOR,
                    Punctuation.EMPTY.toString()));
            if ((FLOAT.equalsIgnoreCase(type) || INT.equalsIgnoreCase(type) || NUMBER.equalsIgnoreCase(type))
                    && StringUtils.isBlank(expected)) {
                expected = ZERO;
            }
            if (RuleOperator.IN == ruleOperator && !expected.contains(Punctuation.COMMA.toString())) {
                ruleOperator = RuleOperator.EQUALS;
            }
            if (RuleOperator.NOT_IN == ruleOperator && !expected.contains(Punctuation.COMMA.toString())) {
                ruleOperator = RuleOperator.NOT_EQUALS;
            }
            customRule.setOperator(ruleOperator);
            customRule.setExpected(expected);
            customRule.setActual(actual);
        }
        return customRule;
    }

    /**
     * This function is responsible for parsing custom rule group node.
     * 
     * @param customRuleGroupNode
     * @return customRuleGroup
     */
    public SimpleRuleGroup parseCustomRuleGroupNode(JsonNode ruleGroupNode) {
        SimpleRuleGroup customRuleGroup = new SimpleRuleGroup();
        if (ruleGroupNode != null) {
            ArrayNode childRuleGroupNodes = (ArrayNode) ruleGroupNode.get(GROUPS);
            List<Rule> rules = new LinkedList<Rule>();
            if (childRuleGroupNodes != null) {
                for (JsonNode childRuleGroupNode : childRuleGroupNodes) {
                    SimpleRuleGroup ruleGroup = parseCustomRuleGroupNode(childRuleGroupNode);
                    if (ruleGroup != null) {
                        rules.add(ruleGroup);
                    }
                }
            }
            ArrayNode childRuleNodes = (ArrayNode) ruleGroupNode.get(RULES);
            if (childRuleNodes != null) {
                for (JsonNode childRuleNode : childRuleNodes) {
                    rules.addAll(adjustActualFields(parseCustomRuleNode(childRuleNode)));
                }
            }
            customRuleGroup.setRules(rules);
        }
        customRuleGroup.setOperator(RuleGroupOperator.getRuleGroupOperator(JsonUtils.getStringValue(ruleGroupNode,
                OPERATOR, RuleGroupOperator.AND.name())));
        return customRuleGroup;
    }

    /**
     * This function is responsible for adjusting fields.
     * 
     * @param rule
     * @return List<Rule>
     */
    public List<Rule> adjustActualFields(Rule rule) {
        List<Rule> processedRules = new LinkedList<Rule>();
        if (rule instanceof SimpleRule) {
            processedRules.add(rule);
        } else if (rule instanceof SimpleRuleGroup) {
            SimpleRuleGroup customRuleGroup = (SimpleRuleGroup) rule;
            List<Rule> childRules = new LinkedList<Rule>();
            for (Rule rul : customRuleGroup.getRules()) {
                childRules.addAll(adjustActualFields(rul));
            }
            customRuleGroup.setRules(childRules);
            processedRules.add(customRuleGroup);
        }
        return processedRules;
    }

    @Override
    public Rule parseRuleNode(JsonNode node) {
        return node.has(GROUPS) || node.has(RULES) ? parseCustomRuleGroupNode(node) : parseCustomRuleNode(node);
    }

}
