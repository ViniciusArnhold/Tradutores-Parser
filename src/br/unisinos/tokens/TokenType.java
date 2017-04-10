package br.unisinos.tokens;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Vinicius.
 *
 * @since ${PROJECT_VERSION}
 */
public enum TokenType {

    VARIABLE("id", "1", "2", "3", "4", "5"),

    NUMERIC_VALUE("num", Double.toString(Double.NEGATIVE_INFINITY), Double.toString(Double.POSITIVE_INFINITY)),

    RESERVED_WORD("reserved_word", "do", "while", "if", "else", "for", "printf", "return", "null", "int", "float", "double", "string", "bool"),

    RELATIONAL_OP("Relational_Op", "<", "<=", "==", "!=", ">=", ">"),

    ARITHMETIC_OP("Arith_Op", "+", "-", "*", "/"),

    EQUAL_OP("Equal_Op", "="),

    COMMENT("comment", "//", "/**/");

    private final String tokenName;
    private final Set<String> possibleValues;

    TokenType(String tokenName, String... possibleValues) {
        this.tokenName = tokenName;
        this.possibleValues = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(possibleValues)));
    }

    public String asTokenName() {
        return this.tokenName;
    }

    public Set<String> possibleValues() {
        return this.possibleValues;
    }
}
