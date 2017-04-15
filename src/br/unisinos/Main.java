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
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Vinicius.
 *
 * @since ${PROJECT_VERSION}
 */
public class Main {

    private static final TokenParser<? extends Token> arithParser = new ArithmeticOperatorToken.Parser();
    private static final TokenParser<? extends Token> commentParser = new CommentToken.Parser();
    private static final TokenParser<? extends Token> equalParser = new EqualOperator.Parser();
    private static final TokenParser<? extends Token> numberParser = new NumberToken.Parser();
    private static final TokenParser<? extends Token> relationalParser = new RelationalOperatorToken.Parser();
    private static final TokenParser<? extends Token> reservedWordParser = new ReservedWordToken.Parser();
    private static final TokenParser<? extends Token> variableParser = new VariableToken.Parser();
    private static final TokenParser<? extends Token> lParenParser = new LeftParenthesisToken.Parser();
    private static final TokenParser<? extends Token> rParenParser = new RightParenthesisToken.Parser();
    private static final TokenParser<? extends Token> lBracketParser = new LeftBracketToken.Parser();
    private static final TokenParser<? extends Token> rBracketParser = new RightBracketToken.Parser();
    private static final TokenParser<? extends Token> lBraceParser = new LeftBraceToken.Parser();
    private static final TokenParser<? extends Token> rBraceParser = new RightBraceToken.Parser();
    private static final TokenParser<? extends Token> commaParser = new CommaToken.Parser();
    private static final TokenParser<? extends Token> semicolonParser = new SemicolonToken.Parser();

    private static final List<TokenParser<? extends Token>> PARSERS;
    private static final String USAGE = "USAGE: <--full> [files]+";

    static {
        List<TokenParser<? extends Token>> hParsers = new ArrayList<>();
        hParsers.add(commentParser);
        hParsers.add(lParenParser);
        hParsers.add(rParenParser);
        hParsers.add(rBracketParser);
        hParsers.add(lBracketParser);
        hParsers.add(lBraceParser);
        hParsers.add(rBraceParser);
        hParsers.add(commaParser);
        hParsers.add(semicolonParser);
        hParsers.add(relationalParser);
        hParsers.add(arithParser);
        hParsers.add(equalParser);
        hParsers.add(numberParser);
        hParsers.add(reservedWordParser);
        hParsers.add(variableParser);
        PARSERS = Collections.unmodifiableList(hParsers);
    }

    public static void main(String... args) throws URISyntaxException, IOException {
        if (args.length == 0) {
            Logger.error(USAGE);
        }
        Logger.info("Program called with " + "'" + Arrays.toString(args) + "'");
        boolean isFull = args[0].equalsIgnoreCase("--full");

        String[] files = Arrays.copyOfRange(args, isFull ? 1 : 0, args.length);
        if (files.length == 0) {
            Logger.error("No file specifed" + System.lineSeparator() + USAGE);
        }

        for (String file : files) {
            Path path = Paths.get(file);
            if (Files.notExists(path)) {
                Logger.error("File doesnt exists");
                Logger.error(file);
                System.exit(1);
            } else if (!Files.isRegularFile(path, LinkOption.NOFOLLOW_LINKS) || !Files.isReadable(path)) {
                Logger.error("Cant read file");
                Logger.error(file);
                System.exit(1);
            }
        }

        HashMap<String, List<Token>> mapping = new HashMap<>();

        for (Path path : Arrays.stream(files).map(Paths::get).collect(Collectors.toList())) {
            Logger.lineBreak();
            Logger.warn("Parsing file: " + path.toAbsolutePath());
            List<Token> tokens = new ArrayList<>();
            for (List<String> list : Utils.split(Files.readAllLines(path), "//FILE_SEPARATION_LINE")) {
                Logger.info("Parsing: ");
                Logger.info(list.stream().collect(Collectors.joining(System.lineSeparator())));

                MultiLineStringReader input = MultiLineStringReader.of(list);

                tokens = new ArrayList<>();
                while (input.hasMoreChars()) {
                    if (!input.hasMoreCharsOnSameLine() && input.hasMoreLines()) {
                        input.nextLine();
                    }
                    input.skipWhitespace();
                    tryParseAny(input, tokens);
                }

                String text = list.stream().collect(Collectors.joining(System.lineSeparator()));

                mapping.put(text, tokens);
            }

            Logger.lineBreak();
            Logger.info("Finished Parsing");
            Logger.info(tokens.toString());
            Logger.info(System.lineSeparator());


        }

        Logger.lineBreak();
        Logger.info("Finished Parsing All Files");
        Logger.info("Results");
        Logger.info(String.valueOf(mapping.entrySet().stream()
                .map(e -> e.getKey() + System.lineSeparator() + e.getValue())
                .collect(Collectors.joining(System.lineSeparator() + System.lineSeparator()))));

        Logger.lineBreak();
        Logger.lineBreak();
        Logger.lineBreak();
        Logger.warn("Program ran sucefully.");
    }

    private static void tryParseAny(MultiLineStringReader input, List<Token> listTokens) {
        for (TokenParser<? extends Token> parser : PARSERS) {
            Optional<? extends Token> token = parser.tryParse(input);
            if (token.isPresent()) {
                listTokens.add(token.get());
                return;
            }
        }
    }
}

