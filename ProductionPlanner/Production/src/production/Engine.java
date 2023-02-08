/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package production;

/**
 *
 * @author riharada
 */
public class Engine extends Specification {
    
    public Engine(Type type) {
        super(type);
    }
    
    public enum type implements Type {
        gas,
        hybrid,
        electric
    }
    
    @Override
    public Type getType() {
        return super.getType();
    }
}
