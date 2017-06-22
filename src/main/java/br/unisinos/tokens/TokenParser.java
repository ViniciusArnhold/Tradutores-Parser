package br.unisinos.tokens;

import br.unisinos.MultiLineStringReader;

import java.util.Optional;

/**
 * Created by Vinicius, Fabio e Eduardo.
 *
 * @since 1.0
 */
public interface TokenParser<T extends Token> {

    // Because the method tryParse can return no token the Optional concept was used.
    Optional<T> tryParse(MultiLineStringReader input);

}
