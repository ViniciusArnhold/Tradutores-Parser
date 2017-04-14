package br.unisinos.tokens.impl;

import br.unisinos.MultiLineStringReader;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.TokenType;

/**
 * Created by first on 10/04/2017.
 */
public class StringLiteralToken extends Token {
    protected StringLiteralToken(String value) {
        super(TokenType.STRING_LITERAL, value);
    }

    public static class Parser implements TokenParser<StringLiteralToken> {

        @Override
        public StringLiteralToken parse(MultiLineStringReader input) {
            return null;
        }
    }
}
