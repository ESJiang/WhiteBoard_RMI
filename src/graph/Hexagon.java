package graph;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Hexagon extends Shape {
    private static final long serialVersionUID = 1L;

    public void draw(Graphics2D graph) {
        graph.setStroke(new BasicStroke(stroke));
        graph.setPaint(color);
        int[] x = { Math.min(x1, x2) + Math.abs(x1 - x2) / 4, Math.min(x1, x2),
                Math.min(x1, x2) + Math.abs(x1 - x2) / 4, Math.max(x1, x2) - Math.abs(x2 - x1) / 4, Math.max(x1, x2),
                Math.max(x1, x2) - Math.abs(x2 - x1) / 4 };
        int[] y = { Math.min(y1, y2), (y1 + y2) / 2, Math.max(y1, y2), Math.max(y1, y2), (y1 + y2) / 2,
                Math.min(y1, y2) };
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graph.drawPolygon(x, y, 6);
    }
}