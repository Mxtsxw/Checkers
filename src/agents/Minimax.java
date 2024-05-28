package agents;

import cherckers.Board;
import cherckers.Constants;
import cherckers.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Minimax implements AI{
    private int depth;
    private final String color;

    private final Random random = new Random();

    public Minimax(String color, int depth) {
        this.color = color;
        this.depth = depth;
    }

    public Board minimax(Board state) {
        EvaluationResult result = playerMAX(state, depth);
        return result.getBoard();
    }

    public EvaluationResult playerMAX(Board state, int depth) {
        if (depth == 0 || state.isTerminal()) {
            int evaluate = state.evaluate(this.color);
            System.out.println(evaluate + " " + state);
            return new EvaluationResult(evaluate, state);
        }

        double maxEval = Double.NEGATIVE_INFINITY;
        List<Board> bestMoves = new ArrayList<>();

        for (Board move : state.getLegalActionsByColor(this.color)) {
            double eval = playerMIN(move, depth - 1).getEvaluation();
            if (eval > maxEval) {
                maxEval = eval;
                bestMoves.clear(); // Reset the move list
                bestMoves.add(move);
            } else if (eval == maxEval) {
                bestMoves.add(move);
            }
        }

        Board bestBoard = bestMoves.get(random.nextInt(bestMoves.size())); // Choose a random best move
        return new EvaluationResult(maxEval, bestBoard);
    }

    public EvaluationResult playerMIN(Board state, int depth) {
        if (depth == 0 || state.isTerminal()) {
            int evaluate = state.evaluate(this.color);
            System.out.println(evaluate + " " + state);
            return new EvaluationResult(evaluate, state);
        }

        double minEval = Double.POSITIVE_INFINITY;
        List<Board> bestMoves = new ArrayList<>();

        for (Board move : state.getLegalActionsByColor(this.color.equals(Constants.RED) ? Constants.BLACK : Constants.RED)) {
            double eval = playerMAX(move, depth - 1).getEvaluation();
            if (eval < minEval) {
                minEval = eval;
                bestMoves.clear(); // Reset the move list
                bestMoves.add(move);
            } else if (eval == minEval) {
                bestMoves.add(move);
            }
        }

        Board bestBoard = bestMoves.get(random.nextInt(bestMoves.size())); // Choose a random best move
        return new EvaluationResult(minEval, bestBoard);
    }

    @Override
    public Board run(Game game) {
        return minimax(game.getBoard());
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
