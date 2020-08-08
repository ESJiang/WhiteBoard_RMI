package graph;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Pencil extends Shape 
{
	private static final long serialVersionUID = 1L;
	public void draw(Graphics2D graph) 
	{
		graph.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		graph.setPaint(color);
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graph.drawLine(x1, y1, x2, y2);
	}
}