package cherckers;

import javax.swing.*;
import java.awt.*;

public class Tile extends JButton implements Cloneable {
    private int row;
    private int col;
    private int tileWidth;
    private int tileHeight;
    private Color drawColor;
    private Color highlightColor;
    private Piece occupyingPiece;
    private boolean highlight;

    /**
     * Constructor for Tile
     * @param row
     * @param col
     * @param tileWidth
     * @param tileHeight
     */
    public Tile(int row, int col, int tileWidth, int tileHeight) {
        this.row = row;
        this.col = col;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        this.drawColor = ((row + col) % 2 == 0) ? Color.LIGHT_GRAY : Color.DARK_GRAY;
        this.highlightColor = Color.GREEN;

        this.occupyingPiece = null;
        this.highlight = false;
    }

    /**
     * Copy constructor for Tile
     */
    public Tile(Tile tile) {
        this.row = tile.row;
        this.col = tile.col;
        this.tileWidth = tile.tileWidth;
        this.tileHeight = tile.tileHeight;
        this.drawColor = tile.drawColor;
        this.highlightColor = tile.highlightColor;
        // Use copy constructor for Piece
        if (tile.occupyingPiece != null) {
            if (tile.occupyingPiece instanceof Pawn) {
                this.occupyingPiece = new Pawn((Pawn) tile.occupyingPiece);
            } else if (tile.occupyingPiece instanceof King) {
                this.occupyingPiece = new King((King) tile.occupyingPiece);
            }
        }
        this.highlight = tile.highlight;
    }

    /**
     * Draw the tile with highlighted color if highlighted
     */
    public void draw() {
        // Set background

        if (this.highlight){
            this.setBackground(this.highlightColor);
        } else {
            this.setBackground(this.drawColor);
        }
        // Draw occupying piece if present
        if (occupyingPiece != null) {
            ImageIcon pieceIcon = new ImageIcon(occupyingPiece.getImgPath());
            Image pieceImage = pieceIcon.getImage().getScaledInstance(this.tileWidth, this.tileHeight, Image.SCALE_SMOOTH);
            ImageIcon resizedPieceIcon = new ImageIcon(pieceImage);

            this.setIcon(resizedPieceIcon);
        } else {
            this.setIcon(null);
        }
        this.repaint();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setOccupyingPiece(Piece piece) {
        occupyingPiece = piece;
    }

    public Piece getOccupyingPiece() {
        return occupyingPiece;
    }

    public boolean isHighlighted() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    @Override
    public Tile clone() {
        try {
            Tile clone = (Tile) super.clone();
            if (clone.getOccupyingPiece() != null)
                clone.setOccupyingPiece(clone.getOccupyingPiece().clone());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        if (occupyingPiece != null) {
            return "Tile<" + occupyingPiece.toString() + ">";
        }
        return String.format("Tile<%d, %d>", row, col);

    }
}
