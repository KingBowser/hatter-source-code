package me.hatter.tools.commons.color;

public class Position {

    private int row;
    private int col;

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public static Position getPosition(int row, int col) {
        return new Position(row, col);
    }
}
