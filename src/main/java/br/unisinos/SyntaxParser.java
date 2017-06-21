package br.unisinos;

import br.unisinos.parse.ParseException;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenType;
import br.unisinos.util.Logger;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class SyntaxParser {

    private static List<Token> tokens;
    private final Deque<Token> reader;
    private Deque<String> nodes = new ArrayDeque<>();

    public SyntaxParser(Deque<Token> reader) {
        this.reader = reader;
    }


    public static void parse(Deque<Token> es) {
        new SyntaxParser(es).doParse();
    }

    private void logTokenParsed(Token tok) {
        Logger.info("On Seq [%s] - Token Parsed: [%s]", parseNodes(), tok);
    }

    private String parseNodes() {
        return nodes.stream().collect(Collectors.joining(" -> "));
    }


    private Deque<Token> dequeOf(Token... items) {
        return new ArrayDeque<>(Arrays.asList(items));
    }

    private Token peek() {
        if (reader.isEmpty()) throw new IllegalStateException("Expected more tokens but instead reached EOF");
        return reader.peek();
    }

    private Token poll() {
        if (reader.isEmpty()) throw new IllegalStateException("Expected more tokens but instead reached EOF");
        return reader.poll();
    }

    private void addParseNode(String node) {
        nodes.add(node);
    }

    private void pollParseNode() {
        nodes.pollLast();
    }

    public boolean doParse() throws ParseException {

        while (!reader.isEmpty()) {
            Token next = peek();

            if (isBasic(next)) {
                parseBasic();
                continue;
            }

            if (next.getType() == TokenType.L_PAREN) {
                parseInner();
                continue;
            }

            commandOp();
        }

        return true;
    }

    private void parseBasic() {
        Token next = poll();
        addParseNode("parseBasic(" + next + ")");

        if (!isBasic(next)) {
            throw new IllegalStateException("Expected a basic token but found: " + next + " <- in -> " + reader);
        }
        logTokenParsed(next);

        pollParseNode();
    }

    private void commandOp() {
        Token next = peek();

        addParseNode("commandOp()");


        if (isBasic(next)) {
            parseBasic();
        } else if (next.getType() == TokenType.L_PAREN) {
            parseInner();
        }

        if (next.getType() == TokenType.THEN_OP) {
            parseToken(TokenType.THEN_OP);
        }

        if (next.getType() == TokenType.AFTER_OP) {
            parseToken(TokenType.AFTER_OP);
        }

        commandOp();

        pollParseNode();
    }

    private boolean isBasic(Token token) {
        switch (token.getType()) {
            case FORWARD:
            case LEFT:
            case RIGHT:
            case BACK:
                return true;
            default:
                return false;
        }
    }


    private void parseInner() {
        addParseNode("parseInner()");

        parseToken(TokenType.L_PAREN);
        commandOp();
        parseToken(TokenType.R_PAREN);

        pollParseNode();
    }

    private void parseToken(TokenType type) {
        addParseNode("parseInner(" + type + ")");

        Token next = poll();
        if (next.getType() != type)
            throw new IllegalStateException(String.format("Expected %s but found %s in [%s]", type, next.getType(), next));
        logTokenParsed(next);

        pollParseNode();
    }
}
