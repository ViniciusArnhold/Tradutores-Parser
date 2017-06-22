package br.unisinos.parse;

import java.util.Arrays;

import static java.lang.String.format;

/**
 * Created by Vinicius, Fabio e Eduardo.
 *
 * @since ${PROJECT_VERSION}
 */
public class ParseException extends RuntimeException {

    public ParseException(String message, String errorLine, int charNum) {
        super(format("%s%s%s%s%s", message, System.lineSeparator(), errorLine, System.lineSeparator(), generateErrorLine(charNum)));
    }

    private static String generateErrorLine(int num) {
        char[] str = new char[num];
        Arrays.fill(str, '-');
        str[num - 1] = '^';
        return new String(str).intern();
    }


}
