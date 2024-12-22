package shapes;

import java.awt.Color;

public class ShapeState {
    public String shapeType;
    public Color color;
    public int width;
    public int height;
    public int x;
    public int y;

    public ShapeState(Shape shape) {
        this.shapeType = shape.getClass().getSimpleName();
        this.color = shape.getColor();
        this.width = shape.getWidth();
        this.height = shape.getHeight();
        this.x = shape.getX();
        this.y = shape.getY();
    }

    public void restore(Shape shape) {
        shape.setColor(color);
        shape.resize(width, height);
        shape.move(x - shape.getX(), y - shape.getY());
    }

}
