package br.unisinos.tokens.impl;

import br.unisinos.MultiLineStringReader;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.TokenType;
import br.unisinos.util.ParserUtils;

/**
 * Created by first on 10/04/2017.
 */
public abstract class SeparatorToken extends Token {

    protected SeparatorToken(TokenType type, String value) {
        super(type, value);
    }

    public static class LeftParenthesisToken extends SeparatorToken {

        protected LeftParenthesisToken(String value) {
            super(TokenType.L_PAREN, value);
        }

        public static class Parser implements TokenParser<LeftParenthesisToken> {

            @Override
            public LeftParenthesisToken parse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.L_PAREN.possibleValues(), 1);
                return parsed != null ? new LeftParenthesisToken(parsed.toString()) : null;
            }
        }
    }

    public static class RightParenthesisToken extends SeparatorToken {

        protected RightParenthesisToken(String value) {
            super(TokenType.R_PAREN, value);
        }

        public static class Parser implements TokenParser<RightParenthesisToken> {

            @Override
            public RightParenthesisToken parse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.R_PAREN.possibleValues(), 1);
                return parsed != null ? new RightParenthesisToken(parsed.toString()) : null;
            }
        }
    }

    public static class LeftBracketToken extends SeparatorToken {

        protected LeftBracketToken(String value) {
            super(TokenType.L_BRACKET, value);
        }

        public static class Parser implements TokenParser<LeftBracketToken> {

            @Override
            public LeftBracketToken parse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.L_BRACKET.possibleValues(), 1);
                return parsed != null ? new LeftBracketToken(parsed.toString()) : null;
            }
        }
    }

    public static class RightBracketToken extends SeparatorToken {

        protected RightBracketToken(String value) {
            super(TokenType.R_BRACKET, value);
        }

        public static class Parser implements TokenParser<RightBracketToken> {

            @Override
            public RightBracketToken parse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.L_PAREN.possibleValues(), 1);
                return parsed != null ? new RightBracketToken(parsed.toString()) : null;
            }
        }
    }

    public static class LeftBraceToken extends SeparatorToken {

        protected LeftBraceToken(String value) {
            super(TokenType.L_BRACE, value);
        }

        public static class Parser implements TokenParser<LeftBraceToken> {

            @Override
            public LeftBraceToken parse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.L_BRACE.possibleValues(), 1);
                return parsed != null ? new LeftBraceToken(parsed.toString()) : null;
            }
        }
    }

    public static class RightBraceToken extends SeparatorToken {

        protected RightBraceToken(String value) {
            super(TokenType.R_BRACE, value);
        }

        public static class Parser implements TokenParser<RightBraceToken> {

            @Override
            public RightBraceToken parse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.R_BRACE.possibleValues(), 1);
                return parsed != null ? new RightBraceToken(parsed.toString()) : null;
            }
        }
    }

    public static class CommaToken extends SeparatorToken {

        protected CommaToken(String value) {
            super(TokenType.COMMA, value);
        }

        public static class Parser implements TokenParser<CommaToken> {

            @Override
            public CommaToken parse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.COMMA.possibleValues(), 1);
                return parsed != null ? new CommaToken(parsed.toString()) : null;
            }
        }
    }

    public static class SemicolonToken extends SeparatorToken {

        protected SemicolonToken(String value) {
            super(TokenType.SEMICOLON, value);
        }

        public static class Parser implements TokenParser<SemicolonToken> {

            @Override
            public SemicolonToken parse(MultiLineStringReader input) {
                Object parsed = ParserUtils.tryParse(input, TokenType.SEMICOLON.possibleValues(), 1);
                return parsed != null ? new SemicolonToken(parsed.toString()) : null;
            }
        }
    }
}
