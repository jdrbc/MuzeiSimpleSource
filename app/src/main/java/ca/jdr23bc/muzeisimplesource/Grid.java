package ca.jdr23bc.muzeisimplesource;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;

import java.util.Iterator;

import static ca.jdr23bc.muzeisimplesource.PatternPainter.Style.Triangles;

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
        public void drawTriangles(Canvas canvas, Paint paint) {
            PointF topLeft = new PointF(x, y);
            PointF topRight = new PointF(x + width, y);
            PointF center = new PointF(x + (width/2), y + (height / 2));
            PointF botLeft = new PointF(x, y + height);
            PointF botRight = new PointF(x + width, y + height);
            Triangle[] triangles = new Triangle[] {
                    new Triangle(topLeft, topRight, center),
                    new Triangle(botLeft, topLeft, center),
                    new Triangle(botLeft, botRight, center),
                    new Triangle(botRight, topRight, center),
            };
            for (Triangle t : triangles) {
                t.draw(canvas, paint);
                paint.setColor(Colour.changeValue(paint.getColor(), 0.8f));
            }
        }
    }

    public class Triangle {
        PointF p1;
        PointF p2;
        PointF p3;
        Triangle(PointF p1, PointF p2, PointF p3) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;
        }

        public void draw(Canvas canvas, Paint paint) {
            Path path = new Path();
            path.setFillType(Path.FillType.EVEN_ODD);
            path.moveTo(p1.x, p1.y);
            path.lineTo(p2.x, p2.y);
            path.lineTo(p3.x, p3.y);
            path.lineTo(p1.x, p1.y);
            path.close();
            canvas.drawPath(path, paint);
        }
    }

}
