package com.hkamran.ai;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JFrame;

public class Visualizer extends JComponent {

	class Point {
		int x;
		int y;
		
		public Point(int x, int y) {
			this.x = x; 
			this.y = y;
		}
	}
	
	private static final long serialVersionUID = 1L;
	public Network network;
	JFrame frame;
	int largestLayer = 0;
	
	public Visualizer(Network network) {
		this.network = network;

		for (Layer layer : network.getLayers()) {
			largestLayer = Math.max(largestLayer, layer.size());
		}
		
		this.frame = new JFrame("Neural Network");		
		frame.setSize(new Dimension(200 * this.network.getLayers().size(), 400 + 45 * largestLayer));

		centerWindow();
		this.repaint();
	}
	
	public void centerWindow() {
		Dimension displayDimension = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(displayDimension.width / 2 - frame.getSize().width / 2,
				displayDimension.height / 2 - frame.getSize().height / 2);
		
		frame.getContentPane().add(this, BorderLayout.CENTER);
		frame.getContentPane().add(this, BorderLayout.CENTER);
		frame.setResizable(true);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		
		this.setFocusable(true);
		this.requestFocusInWindow();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			    RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		int layerMargin = 40;
		int nodeMargin = 20;
		
		Map<Node, Point> map = new HashMap<Node, Point>();
		
		g2.setColor(Color.WHITE);
		g2.fillOval(30, 5, 10, 10);
		
		Network network;
		synchronized (this.network) {
			network = this.network;
		}
		if (network == null) return;
		map.put(network.bias, new Point(21, 11));
		
		int x = 25;
		List<Layer> layers = network.getLayers();
		for (int i = 0; i < layers.size(); i++) {
			Layer layer = layers.get(i);
			
			int y = 35;
			for (Node node : layer.getNodes()) {

				if (i > 0 && i < layers.size() - 1) {
					g2.setColor(Color.yellow);
				} else {
					g2.setColor(Color.WHITE);
					
				}
				Stroke oldStroke = g2.getStroke();
				g2.setStroke(new BasicStroke(2));
				g2.drawOval(x - 3, y - 3, 15, 15);
				
				g2.setStroke(oldStroke);
				
				if (node.getOutput() == 0) {
					g2.setColor(Color.BLACK);
				} else if (node.getOutput() <= 0.3) {
					g2.setColor(Color.GRAY);
				} else {
					g2.setColor(Color.WHITE);
				}
				g2.fillOval(x, y, 10, 10);
				
				map.put(node, new Point(x, y));
				y += nodeMargin;
				
				
			}
			x += layerMargin;
		}
		
		for (Layer layer : layers) {
			
			for (Connection connection : layer.getConnections()) {
				
				Point from = map.get(connection.from);
				Point to = map.get(connection.to);
				
				if (connection.weight < 0) {
					g.setColor(Color.RED);
				} else {
					g.setColor(Color.GREEN);
				}
				

				float width = (float) (2 + Math.abs(0.02 * connection.weight));
                g2.setStroke(new BasicStroke(width));
                if (from  == null) return;
                g2.draw(new Line2D.Float(from.x + 14,from.y + 5, to.x - 5, to.y + 5));
			}
		}

		
		

	}
	
	public void start() throws InterruptedException {
		while (true) {
			this.repaint();
		}
	}
}
