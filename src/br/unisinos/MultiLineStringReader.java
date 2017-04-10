package br.unisinos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by Vinicius.
 *
 * @since ${PROJECT_VERSION}
 */
public class MultiLineStringReader {

    private final int primeiraLinha = 0;
    private final int ultimaLinha;

    private final int primeiraColuna = 0;
    private final List<String> linhas;
    private int ultimaColuna;
    private int curLinha;
    private int curColuna = -1;

    private MultiLineStringReader(List<String> linhas) {
        this.linhas = Collections.unmodifiableList(linhas);
        this.ultimaLinha = linhas.size() - 1;
        this.curLinha = 0;
        this.curColuna = 0;
        this.ultimaColuna = linhas.isEmpty() ? 0 : (linhas.get(0).length() - 1);
    }

    public static MultiLineStringReader of(Path path) throws IOException {
        return new MultiLineStringReader(Files.readAllLines(path));
    }

    public static MultiLineStringReader of(String str) throws IOException {
        return new MultiLineStringReader(Arrays.asList(str.split(System.lineSeparator())));
    }

    public static MultiLineStringReader of(List<String> lists) throws IOException {
        return new MultiLineStringReader(lists);
    }

    public boolean hasMoreCharsOnSameLine() {
        return !isLineDone();
    }

    public boolean hasMoreLines() {
        return curLinha != ultimaLinha;
    }

    public boolean hasMoreChars() {
        return this.curColuna < ultimaColuna || hasMoreLines();
    }

    public int remainingChars() {
        int sum = ultimaColuna - curColuna;
        for (int i = curLinha; i < linhas.size(); i++) {
            sum += linhas.get(i).length();
        }
        return sum;
    }

    public boolean hasMoreChars(int count) {
        return remainingChars() >= count;
    }

    private boolean isLineDone() {
        return this.curColuna == ultimaColuna;
    }

    public char nextChar() {
        if (!hasMoreChars()) {
            throw new IllegalStateException("EOS");
        }
        if (isLineDone()) {
            this.curColuna = primeiraColuna;
            nextLine();
        }
        return linhas.get(curLinha).charAt(curColuna++);
    }

    public String currentLine() {
        return linhas.get(curLinha);
    }

    public char currentChar() {
        return linhas.get(curLinha).charAt(curColuna);
    }

    public int curPos() {
        return curColuna;
    }

    public int linePos() {
        return curLinha;
    }

    public String nextString() {
        return nextString(1);
    }

    public String nextString(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("size is less than 1 " + size);
        }
        char[] str = new char[size];
        int count = 0;
        while (count < size) {
            str[count++] = nextChar();
        }
        return new String(str).intern();
    }

    public String skipWhitespace() {
        if (!Character.isWhitespace(peek())) return "";
        StringBuilder sb = new StringBuilder();
        while (hasMoreChars()) {
            if (!Character.isWhitespace(peek())) {
                break;
            }
            sb.append(nextChar());
        }
        return sb.toString();
    }

    public String peekString() {
        return String.valueOf(peek());
    }

    public char peek() {
        if (!hasMoreChars()) {
            throw new IllegalStateException("EOS");
        }
        char ret;
        Point mark = this.mark();
        ret = nextChar();
        this.moveTo(mark);
        return ret;
    }

    public void nextLine() {
        if (!hasMoreLines()) {
            throw new IllegalStateException("Reader doesn't have more lines");
        }
        curLinha++;
        curColuna = primeiraColuna;
        ultimaColuna = this.linhas.get(curLinha).length() - 1;
    }

    public String readToEndOfLine() {
        StringBuilder sb = new StringBuilder(ultimaColuna - curColuna);
        int thisLinha = curLinha;
        //noinspection ConstantConditions
        while (curLinha != thisLinha) {
            sb.append(nextChar());
        }
        return sb.toString();
    }

    public Point mark() {
        return new Point(this.curColuna, this.curLinha);
    }

    public void moveTo(Point point) {
        Objects.requireNonNull(point);
        if (point.linha < 0
                || point.coluna < 0
                || point.linha > this.ultimaLinha
                || point.coluna > this.linhas.get(point.linha).length() - 1)
            throw new IllegalArgumentException(point.toString());

        this.curColuna = point.coluna;
        this.curLinha = point.linha;
        this.ultimaColuna = currentLine().length() - 1;
    }

    @Override
    public String toString() {
        String sb = "MultiLineStringReader{" + "primeiraLinha=" + primeiraLinha +
                ", ultimaLinha=" + ultimaLinha +
                ", primeiraColuna=" + primeiraColuna +
                ", linhas=" + linhas +
                ", ultimaColuna=" + ultimaColuna +
                ", curLinha=" + curLinha +
                ", curColuna=" + curColuna +
                '}';
        return sb;
    }

    public class Point {
        private final int linha;
        private final int coluna;

        private Point(int coluna, int linha) {
            this.coluna = coluna;
            this.linha = linha;
        }

        @Override
        public String toString() {
            return "[" +
                    linha +
                    " , " +
                    coluna +
                    ']';
        }
    }
}
