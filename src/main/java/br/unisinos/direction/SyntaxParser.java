package br.unisinos.direction;

import br.unisinos.parse.ParseException;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenType;
import br.unisinos.translator.Evaluator;
import br.unisinos.util.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class SyntaxParser {

    private final Deque<Token> reader;
    private Deque<String> nodes = new ArrayDeque<>();
    private final List<Token> cmdsParsed = new ArrayList<>();

    public SyntaxParser(Deque<Token> reader) {
        this.reader = reader;
    }


    public static boolean parse(Deque<Token> es) {
        return new SyntaxParser(es).doParse();
    }

    private void logTokenParsed(Token tok) {
        Logger.info("On Seq [%s] - Parsed: [%s]", parseNodes(), tok);
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

    private void clearParseNode() {
        nodes.clear();
    }

    private boolean doParse() throws ParseException {

        boolean parsed = true;
        while (!reader.isEmpty() && parsed) {
            List<Token> cmds = new ArrayList<>();
            addParseNode("C");
            parsed = C(cmds);
            clearParseNode();
            if (parsed) {
                cmdsParsed.addAll(cmdsParsed.size(), cmds);
                clearParseNode();
            }
        }

        Logger.lineBreak();
        Logger.warn("Arquivo possui sintaxe correta, iniciando eval");
        Logger.lineBreak();

        Evaluator evaluator = Evaluator.create();
        evaluator.eval(cmdsParsed);

        return parsed;
    }

    private void parseBasic(List<Token> cmds) {
        Token next = poll();

        addParseNode("basic(" + next.toString() + ")");
        logTokenParsed(next);
        if (!isBasic(next)) {
            throw new IllegalStateException("Expected a basic token but found: " + next + " <- in -> " + reader);
        }
        cmds.add(next);
    }

    private boolean C(List<Token> cmds) {
        if (B(cmds)) {
            // C -> B entao [B|(C)]
            if (thenOrAfterBOrInnerC(TokenType.THEN_OP, cmds))
                return true;
                // C -> B apos [B|(C)]
            else if (thenOrAfterBOrInnerC(TokenType.AFTER_OP, cmds))
                return true;
            // C -> B
            return true;
        } else if (InnerC(cmds)) {
            // C -> (C) entao [B|(C)]
            if (thenOrAfterBOrInnerC(TokenType.THEN_OP, cmds))
                return true;
                // C -> (C) apos [B|(C)]
            else if (thenOrAfterBOrInnerC(TokenType.AFTER_OP, cmds))
                return true;
            // C -> (C)
            return true;
        }

        return false;
    }

    private boolean B(List<Token> cmds) {
        if (reader.isEmpty())
            return false;

        Token next = peek();

        if (isBasic(next)) {
            parseBasic(cmds);
            return true;
        }
        return false;
    }

    private boolean thenOrAfter(TokenType tokenType, List<Token> cmds) {
        if (reader.isEmpty())
            return false;

        if (peek().getType() == tokenType) {
            addParseNode("B " + tokenType + " ( B | (C) )");
            //[then/after]
            parseToken(tokenType);
            return true;
        }
        return false;
    }

    private boolean BOrInnerC(List<Token> cmds) {
        return B(cmds) || InnerC(cmds);
    }

    private boolean thenOrAfterBOrInnerC(TokenType tokenType, List<Token> cmds) {
        if (reader.isEmpty())
            return false;

        Token next = peek();
        if (peek().getType() == tokenType) {
            // [then|after] [B|(C)]
            if (thenOrAfter(tokenType, cmds)) {
                int curIndex = -1;

                // The comands must be inverted
                if (tokenType == TokenType.AFTER_OP)
                    curIndex = cmds.size();
                if (BOrInnerC(cmds)) {
                    if (curIndex != -1) // Apply a mirror operation on the list
                        mirrorList(cmds, curIndex);

                    return true;
                }
            }
            // after read an after or then operator, B or innerC must be matched
            throw new IllegalStateException(String.format("Expected some expression, but [%s] was found", next));
        }
        return false;
    }

    private boolean InnerC(List<Token> cmds) {
        if (reader.isEmpty())
            return false;

        if (peek().getType() == TokenType.L_PAREN) {
            parseToken(TokenType.L_PAREN);
            addParseNode("( C )");
            // Create a new list
            List<Token> newCmds = new ArrayList<>();
            boolean v = C(newCmds);
            if (v)
                cmds.addAll(cmds.size(), newCmds);
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

        Token next = poll();
        if (next.getType() != type)
            throw new IllegalStateException(String.format("Expected %s but found %s in [%s]", type, next.getType(), next));
    }

    private void mirrorList(List<Token> list, int p) {
        Objects.requireNonNull(list);

        if (!((p < list.size()) && (p > 0)))
            throw new IllegalStateException("Error on mirroring a list.");

        List<Token> l2 = new ArrayList<>(list.subList(p, list.size()));
        list.removeAll(l2);
        list.addAll(0, l2);
    }
}
