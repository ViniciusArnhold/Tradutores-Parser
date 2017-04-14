package br.unisinos;

import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.impl.*;
import br.unisinos.util.Logger;
import br.unisinos.util.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

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

    private static final List<TokenParser<? extends Token>> parsers;

    static {
        parsers = new ArrayList<>();
        parsers.add(commentParser);
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

        for (List<String> list : lists) {

            Logger.info("Parsing " + list);

            MultiLineStringReader input = MultiLineStringReader.of(list);
            List<Token> tokens = new ArrayList<>();


            while (input.hasMoreChars()) {
                input.skipWhitespace();
                if (input.peekString().equalsIgnoreCase(";")) {
                    input.nextChar();
                    input.skipWhitespace();
                }
                MultiLineStringReader.Point dumb = input.mark();
                String x = input.peekString();
                tryParseAny(input, tokens);
            }

            System.out.println(tokens.stream().map(Token::toString).collect(joining(" ")));

        }

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
}

