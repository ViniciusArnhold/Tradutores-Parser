package br.unisinos.direction;

import br.unisinos.util.Logger;
import javafx.geometry.Point2D;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.LongStream;

/**
 * Created by Vinicius, Fabio e Eduardo.
 */
public class MultidimensionAccumulator {

    private final Queue<Direction> moves = new LinkedList<>();

    public MultidimensionAccumulator accumulateLeft(long amount) {
        return accumulate(Direction.left(amount));
    }

    public MultidimensionAccumulator accumulateRight(long amount) {
        return accumulate(Direction.right(amount));
    }

    public MultidimensionAccumulator accumulateUp(long amount) {
        return accumulate(Direction.forward(amount));
    }


    public MultidimensionAccumulator accumulateDown(long amount) {
        return accumulate(Direction.back(amount));
    }

    public MultidimensionAccumulator accumulate(Direction direction) {
        moves.add(direction);
        return this;
    }

    public long totalDistance() {
        return moves.stream().mapToLong(Direction::amount).sum();
    }

    public Point2D calculate() {
        Queue<Direction> moves = new LinkedList<>(this.moves);
        Point2D point2D = new Point2D(0, 0);

        while (!moves.isEmpty()) {
            Direction direction;
            switch ((direction = moves.poll()).cardinality()) {
                case FORWARD:
                    point2D = point2D.add(0, direction.amount());
                    break;
                case LEFT:
                    point2D = point2D.add(-direction.amount(), 0);
                    break;
                case RIGHT:
                    point2D = point2D.add(direction.amount(), 0);
                    break;
                case BACK:
                    point2D = point2D.add(0, -direction.amount());
                    break;
                default:
                    throw new IllegalStateException("Unknown value: " + direction);
            }
        }
        return point2D;
    }

    public void doWalk() {
        Queue<Direction> moves = new LinkedList<>(this.moves);
        Direction direction;
        while ((direction = moves.poll()) != null) {
            Logger.info("Moved: [ " + direction.cardinality() + " ] Ammount: [ " + direction.amount() + " ] ");
        }
    }

}
