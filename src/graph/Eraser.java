package graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Eraser extends Shape {
    private static final long serialVersionUID = 1L;

    public void draw(Graphics2D graph) {
        graph.setStroke(new BasicStroke(stroke * 2, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
        graph.setPaint(Color.white);
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graph.drawLine(x1, y1, x2, y2);
    }
}