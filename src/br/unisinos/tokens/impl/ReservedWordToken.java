package br.unisinos.tokens.impl;

import br.unisinos.MultiLineStringReader;
import br.unisinos.util.ParserUtils;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.TokenType;

/**
 * Created by Vinicius.
 *
 * @since ${PROJECT_VERSION}
 */
public class ReservedWordToken extends Token {


    protected ReservedWordToken(Object value) {
        super(TokenType.RESERVED_WORD, value);
    }

    public static class Parser implements TokenParser<ReservedWordToken> {

        private static final int BIGGEST_SIZE = TokenType.RESERVED_WORD.possibleValues().stream().mapToInt(String::length).max().orElse(0);

        @Override
        public ReservedWordToken parse(MultiLineStringReader input) {
            Object parsed = ParserUtils.tryParse(input, TokenType.RESERVED_WORD.possibleValues(), BIGGEST_SIZE);
            return parsed != null ? new ReservedWordToken(parsed) : null;
        }
    }
}
