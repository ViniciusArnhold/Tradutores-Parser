package br.unisinos.tokens.impl;

import br.unisinos.MultiLineStringReader;
import br.unisinos.parse.ParseException;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.TokenType;

/**
 * Created by Vinicius.
 *
 * @since ${PROJECT_VERSION}
 */
public class CommentToken extends Token {

    protected CommentToken(Object value) {
        super(TokenType.COMMENT, value);
    }

    public static class Parser implements TokenParser<CommentToken> {
        @Override
        public CommentToken parse(MultiLineStringReader input) {
            MultiLineStringReader.Point inicio = input.mark();
            if(!input.hasMoreChars(2)) {
                return null;
            }
            String inicioToken = input.nextString(2);

            if ("//".equalsIgnoreCase(inicioToken)) {
                return new CommentToken("//" + input.readToEndOfLine());
            } else if ("/*".equalsIgnoreCase(inicioToken)) {
                StringBuilder sb = new StringBuilder("/* ");
                do {
                    if (!input.hasMoreChars()) {
                        throw new ParseException("Reached end of file while expecting end of comment."
                                + System.lineSeparator()
                                + input.currentLine(),
                                input.curPos());
                    }
                    sb.append(input.nextChar());

                } while (!(sb.substring(sb.length() - 2, sb.length())).equalsIgnoreCase("*/"));
                return new CommentToken(sb.toString());
            }
            input.moveTo(inicio);
            return null;
        }
    }
}
