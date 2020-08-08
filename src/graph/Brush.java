package graph;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Random;

public class Brush extends Shape 
{
	private static final long serialVersionUID = 1L;
	private int[] fx;
	private int[] fy;
	private static int DENSITY = 15;
	
	public void draw(Graphics2D graph) 
	{
		
		DENSITY = stroke;
		fx = new int[DENSITY];
		fy = new int[DENSITY];
		Random random = new Random();
		for (int i = 0; i < DENSITY; i++) 
		{
			fx[i] = random.nextInt(100);
			fy[i] = random.nextInt(100);	
		}
		graph.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		graph.setPaint(color);;
		double size = (double)stroke / 2;
		for (int i = 0; i < DENSITY; i++) 
		{			
			double deltaX = (double) (fx[i] - 50) / 50 * size;
			double deltaY = (double) (fy[i] - 50) / 50 * size;
			graph.draw(new Line2D.Double(x1 + deltaX , y1 + deltaY, x2 + deltaX, y2 + deltaY));
		}	
	}
}