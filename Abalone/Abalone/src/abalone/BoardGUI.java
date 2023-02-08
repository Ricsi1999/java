/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abalone;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author RICSI
 * @version 1.0
 * @since 2019. 12. 10.
 */
public class BoardGUI {
    
    /**
     * This program fills the board with buttons, and sheets with game status
     * @param buttons is a two dimensional array which includes buttons
     * @param board is a Board type variable
     * @param boardPanel is a panel with all the buttons
     * @param roundLabel is a sheet that shows the number of rounds
     * @param teamLabel is a sheet that shows which team has to move
     * @param currentColor is a Color type variable that can be Black or White
     * @param roundsLeft is an integer that shows how many rounds left
     * @param clickCounter is zero if the player is before panel selection, and one if the player has already choosen
     *      a place to move from, and has to decide where to move
     * @param prevX holds the starter position's X coordinate
     * @param prevY holds the startes position's Y coordinate
     */
    private JButton[][] buttons;
    private Board board;
    private JPanel boardPanel; 
    private JLabel roundLabel;
    private JLabel teamLabel;
    
    private Color currentColor;
    private int roundsLeft;
    
    private int clickCounter;
    private int prevX;
    private int prevY;

    public BoardGUI(int boardSize) {
        board = new Board(boardSize);
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(board.getBoardSize(), board.getBoardSize()));
        buttons = new JButton[board.getBoardSize()][board.getBoardSize()];
        board.setStart();
        clickCounter = 0;
        for(int i = 0; i < board.getBoardSize(); i++) {
            for (int j = 0; j < board.getBoardSize(); j++) {
                JButton button = new JButton();
                board.getRandInt();
                switch(board.choosenColor){
                    case 1:
                        button.setBackground(Color.GRAY);
                        board.get(i, j).setColor(Color.GRAY);
                        break;
                    case 2:
                        button.setBackground(Color.BLACK);
                        board.get(i, j).setColor(Color.BLACK);
                        break;
                    case 3:
                        button.setBackground(Color.WHITE);
                        board.get(i, j).setColor(Color.WHITE);
                        break;
                    default:
                }
                button.setOpaque(true);
                button.addActionListener(new ButtonListener(i, j));
                button.setPreferredSize(new Dimension(60, 60));
                buttons[i][j] = button;
                boardPanel.add(button);
            }
        }
        currentColor = Color.WHITE;
        
        roundLabel = new JLabel(" ");
        roundLabel.setHorizontalAlignment(JLabel.LEFT);
        roundsLeft = board.round;
        roundLabel.setText(roundsLeft + " rounds left");
        
