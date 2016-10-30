package ca.jdr23bc.muzeisimplesource;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PatternPainter {
    private static final List<Style> STYLES =
            Collections.unmodifiableList(Arrays.asList(Style.values()));

    public enum Style {
        Lines, Dots, Grid, Dot_Grid, Triangles, Tree, Dot_Grid_Overlay
    }
    Canvas canvas;
    Style style;
    Random random = new Random();
    ColorScheme colorScheme;

    public PatternPainter(Canvas canvas) {
        this.canvas = canvas;
        this.style = STYLES.get(random.nextInt(STYLES.size()));
    }

    public void paint() {
        int rootColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        this.colorScheme = new ColorScheme(rootColor);

        if (style == Style.Lines) {
            paintLines();
        } else if (style == Style.Dots) {
            paintDots();
        } else if (style == Style.Grid) {
            paintGrid();
        } else if (style == Style.Triangles) {
            paintTriangles();
        } else if (style == Style.Tree) {
            paintTree();
        } else if (style == Style.Dot_Grid) {
            paintDotGrid();
        }
    }

    public void paintTree() {
        fillBackground();

        Paint p = new Paint();
        p.setAntiAlias(true);

        Tree t = new Tree(canvas.getWidth(), canvas.getHeight());
        t.grow();

        p.setColor(colorScheme.popRandom());
        t.drawBranches(canvas, p);

        if (random.nextBoolean() || true) {
            p.setColor(colorScheme.popRandom());
            t.drawLeaves(canvas, p);
        }
    }

    public void paintDots() {
        fillBackground();

        // TODO-jdr dynamically set these values based on canvas size or user params
        int num = random.nextInt(9) + 1;
        int minDotWidth = 25;
        int maxDotWidth = 150;
        int circleColor = colorScheme.popRandom();
        for (int i = 0; i < num; i++) {
            Point p = new Point();
            p.x = random.nextInt(canvas.getWidth());
            p.y = random.nextInt(canvas.getHeight());
            Float radius = random.nextFloat() * (maxDotWidth - minDotWidth) + minDotWidth;
            Circle c = new Circle(radius, p, circleColor);
            c.draw(canvas);
            Log.d(SimpleArtSource.TAG, "painted circle " + c.toString());
        }
    }

    public void paintLines() {
        fillBackground();

        Paint p = new Paint();
        p.setAntiAlias(true);
        int minWidth = 25;
        int maxWidth = 150;
        int alpha = Math.max(255, random.nextInt(255) + 100);
        String direction = "vertical";
        String[] directions = new String[] {"vertical", "horizontal"};
        boolean sameColor = random.nextBoolean();
        if (sameColor) {
            p.setColor(colorScheme.getRandom());
            p.setAlpha(alpha);
        }
        boolean sameDir = random.nextBoolean();
        if (sameDir) {
            direction = directions[random.nextInt(2)];
        }
        boolean sameWeight = random.nextBoolean();
        if (sameWeight) {
            p.setStrokeWidth(random.nextFloat() * (maxWidth - minWidth) + minWidth);
        }

        int num = random.nextInt(9) + 1;
        for (int i = 0; i < num; i++) {
            if (!sameColor) {
                p.setColor(colorScheme.getRandom());
                p.setAlpha(alpha);
            }
            if (!sameDir) {
                direction = directions[random.nextInt(2)];
            }
            if (!sameWeight) {
                p.setStrokeWidth(random.nextFloat() * (maxWidth - minWidth) + minWidth);
            }
            Point start = new Point();
            Point end = new Point();
            if (direction == "vertical") {
                start.y = -500;
                start.x = random.nextInt(canvas.getWidth());
                end.y = canvas.getHeight() + 500;
                end.x = random.nextInt(canvas.getWidth());
            } else {
                start.y = random.nextInt(canvas.getHeight());
                start.x = -500;
                end.y = random.nextInt(canvas.getHeight());
                end.x = canvas.getWidth() + 500;
            }
            canvas.drawLine(start.x, start.y, end.x, end.y, p);
        }
    }

    public void paintGrid() {
        Grid grid = new Grid(canvas, random.nextInt(10) + 2);

        boolean drawSquares = random.nextBoolean();
        if (drawSquares) {
            grid.cellHieght = grid.cellWidth;
        }
        boolean drawBorder = random.nextBoolean();
        Paint borderPaint = new Paint();
        if (drawBorder) {
            borderPaint.setStrokeWidth(random.nextFloat() * 30);
            boolean blackBorder = random.nextBoolean();
            if (blackBorder) {
                borderPaint.setColor(Color.BLACK);
            } else {
                borderPaint.setColor(colorScheme.popRandom());
            }
            borderPaint.setStyle(Paint.Style.STROKE);
        }

        Paint p = new Paint();
        p.setAntiAlias(true);
        while(grid.hasNext()) {
            Grid.Cell cell = grid.next();
            p.setColor(colorScheme.getRandom());
            cell.draw(canvas, p);
            if (drawBorder) {
                cell.draw(canvas, borderPaint);
            }
        }
    }


    public void paintDotGrid() {
        fillBackground();
        int maxRadius = 75;
        int minRadius = 10;

        Grid grid = new Grid(canvas.getWidth(), canvas.getHeight() + 125, random.nextInt(15) + 2);
        grid.cellHieght = grid.cellWidth;
        Paint p = new Paint();
        p.setAntiAlias(true);
        float radius = random.nextInt(maxRadius - minRadius) + minRadius;
        boolean sameRadius = random.nextBoolean();
        p.setColor(colorScheme.getRandom());
        boolean sameColor = random.nextBoolean();
        boolean multiDot = random.nextBoolean();
        int numMultiDot = random.nextInt(3) + 1;
        float multiDotStep = Math.min(Math.max(random.nextFloat(), 0.25f), 0.95f);
        while(grid.hasNext()) {
            Grid.Cell cell = grid.next();
            if (!sameColor) {
                p.setColor(colorScheme.getRandom());
            }
            if (!sameRadius) {
                radius = random.nextInt(maxRadius - minRadius) + minRadius;
            }
            canvas.drawCircle(cell.x, cell.y, radius, p);
            if (multiDot) {
                float multiDotRad = radius * multiDotStep;
                for (int i = 0; i < numMultiDot; i++) {
                    p.setColor(colorScheme.getRandom());
                    canvas.drawCircle(cell.x, cell.y, multiDotRad, p);
                    multiDotRad = multiDotStep * multiDotStep;
                }
            }
        }
    }

    public void paintTriangles() {
        Grid grid = new Grid(canvas, random.nextInt(4) + 2);

        boolean drawSquares = random.nextBoolean();
            grid.cellHieght = grid.cellWidth;
        Paint borderPaint = new Paint();
        borderPaint.setStrokeWidth(random.nextFloat() * 30);
        borderPaint.setColor(Color.BLACK);
        borderPaint.setStyle(Paint.Style.STROKE);

        Paint p = new Paint();
        p.setAntiAlias(true);
        while(grid.hasNext()) {
            p.setColor(colorScheme.getRandom());
            Grid.Cell cell = grid.next();
            cell.drawTriangles(canvas, p);
            cell.drawTriangles(canvas, borderPaint);
        }
    }

    public void fillBackground() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(colorScheme.popRandom());
        canvas.drawPaint(paint);
    }
}
