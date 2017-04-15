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
@SuppressWarnings("unused")
public class MultiLineStringReader {

    private final List<String> linhas;
    private final int ultimaLinha;
    private int curColuna;
    private int ultimaColuna;
    private int curLinha;

    private MultiLineStringReader(List<String> linhas) {
        this.linhas = Collections.unmodifiableList(linhas);
        this.ultimaLinha = linhas.size() - 1;
        this.curLinha = -1;
        this.curColuna = -1;
        this.ultimaColuna = linhas.isEmpty() ? 0 : (linhas.get(0).length() - 1);
    }

    public static MultiLineStringReader of(Path path) throws IOException {
        return new MultiLineStringReader(Files.readAllLines(path));
    }

    public static MultiLineStringReader of(String str) {
        return new MultiLineStringReader(Arrays.asList(str.split(System.lineSeparator())));
    }

    static MultiLineStringReader of(List<String> lists) {
        return new MultiLineStringReader(lists);
    }

    //Boolean operations
    public boolean hasMoreCharsOnSameLine() {
        return !isLineDone();
    }

    boolean hasMoreLines() {
        return curLinha < ultimaLinha;
    }

    public boolean hasMoreChars() {
        // by EPJ: This modification prevents invalid states when a file is empty.
        return (this.curColuna < ultimaColuna || hasMoreLines()) && (this.linhas.size() > 0);
    }

    public boolean hasMoreChars(@SuppressWarnings("SameParameterValue") int count) {
        return remainingChars() >= count;
    }

    private boolean isLineDone() {
        return this.curColuna >= ultimaColuna;
    }

    //Count operations
    private int remainingChars() {
        Point p = mark();
        int count = 0;
        while (this.hasMoreChars()) {
            moveToNext();
            count++;
        }
        moveTo(p);
        return count;
    }

    //GET operations
    public char nextChar() throws IllegalStateException {
        if (!hasMoreChars()) throw new IllegalStateException("EOS");

        moveToNext();
        return linhas.get(curLinha).charAt(curColuna);
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

    public String nextString(int size) throws IllegalArgumentException {
        if (size < 0) throw new IllegalArgumentException("size is less than 0, " + size);

        char[] str = new char[size];
        int count = 0;
        while (count < size) {
            str[count++] = nextChar();
        }
        return new String(str).intern();
    }

    public String readToEndOfLine() {
        String ret = this.linhas.get(curLinha).substring(curColuna);
        nextLine();
        return ret;
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

    //Helper operations
    @SuppressWarnings("UnusedReturnValue")
    public String skipWhitespace() {
        if (!hasMoreChars()) return "";
        if (!Character.isWhitespace(peek())) return "";
        StringBuilder sb = new StringBuilder();
        while (hasMoreChars() && Character.isWhitespace(peek())) {
            sb.append(nextChar());
        }
        return sb.toString();
    }

    void nextLine() {
        if (!hasMoreLines()) {
            throw new IllegalStateException("Reader doesn't have more lines");
        }
        curLinha++;
        curColuna = -1;
        ultimaColuna = this.linhas.get(curLinha).length() - 1;
    }

    public Point mark() {
        return new Point(this.curColuna, this.curLinha);
    }

    public void moveTo(Point point) throws IllegalArgumentException {
        Objects.requireNonNull(point);
        if (point.linha < -1 || point.coluna < -1 || point.linha > this.ultimaLinha || (point.coluna > (linhas.get(point.linha == -1 ? 0 : point.linha).length() - 1)))
            throw new IllegalArgumentException("Point out of possible ranges: " + point);

        this.curColuna = point.coluna;
        this.curLinha = point.linha;
        this.ultimaColuna = linhas.get(point.linha == -1 ? 0 : point.linha).length() - 1;
    }

    private void moveToNext() {
        if (linhas.isEmpty()) {
            throw new IllegalStateException("EOS");
        }
        if (this.curLinha == -1) {
            curLinha++;
        }
        if (this.curColuna >= ultimaColuna) {
            if (this.curLinha >= ultimaLinha) {
                throw new IllegalStateException("EOS");
            }
            curLinha++;
            curColuna = 0;
            ultimaColuna = this.linhas.get(curLinha).length() - 1;
        } else {
            this.curColuna++;
        }

    }

    @Override
    public String toString() {
        return "MultiLineStringReader{" + "linhas=" + linhas +
                ", ultimaLinha=" + ultimaLinha +
                ", curColuna=" + curColuna +
                ", ultimaColuna=" + ultimaColuna +
                ", curLinha=" + curLinha +
                '}';
    }

    public static class Point {
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
