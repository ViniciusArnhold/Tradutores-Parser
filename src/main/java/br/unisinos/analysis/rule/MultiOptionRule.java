package br.unisinos.analysis.rule;

import br.unisinos.MultiLineStringReader;
import br.unisinos.analysis.AnalysisReport;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class MultiOptionRule implements Rule {

    private final Set<Rule> rules;
    private final Consumer<AnalysisReport> reporter;
    private String state = "";

    private MultiOptionRule(Builder builder) {
        this.rules = builder.rules;
        this.reporter = builder.reporter;
    }

    /**
     * Evaluates this predicate on the given argument.
     *
     * @param reader the input argument
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     */
    @Override
    public boolean test(MultiLineStringReader reader) {
        for (Rule rule : rules) {
            AnalysisReport report = AnalysisReport.fromMessage("Begin parsing with: " + rule, state);
            state = report.currentState();
            reporter.accept(report);
        }
        return rules.stream().anyMatch(r -> r.test(reader));
    }

    public static class Builder {

        private Set<Rule> rules = new LinkedHashSet<>();
        private Consumer<AnalysisReport> reporter;

        public Builder(Consumer<AnalysisReport> reporter) {
            this.reporter = Objects.requireNonNull(reporter);
        }

        public Builder orAccept(Rule otherRule) {
            rules.add(Objects.requireNonNull(otherRule));
            return this;
        }


        public MultiOptionRule build() {
            return new MultiOptionRule(this);
        }
    }
}
