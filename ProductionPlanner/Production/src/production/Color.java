/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package production;

/**
 *
 * @author riharada
 */
public class Color extends Specification {
    
    public Color(Type type) {
        super(type);
    }
    
    public enum type implements Type {
        black,
        white,
        gray
    }

    @Override
    public Type getType() {
        return super.getType();
    }
}
