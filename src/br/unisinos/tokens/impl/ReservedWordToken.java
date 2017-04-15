package br.unisinos.tokens.impl;

import br.unisinos.MultiLineStringReader;
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

        @Override
        public ReservedWordToken parse(MultiLineStringReader input) {
            MultiLineStringReader.Point point = input.mark();
            StringBuilder sb = new StringBuilder();
            while (input.hasMoreCharsOnSameLine() && Character.isLetter(input.peek())) {
                sb.append(input.nextChar());
            }
            String text = sb.toString();
            if (text.length() == 0 || !TokenType.RESERVED_WORD.possibleValues().contains(text)) {
                input.moveTo(point);
                return null;
            }
            return new ReservedWordToken(text);
        }
    }
}
