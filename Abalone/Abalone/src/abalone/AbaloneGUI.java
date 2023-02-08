/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abalone;

/**
 *
 * @author RICSI
 * @version 1.0
 * @since 2019. 12. 10.
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class AbaloneGUI {
    
    /**This program creates the surface of Abalon game
     * There are two main options in the menu:
     * 1th is generating a new board, the user can choose between 3x3, 4x4, and 6x6 sizes
     * 2th is a quit from the program
     * @param frame is the frame of the window
     * @param boardGUI is the board model
     * @param INITIAL_BOARD_SIZE is the starter size of the board
     */
    private JFrame frame;
    private BoardGUI boardGUI;

    private final int INITIAL_BOARD_SIZE = 3;
    
    public AbaloneGUI() {
        frame = new JFrame("Abalone");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        boardGUI = new BoardGUI(INITIAL_BOARD_SIZE);
        frame.getContentPane().add(boardGUI.getBoardPanel(), BorderLayout.NORTH);
        frame.getContentPane().add(boardGUI.getRoundLabel(), BorderLayout.CENTER);
        frame.getContentPane().add(boardGUI.getTeamLabel(), BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);
        JMenu newMenu = new JMenu("New");
        gameMenu.add(newMenu);
        int[] boardSizes = new int[]{3, 4, 6};
        for (int boardSize : boardSizes) {
            JMenuItem sizeMenuItem = new JMenuItem(boardSize + "x" + boardSize);
            newMenu.add(sizeMenuItem);
            sizeMenuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.getContentPane().remove(boardGUI.getBoardPanel());
                    frame.getContentPane().remove(boardGUI.getRoundLabel());
                    frame.getContentPane().remove(boardGUI.getTeamLabel());
                    boardGUI = new BoardGUI(boardSize);
                    frame.getContentPane().add(boardGUI.getBoardPanel(),
                            BorderLayout.NORTH);
                    frame.getContentPane().add(boardGUI.getRoundLabel(), BorderLayout.CENTER);
                    frame.getContentPane().add(boardGUI.getTeamLabel(), BorderLayout.SOUTH);
                    frame.pack();
                }     
            });
        }
        
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        gameMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
        frame.pack();
        frame.setVisible(true);
    }
}
