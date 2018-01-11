package com.promelle.rules.engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.promelle.rules.dto.Rule;

/**
 * This class is responsible for providing a rule based engine.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public interface RuleEngine<T extends Rule> {

    String GROUPS = "groups";
    String RULES = "rules";
    String ZERO = "0";
    String NUMBER = "NUMBER";
    String INT = "INT";
    String FLOAT = "FLOAT";
    String OPERATOR = "operator";
    String TYPE = "type";
    String VALUE = "value";
    String NAME = "name";

    /**
     * This function is responsible for parsing of rule node.
     * 
     * @param node
     * @return Rule
     */
    T parseRuleNode(JsonNode node);

}
