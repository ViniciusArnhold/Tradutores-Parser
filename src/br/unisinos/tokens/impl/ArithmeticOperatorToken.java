package br.unisinos.tokens.impl;

import br.unisinos.MultiLineStringReader;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.util.ParserUtils;

import static br.unisinos.tokens.TokenType.ARITHMETIC_OP;

/**
 * Created by Vinicius.
 *
 * @since ${PROJECT_VERSION}
 */
public class ArithmeticOperatorToken extends Token {
    protected ArithmeticOperatorToken(Object value) {
        super(ARITHMETIC_OP, value);
    }

    public static class Parser implements TokenParser<ArithmeticOperatorToken> {

        @Override
        public ArithmeticOperatorToken parse(MultiLineStringReader input) {
            Object parsed = ParserUtils.tryParse(input, ARITHMETIC_OP.possibleValues(), 1);
            return parsed != null ? new ArithmeticOperatorToken(parsed) : null;
        }
    }
}
