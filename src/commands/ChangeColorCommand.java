package commands;

import shapes.Shape;
import java.awt.Color;

public class ChangeColorCommand implements Command {
    private Shape shape;
    private Color oldColor;
    private Color newColor;

    public ChangeColorCommand(Shape shape, Color newColor) {
        this.shape = shape;
        this.oldColor = shape.getColor();
        this.newColor = newColor;
    }

    @Override
    public void execute() {
        shape.setColor(newColor);
    }

    @Override
    public void undo() {
        shape.setColor(oldColor);
    }
}
