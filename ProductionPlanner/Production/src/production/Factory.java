/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package production;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

/**
 *
 * @author riharada
 */
public class Factory {
    
    private ArrayList<Category> categories;
    private ArrayList<Order> ordersPrimary; //orderek elsődleges listája
    private ArrayList<Order> orders; //orderek amikkel számol az algoritmus
    private ArrayList<Order> productionPlan; //smart sortolt orderek
    
    private ArrayList<Order> unfiltered; //filter előtti biztonsági másolat
    private boolean isFiltered = false;
    
    private LocalDateTime productionStart = LocalDateTime.now();
    private LocalDateTime currentTime;
    private GanttChartGUI ganttChart;
    
    private EngineLine engineLine;
    private FrameLine frameLine;
    private PaintLine paintLine;
    
    private boolean isSorted = false;

    public Factory() {
        super();
        ganttChart = new GanttChartGUI();
                
        Database.connection();
        
        categories = new ArrayList<>();
        categories = Database.getCategoriesFromDB(categories);
        
        ordersPrimary = new ArrayList<>();
        ordersPrimary = Database.getOrdersFromDB(ordersPrimary);
        
        orders = new ArrayList<>(ordersPrimary);
        
        engineLine = new EngineLine();
        frameLine = new FrameLine();
        paintLine = new PaintLine();
            
        productionPlan = new ArrayList<>(ordersPrimary);
    }
    
    public void smartSort() {
        
        if(orders.isEmpty()) {
            orders = new ArrayList<>(ordersPrimary);
        }
        
        productionPlan.clear();
        
        int PlanReturn, LowestValue = 8, BestStarterIndex = 0;

        for(int i = 0; i < ordersPrimary.size(); i++) {
            productionPlan.add(orders.get(i));
            orders.remove(i);
            PlanReturn = compareByPriority();
            if(PlanReturn < LowestValue) {
                LowestValue = PlanReturn;
                BestStarterIndex = i;
            }
            productionPlan.clear();
            orders = new ArrayList<>(ordersPrimary);
        }

        productionPlan.add(orders.get(BestStarterIndex));
        orders.remove(BestStarterIndex);
        compareByPriority();
        
        isSorted = true;
    }
    
    public void updateDate() {
        productionStart = LocalDateTime.now();
    }
    
    public void createChart() {
        
        currentTime = LocalDateTime.now();
        
        LocalDateTime start = productionStart, end = productionStart;
        ganttChart.newChange(productionStart, productionStart, productionPlan.get(0).getOrderID());
        
        for(int i = 0; i < productionPlan.get(0).getQtyNeed(); i++) {
            end = end.plusMinutes(frameLine.getProcessTime() + engineLine.getProcessTime() + paintLine.getProcessTime());
        }
        
        ganttChart.newProcess(start, end, productionPlan.get(0).getOrderID());
        
        
        int maxChangeTime = 0;
        
        for(int i = 1; i < productionPlan.size(); i++) {
            
            maxChangeTime = 0;
            
            if(productionPlan.get(i).getCategoryID() != productionPlan.get(i - 1).getCategoryID()) {
                start = end;

                if(getPPDetails(i).getCarColor() != getPPDetails(i - 1).getCarColor()) {
                    if(paintLine.getChangeTime() > maxChangeTime) {
                        maxChangeTime = paintLine.getChangeTime();
                    }
                }
                
                if(getPPDetails(i).getCarEngine() != getPPDetails(i - 1).getCarEngine()) {
                    if(engineLine.getChangeTime() > maxChangeTime) {
                        maxChangeTime = engineLine.getChangeTime();
                    }
                }
                
                if(getPPDetails(i).getCarFrame() != getPPDetails(i - 1).getCarFrame()) {
                    if(frameLine.getChangeTime() > maxChangeTime) {
                        maxChangeTime = frameLine.getChangeTime();
                    }
                }
                
                end = end.plusMinutes(maxChangeTime);
                ganttChart.newChange(start, end, productionPlan.get(i).getOrderID());
            }
            else {
                ganttChart.newChange(productionStart, productionStart, productionPlan.get(i).getOrderID());
            }
            
            start = end;
            
            //annyiszor adja hozzá a process timet ahány rendelést kértek
            for(int j = 0; j < productionPlan.get(i).getQtyNeed(); j++) {
                end = end.plusMinutes(frameLine.getProcessTime() + engineLine.getProcessTime() + paintLine.getProcessTime());
            }
            ganttChart.newProcess(start, end, productionPlan.get(i).getOrderID());
        }
        ganttChart.newTimeline(productionStart, currentTime);
    }
    
