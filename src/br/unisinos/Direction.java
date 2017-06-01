package br.unisinos;

/**
 * Created by vinicius on 25/05/17.
 */
public interface Direction {

    Cardinal cardinalidade();

    long amount();

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
    }

    enum Cardinal {
        NORTH,
        WEST,
        EAST,
        SOUTH
    }

}
