package br.unisinos;

import br.unisinos.parse.ParseException;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.impl.DirectionToken.BackToken;
import br.unisinos.tokens.impl.DirectionToken.DireitaToken;
import br.unisinos.tokens.impl.DirectionToken.EsquerdaToken;
import br.unisinos.tokens.impl.DirectionToken.ForwardToken;
import br.unisinos.tokens.impl.OperatorToken.AfterOperatorToken;
import br.unisinos.util.Logger;
import br.unisinos.util.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static br.unisinos.tokens.impl.OperatorToken.ThenOperatorToken;

public class Main2 {
    private static final TokenParser<? extends Token> thenOperatorParser =
            new ThenOperatorToken.Parser();
    private static final TokenParser<? extends Token> afterOperatorParser =
            new AfterOperatorToken.Parser();

    private static final TokenParser<? extends Token> rightToken = new DireitaToken.Parser();
    private static final TokenParser<? extends Token> leftToken = new EsquerdaToken.Parser();
    private static final TokenParser<? extends Token> upToken = new ForwardToken.Parser();
    private static final TokenParser<? extends Token> backToken = new BackToken.Parser();


    private static final List<TokenParser<? extends Token>> PARSERS;
    private static final String USAGE = "USAGE: <--full> [files]+";
    private static volatile int parseFailCount = 0;

    static {
        List<TokenParser<? extends Token>> hParsers = new ArrayList<>();
        hParsers.add(thenOperatorParser);
        hParsers.add(afterOperatorParser);

        hParsers.add(rightToken);
        hParsers.add(leftToken);
        hParsers.add(upToken);
        hParsers.add(backToken);

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
