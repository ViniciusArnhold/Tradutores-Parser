package br.unisinos.analysis.syntax;

import br.unisinos.analysis.AnalysisReport;
import br.unisinos.analysis.rule.Rule;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class SyntaxAnalyser {

    private SyntaxAnalyser(Builder builder) {
    }

    public static class Builder {

        private Set<Rule> rules = new HashSet<>();

        Builder reporter(Consumer<AnalysisReport> report) {
            return this;
        }

        Builder addRule(Rule rule) {
            this.rules.add(Objects.requireNonNull(rule));
            return  this;
        }

        SyntaxAnalyser builder() {
            return new SyntaxAnalyser(this);
        }
    }
}
