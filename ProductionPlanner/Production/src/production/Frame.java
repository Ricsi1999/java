/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package production;

/**
 *
 * @author riharada
 */
public class Frame extends Specification {
    
    public Frame(Specification.Type type) {
        super(type);
    }
    
    public enum type implements Type {
        sedan,
        coupe,
        suv
    }

    @Override
    public Type getType() {
        return super.getType();
    }
}
