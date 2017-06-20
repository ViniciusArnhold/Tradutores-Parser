package br.unisinos.analysis;

import br.unisinos.MultiLineStringReader;
import br.unisinos.analysis.rule.Rule;

import java.util.Objects;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class DelegatingRule implements Rule {

    private Rule ruleDelegate = null;

    public DelegatingRule setDelegate(Rule rule) {
        ruleDelegate = Objects.requireNonNull(rule);
        return this;
    }

    @Override
    public boolean test(MultiLineStringReader reader) {
        return ruleDelegate.test(reader);
    }
}
