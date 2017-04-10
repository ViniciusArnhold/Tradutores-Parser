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
public class RelationalOperatorToken extends Token {

    protected RelationalOperatorToken(Object value) {
        super(TokenType.RELATIONAL_OP, value);
    }

    public static class Parser implements TokenParser<RelationalOperatorToken> {

        @Override
        public RelationalOperatorToken parse(MultiLineStringReader input) {
            Object parsed = ParserUtils.tryParse(input, TokenType.RELATIONAL_OP.possibleValues(), 2);
            return parsed != null ? new RelationalOperatorToken(parsed) : null;
        }
    }
}
