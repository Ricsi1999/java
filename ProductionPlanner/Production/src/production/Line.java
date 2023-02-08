/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package production;

/**
 *
 * @author riharada
 */
public class Line {
    private int processTime;
    private int changeTime;
    
    public Line(int buildTime, int changeTime) {
        this.processTime = buildTime;
        this.changeTime = changeTime;
    }
    
    public int getProcessTime() {
        return processTime;
    }
    
    public int getChangeTime() {
        return changeTime;
    }
    
    public void setProcessTime(int processTime) {
        this.processTime = processTime;
    }
    
    public void setChangeTime(int changeTime) {
        this.changeTime = changeTime;
    }
}
