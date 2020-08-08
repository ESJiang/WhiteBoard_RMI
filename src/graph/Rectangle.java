package graph;
import java.awt.*;

public class Rectangle extends Shape
{
	private static final long serialVersionUID = 1L;
	public void draw(Graphics2D graph) 
	{		
		graph.setStroke(new BasicStroke(stroke));
		graph.setColor(color);	
		int width = Math.abs(x1 - x2);
		int height = Math.abs(y1 - y2);
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		graph.drawRect(Math.min(x1, x2), Math.min(y1, y2), width, height);
	}
}