package br.unisinos;

import br.unisinos.analysis.AnalysisReport;
import br.unisinos.analysis.rule.*;
import br.unisinos.parse.ParseException;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenType;
import br.unisinos.util.Logger;

import java.util.*;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class SyntaxParser {

    private static List<Token> tokens;
    private final RuleFactory ruleFactory;
    private final TokenTypeRule leftRule;
    private final TokenTypeRule forwardRule;
    private final TokenTypeRule rightRule;
    private final SequentialRule afterRule;
    private final TokenTypeRule backRule;
    private final MultiOptionRule basicRules;
    private final MultiOptionRule multiOptionCommandRule;
    private final SequentialRule innerRule;
    private final SequentialRule thenRule;
    private final MultiOptionRule masterRule;
    private final Deque<Token> reader;
    private MultiOptionRule testBasicRules;

    public SyntaxParser(Deque<Token> reader) {
        this.reader = reader;

        ruleFactory = new RuleFactory()
                .addReporter(Logger::logAnalysis)
                .addReporter(SyntaxParser::collectToken);

        //basico -> FRENTE n
        forwardRule = ruleFactory.newTokenTypeRule(TokenType.FORWARD);

        //basico -> ESQUERDA n
        leftRule = ruleFactory.newTokenTypeRule(TokenType.LEFT);

        //basico -> DIREITA n
        rightRule = ruleFactory.newTokenTypeRule(TokenType.RIGHT);

        //basico -> TRAS n
        backRule = ruleFactory.newTokenTypeRule(TokenType.BACK);

        //comando -> basico
        basicRules = ruleFactory.newMultiRuleBuilder()
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
        multiOptionCommandRule = ruleFactory.newMultiRuleBuilder()
                .orAccept(basicRules)
                .orAccept(thenRuleDelegator)
                .orAccept(afterRuleDelegator)
                .orAccept(innerRuleDelegator)
                .build();

        //comando -> (comando)
        innerRule = ruleFactory.newSequentialRuleBuilder()
                .withToken(TokenType.L_PAREN)
                .withRule(ruleFactory.newMultiRuleBuilder()
                        .orAccept(basicRules)
                        .orAccept(thenRuleDelegator)
                        .orAccept(afterRuleDelegator)
                        .orAccept(innerRuleDelegator)
                        .build())
                .withToken(TokenType.R_PAREN)
                .build();

        MultiOptionRule custom = ruleFactory.newMultiRuleBuilder()
                .orAccept(basicRules)
                .orAccept(thenRuleDelegator)
                .orAccept(innerRuleDelegator)
                .orAccept(afterRuleDelegator)
                .build();

        //comando -> comando APOS comando
        afterRule = ruleFactory.newSequentialRuleBuilder()
                .withRule(custom)
                .withToken(TokenType.AFTER_OP)
                .withRule(custom)
                .build();

        custom = ruleFactory.newMultiRuleBuilder()
                .orAccept(basicRules)
                .orAccept(afterRuleDelegator)
                .orAccept(innerRuleDelegator)
                .orAccept(thenRuleDelegator)
                .build();

        //comando -> comando ENTAO comando
        thenRule = ruleFactory.newSequentialRuleBuilder()
                .withRule(custom)
                .withToken(TokenType.THEN_OP)
                .withRule(custom)
                .build();

        thenRuleDelegator.setDelegate(thenRule);
        afterRuleDelegator.setDelegate(afterRule);
        innerRuleDelegator.setDelegate(innerRule);

        //Unica regra resultante 'S'
        masterRule = ruleFactory.newMultiRuleBuilder()
                .orAccept(thenRule)
                .orAccept(afterRule)
                .orAccept(innerRule)
                .orAccept(basicRules)
                .build();
    }

    private static void collectToken(AnalysisReport analysisReport) {
        if (analysisReport.type() == AnalysisReport.Type.TOKEN) {
            tokens.add(analysisReport.token());
        }
    }

    public static void parse(Deque<Token> es) {
        new SyntaxParser(es).doParse();
    }

    private static void logTokenParsed(Token tok) {
        Logger.info("Parsed [%s]", tok);
    }

    private static void logTokenParsed(TokenType tok) {
        Logger.info("Parsed [%s]", tok);
    }

    public boolean doParse() throws ParseException {
        testBasicRules = new RuleFactory()
                .newMultiRuleBuilder()
                .orAccept(forwardRule)
                .orAccept(leftRule)
                .orAccept(rightRule)
                .orAccept(backRule)
                .build();

        while (!reader.isEmpty()) {
            commandOp();
        }

        return true;
    }

    private Deque<Token> dequeOf(Token... items) {
        return new ArrayDeque<>(Arrays.asList(items));
    }

    private void parseBasic() {
        Token next = peek();
        if (!basicRules.test(reader)) {
            throw new IllegalStateException("Expected a basic token but found: " + (reader.isEmpty() ? "EOF" : reader.poll()) + " <- in -> " + reader);
        }
        logTokenParsed(next);
    }

    private void commandOp() {
        Token next = peek();

        if (isBasic(next)) {
            parseBasic();
            return;
        }

        if (next.getType() == TokenType.L_PAREN) {
            parseInner();
            return;
        }

        if (next.getType() == TokenType.THEN_OP) {
            parseToken(TokenType.THEN_OP);
        }

        if (next.getType() == TokenType.AFTER_OP) {
            parseToken(TokenType.AFTER_OP);
        }

        commandOp();
    }

    private boolean isBasic(Token token) {
        return testBasicRules.test(new ArrayDeque<>(Collections.singleton(token)));
    }

    private Token peek() {
        if (reader.isEmpty()) throw new IllegalStateException("Expected more tokens but instead reached EOF");
        return reader.peek();
    }

    private Token poll() {
        if (reader.isEmpty()) throw new IllegalStateException("Expected more tokens but instead reached EOF");
        return reader.poll();
    }

    private void parseInner() {
        parseToken(TokenType.L_PAREN);
        commandOp();
        parseToken(TokenType.R_PAREN);


    }

    private void parseToken(TokenType type) {
        Token next = poll();
        if (next.getType() != type)
            throw new IllegalStateException(String.format("Expected %s but found %s in [%s]", type, next.getType(), next));
        logTokenParsed(next);
    }
}