    public void resetProductionPlan() {
        
        productionPlan = new ArrayList<>(ordersPrimary);
        isSorted = false;
    }
    
    public long getCategoryIDBySpecs(String[] specs) {
        
        long categoryID = 0;
        for(Category category : categories) {
            if(category.getCarEngine().toString().equals(specs[0]) && category.getCarFrame().toString().equals(specs[1]) && category.getCarColor().toString().equals(specs[2])) {
               categoryID = category.getCategoryID();
               break;
            }
        }
        return categoryID;
    }
    
    public int getIndex(int rowNumber) {
        
        int index = rowNumber;
        
        if(isSorted) {
            for(int i = 0; i < productionPlan.size(); i++) {
                if(ordersPrimary.get(i).getOrderID() == productionPlan.get(rowNumber).getOrderID()) {
                    index = i;
                    break;
                }
            }
        }
        
        return index;
    }
    
    public void newItem(String[] values) {
        
        long categoryID = getCategoryIDBySpecs(values);
        Order newOrder;
        
        //ha nincs 1. számú eleme a listának
        if(ordersPrimary.get(0).getOrderID() > 1) {
            newOrder = new Order(1, categoryID, Integer.parseInt(values[3]), Integer.parseInt(values[4]));
            ordersPrimary.add(0, newOrder);
            Database.addElementToDB(newOrder);
        }
        
        
        else {
            boolean inserted = false;
        
            for(int i = 0; i < ordersPrimary.size() - 1; i++) {
                //ha két listaelem között legalább egy szabad hely van
                if((ordersPrimary.get(i).getOrderID() + 1) != ordersPrimary.get(i + 1).getOrderID()) {
                    //létrehoz egy új elemet a megadott pozícióban
                    newOrder = new Order(i + 2, categoryID, Integer.parseInt(values[3]), Integer.parseInt(values[4]));
                    ordersPrimary.add(i + 1, newOrder);
                    Database.addElementToDB(newOrder);
                    inserted = true;
                    break;
                }
            }
            //ha csak a lista végén van szabad hely
            if(!inserted) {
                newOrder = new Order(ordersPrimary.size() + 1, categoryID, Integer.parseInt(values[3]), Integer.parseInt(values[4]));
                ordersPrimary.add(newOrder);
                Database.addElementToDB(newOrder);
            }
        }
    }
    
    public void deleteItem(int rowNumber) {
        
        Order order = ordersPrimary.get(getIndex(rowNumber));
        ordersPrimary.remove(getIndex(rowNumber));
        Database.deleteElementFromDB(order);
    }
    
    public void setQuantity(int mode, int rowNumber, int newValue) {
        
        if(mode == 1) { //Need
            ordersPrimary.get(getIndex(rowNumber)).setQtyNeed(newValue);
        }
        else { //Done
            ordersPrimary.get(getIndex(rowNumber)).setQtyDone(newValue);
        }
        
        Database.updateElementInDB(ordersPrimary.get(getIndex(rowNumber)));
    }
    
    public boolean move(int mode, int rowNumber) {
        
        boolean success = true;

        if(isSorted) {
            
            //kell nekünk egy ideiglenes elem, amivel helyet cserél a kiválasztott elem
            Order temporary = productionPlan.get(rowNumber);
            
            if(mode == 1) { // up
                //legelső elemet nem lehet megint legfelülre helyezni
                if(rowNumber == 0) {
                    success = false;
                }
                else {
                    productionPlan.set(rowNumber, productionPlan.get(rowNumber - 1));
                    productionPlan.set(rowNumber - 1, temporary);
                }
            }
            else { // down
                //legalsó elemet nem lehet megint legalulra helyezni
                if(rowNumber == productionPlan.size() - 1) {
                    success = false;
                }
                else {
                    productionPlan.set(rowNumber, productionPlan.get(rowNumber + 1));
                    productionPlan.set(rowNumber + 1, temporary);
                }
            }
        }
        else {
            Order temporary = ordersPrimary.get(rowNumber);
            
            if(mode == 1) { // up
                if(rowNumber == 0) {
                    success = false;
                }
                else {
                    ordersPrimary.set(rowNumber, ordersPrimary.get(rowNumber - 1));
                    ordersPrimary.set(rowNumber - 1, temporary);
                }
            }
            else { // down
                if(rowNumber == ordersPrimary.size() - 1) {
                    success = false;
                }
                else {
                    ordersPrimary.set(rowNumber, ordersPrimary.get(rowNumber + 1));
                    ordersPrimary.set(rowNumber + 1, temporary);
                }
            }
        }
        
        return success;
    }
    
