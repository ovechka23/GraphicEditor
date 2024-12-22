package commands;

import shapes.Shape;
import java.util.ArrayList;

public class AddShapeCommand implements Command {
    private Shape shape;
    private ArrayList<Shape> shapes;

    public AddShapeCommand(Shape shape, ArrayList<Shape> shapes) {
        this.shape = shape;
        this.shapes = shapes;
    }

    @Override
    public void execute() {
        shapes.add(shape);
    }

    @Override
    public void undo() {
        shapes.remove(shape);
    }
}