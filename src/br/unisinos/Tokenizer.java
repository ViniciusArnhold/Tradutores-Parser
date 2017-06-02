package br.unisinos;

import java.util.regex.Pattern;

/**
 * Created by Vinicius, Fabio e Eduardo.
 *
 * @since ${PROJECT_VERSION}
 */
final class Tokenizer {
    private static final Pattern WHITESPACE_SPLIT = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");
}
