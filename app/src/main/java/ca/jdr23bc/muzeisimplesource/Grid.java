package ca.jdr23bc.muzeisimplesource;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Iterator;

public class Grid implements Iterator<Grid.Cell> {

    public int width;
    public int height;
    public int rows;
    public int cols;
    public float cellWidth;
    public float cellHieght;

    private Cell currCell;

    public Grid(Canvas canvas, int rows) {
        this(canvas.getWidth(), canvas.getHeight(), rows);
    }

    public Grid(int width, int height, int rows) {
        this(width, height, rows, rows);
    }

    public Grid(int width, int height, int rows, int cols) {
        this.width = width;
        this.height = height;
        this.rows = rows;
        this.cols = cols;
        this.cellWidth = width / rows;
        this.cellHieght = height / rows;
        this.currCell = new Cell(-cellWidth, -cellHieght, this);
    }

    @Override
    public boolean hasNext() {
        boolean hasNext = currCell.y < height + cellHieght;
        if (!hasNext) {
            currCell = new Cell(-cellWidth, -cellHieght, this);
        }
        return hasNext;
    }

    @Override
    public Cell next() {
        currCell = currCell.getNextCell();
        return currCell;
    }

    public class Cell {
        Grid grid;
        float width;
        float height;
        float x;
        float y;

        public Cell(float x, float y, Grid grid) {
            this.grid = grid;
            this.x = x;
            this.y = y;
            this.width = grid.cellWidth;
            this.height = grid.cellHieght;
        }

        public Cell getNextCell() {
            float nextX = x + width;
            float nextY = y;
            if (nextX > grid.width + width) {
                nextX = 0;
                nextY += height;
            }
            return new Cell(nextX, nextY, grid);
        }

        public void draw(Canvas canvas, Paint paint) {
            canvas.drawRect(x, y, x + cellWidth, y + cellHieght, paint);
        }
    }
}
