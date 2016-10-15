package ca.jdr23bc.muzeisimplesource;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

public class Circle {
    float radius;
    Point pos;
    int color;

    public Circle(float radius, Point pos, int color) {
        this.radius = radius;
        this.pos = pos;
        this.color = color;
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        canvas.drawCircle(pos.x, pos.y, radius, paint);
    }

    public String toString() {
        return "radius: " + radius + ", pos: " + pos + ", c: " + color;
    }
}
