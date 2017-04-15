package br.unisinos.parse;

import java.util.Arrays;

/**
 * Created by Vinicius.
 *
 * @since ${PROJECT_VERSION}
 */
public class ParseException extends RuntimeException {

    public ParseException(String message, String errorLine, int charNum) {
        super(String.format("%s%s%s%s%s", message, System.lineSeparator(), errorLine, System.lineSeparator(), generateErrorLine(charNum)));
    }

    public ParseException(Exception e) {
        super(e);
    }

    private static String generateErrorLine(int num) {
        char[] str = new char[num];
        Arrays.fill(str, '-');
        str[num - 1] = '^';
        return new String(str);
    }


}
