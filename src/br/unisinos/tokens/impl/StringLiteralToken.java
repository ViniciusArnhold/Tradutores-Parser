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
    StringLiteralToken(String value) {
        super(TokenType.STRING_LITERAL, value);
    }

    public static class Parser implements TokenParser<StringLiteralToken> {

        @Override
        public Optional<StringLiteralToken> tryParse(MultiLineStringReader input) {
            MultiLineStringReader.Point point = input.mark();

            input.skipWhitespace();
            if (input.nextChar() == '"') {
                StringBuilder sb = new StringBuilder("\"");
                while (input.hasMoreChars() && !Character.isWhitespace(input.peek())) {
                    if (input.peek() == '"') {
                        sb.append(input.nextChar());
                        break;
                    }
                    sb.append(input.nextChar());
                }
                return Optional.of(new StringLiteralToken(sb.toString()));
            } else {
                input.moveTo(point);
                return Optional.empty();
            }
        }
    }
}
