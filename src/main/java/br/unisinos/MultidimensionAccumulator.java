package br.unisinos;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by vinicius on 25/05/17.
 */
public class MultidimensionAccumulator {

    private final Queue<Direction> moves = new LinkedList<>();

    public MultidimensionAccumulator accumulateLeft(long amount) {
        moves.add(Direction.ofWest(amount));
        return this;
    }

    public MultidimensionAccumulator accumulateRight(long amount) {
        moves.add(Direction.ofEast(amount));
        return this;
    }

    public MultidimensionAccumulator accumulateUp(long amount) {
        moves.add(Direction.ofNorth(amount));
        return this;
    }


    public MultidimensionAccumulator accumulateDown(long amount) {
        moves.add(Direction.ofSouth(amount));
        return this;
    }

    public void doWalk() {
        Direction direction;
        while ((direction = moves.poll()) != null) {
            System.out.println("Moved to [ " + direction.cardinalidade() + "" + direction.amount() + " ] ");
        }
    }

}
