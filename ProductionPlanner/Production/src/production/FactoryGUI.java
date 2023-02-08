/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package production;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;
import java.util.Vector;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicMenuUI;

/**
 *
 * @author riharada
 */
public class FactoryGUI extends JFrame {
    
    private static Factory factory;
    private static GanttChartGUI gantt;
    private static Vector<Vector> PPV;
    private static JFrame frame;
    private static TableGUI tableGUI;
    
    
    public FactoryGUI() {
                
        frame = new JFrame("Production Planner");
        factory = new Factory();
        gantt = new GanttChartGUI();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(70, 70, 70));
        
        ColorMenuBar menuBar = new ColorMenuBar();
        menuBar.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JMenu menu = new JMenu("Menu");
        menu.setForeground(Color.WHITE);
        UIManager.put("Menu.font", new Font("Arial", Font.PLAIN, 15));
        UIManager.put("MenuItem.font", new Font("Arial", Font.PLAIN, 15));
        UIManager.put("Table.font", new Font("Arial", Font.PLAIN, 12));
        menu.setUI(new BasicMenuUI());
        menuBar.add(menu);
        
        frame.setJMenuBar(menuBar);
        
        JMenuItem menuItem1 = new JMenuItem("Smart Sort");
        menu.add(menuItem1);
        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(!factory.getIsFiltered()) {
                    if(!factory.getIsSorted()) {
                        gantt.reset();
                        factory.smartSort();
                        factory.createChart();
                        fill();
                        render();
                    }
                    else {
                        JOptionPane.showMessageDialog(frame, "The list is already sorted");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(frame, "Remove filter first!", "Warning", JOptionPane.WARNING_MESSAGE); 
                }
            }
        });
        
        JMenuItem menuItem2 = new JMenuItem("Original List");
        menu.add(menuItem2);
        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                
                if(!factory.getIsFiltered()) {
                        if(factory.getIsSorted()) {
                        gantt.reset();
                        factory.resetProductionPlan();
                        factory.createChart();
                        fill();
                        render();
                    }
                    else {
                        JOptionPane.showMessageDialog(frame, "The original list is already loaded");
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "Remove filter first!", "Warning", JOptionPane.WARNING_MESSAGE); 
                }
            }
        });
        
        JMenuItem menuItem3 = new JMenuItem("Filter/Reset");
        menu.add(menuItem3);
        menuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                filter();
                gantt.reset();
                factory.createChart();
                fill();
                render();
            }
        });
        
        JMenuItem menuItem4 = new JMenuItem("Update");
        menu.add(menuItem4);
        menuItem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                gantt.reset();
                factory.createChart();
                fill();
                render();
            }
        });
        
        JMenuItem exitItem = new JMenuItem("Exit");
        menu.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Database.closeConnection();
                System.exit(0);
            }
        });
        
        UIManager.put("OptionPane.background", new ColorUIResource(new Color(100, 100, 100)));
        UIManager.put("OptionPane.messageForeground", new ColorUIResource(Color.WHITE));
        UIManager.put("TextField.background", new ColorUIResource(new Color(180, 180, 180)));
        UIManager.put("RadioButton.background", new ColorUIResource(new Color(100, 100, 100)));
        UIManager.put("RadioButton.foreground", new ColorUIResource(Color.WHITE));
        UIManager.put("Label.foreground", new ColorUIResource(Color.WHITE));
        UIManager.put("Panel.background", new ColorUIResource(new Color(100, 100, 100)));
        UIManager.put("PopupMenu.background", new ColorUIResource(new Color(70, 70, 70)));
        UIManager.put("PopupMenu.foreground", new ColorUIResource(Color.WHITE));
        UIManager.put("MenuItem.background", new ColorUIResource(new Color(70, 70, 70)));
        UIManager.put("MenuItem.borderPainted", new ColorUIResource(new Color(70, 70, 70)));
        UIManager.put("Menu.foreground", new ColorUIResource(Color.WHITE));
        UIManager.put("MenuItem.foreground", new ColorUIResource(Color.WHITE));
        UIManager.put("Menu.borderPainted", new ColorUIResource(new Color(70, 70, 70)));
        UIManager.put("CheckBox.background", new ColorUIResource(new Color(100, 100, 100)));
        UIManager.put("CheckBox.foreground", new ColorUIResource(Color.WHITE));
        
        
        factory.createChart();
        
        PPV = new Vector<>();
        fill();
        
        tableGUI = new TableGUI(PPV);
        frame.getContentPane().add(gantt.getPanel(), BorderLayout.EAST);
        frame.getContentPane().add(tableGUI, BorderLayout.WEST);
                
        frame.setPreferredSize(new Dimension(1500, 720));
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
    
    private static void fill() {

        PPV.clear();
        for(int i = 0; i < factory.getProductionPlan().size(); i++) {
            Vector<String> row = new Vector<>();
            row.clear();
            row.addElement(String.valueOf(i + 1));
            row.addElement(String.valueOf(factory.getProductionPlan().get(i).getOrderID()));
            row.addElement(factory.getPPDetails(i).getCarEngine().toString());
            row.addElement(factory.getPPDetails(i).getCarFrame().toString());
            row.addElement(factory.getPPDetails(i).getCarColor().toString());
            row.addElement(factory.getPPDetails(i).getModelCode());
            row.addElement(String.valueOf(factory.getProductionPlan().get(i).getQtyNeed()));
            row.addElement(String.valueOf(factory.getProductionPlan().get(i).getQtyDone()));
            PPV.addElement(row);
        }
    }
    
    private static void render() {
        
        frame.getContentPane().repaint();
        frame.getContentPane().add(gantt.getPanel(), BorderLayout.EAST);
        tableGUI = new TableGUI(PPV);
        frame.getContentPane().add(tableGUI, BorderLayout.WEST);
    }
    
    public static void filter() {
        
        if(!factory.getIsFiltered()) {
            
            JPanel inputPanel = new JPanel();
            JPanel enginePanel = new JPanel();
            JPanel framePanel = new JPanel();
            JPanel colorPanel = new JPanel();
            JPanel qtyPanel = new JPanel();

            inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

            JCheckBox gas  = new JCheckBox("Gas");
            JCheckBox hybrid  = new JCheckBox("Hybrid");
            JCheckBox electric  = new JCheckBox("Electric");

            JCheckBox sedan  = new JCheckBox("Sedan");
            JCheckBox suv  = new JCheckBox("SUV");
            JCheckBox coupe  = new JCheckBox("Coupe");

            JCheckBox white  = new JCheckBox("White");
            JCheckBox gray  = new JCheckBox("Gray");
            JCheckBox black  = new JCheckBox("Black");

            enginePanel.add(gas);
            enginePanel.add(hybrid);
            enginePanel.add(electric);

            framePanel.add(sedan);
            framePanel.add(suv);
            framePanel.add(coupe);

            colorPanel.add(white);
            colorPanel.add(gray);
            colorPanel.add(black);

            JLabel label1 = new JLabel("Engines");
            JLabel label2 = new JLabel("Frames");
            JLabel label3 = new JLabel("Colors");
            JLabel label4 = new JLabel("Quantity");
            label1.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            label2.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            label3.setAlignmentX(JLabel.CENTER_ALIGNMENT);
            label4.setAlignmentX(JLabel.CENTER_ALIGNMENT);

            JTextField minField = new JTextField(5);
            JTextField maxField = new JTextField(5);
            qtyPanel.add(new JLabel("Min: "));
            qtyPanel.add(minField);
            qtyPanel.add(Box.createHorizontalStrut(15));
            qtyPanel.add(new JLabel("Max: "));
            qtyPanel.add(maxField);

            inputPanel.add(label1);
            inputPanel.add(enginePanel);
            inputPanel.add(Box.createVerticalStrut(20));
            inputPanel.add(label2);
            inputPanel.add(framePanel);
            inputPanel.add(Box.createVerticalStrut(20));
            inputPanel.add(label3);
            inputPanel.add(colorPanel);
            inputPanel.add(Box.createVerticalStrut(20));
            inputPanel.add(label4);
            inputPanel.add(qtyPanel);

            if(JOptionPane.showOptionDialog(null, inputPanel, "Filter orders", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null) == JOptionPane.OK_OPTION) {
                
                String[] values = new String[11];
                
                if(gas.isSelected()) {
                    values[0] = "t";
                }
                else {
                    values[0] = "f";
                }
                if(hybrid.isSelected()) {
                    values[1] = "t";
                }
                else {
                    values[1] = "f";
                }
                if(electric.isSelected()) {
                    values[2] = "t";
                }
                else {
                    values[2] = "f";
                }

                if(values[0].equals("f") && values[1].equals("f") && values[2].equals("f")) {
                    values[0] = "t";
                    values[1] = "t";
                    values[2] = "t";
                }


                if(sedan.isSelected()) {
                    values[3] = "t";
                }
                else {
                    values[3] = "f";
                }
                if(suv.isSelected()) {
                    values[4] = "t";
                }
                else {
                    values[4] = "f";
                }
                if(coupe.isSelected()) {
                    values[5] = "t";
                }
                else {
                    values[5] = "f";
                }

                if(values[3].equals("f") && values[4].equals("f") && values[5].equals("f")) {
                    values[3] = "t";
                    values[4] = "t";
                    values[5] = "t";
                }


                if(white.isSelected()) {
                    values[6] = "t";
                }
                else {
                    values[6] = "f";
                }
                if(gray.isSelected()) {
                    values[7] = "t";
                }
                else {
                    values[7] = "f";
                }
                if(black.isSelected()) {
                    values[8] = "t";
                }
                else {
                    values[8] = "f";
                }

                if(values[6].equals("f") && values[7].equals("f") && values[8].equals("f")) {
                    values[6] = "t";
                    values[7] = "t";
                    values[8] = "t";
                }
                
                try{
                    if(minField.getText() != null && !minField.getText().equals("")) {
                        values[9] = minField.getText();
                    }
                    else {
                        values[9] = "f";
                    }
                    if(maxField.getText() != null && !maxField.getText().equals("")) {
                        values[10] = maxField.getText();
                    }
                    else {
                        values[10] = "f";
                    }
                    
                    //ellenőrizni hogy be lett-e állítva szűrés
                    
                    int i = 0;
                    while(i < 9 && values[i].equals("t")) {
                        i++;
                    }
                    
                    if(!(i == 9 && values[9].equals("f") && values[10].equals("f"))) {
                        factory.filter(values);
                    }
                }
                catch(Exception e) {
                    JOptionPane.showMessageDialog(null, "Invalid inputs!", "Warning", JOptionPane.WARNING_MESSAGE); 
                }
            }   
        }
        else {
            factory.resetFilter();
        }
    }
    
    public static void changeData(int mode, int row) {
        
        if(!factory.getIsFiltered()) {
            gantt.reset();
            boolean change1 = false, change2 = false;

            if(mode == 1) { //add

                JPanel inputPanel = new JPanel();
                JPanel enginePanel = new JPanel();
                JPanel framePanel = new JPanel();
                JPanel colorPanel = new JPanel();
                JPanel qtyPanel = new JPanel();
                inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

                JRadioButton gas = new JRadioButton("Gas");
                JRadioButton hybrid = new JRadioButton("Hybrid");
                JRadioButton electric = new JRadioButton("Electric");

                JRadioButton sedan = new JRadioButton("Sedan");
                JRadioButton suv = new JRadioButton("SUV");
                JRadioButton coupe = new JRadioButton("Coupe");

                JRadioButton white = new JRadioButton("White");
                JRadioButton gray = new JRadioButton("Gray");
                JRadioButton black = new JRadioButton("Black");

                ButtonGroup engines = new ButtonGroup();
                ButtonGroup frames = new ButtonGroup();
                ButtonGroup colors = new ButtonGroup();

                engines.add(gas);
                engines.add(hybrid);
                engines.add(electric);
                enginePanel.add(gas);
                enginePanel.add(hybrid);
                enginePanel.add(electric);

                frames.add(sedan);
                frames.add(suv);
                frames.add(coupe);
                framePanel.add(sedan);
                framePanel.add(suv);
                framePanel.add(coupe);

                colors.add(white);
                colors.add(gray);
                colors.add(black);
                colorPanel.add(white);
                colorPanel.add(gray);
                colorPanel.add(black);

                JLabel label1 = new JLabel("Engines");
                JLabel label2 = new JLabel("Frames");
                JLabel label3 = new JLabel("Colors");
                JLabel label4 = new JLabel("Quantity");
                label1.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                label2.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                label3.setAlignmentX(JLabel.CENTER_ALIGNMENT);
                label4.setAlignmentX(JLabel.CENTER_ALIGNMENT);

                JTextField needField = new JTextField(5);
                JTextField doneField = new JTextField(5);
                qtyPanel.add(new JLabel("Need: "));
                qtyPanel.add(needField);
                qtyPanel.add(Box.createHorizontalStrut(15));
                qtyPanel.add(new JLabel("Done: "));
                qtyPanel.add(doneField);

                inputPanel.add(label1);
                inputPanel.add(enginePanel);
                inputPanel.add(Box.createVerticalStrut(20));
                inputPanel.add(label2);
                inputPanel.add(framePanel);
                inputPanel.add(Box.createVerticalStrut(20));
                inputPanel.add(label3);
                inputPanel.add(colorPanel);
                inputPanel.add(Box.createVerticalStrut(20));
                inputPanel.add(label4);
                inputPanel.add(qtyPanel);

                boolean correct = true;
                String values[] = new String[5];

                if(JOptionPane.showOptionDialog(null, inputPanel, "Enter new order", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null) == JOptionPane.OK_OPTION) {
                    if(gas.isSelected()) {
                        values[0] = "gas";
                    }
                    else if(hybrid.isSelected()) {
                        values[0] = "hybrid";
                    }
                    else if(electric.isSelected()) {
                        values[0] = "electric";
                    }
                    else {
                        correct = false;
                    }

                    if(correct) {
                        if(sedan.isSelected()) {
                            values[1] = "sedan";
                        }
                        else if(suv.isSelected()) {
                            values[1] = "suv";
                        }
                        else if(coupe.isSelected()) {
                            values[1] = "coupe";
                        }
                        else {
                            correct = false;
                        }
                    }

                    if(correct) {
                        if(white.isSelected()) {
                            values[2] = "white";
                        }
                        else if(gray.isSelected()) {
                            values[2] = "gray";
                        }
                        else if(black.isSelected()) {
                            values[2] = "black";
                        }
                        else {
                            correct = false;
                        }
                    }

                    if(correct) {

                        try{
                            if(needField.getText() != null && !(needField.getText().isEmpty()) && Integer.parseInt(needField.getText()) != 0) {
                                values[3] = needField.getText();

                                if(doneField.getText() != null && !(doneField.getText().isEmpty())) {
                                    values[4] = doneField.getText();

                                    if(values[3].matches("\\d*\\.?\\d+") && values[4].matches("\\d*\\.?\\d+")) {
                                        if(Integer.parseInt(values[3]) >= Integer.parseInt(values[4])) {
                                            change1 = true;
                                            factory.newItem(values);
                                        }
                                        else {
                                            JOptionPane.showMessageDialog(null, "QtyDone cannot be larger than QtyNeed!", "Warning", JOptionPane.WARNING_MESSAGE); 
                                        }
                                    }
                                }
                                else {
                                    correct = false;
                                }
                            }
                            else {
                                correct = false;
                            }
                        }
                        catch(Exception e) {
                            JOptionPane.showMessageDialog(null, "Invalid inputs!", "Warning", JOptionPane.WARNING_MESSAGE); 
                        }
                    }

                    if(!correct) {
                       JOptionPane.showMessageDialog(null, "Invalid inputs!", "Warning", JOptionPane.WARNING_MESSAGE); 
                    }
                }
            }
            else if(mode == 2) { //delete
                factory.deleteItem(row);
                change1 = true;
            }
            else if(mode == 3) { //QtyNeed
                String input = JOptionPane.showInputDialog("New Quantity Need", factory.getProductionPlan().get(row).getQtyNeed());
                if(input != null) {           
                    try{
                        if(input.matches("\\d*\\.?\\d+")) {
                            
                            int value = Integer.parseInt(input);
                            
                            if(value > 0) {
                                factory.setQuantity(1, row, value);
                                change2 = true;
                            }
                            else {
                                JOptionPane.showMessageDialog(null, "The minimum QtyNeed must be 1!", "Warning", JOptionPane.WARNING_MESSAGE);
                            }
                        } 
                    }
                    catch(Exception e) {
                        JOptionPane.showMessageDialog(null, "Invalid inputs!", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
            else if(mode == 4) { //QtyDone
                String input = JOptionPane.showInputDialog("New Quantity Done", factory.getProductionPlan().get(row).getQtyDone());
                if(input != null) {
                    try{
                        if(input.matches("\\d*\\.?\\d+")) {
                            int value = Integer.parseInt(input);
                            factory.setQuantity(0, row, value);
                            change2 = true;
                        }
                    }
                    catch(Exception e) {
                        JOptionPane.showMessageDialog(null, "Invalid input!", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
            else if(mode == 5) { //moveUp
                if(factory.move(1, row)) {
                    change2 = true;
                }
                else {
                    JOptionPane.showMessageDialog(null, "The item is already on the top!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
            else if(mode == 6) { //moveDown
                if(factory.move(0, row)) {
                    change2 = true;
                }
                else {
                    JOptionPane.showMessageDialog(null, "The item is already on the bottom!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
            else if(mode == 7) { //moveSpecific
                String input = JOptionPane.showInputDialog("Move item to specific location", row + 1);
                if(input != null) {

                    try{
                        if(input.matches("\\d*\\.?\\d+") && Integer.parseInt(input) > 0 && Integer.parseInt(input) <= PPV.size()) {
                            int value = Integer.parseInt(input);
                            factory.moveSpecific(value - 1, row);
                            change2 = true;
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Invalid input!", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                    catch(Exception e) {
                        JOptionPane.showMessageDialog(null, "Invalid input!", "Warning", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }

            //change1 = true ha törlünk vagy hozzáadunk új elemet
            if(change1) {
                if(JOptionPane.showConfirmDialog(frame, "Do you want to sort the data?", "Option", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    factory.smartSort();
                }
                else {
                    factory.resetProductionPlan();
                }
                factory.createChart();
                fill();
            }
            //change2 = true ha mozgatunk vagy átírunk elemet
            else if(change2) {
                if(!factory.getIsSorted()) {
                    factory.resetProductionPlan();
                }
                factory.createChart();
                fill();
            }
            //nem történik adat módosítás
            else {
                if(factory.getIsSorted()) {
                    factory.smartSort();
                }
                else {
                    factory.resetProductionPlan();
                }
                factory.createChart();
            }
            render();
            tableGUI.clearSelection();
        }
        else {
            JOptionPane.showMessageDialog(null, "Remove filter first!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public static void filterWarning() {
        JOptionPane.showMessageDialog(null, "Order(s) not found!", "Warning", JOptionPane.WARNING_MESSAGE);
    }
}
