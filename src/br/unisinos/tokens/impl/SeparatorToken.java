package br.unisinos.tokens.impl;

import br.unisinos.MultiLineStringReader;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.TokenType;
import br.unisinos.util.ParserUtils;

import java.util.Optional;

/**
 * Created by first on 10/04/2017.
 */
public abstract class SeparatorToken extends Token {

    SeparatorToken(TokenType type, String value) {
        super(type, value);
    }

    public static class LeftParenthesisToken extends SeparatorToken {

        LeftParenthesisToken(String value) {
            super(TokenType.L_PAREN, value);
        }

        public static class Parser implements TokenParser<LeftParenthesisToken> {

            @Override
            public Optional<LeftParenthesisToken> tryParse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.L_PAREN.possibleValues(), 1);
                return parsed != null ? Optional.of(new LeftParenthesisToken(parsed.toString())) : Optional.empty();
            }
        }
    }

    public static class RightParenthesisToken extends SeparatorToken {

        RightParenthesisToken(String value) {
            super(TokenType.R_PAREN, value);
        }

        public static class Parser implements TokenParser<RightParenthesisToken> {

            @Override
            public Optional<RightParenthesisToken> tryParse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.R_PAREN.possibleValues(), 1);
                return parsed != null ? Optional.of(new RightParenthesisToken(parsed.toString())) : Optional.empty();
            }
        }
    }

    public static class LeftBracketToken extends SeparatorToken {

        LeftBracketToken(String value) {
            super(TokenType.L_BRACKET, value);
        }

        public static class Parser implements TokenParser<LeftBracketToken> {

            @Override
            public Optional<LeftBracketToken> tryParse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.L_BRACKET.possibleValues(), 1);
                return parsed != null ? Optional.of(new LeftBracketToken(parsed.toString())) : Optional.empty();
            }
        }
    }

    public static class RightBracketToken extends SeparatorToken {

        RightBracketToken(String value) {
            super(TokenType.R_BRACKET, value);
        }

        public static class Parser implements TokenParser<RightBracketToken> {

            @Override
            public Optional<RightBracketToken> tryParse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.L_PAREN.possibleValues(), 1);
                return parsed != null ? Optional.of(new RightBracketToken(parsed.toString())) : Optional.empty();
            }
        }
    }

    public static class LeftBraceToken extends SeparatorToken {

        LeftBraceToken(String value) {
            super(TokenType.L_BRACE, value);
        }

        public static class Parser implements TokenParser<LeftBraceToken> {

            @Override
            public Optional<LeftBraceToken> tryParse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.L_BRACE.possibleValues(), 1);
                return parsed != null ? Optional.of(new LeftBraceToken(parsed.toString())) : Optional.empty();
            }
        }
    }

    public static class RightBraceToken extends SeparatorToken {

        RightBraceToken(String value) {
            super(TokenType.R_BRACE, value);
        }

        public static class Parser implements TokenParser<RightBraceToken> {

            @Override
            public Optional<RightBraceToken> tryParse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.R_BRACE.possibleValues(), 1);
                return parsed != null ? Optional.of(new RightBraceToken(parsed.toString())) : Optional.empty();
            }
        }
    }

    public static class CommaToken extends SeparatorToken {

        CommaToken(String value) {
            super(TokenType.COMMA, value);
        }

        public static class Parser implements TokenParser<CommaToken> {

            @Override
            public Optional<CommaToken> tryParse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.COMMA.possibleValues(), 1);
                return parsed != null ? Optional.of(new CommaToken(parsed.toString())) : Optional.empty();
            }
        }
    }

    public static class SemicolonToken extends SeparatorToken {

        SemicolonToken(String value) {
            super(TokenType.SEMICOLON, value);
        }

        public static class Parser implements TokenParser<SemicolonToken> {

            @Override
            public Optional<SemicolonToken> tryParse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.SEMICOLON.possibleValues(), 1);
                return parsed != null ? Optional.of(new SemicolonToken(parsed.toString())) : Optional.empty();
            }
        }
    }
}
