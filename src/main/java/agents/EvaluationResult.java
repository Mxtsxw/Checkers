package agents;

import cherckers.Board;

class EvaluationResult {
    private double evaluation;
    private Board board;

    public EvaluationResult(double evaluation, Board board) {
        this.evaluation = evaluation;
        this.board = board;
    }

    public double getEvaluation() {
        return evaluation;
    }

    public Board getBoard() {
        return board;
    }
}
