package br.unisinos.direction;

import br.unisinos.tokens.TokenType;

/**
 * Created by Vinicius, Fabio e Eduardo.
 */
public interface Direction {

    static Direction right(long amount) {
        return new Simple(Cardinal.RIGHT, amount);
    }

    static Direction left(long amount) {
        return new Simple(Cardinal.LEFT, amount);
    }

    static Direction forward(long amount) {
        return new Simple(Cardinal.FORWARD, amount);
    }

    static Direction back(long amount) {
        return new Simple(Cardinal.BACK, amount);
    }

    static Direction ofTokenType(TokenType type, long amount) {
        switch (type) {
            case FORWARD:
                return forward(amount);
            case LEFT:
                return left(amount);
            case RIGHT:
                return right(amount);
            case BACK:
                return back(amount);
            default:
                throw new IllegalArgumentException("type");
        }
    }

    Cardinal cardinality();

    long amount();

    enum Cardinal {
        FORWARD,
        LEFT,
        RIGHT,
        BACK
    }

    class Simple implements Direction {

        private final Cardinal cardinal;
        private final long amount;

        Simple(Cardinal cardinal, long amount) {
            this.cardinal = cardinal;
            this.amount = amount;
        }

        @Override
        public Cardinal cardinality() {
            return cardinal;
        }

        @Override
        public long amount() {
            return amount;
        }

        @Override
        public String toString() {
            return String.format("%d", amount);
        }
    }

}
