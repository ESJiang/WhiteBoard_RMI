package graph;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.JPanel;

public abstract class Shape implements Serializable 
{
	private static final long serialVersionUID = 1L;
    public int x1, y1, x2, y2;
    public int italic, bold;
    public int stroke;
    public int currentChoice;
    public int length;
    public int fontSize;
    public Color color;
    public BufferedImage image; 
    public JPanel board; 
    public String fontName;
    public String text;
    public abstract void draw(Graphics2D graph);
}