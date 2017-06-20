package br.unisinos.tokens.impl;

import br.unisinos.MultiLineStringReader;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.util.ParserUtils;

import java.util.Optional;

import static br.unisinos.tokens.TokenType.ARITHMETIC_OP;

/**
 * Created by Vinicius, Fabio e Eduardo.
 *
 * @since ${PROJECT_VERSION}
 */
public class ArithmeticOperatorToken extends Token {
    private ArithmeticOperatorToken(Object value) {
        super(ARITHMETIC_OP, value);
    }

    public static class Parser implements TokenParser<ArithmeticOperatorToken> {

        @Override
        public Optional<ArithmeticOperatorToken> tryParse(MultiLineStringReader input) {
            Object parsed = ParserUtils.tryParse(input, ARITHMETIC_OP.possibleValues(), 1);
            return parsed != null ? Optional.of(new ArithmeticOperatorToken(parsed)) : Optional.empty();
        }
    }
}
