package cherckers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Game {
    private Piece selected;
    private String turn;
    private Board board;
    private List<Integer[]> validMoves;
    private List<Integer[]> validJumps;

    public Game() {
        // Initialize the board
        init();
        // Add an ActionListener to handle board clicks
        enableClickEvent();
    }

    public void enableClickEvent() {
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLS; col++) {
                int finalRow = row;
                int finalCol = col;
                this.board.getBoard()[row][col].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Tile clicked at: " + finalRow + ", " + finalCol + " " + Game.this.board.getBoard()[finalRow][finalCol].getOccupyingPiece());
                        Game.this.select(finalRow, finalCol);
                        // Handle the click event, e.g., move a piece
                        Game.this.update();
                    }
                });
            }
        }
    }

    public void resetClickEvent(){
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLS; col++) {
                Tile tile = this.board.getBoard()[row][col];
                for (ActionListener al : tile.getActionListeners()) {
                    tile.removeActionListener(al);
                }
                int finalRow = row;
                int finalCol = col;
                tile.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Tile clicked at: " + finalRow + ", " + finalCol + " " + Game.this.board.getBoard()[finalRow][finalCol].getOccupyingPiece());
                        Game.this.select(finalRow, finalCol);
                        Game.this.update();
                    }
                });
            }
        }
    }

    public void disableClickEvent(){
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLS; col++) {
                Tile tile = this.board.getBoard()[row][col];
                for (ActionListener al : tile.getActionListeners()) {
                    tile.removeActionListener(al);
                }
            }
        }
    }

    public void update() {
        board.draw();
    }

    private void init() {
        selected = null;
        turn = Constants.RED;
        board = new Board(turn);
        validMoves = null;
        validJumps = null;
    }

    public String winner() {
        return board.getWinner();
    }

    public boolean select(int row, int col) {
        resetHighlight();

        if (selected != null) {
            if (!move(row, col)) {
                selected = null;
                return select(row, col);
            }
        }

        Tile tile = board.getTile(row, col);
        Piece piece = tile.getOccupyingPiece();
        if (piece != null && piece.getColor().equals(turn)) {
            selected = piece;
            validMoves = board.getValidMoves(piece);
            validJumps = board.getValidJumps(piece);
            tile.setHighlight(true);
            validMoves.forEach(move -> board.getTile(move[0], move[1]).setHighlight(true));
            validJumps.forEach(jump -> board.getTile(jump[0], jump[1]).setHighlight(true));
            return true;
        }

        return false;
    }

    private boolean move(int row, int col) {
        Tile tile = board.getTile(row, col);
        Piece piece = tile.getOccupyingPiece();
        if (selected != null && piece == null) {
            // Check if the selected piece can move to the specified position
            for (Integer[] move : validMoves) {
                if (Arrays.equals(move, new Integer[]{row, col})) {
                    board.move(selected, row, col);
                    switchTurn();
                    return true;
                }
            }
            // Check if the selected piece can jump to the specified position
            for (Integer[] jump : validJumps) {
                if (Arrays.equals(jump, new Integer[]{row, col})) {
                    board.jump(selected, row, col);
                    switchTurn();
                    return true;
                }
            }
        }
        return false;
    }

    private void switchTurn() {
        selected = null;
        validMoves = null;
        validJumps = null;
        board.switchTurn();
        turn = this.board.getTurn();
        System.out.println(this.turn);
    }

    private void resetHighlight() {
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLS; col++) {
                Tile tile = board.getTile(row, col);
                if (tile != null) {
                    tile.setHighlight(false);
                }
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public void aiMove(Board newBoard) {
        board = newBoard;
        selected = null;
        validMoves = null;
        validJumps = null;
        turn = this.board.getTurn();
    }

    public List<Board> getLegalActions() {
        return board.getLegalActions();
    }

    public boolean isTerminal() {
        return board.isTerminal();
    }

    public int evaluate() {
        return board.evaluate(turn, new HashMap<>());
    }

    public int evaluate(String color, HashMap<String, Integer> criterias) {
        return board.evaluate(color, criterias);
    }

    public String getTurn() {
        return turn;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

}
