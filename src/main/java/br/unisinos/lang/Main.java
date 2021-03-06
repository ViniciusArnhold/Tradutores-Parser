package br.unisinos.lang;

import br.unisinos.MultiLineStringReader;
import br.unisinos.parse.ParseException;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.impl.*;
import br.unisinos.tokens.impl.SeparatorToken.*;
import br.unisinos.util.Logger;
import br.unisinos.util.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static br.unisinos.tokens.impl.SeparatorToken.*;

/**
 * Created by Vinicius, Fabio e Eduardo.
 *
 * @since 1.0
 */
class Main {

    private static final TokenParser<? extends Token> arithParser = new ArithmeticOperatorToken.Parser();
    private static final TokenParser<? extends Token> commentParser = new CommentToken.Parser();
    private static final TokenParser<? extends Token> equalParser = new EqualOperator.Parser();
    private static final TokenParser<? extends Token> numberParser = new NumberToken.Parser();
    private static final TokenParser<? extends Token> relationalParser = new RelationalOperatorToken.Parser();
    private static final TokenParser<? extends Token> reservedWordParser = new ReservedWordToken.Parser();
    private static final TokenParser<? extends Token> variableParser = new VariableToken.Parser();
    private static final TokenParser<? extends Token> stringLiteralParser = new StringLiteralToken.Parser();
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
    private static volatile int parseFailCount = 0;

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
        hParsers.add(stringLiteralParser);
        hParsers.add(variableParser);
        PARSERS = Collections.unmodifiableList(hParsers);
    }

    public static void main(String... args) throws IOException {
        // Arguments processing
        if (args.length == 0) {
            Logger.error(USAGE);
            System.exit(0);
        }


        Logger.info("Program called with " + "'" + Arrays.toString(args) + "'");
        boolean isFull = (args.length > 0) && args[0].equalsIgnoreCase("--full");

        String[] files = Arrays.copyOfRange(args, isFull ? 1 : 0, args.length);
        if (files.length == 0) {
            Logger.error("No file specifed" + System.lineSeparator() + USAGE);
            System.exit(1);
        }

        // Check file's consistency
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
            for (List<String> list : Utils.split(Files.readAllLines(path))) {

                MultiLineStringReader input = MultiLineStringReader.of(list);

                if (!input.hasMoreChars()) {
                    Logger.warn("Found empty file, skipping.");
                    continue;
                }
                // by EPJ: Moved here to show only if the file is not empty!
                Logger.warn("Parsing:");
                Logger.info(list.stream().collect(Collectors.joining(System.lineSeparator())));

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
            if (tokens.isEmpty())
                Logger.info("No token for empty files.");
            else {
                Logger.info(tokens.toString());
                Logger.info(System.lineSeparator());
            }
        }

        Logger.lineBreak();
        Logger.info("           ----------          ");
        Logger.lineBreak();
        Logger.info("Finished Parsing All Files");
        Logger.info("Results");
        Logger.info(String.valueOf(
                mapping.entrySet().stream()
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
                parseFailCount = 0;
                listTokens.add(token.get());
                return;
            }
        }


        parseFailCount++;
        // By EPJ: Throw an expection because no token was matched.
        if (parseFailCount > 3) {
            throw new ParseException("Unknown token.", input.currentLine(), input.hasMoreChars() ? (input.curPos() + 1) : input.curPos());
        }
    }

}

