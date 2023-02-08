/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package production;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicPanelUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author riharada
 */
public class TableGUI extends JPanel {
    
    private JTable table;
    private Vector<String> header;
    private int selectedRow;
    private Vector<Vector> tableSource;
    
    public TableGUI(Vector<Vector> data) {
        
        header = new Vector<String>();
        header.add("");
        header.add("OrderID");
        header.add("Engine");
        header.add("Frame");
        header.add("Color");
        header.add("Modelname");
        header.add("QtyNeed");
        header.add("QtyDone");
        
        tableSource = data;
        
        DefaultTableModel tableModel = new DefaultTableModel(data, header);
        
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);

        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setPreferredScrollableViewportSize(new Dimension(440, 600));
        table.setDefaultEditor(Object.class, null);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(60);
        table.getColumnModel().getColumn(4).setPreferredWidth(60);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);
        table.getColumnModel().getColumn(6).setPreferredWidth(70);
        table.getColumnModel().getColumn(7).setPreferredWidth(70);
        
        for(int i = 0; i < header.size(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }
        
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent event) {
                selectedRow = table.getSelectedRow();
            }
        });
        
        JPopupMenu popupMenu = new JPopupMenu();
        
        JMenuItem menuitem1 = new JMenuItem("New");
        popupMenu.add(menuitem1);
        menuitem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                FactoryGUI.changeData(1, 0);
            }
        });
        
        JMenuItem menuitem2 = new JMenuItem("Delete");
        popupMenu.add(menuitem2);
        menuitem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(selectedRow >= 0 && selectedRow < tableSource.size()) {
                    if(JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the selected data?", "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        FactoryGUI.changeData(2, selectedRow);
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "You must choose a row for this action!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        popupMenu.addSeparator();
        
        JMenuItem menuitem3 = new JMenuItem("Change QtyNeed");
        popupMenu.add(menuitem3);
        menuitem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(selectedRow >= 0 && selectedRow < tableSource.size()) {
                    FactoryGUI.changeData(3, selectedRow);
                }
                else {
                    JOptionPane.showMessageDialog(null, "You must choose a row for this action!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        JMenuItem menuitem4 = new JMenuItem("Change QtyDone");
        popupMenu.add(menuitem4);
        menuitem4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(selectedRow >= 0 && selectedRow < tableSource.size()) {
                    FactoryGUI.changeData(4, selectedRow);
                }
                else {
                    JOptionPane.showMessageDialog(null, "You must choose a row for this action!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        popupMenu.addSeparator();
        
        JMenu moveMenu = new JMenu("Move..");
        popupMenu.add(moveMenu);
        
        JMenuItem menuitem5 = new JMenuItem("Up");
        moveMenu.add(menuitem5);
        menuitem5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(selectedRow >= 0 && selectedRow < tableSource.size()) {
                    FactoryGUI.changeData(5, selectedRow);
                }
                else {
                    JOptionPane.showMessageDialog(null, "You must choose a row for this action!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        JMenuItem menuitem6 = new JMenuItem("Down");
        moveMenu.add(menuitem6);
        menuitem6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(selectedRow >= 0 && selectedRow < tableSource.size()) {
                    FactoryGUI.changeData(6, selectedRow);
                }
                else {
                    JOptionPane.showMessageDialog(null, "You must choose a row for this action!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        JMenuItem menuitem7 = new JMenuItem("Specific location");
        moveMenu.add(menuitem7);
        menuitem7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(selectedRow >= 0 && selectedRow < tableSource.size()) {
                    FactoryGUI.changeData(7, selectedRow);
                }
                else {
                    JOptionPane.showMessageDialog(null, "You must choose a row for this action!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        table.setComponentPopupMenu(popupMenu);
        
        UIManager.put("Table.background", new ColorUIResource(new java.awt.Color(180, 180, 180)));
        UIManager.put("Table.gridColor", new ColorUIResource(new java.awt.Color(70, 70, 70)));
        UIManager.put("Table.scrollPaneBorder", new ColorUIResource(new java.awt.Color(70, 70, 70)));
        UIManager.put("TableHeader.background", new ColorUIResource(new java.awt.Color(100, 100, 100)));
        UIManager.put("TableHeader.font", new Font("Arial Black", Font.PLAIN, 12));
        UIManager.put("TableHeader.foreground", new ColorUIResource(java.awt.Color.WHITE));
        UIManager.put("TableHeader.cellBorder", new ColorUIResource(new java.awt.Color(70, 70, 70)));
        
        table.setUI(new BasicTableUI());
        table.getTableHeader().setUI(new BasicTableHeaderUI());
        table.setRowHeight(30);
        
        JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        UIManager.put("ScrollBar.thumb", new ColorUIResource(new java.awt.Color(100, 100, 100)));
        UIManager.put("ScrollBar.track", new ColorUIResource(new java.awt.Color(70, 70, 70)));
        UIManager.put("ScrollBar.thumbShadow", new ColorUIResource(new java.awt.Color(70, 70, 70)));
        UIManager.put("ScrollPane.background", new ColorUIResource(new java.awt.Color(70, 70, 70)));
        UIManager.put("ScrollPane.border", new ColorUIResource(new java.awt.Color(70, 70, 70)));
        
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
        });
        
        scrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
        });
        
        scrollPane.setUI(new BasicScrollPaneUI());
        
        this.setBackground(new java.awt.Color(70, 70, 70));
        this.setUI(new BasicPanelUI());
        this.add(scrollPane);
    }
    
    public void clearSelection() {
        try {
            table.clearSelection();
        }
        catch(ArrayIndexOutOfBoundsException e) {
            table.setRowSelectionInterval(0, 0);
        }
        table.updateUI();
    }
}
