package shapes;

import java.awt.Color;
import java.awt.Graphics;

public class Triangle extends Shape {
    private int base;
    private int height;

    public Triangle(Color color, int base, int height, int x, int y) {
        super(color, x, y);
        this.base = base;
        this.height = height;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        int[] xPoints = {x, x - base / 2, x + base / 2};
        int[] yPoints = {y, y + height, y + height};
        g.fillPolygon(xPoints, yPoints, 3); // Рисуем треугольник
    }

    @Override
    public int getWidth() {
        return base;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void resize(int newBase, int newHeight) {
        this.base = newBase;
        this.height = newHeight;
    }
}
