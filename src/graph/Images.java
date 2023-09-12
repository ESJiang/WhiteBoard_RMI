package graph;

import java.awt.Graphics2D;

public class Images extends Shape {
    private static final long serialVersionUID = 1L;

    public void draw(Graphics2D graph) {
        graph.drawImage(image, 0, 0, board);
    }
}