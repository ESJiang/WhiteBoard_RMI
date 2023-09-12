package graph;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Random;

public class Spray extends Shape {
    private static final long serialVersionUID = 1L;
    private int[] fx = new int[DENSITY], fy = new int[DENSITY];
    private static final int DENSITY = 500;

    public Spray() {
        for (int i = 0; i < DENSITY; i++) {
            fx[i] = new Random().nextInt(100);
            fy[i] = new Random().nextInt(100);
        }
    }

    public void draw(Graphics2D graph) {
        graph.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graph.setPaint(color);
        for (int i = 0; i < DENSITY; i++) {
            double radianX = (double) (fx[i] - 50) / 50 * Math.PI * (double) stroke / 2;
            double radianY = (double) (fy[i] - 50) / 50 * Math.PI * (double) stroke / 2;
            graph.draw(new Line2D.Double(x1 + radianX * Math.cos(radianX), y1 + radianY * Math.cos(radianY),
                    x1 + radianX * Math.cos(radianX), y1 + radianY * Math.cos(radianY)));
        }
    }
}