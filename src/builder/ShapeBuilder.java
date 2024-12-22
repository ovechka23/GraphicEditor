// builder/ShapeBuilder.java
package builder;

import shapes.Shape;
import shapes.ShapeFactory;

import java.awt.Color;

public class ShapeBuilder {
    private String type;
    private Color color;
    private int size; // для некоторых фигур
    private int width; // для прямоугольников и квадратов
    private int height; // для прямоугольников и треугольников
    private int x;
    private int y;

    public ShapeBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public ShapeBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    public ShapeBuilder setCircleSize(int width, int height) {  // Новый метод для установки размеров овала
        this.width = width;
        this.height = height;
        return this;
    }

    public ShapeBuilder setRectangleSize(int width, int height) {
        this.width = width; // ширина для прямоугольника
        this.height = height; // высота для прямоугольника
        return this;
    }

    public ShapeBuilder setTriangleSize(int base, int height) {
        this.width = base; // основание для треугольника
        this.height = height; // высота для треугольника
        return this;
    }

    public ShapeBuilder setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Shape build() {
        return ShapeFactory.createShape(type, color, size, width, height, x, y);
    }
}
