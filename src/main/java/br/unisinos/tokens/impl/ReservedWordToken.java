package br.unisinos.tokens.impl;

import br.unisinos.MultiLineStringReader;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.TokenType;

import java.util.Optional;

/**
 * Created by Vinicius, Fabio e Eduardo.
 *
 * @since 1.0
 */
public class ReservedWordToken extends Token {


    private ReservedWordToken(Object value) {
        super(TokenType.RESERVED_WORD, value);
    }

    public static class Parser implements TokenParser<ReservedWordToken> {

        @Override
        public Optional<ReservedWordToken> tryParse(MultiLineStringReader input) {
            MultiLineStringReader.Point point = input.mark();
            StringBuilder sb = new StringBuilder();

            while (input.hasMoreCharsOnSameLine() && Character.isLetter(input.peek())) {
                sb.append(input.nextChar());
            }
            String text = sb.toString();
            if (text.length() == 0 || !TokenType.RESERVED_WORD.possibleValues().contains(text)) {
                input.moveTo(point);
                return Optional.empty();
            }
            return Optional.of(new ReservedWordToken(text));
        }
    }
}
