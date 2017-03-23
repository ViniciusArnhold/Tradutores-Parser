package br.unisinos.tokens;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;

/**
 * Created by first on 23/03/2017.
 */
public class TokenEntry<K extends IToken<TokenType>,V extends ITokenValue> extends SimpleEntry<K,V> {

    public TokenEntry(K key, V value) {
        super(key, value);
    }

    public TokenEntry(Map.Entry<? extends K, ? extends V> entry) {
        super(entry);
    }
}
