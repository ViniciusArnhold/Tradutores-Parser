package br.unisinos.analysis.rule;

import br.unisinos.analysis.AnalysisConsumer;
import br.unisinos.analysis.AnalysisReport;
import br.unisinos.tokens.TokenType;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class RuleFactory {

    private DelegatingAnalysisConsumer delegatingConsumer = new DelegatingAnalysisConsumer();

    public RuleFactory() {

    }

    public RuleFactory addReporter(AnalysisConsumer consumer) {
        this.delegatingConsumer.delegates.add(consumer);
        return this;
    }

    public TokenTypeRule newTokenTypeRule(TokenType parser) {
        return new TokenTypeRule(Objects.requireNonNull(parser), delegatingConsumer);
    }

    public MultiOptionRule.Builder newMultiRuleBuilder() {
        return new MultiOptionRule.Builder(delegatingConsumer);
    }

    public DelegatingRule newDelegatingRule() {
        return new DelegatingRule(delegatingConsumer);
    }

    public SequentialRule.Builder newSequentialRuleBuilder() {
        return new SequentialRule.Builder(delegatingConsumer);
    }

    private class DelegatingAnalysisConsumer implements AnalysisConsumer {
        private Set<AnalysisConsumer> delegates = new LinkedHashSet<>();

        @Override
        public void accept(AnalysisReport analysisReport) {
            for (AnalysisConsumer consumer : delegates) {
                consumer.accept(analysisReport);
            }
        }
    }
}
