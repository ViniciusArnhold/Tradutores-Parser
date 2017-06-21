package br.unisinos.analysis.rule;

import br.unisinos.analysis.AnalysisReport;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenType;
import br.unisinos.util.Utils;

import java.util.Deque;
import java.util.function.Consumer;

public class TokenTypeRule implements Rule {
    private final TokenType token;
    private final Consumer<AnalysisReport> reporter;

    TokenTypeRule(TokenType token, Consumer<AnalysisReport> reporter) {
        this.reporter = reporter;
        this.token = token;
    }

    @Override
    public boolean test(Deque<Token> tokQueue) {
        Utils.checkNonEmpty(tokQueue);
        Token tok = tokQueue.poll();
        if (tok.getType().equals(token)) {
            return true;
        }
        tokQueue.addFirst(tok);
        return false;
    }
}