package graph;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

public class Line extends Shape {
    private static final long serialVersionUID = 1L;

    public void draw(Graphics2D graph) {
        graph.setStroke(new BasicStroke(stroke));
        graph.setColor(color);
        graph.drawLine(x1, y1, x2, y2);
    }
}