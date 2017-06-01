package br.unisinos.tokens.impl;

import br.unisinos.tokens.Token;
import br.unisinos.tokens.TokenType;

/**
 * Created by vinicius on 01/06/17.
 */
public abstract class DirectionToken extends Token {

    protected DirectionToken(TokenType type, Object value) {
        super(type, value);
    }
}
