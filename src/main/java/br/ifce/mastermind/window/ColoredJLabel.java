package br.ifce.mastermind.window;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JLabel;

public class ColoredJLabel extends JLabel {
	
	private static final long	serialVersionUID	= 1L;
	
	private Color				color;
	
	public ColoredJLabel(Color color) {
		super("   ");
		this.color = color;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (this.color != null) g.setColor(this.color);
		else {
			g.setColor(Color.LIGHT_GRAY);
			this.color = Color.LIGHT_GRAY;
		}
		
		g.fillOval(0, 0, getWidth(), getHeight());
		
	}
	
	public Color getColor() {
		return this.color;
	}
}
