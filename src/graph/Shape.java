package graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.JPanel;

public abstract class Shape implements Serializable {
    private static final long serialVersionUID = 1L;
    public int x1, y1, x2, y2, italic, bold, stroke, currentChoice, length, fontSize;
    public Color color;
    public String fontName, text;

    public abstract void draw(Graphics2D graph);

    BufferedImage image;
    JPanel board;
}