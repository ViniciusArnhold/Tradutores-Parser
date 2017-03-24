package br.unisinos;

import br.unisinos.tokens.IToken;
import br.unisinos.tokens.ITokenValue;
import br.unisinos.tokens.TokenEntry;
import br.unisinos.tokens.TokenType;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by first on 23/03/2017.
 */
public class TokenIdentifier {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d*\\.?\\d*");

    private TokenIdentifier() {

    }

    public static TokenEntry<IToken<TokenType>,ITokenValue> identifyType(String text, Optional<TokenType> expectedType) {
        
        Optional<Double> numeric = tryParseToNumber(text);


        if(numeric.isPresent()) { // Eh um numero


        }
        return null;
    }

    private static Optional<Double> tryParseToNumber(String text) {
        Objects.requireNonNull(text);
        if(text.length() ==  0) throw new IllegalArgumentException("Text cannot be empty");
        boolean isContendent = Character.isDigit(text.charAt(0))
                && Character.isDigit(text.charAt(text.length()-1));

        if(!isContendent) {
            return Optional.empty();
        }
        else if(!text.matches(NUMBER_PATTERN.pattern())) {
            return Optional.empty();
        }
        return Optional.of(Double.parseDouble(text));
    }

    private enum NumberType {
        NUMBER,
        DOT,
    }



}
