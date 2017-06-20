package br.unisinos.tokens.impl;

import br.unisinos.MultiLineStringReader;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.TokenType;

import java.util.Optional;

/**
 * Created by eduardoxy on 01/06/17.
 */
public abstract class OperatorToken extends Token {
    OperatorToken(TokenType type, String value) {
        super(type, value);
    }

    private static Optional<String> tryParse(TokenType type, MultiLineStringReader input) {
        MultiLineStringReader.Point point = input.mark();
        StringBuilder sb = new StringBuilder();

        while (input.hasMoreCharsOnSameLine() && Character.isLetter(input.peek())) {
            sb.append(input.nextChar());
        }
        String text = sb.toString();
        if (text.length() == 0 || !type.asTokenName().equalsIgnoreCase(text)) {
            input.moveTo(point);
            return Optional.empty();
        }
        return Optional.of(text);
    }

    public static class ThenOperatorToken extends OperatorToken {
        ThenOperatorToken(String value) {
            super(TokenType.THEN_OP, value);
        }

        public static class Parser implements TokenParser<ThenOperatorToken> {
            @Override
            public Optional<ThenOperatorToken> tryParse(MultiLineStringReader input) {
                return OperatorToken.tryParse(TokenType.THEN_OP, input).map(ThenOperatorToken::new);
            }
        }
    }

    public static class AfterOperatorToken extends OperatorToken {
        AfterOperatorToken(String value) {
            super(TokenType.AFTER_OP, value);
        }

        public static class Parser implements TokenParser<OperatorToken> {

            @Override
            public Optional<OperatorToken> tryParse(MultiLineStringReader input) {
                return OperatorToken.tryParse(TokenType.AFTER_OP, input).map(AfterOperatorToken::new);
            }
        }
    }
}
