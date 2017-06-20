package br.unisinos.analysis.rule;

import br.unisinos.MultiLineStringReader;
import br.unisinos.analysis.AnalysisReport;
import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenParser;

import java.util.Optional;
import java.util.function.Consumer;

public class ParserRule implements Rule {
    private final TokenParser<? extends Token> parser;
    private final Consumer<AnalysisReport> reporter;

    public ParserRule(TokenParser<? extends Token> parser, Consumer<AnalysisReport> reporter) {
        this.reporter = reporter;
        this.parser = parser;
    }

    @Override
    public boolean test(MultiLineStringReader reader) {
        Optional<? extends Token> tok = parser.tryParse(reader);
        tok.ifPresent(t -> reporter.accept(AnalysisReport.fromToken(t, "")));
        return tok.isPresent();
    }
}