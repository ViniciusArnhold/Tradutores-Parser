package br.unisinos.translator;

import br.unisinos.Direction;
import br.unisinos.tokens.impl.DirectionToken;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class ValueExpression implements Expression {

    private final DirectionToken token;

    public ValueExpression(DirectionToken token) {
        this.token = token;
    }

    @Override
    public Direction eval() {
        return (Direction) token.getValue();
    }
}
