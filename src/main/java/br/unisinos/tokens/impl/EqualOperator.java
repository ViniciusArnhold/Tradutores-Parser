package br.unisinos.tokens.impl;

import br.unisinos.MultiLineStringReader;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.TokenType;
import br.unisinos.util.ParserUtils;

import java.util.Optional;

/**
 * Created by Vinicius, Fabio e Eduardo.
 *
 * @since 1.0
 */
public class EqualOperator extends Token {
    private EqualOperator(Object value) {
        super(TokenType.EQUAL_OP, value);
    }


    public static class Parser implements TokenParser<EqualOperator> {

        @Override
        public Optional<EqualOperator> tryParse(MultiLineStringReader input) {
            Object parsed = ParserUtils.tryParse(input, TokenType.EQUAL_OP.possibleValues(), 1);
            return parsed != null ? Optional.of(new EqualOperator(parsed)) : Optional.empty();
        }
    }
}
