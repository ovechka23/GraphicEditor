package shapes;

import java.awt.Color;
import java.awt.Graphics;

public class Circle extends Shape {
    private int width;  // Ширина овала
    private int height; // Высота овала

    public Circle(Color color, int width, int height, int x, int y) {
        super(color, x, y);
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x, y, width, height); // Рисуем овал
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void resize(int newWidth, int newHeight) {
        this.width = newWidth;
        this.height = newHeight;
    }
}
