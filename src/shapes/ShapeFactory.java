// shapes/ShapeFactory.java
package shapes;

import java.awt.Color;

public class ShapeFactory {
    public static Shape createShape(String type, Color color, int size, int width, int height, int x, int y) {
        switch (type.toLowerCase()) {
            case "circle":
                return new Circle(color, width, height, x, y);
            case "rectangle":
                return new Rectangle(color, width, height, x, y); // width and height for rectangle
            case "triangle":
                return new Triangle(color, width, height, x, y); // base and height for triangle
            case "square":
                return new Square(color, size, x, y); // width and height are the same for square
            default:
                throw new IllegalArgumentException("Unknown shape type");
        }
    }
}
