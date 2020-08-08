package graph;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Triangle extends Shape 
{	
	private static final long serialVersionUID = 1L;
	public void draw(Graphics2D graph) 
	{
		int[] xVertices = {(x1 + x2) / 2, x1, x2};
		int[] yVertices = {y1, y2, y2};	
		graph.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		graph.setPaint(color);
		graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graph.drawPolygon(xVertices, yVertices, 3);
	}
}