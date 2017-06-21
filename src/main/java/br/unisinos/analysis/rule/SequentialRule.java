package br.unisinos.analysis.rule;

import br.unisinos.analysis.AnalysisReport;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenType;

import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class SequentialRule implements Rule {

    private final Set<Rule> rules;
    private final Consumer<AnalysisReport> reporter;
    private String state = "";

    private SequentialRule(Builder builder) {
        this.reporter = Objects.requireNonNull(builder.reporter);
        this.rules = Objects.requireNonNull(builder.rules);
    }

    @Override
    public boolean test(Deque<Token> reader) {
        for (Rule rule : rules) {
            AnalysisReport report = AnalysisReport.fromMessage("Begin parsing with: " + rule, state);
            state = report.currentState();
            reporter.accept(report);
        }
        return rules.stream().allMatch(r -> r.test(reader));
    }

    public static class Builder {

        Set<Rule> rules = new LinkedHashSet<>();
        Consumer<AnalysisReport> reporter;

        Builder(Consumer<AnalysisReport> reporter) {
            this.reporter = Objects.requireNonNull(reporter);
        }

        public Builder withToken(TokenType tokType) {
            return withRule(new TokenTypeRule(tokType, reporter));
        }

        public Builder withRule(Rule rule) {
            this.rules.add(rule);
            return this;
        }

        public SequentialRule build() {
            return new SequentialRule(this);
        }

    }
}
