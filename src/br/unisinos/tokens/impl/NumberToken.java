package br.unisinos.tokens.impl;

import br.unisinos.MultiLineStringReader;
import br.unisinos.MultiLineStringReader.Point;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.TokenType;

import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Created by Vinicius.
 *
 * @since ${PROJECT_VERSION}
 */
public class NumberToken extends Token {

    protected NumberToken(Double value) {
        super(TokenType.NUMERIC_VALUE, value);
    }

    protected NumberToken(Integer value) {
        super(TokenType.NUMERIC_VALUE, value);
    }

    public static class Parser implements TokenParser<NumberToken> {

        private static final Predicate<String> INT_PATTERN = Pattern.compile("^\\d+(.\\d+)?$").asPredicate();
        private static final Predicate<String> DOUBLE_PATTERN = Pattern.compile("^\\d+(.\\d+)?$").asPredicate();

        @Override
        public NumberToken parse(MultiLineStringReader input) {
            Point inicio = input.mark();

            StringBuilder sb = new StringBuilder();
            while (input.hasMoreCharsOnSameLine()) {
                char next = input.peek();
                if (!Character.isDigit(next) && next != '.') {
                    break;
                }
                sb.append(input.nextChar());
            }
            String text = sb.toString();
            if (INT_PATTERN.test(text)) {
                return new NumberToken(Integer.valueOf(text));
            } else if (DOUBLE_PATTERN.test(text)) {
                return new NumberToken(Double.valueOf(text));
            }
            input.moveTo(inicio);
            return null;
        }
    }
}
