package graph;

import java.awt.Font;
import java.awt.Graphics2D;

public class Text extends Shape {
    private static final long serialVersionUID = 1L;

    public void draw(Graphics2D g) {
        g.setColor(color);
        g.setFont(new Font(fontName, italic + bold, fontSize));
        if (text != null) {
            g.drawString(text, x1, y1);
        }
    }
}