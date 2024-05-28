package cherckers;

public class Pawn extends Piece {
    private final String type;
    private final String imgPath;

    public Pawn(int row, int col, String color) {
        super(row, col, color);
        this.type = "pawn";
        this.imgPath = String.format("images/%s-pawn.png", color.toLowerCase());
    }

    /**
     * Copy constructor
     */
    public Pawn(Pawn pawn) {
        super(pawn.getRow(), pawn.getCol(), pawn.getColor());
        this.type = "pawn";
        this.imgPath = pawn.getImgPath();
    }

    /**
     * Return the possible moves/actions for the pawn
     */
    @Override
    public Integer[][] possibleMoves() {
        Integer[][] moves = new Integer[2][2];
        if (super.getColor().equals(Constants.BLACK)) {
            moves[0] = new Integer[]{+1, -1};
            moves[1] = new Integer[]{+1, +1};
        } else {
            moves[0] = new Integer[]{-1, -1};
            moves[1] = new Integer[]{-1, +1};
        }
        return moves;
    }

    @Override
    public void move(int row, int col) {
        this.row = row;
        this.col = col;
        this.x = Constants.SQUARE_SIZE * this.col + Constants.SQUARE_SIZE / 2;
        this.y = Constants.SQUARE_SIZE * this.row + Constants.SQUARE_SIZE / 2;
    }

    public String getImgPath() {
        return this.imgPath;
    }

    @Override
    public String toString() {
        return String.format("Pawn<%s, (%s, %s)>", color, super.getRow(), super.getCol());
    }
}
