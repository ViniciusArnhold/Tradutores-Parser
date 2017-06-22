package br.unisinos.direction;

import br.unisinos.util.Logger;
import javafx.geometry.Point2D;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by vinicius on 25/05/17.
 */
public class MultidimensionAccumulator {

    private final Queue<Direction> moves = new LinkedList<>();

    public MultidimensionAccumulator accumulateLeft(long amount) {
        moves.add(Direction.left(amount));
        return this;
    }

    public MultidimensionAccumulator accumulateRight(long amount) {
        moves.add(Direction.right(amount));
        return this;
    }

    public MultidimensionAccumulator accumulateUp(long amount) {
        moves.add(Direction.forward(amount));
        return this;
    }


    public MultidimensionAccumulator accumulateDown(long amount) {
        moves.add(Direction.back(amount));
        return this;
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
