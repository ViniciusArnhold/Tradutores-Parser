package br.unisinos.analysis.rule;

import br.unisinos.tokens.Token;

import java.util.Deque;
import java.util.function.Predicate;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public interface Rule extends Predicate<Deque<Token>> {

}
