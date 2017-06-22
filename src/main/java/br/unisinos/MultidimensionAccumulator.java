package br.unisinos;

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
            switch ((direction = moves.poll()).cardinalidade()) {
                case NORTH:
                    point2D = point2D.add(0, direction.amount());
                    break;
                case WEST:
                    point2D = point2D.add(-direction.amount(), 0);
                    break;
                case EAST:
                    point2D = point2D.add(direction.amount(), 0);
                    break;
                case SOUTH:
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
            Logger.info("Moved: [ " + direction.cardinalidade() + " ] Ammount: [ " + direction.amount() + " ] ");
        }
    }

}
