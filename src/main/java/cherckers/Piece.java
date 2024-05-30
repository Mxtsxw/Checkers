package cherckers;

public abstract class Piece implements Cloneable{
    protected int row;
    protected int col;
    protected String color;
    protected int x;
    protected int y;

    public Piece(int row, int col, String color) {
        this.row = row;
        this.col = col;
        this.color = color;
        this.x = Constants.SQUARE_SIZE * this.col + Constants.SQUARE_SIZE / 2;
        this.y = Constants.SQUARE_SIZE * this.row + Constants.SQUARE_SIZE / 2;
    }

    /**
     * Return the possible moves/actions for the piece.
     */
    public abstract Integer[][] possibleMoves();

    /**
     * Move the piece to a new position.
     * @param row int The new row index.
     * @param col int The new column index.
     */
    public abstract void move(int row, int col);

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String getColor() {
        return color;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public abstract String getImgPath();

    @Override
    public Piece clone() {
        try {
            return (Piece) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return String.format("%s<%d, %d>", this.getClass().getSimpleName(), this.row, this.col);
    }
}