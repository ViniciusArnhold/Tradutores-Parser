package br.unisinos;

import br.unisinos.analysis.rule.DelegatingRule;
import br.unisinos.analysis.rule.Rule;
import br.unisinos.analysis.rule.RuleFactory;
import br.unisinos.parse.ParseException;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;
import br.unisinos.tokens.impl.DirectionToken.BackToken;
import br.unisinos.tokens.impl.DirectionToken.DireitaToken;
import br.unisinos.tokens.impl.DirectionToken.EsquerdaToken;
import br.unisinos.tokens.impl.DirectionToken.ForwardToken;
import br.unisinos.tokens.impl.OperatorToken.AfterOperatorToken;
import br.unisinos.tokens.impl.SeparatorToken;
import br.unisinos.util.Logger;
import br.unisinos.util.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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

    public static void main(String... args) throws IOException, URISyntaxException {
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
        Path pathFile = null;
        List<Path> listPaths = new ArrayList<>();
        for (String file : files) {
            URL url = Main2.class.getResource("/" + file);
            if (url == null || !Files.exists(pathFile = Paths.get(url.toURI()))) {
                Logger.error("File doesnt exists");
                Logger.error(file);
                System.exit(1);
            } else if (!Files.isRegularFile(pathFile, LinkOption.NOFOLLOW_LINKS) || !Files.isReadable(pathFile)) {
                Logger.error("Cant read file");
                Logger.error(file);
                System.exit(1);
            }
            listPaths.add(pathFile);
        }

        HashMap<String, List<Token>> mapping = new HashMap<>();

        RuleFactory ruleFactory = new RuleFactory();
        ruleFactory.addReporter(Logger::logAnalysis);

        //basico -> FRENTE n
        Rule forwardRule = ruleFactory.newParserRule(new ForwardToken.Parser());

        //basico -> ESQUERDA n
        Rule leftRule = ruleFactory.newParserRule(new EsquerdaToken.Parser());

        //basico -> DIREITA n
        Rule rightRule = ruleFactory.newParserRule(new DireitaToken.Parser());

        //basico -> TRAS n
        Rule backRule = ruleFactory.newParserRule(new BackToken.Parser());

        //comando -> basico
        Rule basicRule = ruleFactory.newMultiRuleBuilder()
                .orAccept(forwardRule)
                .orAccept(leftRule)
                .orAccept(rightRule)
                .orAccept(backRule)
                .build();


        //Delegators servem para resolver ciclos.
        DelegatingRule thenRuleDelegator = ruleFactory.newDelegatingRule();
        DelegatingRule afterRuleDelegator = ruleFactory.newDelegatingRule();
        DelegatingRule innerRuleDelegator = ruleFactory.newDelegatingRule();

        //Aceita a primeira regra que funcionar
        Rule multiOptionCommandRule = ruleFactory.newMultiRuleBuilder()
                .orAccept(basicRule)
                .orAccept(thenRuleDelegator)
                .orAccept(afterRuleDelegator)
                .orAccept(innerRuleDelegator)
                .build();

        //comando -> (comando)
        Rule innerRule = ruleFactory.newSequentialRuleBuilder()
                .withParser(new SeparatorToken.LeftParenthesisToken.Parser())
                .withRule(ruleFactory.newMultiRuleBuilder()
                        .orAccept(basicRule)
                        .orAccept(thenRuleDelegator)
                        .orAccept(afterRuleDelegator)
                        .orAccept(innerRuleDelegator)
                        .build())
                .withParser(new SeparatorToken.RightParenthesisToken.Parser())
                .build();

        Rule custom = ruleFactory.newMultiRuleBuilder()
                .orAccept(basicRule)
                .orAccept(thenRuleDelegator)
                .orAccept(innerRuleDelegator)
                .orAccept(afterRuleDelegator)
                .build();

        //comando -> comando APOS comando
        Rule afterRule = ruleFactory.newSequentialRuleBuilder()
                .withRule(custom)
                .withParser(new ThenOperatorToken.Parser())
                .withRule(custom)
                .build();

        custom = ruleFactory.newMultiRuleBuilder()
                .orAccept(basicRule)
                .orAccept(afterRuleDelegator)
                .orAccept(innerRuleDelegator)
                .orAccept(thenRuleDelegator)
                .build();

        //comando -> comando ENTAO comando
        Rule thenRule = ruleFactory.newSequentialRuleBuilder()
                .withRule(custom)
                .withParser(new AfterOperatorToken.Parser())
                .withRule(custom)
                .build();

        thenRuleDelegator.setDelegate(thenRule);
        afterRuleDelegator.setDelegate(afterRule);
        innerRuleDelegator.setDelegate(innerRule);

        //Unica regra resultante 'S'
        Rule masterRule = ruleFactory.newMultiRuleBuilder()
                .orAccept(thenRule)
                .orAccept(afterRule)
                .orAccept(innerRule)
                .orAccept(basicRule)
                .build();


        for (Path path : listPaths) {
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
                    Logger.warn("Rule result " + basicRule.test(input));
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