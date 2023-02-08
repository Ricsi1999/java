/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package abalone;

import java.awt.Color;
import java.awt.Point;
import java.util.Random;

/**
 *
 * @author RICSI
 * @version 1.0
 * @since 2019. 12. 10.
 */
public class Board {
    
    /**
     * @param board is a two dimensional array which is the board of the game
     * @param boardSize is an integer which is the length of the columns and the rows
     * @param black is a variable that equals to the number of black tiles
     * @param white is a variable that equals to the number of white tiles
     * @param none is a variable that equals to the number empty spaces
     * @param choosenColor  is a random generated integer in the range of 1-3
     * @param round is an integer which equals to the remaining number of rounds
     */
    
    private Field[][] board;
    private int boardSize;
    private int black, white, none;
    public int choosenColor;
    public int round;
    
    //Creates field in every cell of the board
    public Board(int boardSize) {
        this.boardSize = boardSize;
        board = new Field[this.boardSize][this.boardSize];
        for (int i = 0; i < this.boardSize; ++i) {
            for (int j = 0; j < this.boardSize; ++j) {
                board[i][j] = new Field();
            }
        }
    }
    
    /**
     * Generates a random integer between 1 and 3 (1 is none, 2 is black, 3 is white).
     * If the generated number equals to a ran out color, it generates again until it is a different number.
    */
    public void getRandInt() {
        Random r = new Random();
        int result = r.nextInt(4-1) + 1;
        if(result == 1 && none > 0) {
            none--;
            choosenColor = 1;  
        }
        else if(result == 2 && black > 0) {
            black--;
            choosenColor = 2;
        }
        else if(result == 3 && white > 0) {
            white--;
            choosenColor = 3;
        }
        else if(none > 0 || black > 0 || white > 0){
            getRandInt();
        }
    }
    
    //Sets a starter amount to the given variables
    public void setStart() {
        round = this.boardSize * 5;
        none = ((this.boardSize)*(this.boardSize) - (this.boardSize * 2));  //1
        black = this.boardSize; //2
        white = this.boardSize; //3
    }
    
    /**
     * Returns a field in the given position (X, Y)
     * @param x is a vertical coordinate
     * @param y is a horizontal coordinate
     */
    
    public Field get(int x, int y) {
        return board[x][y];
    }
    
    /**
     * Gets the x and y coordinates of the field
     * @param point is a point in the coordinate-system
     */
    public Field get(Point point) {
        int x = (int)point.getX();
        int y = (int)point.getY();
        return get(x, y);
    }

    /**
     * Returns the size of the board
     */
    public int getBoardSize() {
        return boardSize;
    }
    
    /**
     * Returns the status of the game
     * @param l is an integer in the range of 0-3:
     * 0: the game is not over
     * 1: team white wins
     * 2: team black wins
     * 3: draw
     */
    public int isOver() {
        int l = 0;
        int sumBlack = 0, sumWhite = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if(board[i][j].getColor() == Color.BLACK) {
                    sumBlack++;
                }
                else if(board[i][j].getColor() == Color.WHITE) {
                    sumWhite++;
                }
            }
        }
        if(--round == 0) {
            if(sumWhite > sumBlack) {
                l = 1; // White wins
            } else if(sumBlack > sumWhite) {
                l = 2; // Black wins
            } else {
                l = 3; // Draw
            }
        } else {
            if(sumWhite == 0 && sumBlack > sumWhite) {
                l = 2;
            } else if (sumBlack == 0 && sumWhite > sumBlack) {
                l = 1;
            }
        }
        if(l > 0) {
            round = 0;
        }
        return l;
    }
}
