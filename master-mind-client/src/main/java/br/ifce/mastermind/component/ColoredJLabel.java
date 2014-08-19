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
            g.setColor(Color.LIGHT_GRAY);
            this.color = Color.LIGHT_GRAY;
        }

//        if (this.color.equals(Color.BLACK) || this.color.equals(Color.WHITE) || this.color.equals(Color.LIGHT_GRAY)) {
//            g.fillOval(0, 0, getWidth()/2, getHeight()/2);
//        } else {
            g.fillOval(0, 0, getWidth(), getHeight());
//        }


    }

    public Color getColor() {
        return this.color;
    }
}
