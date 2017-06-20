package br.unisinos.analysis;

import br.unisinos.tokens.Token;
import br.unisinos.util.Utils;

import java.util.Objects;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public interface AnalysisReport {

    static AnalysisReport fromException(Throwable throwable, String previousState) {
        Objects.requireNonNull(throwable);
        Objects.requireNonNull(previousState);
        return new AbstractReport() {
            @Override
            public String previousState() {
                return previousState;
            }

            @Override
            public String currentState() {
                return String.format("Suffered Exception [%s]", Utils.formatException(throwable));
            }

            @Override
            public Type type() {
                return Type.EXCEPTION;
            }
        };
    }

    static AnalysisReport fromMessage(String message, String previousState) {
        Objects.requireNonNull(message);
        Objects.requireNonNull(previousState);
        return new AbstractReport() {
            @Override
            public String previousState() {
                return previousState;
            }

            @Override
            public String currentState() {
                return message;
            }

            @Override
            public Type type() {
                return Type.INFO;
            }
        };
    }

    static AnalysisReport fromToken(Token token, String previousState) {
        Objects.requireNonNull(token);
        Objects.requireNonNull(previousState);
        return new AbstractReport() {
            @Override
            public String previousState() {
                return previousState;
            }

            @Override
            public String currentState() {
                return String.format("Parser token: [%s]", token);
            }

            @Override
            public Token token() {
                return token;
            }

            @Override
            public Type type() {
                return Type.TOKEN;
            }
        };
    }

    String toString();

    String previousState();

    String currentState();

    String info();

    Throwable exception();

    Token token();

    Type type();

    enum Type {
        TOKEN,
        INFO,
        EXCEPTION,
    }
}
