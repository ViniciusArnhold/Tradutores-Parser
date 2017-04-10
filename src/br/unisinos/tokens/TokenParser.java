package br.unisinos.tokens;

import br.unisinos.MultiLineStringReader;

/**
 * Created by Vinicius.
 *
 * @since ${PROJECT_VERSION}
 */
public interface TokenParser<T extends Token> {

    T parse(MultiLineStringReader input);

}
