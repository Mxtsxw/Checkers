package agents;

import cherckers.Board;
import cherckers.Constants;
import cherckers.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MinimaxAlphaBeta implements AI {
    private int depth;
    private final String color;
    private final Random random = new Random();
    private Map<String, Integer> criterias;
    public int nodeCounter;

    public MinimaxAlphaBeta(String color, int depth) {
        this.color = color;
        this.depth = depth;
        this.criterias = Map.of("Material", 2, "King", 5, "Eatable", 2, "Movable", 1, "Win", 1000);
    }

    public Board alphabeta(Board state) {
        nodeCounter = 0;
        EvaluationResult result = playerMAX(state, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        return result.getBoard();
    }

    public EvaluationResult playerMAX(Board state, int depth, double alpha, double beta) {
        nodeCounter++;
        if (depth == 0 || state.isTerminal()) {
            int evaluate = state.evaluate(this.color, this.criterias);
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
        nodeCounter++;
        if (depth == 0 || state.isTerminal()) {
            int evaluate = state.evaluate(this.color, this.criterias);
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
        System.out.println("Running MinimaxAlphaBeta (" + depth + ")" + " for " + color + " player" + " with " + criterias);
        Board res = alphabeta(game.getBoard());
        System.out.println("Node count: " + nodeCounter);
        return res;
    }

    @Override
    public void update() {
        // Do nothing
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setCriterias(Map<String, Integer> criterias) {
        this.criterias = criterias;
    }

    @Override
    public Map<String, Integer> getCriterias() {
        return this.criterias;
    }

    @Override
    public String toString() {
        return "MinimaxAlphaBeta (" + depth + ") : " + criterias;
    }
}