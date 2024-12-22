package commands;

import shapes.Shape;

public class ResizeShapeCommand implements Command {
    private Shape shape;
    private int oldWidth;
    private int oldHeight;
    private int newWidth;
    private int newHeight;

    public ResizeShapeCommand(Shape shape, int newWidth, int newHeight) {
        this.shape = shape;
        this.oldWidth = shape.getWidth();
        this.oldHeight = shape.getHeight();
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }

    @Override
    public void execute() {
        shape.resize(newWidth, newHeight);
    }

    @Override
    public void undo() {
        shape.resize(oldWidth, oldHeight);
    }
}
