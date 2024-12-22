package commands;

import shapes.Shape;

public class MoveShapeCommand implements Command {
    private Shape shape;
    private int oldX, oldY; // Старые координаты
    private int newX, newY; // Новые координаты

    public MoveShapeCommand(Shape shape, int newX, int newY) {
        this.shape = shape;
        this.oldX = shape.getX();
        this.oldY = shape.getY();
        this.newX = newX;
        this.newY = newY;
    }

    @Override
    public void execute() {
        // Применяем новые координаты
        shape.move(newX - shape.getX(), newY - shape.getY());
    }

    @Override
    public void undo() {
        // Возвращаем фигуру на старое место
        shape.move(oldX - shape.getX(), oldY - shape.getY());
    }
}