package br.unisinos.tokens.impl;

import br.unisinos.MultiLineStringReader;
import br.unisinos.parse.ParseException;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.TokenType;

import java.util.HashMap;
import java.util.Optional;

/**
 * Created by Vinicius.
 *
 * @since ${PROJECT_VERSION}
 */
public class VariableToken extends Token {

    private static final HashMap<String, Integer> VARIABLES = new HashMap<>();
    private static volatile int nextID = 1;

    private final String text;

    protected VariableToken(String text) {
        super(TokenType.VARIABLE, VARIABLES.computeIfAbsent(text, (t) -> nextID++));
        this.text = text;
    }

    public static class Parser implements TokenParser<VariableToken> {

        @Override
        public Optional<VariableToken> tryParse(MultiLineStringReader input) {
            MultiLineStringReader.Point point = input.mark();
            Optional<ReservedWordToken> reservedWordToken = new ReservedWordToken.Parser().tryParse(input);
            if (reservedWordToken.isPresent()) {
                throw new ParseException("Expected a variable but found a reserved word", input.currentLine(), input.curPos() - 1);
            }
            StringBuilder sb = new StringBuilder();
            while (input.hasMoreCharsOnSameLine() && !Character.isWhitespace(input.peek()) && Character.isLetter(input.peek())) {
                sb.append(input.nextChar());
            }
            String text = sb.toString();
            if (text.length() == 0) {
                input.moveTo(point);
                return Optional.empty();
            }
            return Optional.of(new VariableToken(text));
        }
    }
}
