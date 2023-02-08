/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package production;

/**
 *
 * @author riharada
 */
public class Order {
    
    private long orderID;
    private long categoryID;
    private int qtyNeed;
    private int qtyDone;

    public Order(long orderID, long categoryID, int qtyNeed, int qtyDone) {
        this.orderID = orderID;
        this.categoryID = categoryID;
        this.qtyNeed = qtyNeed;
        this.qtyDone = qtyDone;
    }

    public long getOrderID() {
        return orderID;
    }

    public long getCategoryID() {
        return categoryID;
    }

    public int getQtyNeed() {
        return qtyNeed;
    }

    public int getQtyDone() {
        return qtyDone;
    }
    
    public void setQtyNeed(int qtyNeed) {
        this.qtyNeed = qtyNeed;
    }

    public void setQtyDone(int qtyDone) {
        this.qtyDone = qtyDone;
    }
}
