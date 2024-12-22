package commands;

import shapes.Shape;
import java.util.ArrayList;

public class RemoveShapeCommand implements Command {
    private Shape shape;
    private ArrayList<Shape> shapes;

    public RemoveShapeCommand(Shape shape, ArrayList<Shape> shapes) {
        this.shape = shape;
        this.shapes = shapes;
    }

    @Override
    public void execute() {
        shapes.remove(shape);
    }

    @Override
    public void undo() {
        shapes.add(shape);
    }
}
