package br.unisinos.tokens;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Vinicius, Fabio e Eduardo.
 *
 * @since ${PROJECT_VERSION}
 */
public enum TokenType {

    VARIABLE("id", "1", "2", "3", "4", "5"),

    NUMERIC_VALUE("num", Double.toString(Double.NEGATIVE_INFINITY), Double.toString(Double.POSITIVE_INFINITY)),

    RESERVED_WORD("reserved_word", "do", "while", "if", "else", "for", "printf", "return", "null", "int", "float", "double", "string", "bool", "char"),

    RELATIONAL_OP("Relational_Op", "<", "<=", "==", "!=", ">=", ">"),

    STRING_LITERAL("string_literal", "abc"),

    ARITHMETIC_OP("Arith_Op", "+", "-", "*", "/"),

    EQUAL_OP("Equal_Op", "="),

    L_PAREN("l_paren", "("),
    R_PAREN("r_paren", ")"),

    L_BRACKET("l_bracket", "["),
    R_BRACKET("r_bracket", "]"),

    L_BRACE("l_brace", "{"),
    R_BRACE("r_brace", "}"),

    COMMA("comma", ","),

    SEMICOLON("semicolon", ";"),

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
