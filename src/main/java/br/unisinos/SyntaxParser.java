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

    private void clearParseNode()
    {
        nodes.clear();
    }

    public boolean doParse() throws ParseException {

        boolean parsed = true;
        while (!reader.isEmpty() && parsed) {
            parsed = C();
            if (parsed)
                clearParseNode();
        }

        return parsed;
    }

    private void parseBasic() {
        Token next = poll();
        addParseNode("parseBasic(" + next + ")");

        if (!isBasic(next)) {
            throw new IllegalStateException("Expected a basic token but found: " + next + " <- in -> " + reader);
        }
        logTokenParsed(next);

        //pollParseNode();
    }

    private boolean C()
    {

        if (B())
        {
            // C -> B entao [B|(C)]
            if (thenOrAfterBOrInnerC(TokenType.THEN_OP))
                return true;
            // C -> B apos [B|(C)]
            else if (thenOrAfterBOrInnerC(TokenType.AFTER_OP))
                return true;
            // C -> B
            return true;
        }
        else if (InnerC())
        {
            // C -> (C) entao [B|(C)]
            if (thenOrAfterBOrInnerC(TokenType.THEN_OP))
                return true;
            // C -> (C) apos [B|(C)]
            else if (thenOrAfterBOrInnerC(TokenType.AFTER_OP))
                return true;
            // C -> (C)
            return true;
        }

        return false;
    }

    private boolean B()
    {
        if (reader.isEmpty())
            return false;

        Token next = peek();

        if (isBasic(next))
        {
            parseBasic();
            return true;
        }
        return false;
    }

    private boolean thenOrAfter(TokenType tokenType)
    {
        if (reader.isEmpty())
            return false;

        if (peek().getType() == tokenType)
        {
            //[then/after]
            parseToken(tokenType);
            return true;
        }
        return false;
    }

    private boolean BOrInnerC()
    {
        if (B() || InnerC())
            return true;
        else
            return false;
    }

    private boolean thenOrAfterBOrInnerC(TokenType tokenType)
    {
        if (reader.isEmpty())
            return false;

        Token next = peek();
        if (peek().getType() == tokenType)
        {
            // [then|after] [B|(C)]
            if (thenOrAfter(tokenType) && BOrInnerC())
                return true;
            else
                throw new IllegalStateException(String.format("Expected some expression, but [%s] was found", next));
        }
        return false;
    }

    private boolean InnerC() {
        if (reader.isEmpty())
            return false;

        if (peek().getType() == TokenType.L_PAREN)
        {
            parseToken(TokenType.L_PAREN);
            boolean v = C();
            parseToken(TokenType.R_PAREN);
            return v;
        }
        return false;
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

    private void parseToken(TokenType type) {
        addParseNode("parseInner(" + type + ")");

        Token next = poll();
        if (next.getType() != type)
            throw new IllegalStateException(String.format("Expected %s but found %s in [%s]", type, next.getType(), next));
        logTokenParsed(next);

        //pollParseNode();
    }
}
