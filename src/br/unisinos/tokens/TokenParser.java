package br.unisinos.tokens;

import br.unisinos.MultiLineStringReader;

import java.util.Optional;

/**
 * Created by Vinicius.
 *
 * @since ${PROJECT_VERSION}
 */
public interface TokenParser<T extends Token> {

    Optional<T> tryParse(MultiLineStringReader input);

}
