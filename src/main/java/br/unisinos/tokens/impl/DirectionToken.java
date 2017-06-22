package br.unisinos.tokens.impl;

import br.unisinos.direction.Direction;
import br.unisinos.MultiLineStringReader;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.TokenType;
import br.unisinos.util.ParserUtils;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static br.unisinos.MultiLineStringReader.Point;

/**
 * Created by vinicius on 01/06/17.
 */
public abstract class DirectionToken extends Token {

    private static final Predicate<String> INT_PATTERN = Pattern.compile("^\\d+$").asPredicate();

    protected DirectionToken(TokenType type, Direction direction) {
        super(type, direction);
    }

    private static Optional<Direction> tryParse(TokenType type, MultiLineStringReader input) {
        Point point = input.mark();
        int line = input.linePos();
        // by EPJ: On the first time, the line will be -1 and the linePos will be always 0
        if (line < 0)
            line++;

        String text = ParserUtils.parseUntilWhitespace(input, true);
        if (text.equalsIgnoreCase(type.asTokenName())) {
            // by EPJ: Adjusted to read after the token
            if (input.skipWhitespace().length() == 0 || input.linePos() != line) {
                input.moveTo(point);
                return Optional.empty();
            }
            StringBuilder sb = new StringBuilder();
            while (input.hasMoreCharsOnSameLine() && Character.isDigit(input.peek())) {
                sb.append(input.nextChar());
            }
            if (INT_PATTERN.test(sb.toString())) {
                return Optional.of(Direction.ofTokenType(type, Long.parseLong(sb.toString())));
            }
        } else {
            input.moveTo(point);
        }

        return Optional.empty();
    }

    public static class EsquerdaToken extends DirectionToken {
        public EsquerdaToken(Direction direction) {
            super(TokenType.LEFT, direction);
        }

        public static class Parser implements TokenParser<EsquerdaToken> {
            @Override
            public Optional<EsquerdaToken> tryParse(MultiLineStringReader input) {
                return DirectionToken.tryParse(TokenType.LEFT, input)
                        .map(EsquerdaToken::new);
            }
        }
    }

    public static class BackToken extends DirectionToken {
        BackToken(Direction direction) {
            super(TokenType.BACK, direction);
        }

        public static class Parser implements TokenParser<BackToken> {
            @Override
            public Optional<BackToken> tryParse(MultiLineStringReader input) {
                return DirectionToken.tryParse(TokenType.BACK, input)
                        .map(BackToken::new);
            }
        }
    }

    public static class ForwardToken extends DirectionToken {
        ForwardToken(Direction direction) {
            super(TokenType.FORWARD, direction);
        }

        public static class Parser implements TokenParser<ForwardToken> {
            @Override
            public Optional<ForwardToken> tryParse(MultiLineStringReader input) {
                return DirectionToken.tryParse(TokenType.FORWARD, input)
                        .map(ForwardToken::new);
            }
        }
    }

    public static class DireitaToken extends DirectionToken {
        DireitaToken(Direction direction) {
            super(TokenType.RIGHT, direction);
        }

        public static class Parser implements TokenParser<DireitaToken> {
            @Override
            public Optional<DireitaToken> tryParse(MultiLineStringReader input) {
                return DirectionToken.tryParse(TokenType.RIGHT, input)
                        .map(DireitaToken::new);
            }
        }
    }
}
