package graph;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Pentagon extends Shape {
    private static final long serialVersionUID = 1L;

    public void draw(Graphics2D graph) {
        graph.setStroke(new BasicStroke(stroke));
        graph.setPaint(color);
        int[] x = { (x1 + x2) / 2, Math.min(x1, x2), Math.min(x1, x2) + Math.abs(x1 - x2) / 4,
                Math.max(x1, x2) - Math.abs(x1 - x2) / 4, Math.max(x1, x2) };
        int[] y = { Math.min(y1, y2), (int) (Math.min(y1, y2) + Math.abs(y1 - y2) / 2.5), Math.max(y1, y2),
                Math.max(y1, y2), (int) (Math.min(y1, y2) + Math.abs(y1 - y2) / 2.5) };
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graph.drawPolygon(x, y, 5);
    }
}