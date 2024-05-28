package cherckers;

public class King extends Piece {
    private final String type;
    private final String imgPath;

    public King(int row, int col, String color) {
        super(row, col, color);
        this.type = "king";
        this.imgPath = String.format("images/%s-king.png", color.toLowerCase());
    }

    /**
     * Copy constructor
     */
    public King(King king) {
        super(king.getRow(), king.getCol(), king.getColor());
        this.type = "king";
        this.imgPath = king.getImgPath();
    }

    /**
     * Return the possible moves/actions for the king.
     */
    @Override
    public Integer[][] possibleMoves() {
        Integer[][] moves = new Integer[4][2];
        moves[0] = new Integer[]{-1, -1};
        moves[1] = new Integer[]{+1, -1};
        moves[2] = new Integer[]{-1, +1};
        moves[3] = new Integer[]{+1, +1};
        return moves;
    }

    @Override
    public void move(int row, int col) {
        this.row = row;
        this.col = col;
        this.x = Constants.SQUARE_SIZE * this.col + Constants.SQUARE_SIZE / 2;
        this.y = Constants.SQUARE_SIZE * this.row + Constants.SQUARE_SIZE / 2;
    }

    @Override
    public String toString() {
        return String.format("King<%s, (%s, %s)>", color, super.getRow(), super.getCol());
    }

    public String getImgPath() {
        return imgPath;
    }
}
