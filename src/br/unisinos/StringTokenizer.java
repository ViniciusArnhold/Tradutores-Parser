package br.unisinos;

import br.unisinos.tokens.TokenEntry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by first on 23/03/2017.
 */
public final class StringTokenizer {

    private final List<String> linhas;

    private static final Pattern WHITESPACE_SPLIT = Pattern.compile("([^\"]\\S*|\".+?\")\\s*");

    private StringTokenizer(Path file) {
        Objects.requireNonNull(file);

        try {
        this.linhas = Files.readAllLines(file, StandardCharsets.UTF_8);
        if(this.linhas.size() == 0) {
            throw new IllegalArgumentException("File is empty");
        }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static List<TokenEntry> parseToList(Path file) {
        return new StringTokenizer(file).doParse();
    }

    private List<TokenEntry> doParse() {

        boolean isDone = false;
        ListIterator<String> iterator = linhas.listIterator();
        String curText = iterator.next().trim();

        while(!isDone) {
            List<String> tokens = splitByWhitespace(curText);




        }

        return null;
    }

    public static List<String> splitByWhitespace(String args) {
        Matcher matcher = WHITESPACE_SPLIT.matcher(Objects.requireNonNull(args));

        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group(1).replace("\"", ""));
        }
        return list;
    }
}
