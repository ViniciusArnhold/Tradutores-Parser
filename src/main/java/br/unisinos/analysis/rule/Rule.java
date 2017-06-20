package br.unisinos.analysis.rule;

import br.unisinos.MultiLineStringReader;

import java.util.function.Predicate;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public interface Rule extends Predicate<MultiLineStringReader> {
}
