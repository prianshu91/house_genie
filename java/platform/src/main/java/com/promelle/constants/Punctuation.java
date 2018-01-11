package com.promelle.constants;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for holding punctuation chars.
 * 
 * @author Hemant Kumar
 * @version 1.0
 */
public enum Punctuation {

    EMPTY(""),
    SPACE(" "),
    COMMA(","), 
    DOT("."),
    SINGLE_QUOTE("'"),
    DOUBLE_QUOTE("\""),
    COLON(":"),
    UNDERSCORE("_"),
    BRACKET_OPEN("("),
    BRACKET_CLOSE(")"),
    CURLY_BRACKET_OPEN("{"),
    CURLY_BRACKET_CLOSE("}"),
    BACK_SLASH("\\"),
    FORWARD_SLASH("/"),
    ASTERIK("*");

    private String s;
    private static final Map<String, Punctuation> INSTANCE_MAP = new HashMap<String, Punctuation>();

    static {
        for (Punctuation p : Punctuation.values()) {
            INSTANCE_MAP.put(p.toString(), p);
        }
    }

    Punctuation(String s) {
        this.s = s;
    }

    @Override
    public String toString() {
        return s;
    }

    public static Punctuation fromString(String s) {
        return INSTANCE_MAP.get(s);
    }

}
