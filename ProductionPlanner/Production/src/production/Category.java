/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package production;

/**
 *
 * @author riharada
 */
public class Category {
    
    private long categoryID;
    private Engine.type carEngine;
    private Frame.type carFrame;
    private Color.type carColor;
    private String modelCode;

    public Category(long categoryID, Engine.type carEngine, Frame.type carFrame, Color.type carColor, String modelCode) {
        this.categoryID = categoryID;
        this.carEngine = carEngine;
        this.carFrame = carFrame;
        this.carColor = carColor;
        this.modelCode = modelCode;
    }

    public long getCategoryID() {
        return categoryID;
    }

    public Engine.type getCarEngine() {
        return carEngine;
    }

    public Frame.type getCarFrame() {
        return carFrame;
    }

    public Color.type getCarColor() {
        return carColor;
    }

    public String getModelCode() {
        return modelCode;
    }
}
