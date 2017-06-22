package br.unisinos.tokens.impl;

import br.unisinos.MultiLineStringReader;
import br.unisinos.parse.ParseException;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.TokenType;

import java.util.Optional;

/**
 * Created by Vinicius, Fabio e Eduardo.
 *
 * @since 1.0
 */
public class CommentToken extends Token {

    private CommentToken(Object value) {
        super(TokenType.COMMENT, value);
    }

    public static class Parser implements TokenParser<CommentToken> {
        @Override
        public Optional<CommentToken> tryParse(MultiLineStringReader input) {
            MultiLineStringReader.Point inicio = input.mark();
            if (!input.hasMoreChars(2)) {
                return Optional.empty();
            }
            String inicioToken = input.nextString(2);

            if ("//".equalsIgnoreCase(inicioToken)) {
                return Optional.of(new CommentToken("/" + input.readToEndOfLine()));
            } else if ("/*".equalsIgnoreCase(inicioToken)) {
                StringBuilder sb = new StringBuilder("/* ");
                do {
                    if (!input.hasMoreChars()) {
                        throw new ParseException("Reached end of file while expecting end of comment.", input.currentLine(), input.curPos());
                    }
                    sb.append(input.nextChar());

                } while (!(sb.substring(sb.length() - 2, sb.length())).equalsIgnoreCase("*/"));
                return Optional.of(new CommentToken(sb.toString()));
            }
            input.moveTo(inicio);
            return Optional.empty();
        }
    }
}
