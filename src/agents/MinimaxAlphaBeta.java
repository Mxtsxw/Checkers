package agents;

import cherckers.Board;
import cherckers.Constants;
import cherckers.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinimaxAlphaBeta implements AI {
    private int depth;
    private final String color;
    private final Random random = new Random();

    public MinimaxAlphaBeta(String color, int depth) {
        this.color = color;
        this.depth = depth;
    }

    public Board alphabeta(Board state) {
        EvaluationResult result = playerMAX(state, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        return result.getBoard();
    }

    public EvaluationResult playerMAX(Board state, int depth, double alpha, double beta) {
        if (depth == 0 || state.isTerminal()) {
            int evaluate = state.evaluate(this.color);
            System.out.println(evaluate + " " + state);
            return new EvaluationResult(evaluate, state);
        }

        double maxEval = Double.NEGATIVE_INFINITY;
        List<Board> bestMoves = new ArrayList<>();

        for (Board move : state.getLegalActionsByColor(this.color)) {
            double eval = playerMIN(move, depth - 1, alpha, beta).getEvaluation();
            if (eval > maxEval) {
                maxEval = eval;
                bestMoves.clear(); // Reset the move list
                bestMoves.add(move);
            } else if (eval == maxEval) {
                bestMoves.add(move);
            }
            alpha = Math.max(alpha, eval);
            if (beta <= alpha) {
                break; // Beta cut-off
            }
        }

        Board bestBoard = bestMoves.get(random.nextInt(bestMoves.size())); // Choose a random best move
        return new EvaluationResult(maxEval, bestBoard);
    }

    public EvaluationResult playerMIN(Board state, int depth, double alpha, double beta) {
        if (depth == 0 || state.isTerminal()) {
            int evaluate = state.evaluate(this.color);
            System.out.println(evaluate + " " + state);
            return new EvaluationResult(evaluate, state);
        }

        double minEval = Double.POSITIVE_INFINITY;
        List<Board> bestMoves = new ArrayList<>();
        String opponentColor = this.color.equals(Constants.RED) ? Constants.BLACK : Constants.RED;

        for (Board move : state.getLegalActionsByColor(opponentColor)) {
            double eval = playerMAX(move, depth - 1, alpha, beta).getEvaluation();
            if (eval < minEval) {
                minEval = eval;
                bestMoves.clear(); // Reset the move list
                bestMoves.add(move);
            } else if (eval == minEval) {
                bestMoves.add(move);
            }
            beta = Math.min(beta, eval);
            if (beta <= alpha) {
                break; // Alpha cut-off
            }
        }

        Board bestBoard = bestMoves.get(random.nextInt(bestMoves.size())); // Choose a random best move
        return new EvaluationResult(minEval, bestBoard);
    }

    @Override
    public Board run(Game game) {
        return alphabeta(game.getBoard());
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}