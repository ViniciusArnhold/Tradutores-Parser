package br.unisinos.translator;

import br.unisinos.direction.Direction;
import br.unisinos.direction.MultidimensionAccumulator;
import br.unisinos.tokens.Token;
import br.unisinos.util.Logger;
import javafx.geometry.Point2D;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @author Vinicius Pegorini Arnhold.
 */
public class Evaluator {

    private final MultidimensionAccumulator accumulator = new MultidimensionAccumulator();

    private Evaluator() {
    }

    public static Evaluator create() {
        return new Evaluator();
    }

    public void eval(List<Token> cmdsParsed) {
        cmdsParsed.stream().map(t -> (Direction) t.getValue()).forEachOrdered(accumulator::accumulate);

        Point2D pontoResultante = accumulator.calculate();

        accumulator.doWalk();

        Logger.lineBreak();
        Logger.warn("Ponto final: [ x = %s, y = %s ]", pontoResultante.getX(), pontoResultante.getY());

        Logger.warn("Distancia do ponto de inicio: [%s]", formatDouble(pontoResultante.distance(Point2D.ZERO)));
        Logger.warn("Distancia total percorrida: [%s]", accumulator.totalDistance());
    }

    private String formatDouble(double distance) {
        DecimalFormat df = new DecimalFormat("#.###");
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(distance);
    }

    public MultidimensionAccumulator getAcumulator() {
        return accumulator;
    }
}
