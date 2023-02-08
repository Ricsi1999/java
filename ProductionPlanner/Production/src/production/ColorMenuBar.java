/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package production;

import javax.swing.JMenuBar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.plaf.basic.BasicMenuBarUI;

/**
 *
 * @author riharada
 */
public class ColorMenuBar extends JMenuBar {
    
    private Color color = new Color(100, 100, 100);

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
    }
}
