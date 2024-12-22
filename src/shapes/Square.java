package shapes;

import java.awt.Color;
import java.awt.Graphics;

public class Square extends Rectangle {

    public Square(Color color, int size, int x, int y) {
        super(color, size, size, x, y); // Square is a rectangle with equal width and height
    }
}
