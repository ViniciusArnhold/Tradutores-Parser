package br.unisinos.analysis.rule;

import br.unisinos.MultiLineStringReader;
import br.unisinos.analysis.AnalysisConsumer;

import java.util.Objects;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class DelegatingRule implements Rule {

    private final AnalysisConsumer analysisConsumer;
    private Rule ruleDelegate = null;

    DelegatingRule(AnalysisConsumer consumer) {
        analysisConsumer = consumer;
    }

    public DelegatingRule setDelegate(Rule rule) {
        ruleDelegate = Objects.requireNonNull(rule);
        return this;
    }

    @Override
    public boolean test(MultiLineStringReader reader) {
        return ruleDelegate.test(reader);
    }
}