    public void moveSpecific(int place, int rowNumber) {
        if(isSorted) {
            Order temporary = productionPlan.get(rowNumber);
            productionPlan.remove(temporary);
            productionPlan.add(place, temporary);
        }
        else {
            Order temporary = ordersPrimary.get(rowNumber);
            ordersPrimary.remove(temporary);
            ordersPrimary.add(place, temporary);
        }
    }
    
    public void filter(String[] values) {
        
        if(isFiltered) {
            isFiltered = false;
            productionPlan = unfiltered;
        }
        else {
            isFiltered = true;
            unfiltered = new ArrayList<>(productionPlan);
            boolean passed;

            int i = 0;

            while(i < productionPlan.size()) {

                passed = true;

                //gas
                if(values[0].equals("f")) {
                    if(getPPDetails(i).getCarEngine() == Engine.type.gas) {
                        passed = false;
                    }
                }

                //hybrid
                if(values[1].equals("f")) {
                    if(getPPDetails(i).getCarEngine() == Engine.type.hybrid) {
                        passed = false;
                    }
                }

                //electric
                if(values[2].equals("f")) {
                    if(getPPDetails(i).getCarEngine() == Engine.type.electric) {
                        passed = false;
                    }
                }

                //sedan
                if(values[3].equals("f")) {
                    if(getPPDetails(i).getCarFrame() == Frame.type.sedan) {
                        passed = false;
                    }
                }

                //suv
                if(values[4].equals("f")) {
                    if(getPPDetails(i).getCarFrame() == Frame.type.suv) {
                        passed = false;
                    }
                }

                //coupe
                if(values[5].equals("f")) {
                    if(getPPDetails(i).getCarFrame() == Frame.type.coupe) {
                        passed = false;
                    }
                }

                //white
                if(values[6].equals("f")) {
                    if(getPPDetails(i).getCarColor() == Color.type.white) {
                        passed = false;
                    }
                }

                //gray
                if(values[7].equals("f")) {
                    if(getPPDetails(i).getCarColor() == Color.type.gray) {
                        passed = false;
                    }
                }

                //black
                if(values[8].equals("f")) {
                    if(getPPDetails(i).getCarColor() == Color.type.black) {
                        passed = false;
                    }
                }

                //min
                if(!values[9].equals("f")) {
                    if(productionPlan.get(i).getQtyNeed() < Integer.parseInt(values[9])) {
                        passed = false;
                    }
                }

                //max
                if(!values[10].equals("f")) {
                    if(productionPlan.get(i).getQtyNeed() > Integer.parseInt(values[10])) {
                        passed = false;
                    }
                }

                //ellenőrzés
                if(!passed) {
                    productionPlan.remove(i);
                }
                else {
                    i++;
                }
            }
            if(productionPlan.isEmpty()) {
                productionPlan = unfiltered;
                isFiltered = false;
                FactoryGUI.filterWarning();
            }
        }
    }
    
    public void resetFilter() {
        isFiltered = false;
        productionPlan = unfiltered;
    }
    
    public Category getPPDetailsHT(boolean head) {
        
        return head ? categories.get((int)productionPlan.get(0).getCategoryID() - 1) : categories.get((int)productionPlan.get(productionPlan.size() - 1).getCategoryID() - 1);
    }
    
    public Category getPPDetails(int index) {
        
        return categories.get((int)productionPlan.get(index).getCategoryID() - 1);
    }

    public Category getOrderDetails(int index) {
        
        return categories.get((int)orders.get(index).getCategoryID() - 1);
    }
    
