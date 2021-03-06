package old.pmso;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import old.pmso.PMSO.Particle;
import old.visual.DrawConfig;
import visual.SteinerTree;


import basic.Line;
import basic.Point;

public class PMSOAnimation {
	
	private PMSO solver;
	private int steps;
	private DrawConfig drawcfg;
	private int sleep;
	public PMSOAnimation(PMSO solver, int steps, int sleep,DrawConfig drawcfg) {
		this.solver = solver;
		this.steps = steps;
		this.drawcfg = drawcfg;
		this.sleep = sleep;
	}
	
	public void play() {
		 JFrame frame=new JFrame();
         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         frame.setBounds(drawcfg.x,drawcfg.y,drawcfg.width,drawcfg.length);
         DrawPanel draw=new DrawPanel();
         frame.getContentPane().add(draw);
         frame.setVisible(true);
         for(int i=0;i<steps;i++) {
        	 solver.evolve();
        	 draw.counter++;
        	 draw.repaint();
        	 try{Thread.sleep(sleep); } catch(Exception e) {}
         }
	}

	@SuppressWarnings("serial")
	class DrawPanel extends JPanel{
		/**
		 * SETUP
		 */
		private double maxX;
		private double maxY;
		private int counter;
		public DrawPanel() {
			this.maxX = getMaxX(solver.stp.getPoints());
			this.maxY = getMaxY(solver.stp.getPoints());
			this.counter = 0;
		}
		
		private double getMaxY(Point[] points) {
			double h = points[0].getY();
			for(Point point : points) {
				if(point.getY() > h) h = point.getY();
			}
			return h;
		}
		private double getMaxX(Point[] points) {
			double w = points[0].getX();
			for(Point point : points) {
				if(point.getX() > w) w = point.getX();
			}
			return w;
		}
		
		/**
		 * PAINT
		 */
		public void paintComponent(Graphics g) {
			g.setColor(Color.WHITE);
		    g.fillRect(0,0,this.getWidth(),this.getHeight());
		    paintParticles(g,solver.swarms);
			paintSteinerTree(g,solver.getSteinerTree());
			paintBottleNeck(g,solver.getSteinerTree());
		    g.setColor(Color.BLACK);
		    g.drawString("Cycle: " + counter + "/" + steps, 50, 50);
		    g.drawString("Bottleneck: " + solver.getSteinerTree().getBottleneck().getLength() , 150, 50);
		}

		private void paintSteinerTree(Graphics g, SteinerTree steinerTree) {
			g.setColor(Color.BLACK);
			paintTree(g,steinerTree);
			g.setColor(Color.RED);
			paintSteinerStuff(g,steinerTree);
		}

		private void paintTree(Graphics g, SteinerTree steinerTree) {
			paintPoints(g,steinerTree.getPoints());
			paintLines(g,steinerTree.getLines());
		}
		
		private void paintPoints(Graphics g, Point[] points) {
			for(Point point : points) {
				paintPoint(g,point);
			}
		}

		private void paintPoint(Graphics g, Point point) {
			Point p = convertPointToDrawScale(point);
			int psize = drawcfg.pointsize;
			g.fillOval( (int) p.getX()-psize/2, (int) p.getY()-psize/2, psize, psize);
		}

		private Point convertPointToDrawScale(Point point) {
			double x = (0.1 + 0.8 * (point.getX()/maxX)) * this.getWidth();
			double y = (0.1 + 0.8 * (point.getY()/maxY)) * this.getHeight();
			return new Point(x,y);
		}

		private void paintLines(Graphics g, Line[] lines) {
			for(Line line : lines) {
				paintLine(g,line);
			}
		}

		private void paintLine(Graphics g,Line line) {
			Point a = convertPointToDrawScale(line.getA());
			Point b = convertPointToDrawScale(line.getB());
			g.drawLine((int)a.getX(), (int)a.getY(), (int)b.getX(), (int)b.getY());
		}

		private void paintSteinerStuff(Graphics g, SteinerTree steinerTree) {
			g.setColor(Color.red);
			for(Point p : steinerTree.getSteinerPoints()) {
				paintPoint(g,p);
			}
			
			for(Line l : steinerTree.getLines()) {
				if(isSteinerLine(steinerTree,l)) {
					paintLine(g,l);
				}
			}
			
		}

		private boolean isSteinerLine(SteinerTree steinerTree, Line line) {
			for(Point p : steinerTree.getSteinerPoints()) {
				if( p == line.getA() || p == line.getB()) return true;
			}
			return false;
		}

		private void paintBottleNeck(Graphics g, SteinerTree steinerTree) {
			g.setColor(Color.GREEN);
			paintLine(g,steinerTree.getBottleneck());
			
		}
		
		private void paintParticles(Graphics g, Particle[][] swarms) {
			Color[] colors = new Color[] {	Color.BLUE,
					Color.CYAN,
					Color.DARK_GRAY,
					Color.LIGHT_GRAY,
					Color.PINK,
					Color.MAGENTA,
					Color.orange,
					Color.YELLOW};
			int i=0;
			for(Particle[] swarm : swarms) {
				g.setColor(colors[i++ % colors.length]);
				for(Particle p : swarm) {
				paintPoint(g, new Point(p.px,p.py));
				}
			}
		}

	}
}
