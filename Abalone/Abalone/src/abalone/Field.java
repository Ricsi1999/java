/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abalone;

import java.awt.Color;

/**
 *
 * @author RICSI
 * @version 1.0
 * @since 2019. 12. 10.
 */

public class Field {

    //Color type variable with the name color
    private Color color;

    //Sets the field's color to the given parameter
    public void setColor(Color color) {
        this.color = color;
    }
    
    //Returns the color of the field
    public Color getColor() {
        return color;
    }
}
