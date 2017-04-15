package br.unisinos.util;

import br.unisinos.MultiLineStringReader;

import java.util.Objects;
import java.util.Set;

/**
 * Created by Vinicius.
 *
 * @since ${PROJECT_VERSION}
 */
public class ParserUtils {

    private ParserUtils() {

    }

    public static String tryParse(MultiLineStringReader input, Set<String> possibleValues, int biggestSize) {
        MultiLineStringReader.Point inicio = input.mark();

        StringBuilder sb = new StringBuilder();
        while (input.hasMoreCharsOnSameLine() && biggestSize-- != 0 && !Character.isWhitespace(input.peek())) {
            char next = input.nextChar();
            sb.append(next);
        }

        if (possibleValues.contains(sb.toString()))
            return sb.toString().intern();

        input.moveTo(inicio);
        return null;
    }

    public static String parseUntilWhitespace(MultiLineStringReader input, boolean sameLine) {
        Objects.requireNonNull(input);
        StringBuilder sb = new StringBuilder();
        while (sameLine ? input.hasMoreCharsOnSameLine() : input.hasMoreChars()) {
            if (Character.isWhitespace(input.peek()))
                break;
            sb.append(input.nextChar());
        }
        return sb.toString();
    }
}
