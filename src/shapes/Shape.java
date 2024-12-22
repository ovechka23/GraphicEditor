package shapes;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Shape {
    protected Color color;
    protected int x, y; // Позиция фигуры

    // Конструктор класса Shape
    public Shape(Color color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public abstract void draw(Graphics g);

    public void setColor(Color color) {
        this.color = color;
    }

    public void move(int deltaX, int deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract void resize(int newWidth, int newHeight);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getColor() {
        return color;
    }
}