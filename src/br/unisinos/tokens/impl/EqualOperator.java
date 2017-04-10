package br.unisinos.tokens.impl;

import br.unisinos.MultiLineStringReader;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.TokenType;
import br.unisinos.util.ParserUtils;

/**
 * Created by Vinicius.
 *
 * @since ${PROJECT_VERSION}
 */
public class EqualOperator extends Token {
    protected EqualOperator(Object value) {
        super(TokenType.EQUAL_OP, value);
    }


    public static class Parser implements TokenParser<EqualOperator> {

        @Override
        public EqualOperator parse(MultiLineStringReader input) {
            Object parsed = ParserUtils.tryParse(input, TokenType.EQUAL_OP.possibleValues(), 1);
            return parsed != null ? new EqualOperator(parsed) : null;
        }
    }
}
