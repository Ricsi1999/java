/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package production;

/**
 *
 * @author riharada
 */
public class Specification {
    
    protected interface Type { }
    
    private Type type;

    public Specification(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }    
}
