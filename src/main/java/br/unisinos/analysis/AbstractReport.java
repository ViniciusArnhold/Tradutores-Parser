package br.unisinos.analysis;

import br.unisinos.tokens.Token;

import java.util.NoSuchElementException;

abstract class AbstractReport implements AnalysisReport {

    @Override
    public String info() {
        throw new NoSuchElementException();
    }

    @Override
    public Throwable exception() {
        throw new NoSuchElementException();
    }

    @Override
    public Token token() {
        throw new NoSuchElementException();
    }
}