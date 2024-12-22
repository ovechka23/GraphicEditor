package gui;

import shapes.*;
import commands.*;
import commands.Command;
import builder.ShapeBuilder;
import shapes.Rectangle;
import shapes.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Stack;

public class EditorFrame extends JFrame {
    private ArrayList<Shape> shapes;
    private Color currentColor = Color.RED;
    private Shape selectedShape = null;
    private Point lastMousePoint;
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;
    private boolean isResizing = false;

    public EditorFrame() {
        shapes = new ArrayList<>();
        setTitle("Graphic Editor");
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setFocusable(true);
        requestFocusInWindow();

        // Панель с кнопками
        JPanel controlPanel = new JPanel();
        JButton createCircleButton = new JButton("Create Circle");
        JButton createRectangleButton = new JButton("Create Rectangle");
        JButton createSquareButton = new JButton("Create Square");
        JButton createTriangleButton = new JButton("Create Triangle");
        JButton changeColorButton = new JButton("Change Color");
        JButton undoButton = new JButton("Undo");
        JButton redoButton = new JButton("Redo");

        // Назначаем действия на кнопки
        createCircleButton.addActionListener(e -> addShape("circle"));
        createRectangleButton.addActionListener(e -> addShape("rectangle"));
        createSquareButton.addActionListener(e -> addShape("square"));
        createTriangleButton.addActionListener(e -> addShape("triangle"));

        changeColorButton.addActionListener(e -> changeCurrentColor());
        undoButton.addActionListener(e -> undoLastAction());
        redoButton.addActionListener(e -> redoLastAction());

        controlPanel.add(createCircleButton);
        controlPanel.add(createRectangleButton);
        controlPanel.add(createSquareButton);
        controlPanel.add(createTriangleButton);
        controlPanel.add(changeColorButton);
        controlPanel.add(undoButton);
        controlPanel.add(redoButton);

        add(controlPanel, BorderLayout.NORTH);

        // Устанавливаем обработчики мыши
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    for (Shape shape : shapes) {
                        if (isClickInsideShape(shape, e.getX(), e.getY())) {
                            removeShape(shape); // Удаляем фигуру
                            return;
                        }
                    }
                }

                boolean clickedOnShape = false;
                for (Shape shape : shapes) {
                    if (isClickInsideShape(shape, e.getX(), e.getY())) {
                        clickedOnShape = true;

                        selectedShape = shape;
                        lastMousePoint = e.getPoint();

                        // Проверка нажатия на ручку для изменения размера
                        Rectangle bounds = new Rectangle(
                                selectedShape.getColor(),
                                selectedShape.getX(),
                                selectedShape.getY(),
                                selectedShape.getWidth(),
                                selectedShape.getHeight()
                        );
                        isResizing = e.getX() >= bounds.getX() + bounds.getWidth() - 10
                                && e.getY() >= bounds.getY() + bounds.getHeight() - 10;

                        if (isResizing) {
                            // Сохраняем состояние до изменения размера
                            ResizeShapeCommand resizeCommand = new ResizeShapeCommand(
                                    selectedShape,
                                    selectedShape.getWidth(),
                                    selectedShape.getHeight()
                            );
                            undoStack.push(resizeCommand);
                        } else {
                            // Сохраняем состояние до перемещения
                            MoveShapeCommand moveCommand = new MoveShapeCommand(
                                    selectedShape,
                                    selectedShape.getX(),
                                    selectedShape.getY()
                            );
                            undoStack.push(moveCommand);
                        }

                        repaint();
                        break;
                    }
                }

