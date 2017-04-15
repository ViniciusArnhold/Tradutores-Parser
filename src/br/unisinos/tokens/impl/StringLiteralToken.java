package br.unisinos.tokens.impl;

import br.unisinos.MultiLineStringReader;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.TokenType;

import java.util.Optional;

/**
 * Created by first.
 *
 * @since ${PROJECT_VERSION}
 */
public class StringLiteralToken extends Token {
    protected StringLiteralToken(String value) {
        super(TokenType.STRING_LITERAL, value);
    }

    public static class Parser implements TokenParser<StringLiteralToken> {

        @Override
        public Optional<StringLiteralToken> tryParse(MultiLineStringReader input) {
            return Optional.empty();
        }
    }
}
