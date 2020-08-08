package graph;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Random;

public class Spray extends Shape 
{	
	private static final long serialVersionUID = 1L;
	private int[] fx;
	private int[] fy;
	private static final int DENSITY = 500;	
	public Spray() 
	{	
		fx = new int[DENSITY];
		fy = new int[DENSITY];
		Random random = new Random();	
		for (int i = 0; i < DENSITY; i++) 
		{
			fx[i] = random.nextInt(100);
			fy[i] = random.nextInt(100);	
		}
	}
	
	public void draw(Graphics2D graph) 
	{	
		//JOIN_BEVEL, JOIN_MITER, JOIN_ROUND
		//CAP_BUTT, CAP_ROUND, CAP_SQUARE
		graph.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		graph.setPaint(color);
		double radius = (double)stroke / 2;
		for (int i = 0; i < DENSITY; i++) 
		{			
			double radianX = (double) (fx[i] - 50) / 50 * Math.PI * radius;
			double radianY = (double) (fy[i] - 50) / 50 * Math.PI * radius;
			graph.draw(new Line2D.Double(x1 + radianX * Math.cos(radianX), y1 + radianY * Math.cos(radianY), 
					x1 + radianX * Math.cos(radianX), y1 + radianY * Math.cos(radianY)));

		}		
	}
}