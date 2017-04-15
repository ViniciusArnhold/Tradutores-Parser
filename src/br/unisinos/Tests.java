package br.unisinos;

import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.impl.*;
import br.unisinos.tokens.impl.SeparatorToken.*;
import br.unisinos.util.Logger;
import br.unisinos.util.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Vinicius.
 *
 * @since ${PROJECT_VERSION}
 */
public class Tests {


    private static final ArithmeticOperatorToken.Parser arithParser = new ArithmeticOperatorToken.Parser();
    private static final CommentToken.Parser commentParser = new CommentToken.Parser();
    private static final EqualOperator.Parser equalParser = new EqualOperator.Parser();
    private static final NumberToken.Parser numberParser = new NumberToken.Parser();
    private static final RelationalOperatorToken.Parser relationalParser = new RelationalOperatorToken.Parser();
    private static final ReservedWordToken.Parser reservedWordParser = new ReservedWordToken.Parser();
    private static final VariableToken.Parser variableParser = new VariableToken.Parser();
    private static final TokenParser<? extends Token> lParenParser = new LeftParenthesisToken.Parser();
    private static final TokenParser<? extends Token> rParenParser = new RightParenthesisToken.Parser();
    private static final TokenParser<? extends Token> lBracketParser = new LeftBracketToken.Parser();
    private static final TokenParser<? extends Token> rBracketParser = new RightBracketToken.Parser();
    private static final TokenParser<? extends Token> lBraceParser = new LeftBraceToken.Parser();
    private static final TokenParser<? extends Token> rBraceParser = new RightBraceToken.Parser();
    private static final TokenParser<? extends Token> commaParser = new CommaToken.Parser();
    private static final TokenParser<? extends Token> semicolonParser = new SemicolonToken.Parser();


    private static final List<TokenParser<? extends Token>> parsers;
    private static volatile int counter = 1;

    static {
        parsers = new ArrayList<>();
        parsers.add(commentParser);
        parsers.add(lParenParser);
        parsers.add(rParenParser);
        parsers.add(rBracketParser);
        parsers.add(lBracketParser);
        parsers.add(lBraceParser);
        parsers.add(rBraceParser);
        parsers.add(commaParser);
        parsers.add(semicolonParser);
        parsers.add(relationalParser);
        parsers.add(arithParser);
        parsers.add(equalParser);
        parsers.add(numberParser);
        parsers.add(reservedWordParser);
        parsers.add(variableParser);
    }

    public static void main(String... args) throws URISyntaxException, IOException {
        Path path = Paths.get(ClassLoader.getSystemClassLoader().getResource("./fileInput_1.txt").toURI());

        List<List<String>> lists = Utils.split(Files.readAllLines(path), "//FILE_SEPARATION_LINE");

        HashMap<String, List<Token>> map = new HashMap<>();


        for (List<String> list : lists) {


            MultiLineStringReader input = MultiLineStringReader.of(list);

            List<Token> tokens = new ArrayList<>();
            while (input.hasMoreChars()) {
                if (!input.hasMoreCharsOnSameLine() && input.hasMoreLines()) {
                    input.nextLine();
                }
                input.skipWhitespace();
                tryParseAny(input, tokens);
            }

            String text = list.stream().collect(Collectors.joining(System.lineSeparator()));
            Logger.info("Parsed " + System.lineSeparator() + text);
            Logger.info("Results " + System.lineSeparator() + tokens);
            Logger.lineBreak();

            map.put(text, tokens);
            counter = 0;
        }

        Logger.lineBreak();
        Logger.lineBreak();
        Logger.info("Finished Parsing");
        Logger.info("Results");
        Logger.warn(String.valueOf(map.entrySet().stream().map(e -> e.getKey() + System.lineSeparator() + e.getValue()).collect(Collectors.joining(System.lineSeparator() + System.lineSeparator()))));


    }

    public static final Token tryParseAny(MultiLineStringReader input, List<Token> listTokens) {
        for (TokenParser<?> parser : parsers) {
            char dumb = input.peek();
            Token token = parser.parse(input);
            if (token != null) {
                listTokens.add(token);
                return token;
            }
        }
        return null;
    }

    private static void printState(List<Token> listTokens, MultiLineStringReader input) {
        System.out.println();
        Logger.info("Run: " + counter++);
        Logger.info("Current Tokens: " + listTokens.stream().map(Token::toString).collect(Collectors.joining(" ")));
        MultiLineStringReader.Point p = input.mark();
        Logger.info("Next 10 chars: " + (input.hasMoreChars(10) ? input.nextString(10) : input.nextString(input.remainingChars())));
        input.moveTo(p);
    }
}