    public int compareByPriority() {
        
        int priority = 1;
        int maxpriority = 1;
        
        while(!orders.isEmpty()) {
            
            int index = 0;
            
            if(priority > maxpriority) {
                maxpriority = priority;
            }
            
            while(index < orders.size()) {
                
                switch(priority) {
                    case 1: {
                        // Check all matches
                        //Head
                        if(productionPlan.get(0).getCategoryID() == orders.get(index).getCategoryID()) {
                            productionPlan.add(0, orders.get(index));
                            orders.remove(index);
                            index = -1;
                            priority = 1;
                        }

                        //Tail
                        else if(productionPlan.get(productionPlan.size() - 1).getCategoryID() == orders.get(index).getCategoryID()) {
                            productionPlan.add(orders.get(index));
                            orders.remove(index);
                            index = -1;
                            priority = 1;
                        }
                        break;
                    }
                    case 2: {
                        //Check engine and frame matches
                        //Head
                        if(getPPDetailsHT(true).getCarEngine() == getOrderDetails(index).getCarEngine() && getPPDetailsHT(true).getCarFrame() == getOrderDetails(index).getCarFrame()) {
                            productionPlan.add(0, orders.get(index));
                            orders.remove(index);
                            index = -1;
                            priority = 1;
                        }

                        //Tail
                        else if(getPPDetailsHT(false).getCarEngine() == getOrderDetails(index).getCarEngine() && getPPDetailsHT(false).getCarFrame() == getOrderDetails(index).getCarFrame()) {
                            productionPlan.add(orders.get(index));
                            orders.remove(index);
                            index = -1;
                            priority = 1;
                        }
                        break;
                    }
                    case 3: {
                        //Check frame and color matches
                        //Head
                        if(getPPDetailsHT(true).getCarFrame() == getOrderDetails(index).getCarFrame() && getPPDetailsHT(true).getCarColor() == getOrderDetails(index).getCarColor()) {
                            productionPlan.add(0, orders.get(index));
                            orders.remove(index);
                            index = -1;
                            priority = 1;
                        }

                        //Tail
                        else if(getPPDetailsHT(false).getCarFrame() == getOrderDetails(index).getCarFrame() && getPPDetailsHT(false).getCarColor() == getOrderDetails(index).getCarColor()) {
                            productionPlan.add(orders.get(index));
                            orders.remove(index);
                            index = -1;
                            priority = 1;
                        }
                        break;
                    }
                    case 4: {
                        //Check engine and color matches
                        //Head
                        if(getPPDetailsHT(true).getCarEngine() == getOrderDetails(index).getCarEngine() && getPPDetailsHT(true).getCarColor() == getOrderDetails(index).getCarColor()) {
                            productionPlan.add(0, orders.get(index));
                            orders.remove(index);
                            index = -1;
                            priority = 1;
                        }

                        //Tail
                        else if(getPPDetailsHT(false).getCarEngine() == getOrderDetails(index).getCarEngine() && getPPDetailsHT(false).getCarColor() == getOrderDetails(index).getCarColor()) {
                            productionPlan.add(orders.get(index));
                            orders.remove(index);
                            index = -1;
                            priority = 1;
                        }
                        break;
                    }
                    case 5: {
                        //Check frame match
                        //Head
                        if(getPPDetailsHT(true).getCarFrame() == getOrderDetails(index).getCarFrame()) {
                            productionPlan.add(0, orders.get(index));
                            orders.remove(index);
                            index = -1;
                            priority = 1;
                        }

                        //Tail
                        else if(getPPDetailsHT(false).getCarFrame() == getOrderDetails(index).getCarFrame()) {
                            productionPlan.add(orders.get(index));
                            orders.remove(index);
                            index = -1;
                            priority = 1;
                        }
                        break;
                    }
                    case 6: {
                        //Check engine match
                        //Head
                        if(getPPDetailsHT(true).getCarEngine() == getOrderDetails(index).getCarEngine()) {
                            productionPlan.add(0, orders.get(index));
                            orders.remove(index);
                            index = -1;
                            priority = 1;
                        }

                        //Tail
                        else if(getPPDetailsHT(false).getCarEngine() == getOrderDetails(index).getCarEngine()) {
                            productionPlan.add(orders.get(index));
                            orders.remove(index);
                            index = -1;
                            priority = 1;
                        }
                        break;
                    }
                    case 7: {
                        //Check color match
                        //Head
                        if(getPPDetailsHT(true).getCarColor() == getOrderDetails(index).getCarColor()) {
                            productionPlan.add(0, orders.get(index));
                            orders.remove(index);
                            index = -1;
                            priority = 1;
                        }

                        //Tail
                        else if(getPPDetailsHT(false).getCarColor() == getOrderDetails(index).getCarColor()) {
                            productionPlan.add(orders.get(index));
                            orders.remove(index);
                            index = -1;
                            priority = 1;
                        }
                        break;
                    }
                    case 8: {
                        //No match
                        productionPlan.add(orders.get(index));
                        orders.remove(index);
                        index = -1;
                        priority = 1;
                    }
                    default: {
                        break;
                    }
                }
                index++;
            }
            priority++;
        }
        return maxpriority;
    }
    
    public ArrayList<Order> getProductionPlan() {
        
        return productionPlan;
    }
    
    public ArrayList<Category> getCategories() {
        
        return categories;
    }
    
    public boolean getIsSorted() {
        
        return isSorted;
    }
    
    public void setIsSorted(boolean isSorted) {
        
        this.isSorted = isSorted;
    }
    
    public boolean getIsFiltered() {
        
        return isFiltered;
    }
    
    public void setIsFiltered(boolean isFiltered) {
        
        this.isFiltered = isFiltered;
    }
}