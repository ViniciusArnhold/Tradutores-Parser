package br.unisinos.direction;

import br.unisinos.tokens.TokenType;

/**
 * Created by vinicius on 25/05/17.
 */
public interface Direction {

    static Direction ofEast(long amount) {
        return new Simple(Cardinal.EAST, amount);
    }

    static Direction ofWest(long amount) {
        return new Simple(Cardinal.WEST, amount);
    }

    static Direction ofNorth(long amount) {
        return new Simple(Cardinal.NORTH, amount);
    }

    static Direction ofSouth(long amount) {
        return new Simple(Cardinal.SOUTH, amount);
    }

    static Direction ofTokenType(TokenType type, long amount) {
        switch (type) {
            case FORWARD:
                return ofNorth(amount);
            case LEFT:
                return ofWest(amount);
            case RIGHT:
                return ofEast(amount);
            case BACK:
                return ofSouth(amount);
            default:
                throw new IllegalArgumentException("type");
        }
    }

    Cardinal cardinalidade();

    long amount();

    enum Cardinal {
        NORTH,
        WEST,
        EAST,
        SOUTH
    }

    class Simple implements Direction {

        private final Cardinal cardinal;
        private final long amount;

        Simple(Cardinal cardinal, long amount) {
            this.cardinal = cardinal;
            this.amount = amount;
        }

        @Override
        public Cardinal cardinalidade() {
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