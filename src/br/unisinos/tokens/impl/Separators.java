package br.unisinos.tokens.impl;

import br.unisinos.MultiLineStringReader;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.TokenType;
import br.unisinos.util.ParserUtils;

/**
 * Created by first on 10/04/2017.
 */
public class Separators {

    public static class LeftParenthesisToken extends Token {

        protected LeftParenthesisToken(Object value) {
            super(TokenType.L_PAREN, value);
        }

        public static class Parser implements TokenParser<LeftParenthesisToken> {

            @Override
            public LeftParenthesisToken parse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.L_PAREN.possibleValues(), 1);
                return parsed != null ? new LeftParenthesisToken(parsed) : null;
            }
        }
    }

    public static class RightParenthesisToken extends Token {

        protected RightParenthesisToken(Object value) {
            super(TokenType.R_PAREN, value);
        }

        public static class Parser implements TokenParser<RightParenthesisToken> {

            @Override
            public RightParenthesisToken parse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.R_PAREN.possibleValues(), 1);
                return parsed != null ? new RightParenthesisToken(parsed) : null;
            }
        }
    }

    public static class LeftBracketToken extends Token {

        protected LeftBracketToken(Object value) {
            super(TokenType.L_BRACKET, value);
        }

        public static class Parser implements TokenParser<LeftBracketToken> {

            @Override
            public LeftBracketToken parse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.L_BRACKET.possibleValues(), 1);
                return parsed != null ? new LeftBracketToken(parsed) : null;
            }
        }
    }

    public static class RightBracketToken extends Token {

        protected RightBracketToken(Object value) {
            super(TokenType.R_BRACKET, value);
        }

        public static class Parser implements TokenParser<RightBracketToken> {

            @Override
            public RightBracketToken parse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.L_PAREN.possibleValues(), 1);
                return parsed != null ? new RightBracketToken(parsed) : null;
            }
        }
    }

    public static class LeftBraceToken extends Token {

        protected LeftBraceToken(Object value) {
            super(TokenType.L_BRACE, value);
        }

        public static class Parser implements TokenParser<LeftBraceToken> {

            @Override
            public LeftBraceToken parse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.L_BRACE.possibleValues(), 1);
                return parsed != null ? new LeftBraceToken(parsed) : null;
            }
        }
    }

    public static class RightBraceToken extends Token {

        protected RightBraceToken(Object value) {
            super(TokenType.R_BRACE, value);
        }

        public static class Parser implements TokenParser<RightBraceToken> {

            @Override
            public RightBraceToken parse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.R_BRACE.possibleValues(), 1);
                return parsed != null ? new RightBraceToken(parsed) : null;
            }
        }
    }

    public static class CommaToken extends Token {

        protected CommaToken(Object value) {
            super(TokenType.COMMA, value);
        }

        public static class Parser implements TokenParser<CommaToken> {

            @Override
            public CommaToken parse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.COMMA.possibleValues(), 1);
                return parsed != null ? new CommaToken(parsed) : null;
            }
        }
    }

    public static class SemicolonToken extends Token {

        protected SemicolonToken(Object value) {
            super(TokenType.SEMICOLON, value);
        }

        public static class Parser implements TokenParser<SemicolonToken> {

            @Override
            public SemicolonToken parse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.SEMICOLON.possibleValues(), 1);
                return parsed != null ? new SemicolonToken(parsed) : null;
            }
        }
    }
}
