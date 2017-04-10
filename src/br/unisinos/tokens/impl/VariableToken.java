package br.unisinos.tokens.impl;

import br.unisinos.MultiLineStringReader;
import br.unisinos.parse.ParseException;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.TokenType;
import br.unisinos.util.ParserUtils;

import java.util.HashMap;

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
        public VariableToken parse(MultiLineStringReader input) {
            ReservedWordToken reservedWordToken = new ReservedWordToken.Parser().parse(input);
            if (reservedWordToken != null) {
                throw new ParseException("Expected a variable but found a reserved word", input.curPos() - 1);
            }
            MultiLineStringReader.Point point = input.mark();
            String text = ParserUtils.parseUntilWhitespace(input, true);
            if (text.length() == 0) {
                input.moveTo(point);
                return null;
            }
            return new VariableToken(text);
        }
    }

}
