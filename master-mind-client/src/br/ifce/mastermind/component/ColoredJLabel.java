package br.ifce.mastermind.component;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jrocha on 25/07/14.
 */
public class ColoredJLabel extends JLabel {

    private Color color;

    public ColoredJLabel(Color color) {
        super("   ");
        this.color = color;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.color != null)
            g.setColor(this.color);
        else {
            JPanel p = new JPanel();
            g.setColor(p.getBackground());
        }
        g.fillOval(0, 0, getWidth(), getHeight());
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
