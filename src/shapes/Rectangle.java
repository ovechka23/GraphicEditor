package shapes;

import java.awt.Color;
import java.awt.Graphics;

public class Rectangle extends Shape {
    private int width, height;

    public Rectangle(Color color, int width, int height, int x, int y) {
        super(color, x, y);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height); // Рисуем прямоугольник
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void resize(int newWidth, int newHeight) {
        this.width = newWidth;
        this.height = newHeight;
    }
}