        teamLabel = new JLabel(" ");
        teamLabel.setHorizontalAlignment(JLabel.LEFT);
        teamLabel.setText(getCurrentColor() + "'s turn");
    }
    
    /**
     * Returns the boardPanel 
     */
    public JPanel getBoardPanel() {
        return boardPanel;
    }
    
    /**
     * Transforms the color names to string in order to function the team label 
     */
    public String getCurrentColor() {
        if(currentColor == Color.WHITE) {
            return "White";
        } else {
            return "Black";
        }
    }
    
    class ButtonListener implements ActionListener {

        private int x, y;

        public ButtonListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * This program implements the method when the mouse button is down on a button
         * The current player can choose a startes position, then choose a direction to move by pressing an adjacent panel
         * @param e is the mouse click event on buttons 
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if(board.get(x, y).getColor().equals(currentColor) && clickCounter == 0 && board.round > 0) {
                ++clickCounter;
                prevX = x;
                prevY = y;
                buttons[x][y].setBackground(Color.YELLOW);
            } else if(clickCounter > 0 && !(x == prevX && y == prevY) && (Math.abs(x - prevX) + Math.abs(y - prevY) < 2)) {
                buttons[prevX][prevY].setBackground(currentColor);    
                clickCounter = 0;
                
                if(x < prevX) {
                    moveUp();
                } else if(x > prevX) {
                    moveDown();
                } else if(y > prevY) {
                    moveRight();
                } else if(y < prevY) {
                    moveLeft();
                }
                    
                if(currentColor == Color.WHITE) {
                    currentColor = Color.BLACK;
                } else if(currentColor == Color.BLACK) {
                    currentColor = Color.WHITE;
                }
                    
                int over = board.isOver();
                if (over == 1) {
                    JOptionPane.showMessageDialog(boardPanel, "White wins!");
                } else if(over == 2) {
                    JOptionPane.showMessageDialog(boardPanel, "Black wins!");
                } else if(over == 3) {
                    JOptionPane.showMessageDialog(boardPanel, "Draw!");
                }
                    
                roundsLeft = board.round;
                roundLabel.setText(roundsLeft + " rounds left");
                teamLabel.setText(getCurrentColor() + "'s turn");
            }
        }
        
        /**
         * This method moves right every panel from the given place
         */
        public void moveRight() {
            int limit = board.getBoardSize()-1;
            for(int i = board.getBoardSize()-1; i > prevY; i--) {                
                if(board.get(prevX, i).getColor() == Color.GRAY) {
                    limit = i;
                }
            }
            for(int i = limit; i > prevY; i--) {
                if(board.get(prevX, i-1).getColor() != Color.GRAY) {
                    board.get(prevX, i).setColor(board.get(prevX, i-1).getColor());
                    buttons[prevX][i].setBackground(board.get(prevX, i-1).getColor());
                }
            }
            board.get(prevX, prevY).setColor(Color.GRAY);
            buttons[prevX][prevY].setBackground(Color.GRAY);
        }
        
        /**
         * This method moves up every panel from the given place
         */
        public void moveUp() {
            int limit = 0;
            for(int i = 0; i < prevX; i++) {
                if(board.get(i, prevY).getColor() == Color.GRAY) {
                    limit = i;
                }
            }
            for(int i = limit; i < prevX; i++) {
                if(board.get(i+1, prevY).getColor() != Color.GRAY) {
                    board.get(i, prevY).setColor(board.get(i+1, prevY).getColor());
                    buttons[i][prevY].setBackground(board.get(i+1, prevY).getColor());
                } 
            }
            board.get(prevX, prevY).setColor(Color.GRAY);
            buttons[prevX][prevY].setBackground(Color.GRAY);
        }
    
        /**
         * This method moves left every panel from the given place
         */
        public void moveLeft() {
            int limit = 0;
            for(int i = 0; i < prevY; i++) {
                if(board.get(prevX, i).getColor() == Color.GRAY) {
                    limit = i;
                }
            }
            for(int i = limit; i < prevY; i++) {
                if(board.get(prevX, i+1).getColor() != Color.GRAY) {
                    board.get(prevX, i).setColor(board.get(prevX, i+1).getColor());
                    buttons[prevX][i].setBackground(board.get(prevX, i+1).getColor());
                }
            }
            board.get(prevX, prevY).setColor(Color.GRAY);
            buttons[prevX][prevY].setBackground(Color.GRAY);
        }
        
        /**
         * This method moves down every panel from the given place
         */
        public void moveDown() {
            int limit = board.getBoardSize()-1;
            for(int i = board.getBoardSize()-1; i > prevX; i--) {
                if(board.get(i, prevY).getColor() == Color.GRAY) {
                    limit = i;
                }
            }
            for(int i = limit; i > prevX; i--) {
                if(board.get(i-1, prevY).getColor() != Color.GRAY) {
                    board.get(i, prevY).setColor(board.get(i-1, prevY).getColor());
                    buttons[i][prevY].setBackground(board.get(i-1, prevY).getColor());
                }
                
            }
            board.get(prevX, prevY).setColor(Color.GRAY);
            buttons[prevX][prevY].setBackground(Color.GRAY);
        }
    }
    
    //returns roundLabel
    public JLabel getRoundLabel() {
        return roundLabel;
    }
    
    //returns teamLabel
    public JLabel getTeamLabel() {
        return teamLabel;
    }    
}
