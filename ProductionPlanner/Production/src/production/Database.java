package production;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    
    private static Connection con = null;
    private static PreparedStatement ps = null;
    private static ResultSet rs = null;
    
    public static Connection connection() {

        try {
            Class.forName("org.sqlite.JDBC");

            con = DriverManager.getConnection("jdbc:sqlite:database/database.db");

            System.out.println("connection succesful");

            return con;
        }
        catch(Exception e) {
            System.out.println("connection failed");
            return null;
        }
    }
    
    public static ArrayList<Order> getOrdersFromDB(ArrayList<Order> orders) {
        
        try {
            String sql = "SELECT * FROM orders";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            
            while(rs.next()) {
               orders.add(new Order(
                    (long)rs.getInt("OrderID"),
                    (long)rs.getInt("CategoryID"),
                    rs.getInt("QtyNeed"),
                    rs.getInt("QtyDone")
               ));
            }   
        }
        catch(Exception e) {
            System.out.println(e);
        }
        
        return orders;
    }
    
    public static ArrayList<Category> getCategoriesFromDB(ArrayList<Category> categories) {

        try {
            String sql = "SELECT * FROM categories";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                categories.add(new Category(
                    (long)rs.getInt("CategoryID"),
                    Engine.type.valueOf(rs.getString("Engine")),
                    Frame.type.valueOf(rs.getString("Frame")),
                    Color.type.valueOf(rs.getString("Color")),
                    rs.getString("Modelname")
                ));
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return categories;
    }
    
    public static void addElementToDB(Order order) {
        
        try {
            String sql = "INSERT INTO orders(OrderID, CategoryID, QtyNeed, QtyDone) VALUES(?, ?, ?, ?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, (int)order.getOrderID());
            ps.setInt(2, (int)order.getCategoryID());
            ps.setInt(3, order.getQtyNeed());
            ps.setInt(4, order.getQtyDone());
            ps.executeUpdate();
        }
        catch(Exception e) {
            System.out.println(e);
        }
        
    }
    
    public static void deleteElementFromDB(Order order) {
        
        try {
            String sql = "DELETE FROM orders WHERE OrderID = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, (int)order.getOrderID());
            ps.executeUpdate();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
    
    public static void updateElementInDB(Order order) {
        
        try {
            String sql = "UPDATE orders SET QtyNeed = ?, QtyDone = ? WHERE OrderID = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, order.getQtyNeed());
            ps.setInt(2, order.getQtyDone());
            ps.setInt(3, (int)order.getOrderID());
            ps.executeUpdate();
        }
        catch(SQLException e) {
            System.out.println(e);
        }
    }
    
    public static void closeConnection() {
        
        try {
            rs.close();
            ps.close();
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
    
//    public ArrayList<Order> fillOrders(ArrayList<Order> orders) {
//        orders.add( new Order(1, 21, 2, 0) );
//        orders.add( new Order(2, 10, 3, 0) );
//        orders.add( new Order(3, 12, 2, 0) );
//        orders.add( new Order(4, 4, 2, 0) );
//        orders.add( new Order(5, 7, 6, 0) );
//        orders.add( new Order(6, 20, 3, 0) );
//        orders.add( new Order(7, 22, 8, 0) );
//        orders.add( new Order(8, 26, 1, 0) );
//        orders.add( new Order(9, 2, 3, 0) );
//        orders.add( new Order(10, 16, 9, 0) );
//        orders.add( new Order(11, 11, 1, 0) );
//        orders.add( new Order(12, 22, 5, 0) );
//        orders.add( new Order(13, 21, 7, 0) );
//        orders.add( new Order(14, 15, 4, 0) );
//        orders.add( new Order(15, 27, 8, 0) );
//        orders.add( new Order(16, 9, 2, 0) );
//        orders.add( new Order(17, 6, 7, 0) );
//        orders.add( new Order(18, 18, 4, 0) );
//        orders.add( new Order(19, 1, 4, 0) );
//        orders.add( new Order(20, 20, 2, 0) );
//        orders.add( new Order(21, 3, 3, 0) );
//        orders.add( new Order(22, 19, 5, 0) );
//        orders.add( new Order(23, 2, 9, 0) );
//        orders.add( new Order(24, 6, 4, 0) );
//        
//        return orders;
//    }
//    
//    public ArrayList<Category> fillCategories(ArrayList<Category> categories) {
//        categories.add( new Category(1, Engine.type.gas, Frame.type.sedan, Color.type.white, "GASSEDWHT") );
//        categories.add( new Category(2, Engine.type.gas, Frame.type.sedan, Color.type.gray, "AAAAAAAAA") );
//        categories.add( new Category(3, Engine.type.gas, Frame.type.sedan, Color.type.black, "AAAAAAAAA") );
//        categories.add( new Category(4, Engine.type.gas, Frame.type.suv, Color.type.white, "AAAAAAAAA") );
//        categories.add( new Category(5, Engine.type.gas, Frame.type.suv, Color.type.gray, "AAAAAAAAA") );
//        categories.add( new Category(6, Engine.type.gas, Frame.type.suv, Color.type.black, "AAAAAAAAA") );
//        categories.add( new Category(7, Engine.type.gas, Frame.type.coupe, Color.type.white, "AAAAAAAAA") );
//        categories.add( new Category(8, Engine.type.gas, Frame.type.coupe, Color.type.gray, "AAAAAAAAA") );
//        categories.add( new Category(9, Engine.type.gas, Frame.type.coupe, Color.type.black, "AAAAAAAAA") );
//        categories.add( new Category(10, Engine.type.hybrid, Frame.type.sedan, Color.type.white, "AAAAAAAAA") );
//        categories.add( new Category(11, Engine.type.hybrid, Frame.type.sedan, Color.type.gray, "AAAAAAAAA") );
//        categories.add( new Category(12, Engine.type.hybrid, Frame.type.sedan, Color.type.black, "AAAAAAAAA") );
//        categories.add( new Category(13, Engine.type.hybrid, Frame.type.suv, Color.type.white, "AAAAAAAAA") );
//        categories.add( new Category(14, Engine.type.hybrid, Frame.type.suv, Color.type.gray, "AAAAAAAAA") );
//        categories.add( new Category(15, Engine.type.hybrid, Frame.type.suv, Color.type.black, "AAAAAAAAA") );
//        categories.add( new Category(16, Engine.type.hybrid, Frame.type.coupe, Color.type.white, "AAAAAAAAA") );
//        categories.add( new Category(17, Engine.type.hybrid, Frame.type.coupe, Color.type.gray, "AAAAAAAAA") );
//        categories.add( new Category(18, Engine.type.hybrid, Frame.type.coupe, Color.type.black, "AAAAAAAAA") );
//        categories.add( new Category(19, Engine.type.electric, Frame.type.sedan, Color.type.white, "AAAAAAAAA") );
//        categories.add( new Category(20, Engine.type.electric, Frame.type.sedan, Color.type.gray, "AAAAAAAAA") );
//        categories.add( new Category(21, Engine.type.electric, Frame.type.sedan, Color.type.black, "AAAAAAAAA") );
//        categories.add( new Category(22, Engine.type.electric, Frame.type.suv, Color.type.white, "AAAAAAAAA") );
//        categories.add( new Category(23, Engine.type.electric, Frame.type.suv, Color.type.gray, "AAAAAAAAA") );
//        categories.add( new Category(24, Engine.type.electric, Frame.type.suv, Color.type.black, "AAAAAAAAA") );
//        categories.add( new Category(25, Engine.type.electric, Frame.type.coupe, Color.type.white, "AAAAAAAAA") );
//        categories.add( new Category(26, Engine.type.electric, Frame.type.coupe, Color.type.gray, "AAAAAAAAA") );
//        categories.add( new Category(27, Engine.type.electric, Frame.type.coupe, Color.type.black, "AAAAAAAAA") );
//        
//        return categories;
//    }
}
