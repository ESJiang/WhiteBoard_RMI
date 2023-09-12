package graph;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class FillRoundRect extends Shape {
    private static final long serialVersionUID = 1L;

    public void draw(Graphics2D graph) {
        graph.setStroke(new BasicStroke(stroke));
        graph.setPaint(color);
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graph.fillRoundRect(Math.min(x1, x2), Math.min(y1, y2),
                Math.abs(x1 - x2), Math.abs(y1 - y2), 60, 60);
    }
}