                if (!clickedOnShape) {
                    selectedShape = null;
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (selectedShape != null) {
                    if (isResizing) {
                        // Сохраняем состояние после изменения размера
                        ResizeShapeCommand resizeCommand = new ResizeShapeCommand(
                                selectedShape,
                                selectedShape.getWidth(),
                                selectedShape.getHeight()
                        );
                        undoStack.push(resizeCommand);
                    } else {
                        // Сохраняем новое положение фигуры после перемещения
                        int currentX = selectedShape.getX();
                        int currentY = selectedShape.getY();
                        MoveShapeCommand moveCommand = new MoveShapeCommand(
                                selectedShape,
                                currentX,
                                currentY
                        );
                        undoStack.push(moveCommand);
                    }
                    redoStack.clear();
                }
                isResizing = false;
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedShape != null) {
                    if (isResizing) {
                        // Вычисляем новые размеры
                        int newWidth = Math.max(10, e.getX() - selectedShape.getX());
                        int newHeight = Math.max(10, e.getY() - selectedShape.getY());
                        selectedShape.resize(newWidth, newHeight); // Применяем новые размеры
                        repaint();
                    } else {
                        // Двигаем фигуру
                        int deltaX = e.getX() - lastMousePoint.x;
                        int deltaY = e.getY() - lastMousePoint.y;

                        // Применяем движение к фигуре
                        selectedShape.move(deltaX, deltaY);
                        lastMousePoint = e.getPoint(); // Обновляем позицию мыши
                        repaint();
                    }
                }
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
                    undoLastAction();
                } else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Y) {
                    redoLastAction();
                }
            }
        });
    }

    private void undoLastAction() {
        if (!undoStack.isEmpty()) {
            Command lastCommand = undoStack.pop();
            lastCommand.undo();
            redoStack.push(lastCommand);
            repaint();
        }
    }

    private void redoLastAction() {
        if (!redoStack.isEmpty()) {
            Command lastCommand = redoStack.pop();
            lastCommand.execute();
            undoStack.push(lastCommand);
            repaint();
        }
    }

    private void addShape(String type) {
        ShapeBuilder builder = new ShapeBuilder();
        Shape shape = null;
        int x = (getWidth() - 160) / 2;
        int y = (getHeight() - 80) / 2;

        switch (type) {
            case "circle":
                shape = builder.setType("circle")
                        .setColor(currentColor)
                        .setCircleSize(100, 100)
                        .setPosition(x, y)
                        .build();
                break;
            case "rectangle":
                shape = builder.setType("rectangle")
                        .setColor(currentColor)
                        .setRectangleSize(160, 80)
                        .setPosition(x, y)
                        .build();
                break;
            case "square":
                shape = builder.setType("rectangle")
                        .setColor(currentColor)
                        .setRectangleSize(100, 100)
                        .setPosition(x, y)
                        .build();
                break;
            case "triangle":
                shape = builder.setType("triangle")
                        .setColor(currentColor)
                        .setTriangleSize(100, 100)
                        .setPosition(x, y)
                        .build();
                break;
        }

        Command addShapeCommand = new AddShapeCommand(shape, shapes);
        addShapeCommand.execute();
        undoStack.push(addShapeCommand);
        redoStack.clear();
        repaint();
    }

    private void removeShape(Shape shape) {
        Command removeShapeCommand = new RemoveShapeCommand(shape, shapes);
        removeShapeCommand.execute();
        undoStack.push(removeShapeCommand);
        redoStack.clear();
        repaint();
    }

    private void changeCurrentColor() {
        Color newColor = JColorChooser.showDialog(this, "Choose a color", currentColor);
        if (newColor != null && selectedShape != null) {
            Command changeColorCommand = new ChangeColorCommand(selectedShape, newColor);
            changeColorCommand.execute();
            undoStack.push(changeColorCommand);
            redoStack.clear();
            repaint();
        }
    }

    private boolean isClickInsideShape(Shape shape, int mouseX, int mouseY) {
        if (shape instanceof Circle) {
            return mouseX >= shape.getX() && mouseX <= (shape.getX() + shape.getWidth()) &&
                    mouseY >= shape.getY() && mouseY <= (shape.getY() + shape.getHeight());
        } else if (shape instanceof Rectangle) {
            return mouseX >= shape.getX() && mouseX <= (shape.getX() + shape.getWidth()) &&
                    mouseY >= shape.getY() && mouseY <= (shape.getY() + shape.getHeight());
        } else if (shape instanceof Triangle) {
            int[] xPoints = {shape.getX(), shape.getX() - shape.getWidth() / 2, shape.getX() + shape.getWidth() / 2};
            int[] yPoints = {shape.getY(), shape.getY() + shape.getHeight(), shape.getY() + shape.getHeight()};
            Polygon triangle = new Polygon(xPoints, yPoints, 3);
            return triangle.contains(mouseX, mouseY);
        }
        return false;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for (Shape shape : shapes) {
            shape.draw(g);
        }

        if (selectedShape != null) {
            g.setColor(Color.BLUE);
            if (selectedShape instanceof Triangle) {
                int[] xPoints = {selectedShape.getX(), selectedShape.getX() + selectedShape.getWidth() / 2, selectedShape.getX() - selectedShape.getWidth() / 2};
                int[] yPoints = {selectedShape.getY(), selectedShape.getY() + selectedShape.getHeight(), selectedShape.getY() + selectedShape.getHeight()};

                // Нахождение минимальных и максимальных x и y для рамки
                int minX = Math.min(Math.min(xPoints[0], xPoints[1]), xPoints[2]);
                int minY = Math.min(Math.min(yPoints[0], yPoints[1]), yPoints[2]);
                int maxX = Math.max(Math.max(xPoints[0], xPoints[1]), xPoints[2]);
                int maxY = Math.max(Math.max(yPoints[0], yPoints[1]), yPoints[2]);

                // Рисуем рамку
                g.drawRect(minX, minY, maxX - minX, maxY - minY);
                if (selectedShape.getColor() == Color.RED) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(Color.RED);
                }
                g.fillRect(maxX - 10, maxY - 10, 10, 10); // Ручка изменения размера
            } else {
                g.drawRect(selectedShape.getX(), selectedShape.getY(), selectedShape.getWidth(), selectedShape.getHeight());

                if (selectedShape.getColor() == Color.RED) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(Color.RED);
                }
                g.fillRect(selectedShape.getX() + selectedShape.getWidth() - 10, selectedShape.getY() + selectedShape.getHeight() - 10, 10, 10);
            }
        }
    }
}
