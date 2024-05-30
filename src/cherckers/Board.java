package cherckers;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Board implements Cloneable {
    private Tile[][] board;
    private int redLeft;
    private int blackLeft;
    private int redKings;
    private int blackKings;
    private String turn;
    private int count;

    public Board(String turn) {
        this.board = new Tile[Constants.ROWS][Constants.COLS];
        this.redLeft = this.blackLeft = 12;
        this.redKings = this.blackKings = 0;
        this.turn = turn;
        this.count = 0;
        setup();
    }

    /**
     * Copy constructor
     */
    public Board(Board board) {
        this.board = new Tile[Constants.ROWS][Constants.COLS];
        this.redLeft = board.redLeft;
        this.blackLeft = board.blackLeft;
        this.redKings = board.redKings;
        this.blackKings = board.blackKings;
        this.turn = board.turn;
        this.count = board.count;
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLS; col++) {
                this.board[row][col] = new Tile(board.getTile(row, col));
            }
        }
    }



    private void setup() {
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLS; col++) {
                Tile tile = new Tile(row, col, Constants.SQUARE_SIZE, Constants.SQUARE_SIZE);
                tile.setPreferredSize(new Dimension(Constants.SQUARE_SIZE, Constants.SQUARE_SIZE));

                // Set the background color of the tiles
                if ((row + col) % 2 == 0) {
                    tile.setBackground(Color.WHITE);
                } else {
                    tile.setBackground(Color.DARK_GRAY);
                }

                // Place the initial pieces on the board
                if (row < 3 && (row + col) % 2 != 0) {
                    tile.setOccupyingPiece(new Pawn(row, col, Constants.BLACK));
                } else if (row > 4 && (row + col) % 2 != 0) {
                    tile.setOccupyingPiece(new Pawn(row, col, Constants.RED));
                }

                board[row][col] = tile;
            }
        }
    }


    /**
     * Promote a pawn to king if it reaches the opposite end of the board
     * @param piece
     */
    public void promoteToKing(Piece piece) {
        if (piece.getColor().equals(Constants.BLACK) && piece.getRow() == Constants.ROWS - 1) {
            this.board[piece.getRow()][piece.getCol()].setOccupyingPiece(new King(piece.getRow(), piece.getCol(), Constants.BLACK));
            this.blackKings++;
        } else if (piece.getColor().equals(Constants.RED) && piece.getRow() == 0) {
            this.board[piece.getRow()][piece.getCol()].setOccupyingPiece(new King(piece.getRow(), piece.getCol(), Constants.RED));
            this.redKings++;
        }
    }

    /**
     * Move the piece to the new position
     * @param piece
     * @param row
     * @param col
     */
    public void move(Piece piece, int row, int col) {
        this.board[piece.getRow()][piece.getCol()].setOccupyingPiece(null);
        this.board[row][col].setOccupyingPiece(piece);
        piece.move(row, col);
        promoteToKing(piece);
    }

    /**
     * Move the piece to the new position and remove the jumped piece
     * @param piece
     * @param row
     * @param col
     */
    public void jump(Piece piece, int row, int col) {
        int middleRow = (piece.getRow() + row) / 2;
        int middleCol = (piece.getCol() + col) / 2;
        board[middleRow][middleCol].setOccupyingPiece(null);
        move(piece, row, col);
        if (piece.getColor().equals(Constants.RED)) {
            blackLeft--;
        } else {
            redLeft--;
        }
    }

    /**
     * Return the valid moves for the piece
     */
    public List<Integer[]> getValidMoves(Piece piece) {
        List<Integer[]> moves = new ArrayList<>();
        for (Integer[] move : piece.possibleMoves()) {
            int newRow = piece.getRow() + move[0];
            int newCol = piece.getCol() + move[1];
            if (isValidPosition(newRow, newCol) && board[newRow][newCol].getOccupyingPiece() == null) {
                Integer[] moveArray = { newRow, newCol };
                moves.add(moveArray);
            }
        }
        return moves;
    }

    /**
     * Return the valid jumps for the piece
     */
    public List<Integer[]> getValidJumps(Piece piece) {
        List<Integer[]> jumps = new ArrayList<>();
        for (Integer[] jump : piece.possibleMoves()) {
            int jumpRow = piece.getRow() + jump[0];
            int jumpCol = piece.getCol() + jump[1];
            int landRow = jumpRow + jump[0];
            int landCol = jumpCol + jump[1];
            if (isValidPosition(landRow, landCol)) {
                Piece jumpPiece = board[jumpRow][jumpCol].getOccupyingPiece();
                Piece landPiece = board[landRow][landCol].getOccupyingPiece();
                if (jumpPiece != null && !jumpPiece.getColor().equals(piece.getColor()) && landPiece == null) {
                    Integer[] moveArray = { landRow, landCol };
                    jumps.add(moveArray);
                }
            }
        }
        return jumps;
    }

    /**
     * Evaluate the board
     */
    public int evaluate(String color, Map<String, Integer> criterias) {

        // Retrieve all weights with default
        int materialWeight = criterias.getOrDefault("Material", 2);
        int kingWeight = criterias.getOrDefault("King", 5);
        int eatableWeight = criterias.getOrDefault("Eatable", 2);
        int movableWeight = criterias.getOrDefault("Movable", 1);
        int winWeight = criterias.getOrDefault("Win", 1000);

        int res = 0;

        if (isTerminal()) {
            String winner = getWinner();

            if (winner.equals(color)) {
                return winWeight;
            } else {
                return -winWeight;
            }
        }

        if (color.equals(Constants.RED)) {
            res += (this.redLeft - this.blackLeft) * materialWeight;
            res += (this.redKings - this.blackKings) * kingWeight;
            res += (getAllMoves(Constants.RED).size() - getAllMoves(Constants.BLACK).size()) * movableWeight;
            res += (getAllJumps(Constants.RED).size() - getAllJumps(Constants.BLACK).size()) * eatableWeight;
        } else if (color.equals(Constants.BLACK)) {
            res += (this.blackLeft - this.redLeft) * materialWeight;
            res += (this.blackKings - this.redKings) * kingWeight;
            res += (getAllMoves(Constants.BLACK).size() - getAllMoves(Constants.RED).size()) * movableWeight;
            res += (getAllJumps(Constants.BLACK).size() - getAllJumps(Constants.RED).size()) * eatableWeight;
        }
        return res;
    }

    /**
     * Return all the pieces of the given color
     */
    public List<Piece> getAllPieces(String color) {
        List<Piece> pieces = new ArrayList<>();
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLS; col++) {
                Piece piece = board[row][col].getOccupyingPiece();
                if (piece != null && piece.getColor().equals(color)) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }

    /**
     * Check if the game is over
     */
    public boolean isTerminal() {
        return redLeft == 0 || blackLeft == 0 || getAllMoves(Constants.RED).size() == 0 || getAllMoves(Constants.BLACK).size() == 0;
    }

    /**
     * Get all the possible moves for the given color
     */
    public List<Integer[]> getAllMoves(String color) {
        List<Integer[]> moves = new ArrayList<>();
        for (Piece piece : getAllPieces(color)) {
            moves.addAll(getValidMoves(piece));
            moves.addAll(getValidJumps(piece));
        }
        return moves;
    }

    public List<Integer[]> getAllSimpleMoves(String color) {
        List<Integer[]> moves = new ArrayList<>();
        for (Piece piece : getAllPieces(color)) {
            moves.addAll(getValidMoves(piece));
        }
        return moves;
    }

    public List<Integer[]> getAllJumps(String color) {
        List<Integer[]> jumps = new ArrayList<>();
        for (Piece piece : getAllPieces(color)) {
            jumps.addAll(getValidJumps(piece));
        }
        return jumps;
    }

    /**
     * Get all the possible moves (resulting board states)
     */
    public List<Board> getLegalActionsByColor(String color) {
        List<Board> boards = new ArrayList<>();
        for (Piece piece : getAllPieces(color)) {
            List<Integer[]> jumps = getValidJumps(piece);
            if (jumps.size() > 0) {
                for (Integer[] jump : jumps) {
                    Board newBoard = this.clone(); // new Board(this);
                    Piece tempPiece = newBoard.getTile(piece.getRow(), piece.getCol()).getOccupyingPiece();
                    newBoard.jump(tempPiece, jump[0], jump[1]);
                    newBoard.count++;
                    newBoard.turn = turn.equals(Constants.RED) ? Constants.BLACK : Constants.RED;
                    // Double jump case
                    if (newBoard.getValidJumps(tempPiece).size() > 0) {
                        boards.addAll(newBoard.getLegalActionsByColor(color));
                    } else {
                        boards.add(newBoard);
                    }
                }
            } else {
                List<Integer[]> moves = getValidMoves(piece);
                if (moves.size() > 0) {
                    for (Integer[] move : moves) {
                        Board newBoard = this.clone(); // new Board(this);
                        Piece tempPiece = newBoard.getTile(piece.getRow(), piece.getCol()).getOccupyingPiece();
                        newBoard.move(tempPiece, move[0], move[1]);
                        newBoard.count++;
                        newBoard.turn = turn.equals(Constants.RED) ? Constants.BLACK : Constants.RED;
                        boards.add(newBoard);
                    }
                }
            }
        }
        return boards;
    }

    /**
     * Get all the possible moves (resulting board states)
     */
    public List<Board> getLegalActions() {
        return turn.equals(Constants.RED) ? getLegalActionsByColor(Constants.RED) : getLegalActionsByColor(Constants.BLACK);
    }

    /**
     * Return the winner of the game
     */
    public String getWinner() {
        if (redLeft == 0 || getAllMoves(Constants.RED).size() == 0) {
            return Constants.BLACK;
        } else if (blackLeft == 0 || getAllMoves(Constants.BLACK).size() == 0) {
            return Constants.RED;
        }
        return null;
    }

    /**
     * Get the tile at given index
     */
    public Tile getTile(int row, int col) {
        return board[row][col];
    }

    /**
     * Switch the turn
     */
    public void switchTurn() {
        this.turn = turn.equals(Constants.RED) ? Constants.BLACK : Constants.RED;
        this.count++;
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < Constants.ROWS && col >= 0 && col < Constants.COLS;
    }



    /**
     * Draw the board
     */
    public void draw() {
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLS; col++) {
                Tile tile = board[row][col];
                if (tile != null) {
                    tile.draw();
                }
            }
        }
    }

    public Tile[][] getBoard() {
        return board;
    }

    public String getTurn() {
        return turn;
    }

    public void setTile(int row, int col, Tile tile){
        this.board[row][col] = tile;
    }

    public void setBoard(Tile[][] board) {
        this.board = board;
    }

    @Override
    public Board clone() {
        try {
            Board clone = (Board) super.clone();
            JPanel newBoardPanel = new JPanel(new GridLayout(Constants.BOARD_SIZE, Constants.BOARD_SIZE));
            clone.setBoard(new Tile[Constants.ROWS][Constants.COLS]);
            for (int row = 0; row < Constants.ROWS; row++) {
                for (int col = 0; col < Constants.COLS; col++) {
                    clone.setTile(row, col, getTile(row, col).clone());
                    newBoardPanel.add(clone.getTile(row, col));
                }
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < Constants.ROWS; row++) {
            for (int col = 0; col < Constants.COLS; col++) {
                Tile tile = board[row][col];
                if (tile.getOccupyingPiece() != null) {
                    sb.append(tile.getOccupyingPiece().getColor().charAt(0));
                } else {
                    sb.append("-");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
