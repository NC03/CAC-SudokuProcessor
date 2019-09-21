/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sudokusolver;

/**
 *
 * @author akash
 */
import java.io.IOException;

public class SudokuSolver {

    public static int[][] boxGrid = new int[9][9];
    int width;
    int height;
    
//    public static void print() {
//        
//        //Prints Grid Box
//        for (int d = 0; d < boxGrid.length; d++) {
//            //column
//            for (int e = 0; e < boxGrid[d].length;e++) {
//                
//                System.out.print(boxGrid[d][e] + " ");
//            }
//            System.out.println();
//        }
//    }
    
    public static String text()
    {
        String out = "";
        for (int d = 0; d < boxGrid.length; d++) {
            //column
            for (int e = 0; e < boxGrid[d].length;e++) {
                
                out += (boxGrid[d][e] + " ");
            }
            out += "\n";
        }
        return out;
    }
    
    public SudokuSolver(SudokuGraphicsProcessor sgp)
    {
//        boxGrid = sgp.boxGridInitial;
//        int startX = sgp.startingX;
//        int startY = sgp.startingY;
//        int endX = sgp.endingX;
//        int endY = sgp.endingX;
//        
//        width = endX - startX;
//        height = endY - startY;
//        
//        solveSudoku();
    }
    
    public static void main(String[] args) throws IOException {
        
        GUI.main(null);
        
//        SudokuGraphicsProcessor sgp = new SudokuGraphicsProcessor();
//        boxGrid = sgp.boxGridInitial;
//        int startX = sgp.startingX;
//        int startY = sgp.startingY;
//        int endX = sgp.endingX;
//        int endY = sgp.endingX;
//        
//        int width = endX - startX;
//        int height = endY - startY;
//        
//        solveSudoku();
//        print();
//        sgp.createFinalImage(boxGrid, width, height);
        
    }
    
    public static boolean checkRow(int col, int num){
        
        for (int i = 0; i < boxGrid.length; i++) {
            
            if (boxGrid[i][col] == num) {
                
                return false;
                
            }
        
        }
        
        return true;
    }
    
    public static boolean checkColumn(int row, int num){
        
        for (int i = 0; i < boxGrid.length; i++) {
            
            if (boxGrid[row][i] == num) {
                
                return false;
                
            }
        
        }
        
        return true;
    }
    
    public static boolean checkBox(int row, int col, int num){
        
        int startRow = row - (row % 3);
        int startCol = col - (col % 3);
        for (int i = 0; i < 3; i++) {
            
            for (int j = 0; j < 3; j++) {
            
                if (boxGrid[startRow + i][startCol + j] == num) {
                
                    return false;
                
                }
            
            }
            
        }
        
        return true;
    }
    
    public static boolean doesWork(int row, int col, int num){
        
        if (checkRow(col, num) && checkColumn(row, num) && checkBox(row, col, num)) {
            
            return true;
            
        }
        return false;
    }
    
    public static boolean solveSudoku(){
        
        boolean isDone = false;
        
        for (int i = 0; i < boxGrid.length; i++) {
            
            for (int j = 0; j < boxGrid[i].length;j++) {
                
                if (boxGrid[i][j] == 0) {
                    
                    for (int n = 1; n <= 9; n++) {
                        
                        if (doesWork(i, j, n)) {
                            
                            boxGrid[i][j] = n;
                            if (solveSudoku()) {
                                
                                return true;
                            } else {
                                
                                boxGrid[i][j] = 0;
                                
                            }
                            
                        }
                        
                    }
                    return false;
                    
                }
                
            }
            
        }
        isDone = true;
        for (int i = 0; i < boxGrid.length; i++) {
            
            for (int j = 0; j < boxGrid[i].length;j++) {
            
                if (boxGrid[i][j] == 0) {
                    
                    isDone = false;
                    
                }
                
            }
        }
        
        if (isDone == true) {
            
            return true;
            
        }
        
        return false;
    }

}
