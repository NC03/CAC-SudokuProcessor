package sudokusolver;

import java.util.ArrayList;

public class SudokuSolver {

    public static int[][] boxGrid = new int[9][9];
    int width;
    int height;
    
    public static ArrayList<Integer> findBlankGrids(int[][] boxGrid) {
        
        ArrayList<Integer> blankGrids = new ArrayList<>();
        for (int y = 0; y < boxGrid.length; y++) {
            
            for (int x = 0; x < boxGrid.length; x++) {
            
                if (boxGrid[y][x] == 0) {
                    
                    blankGrids.add((y*9) + x);
                    
                }
                
            }
            
        }
        return blankGrids;
        
    }
    
    public static void print() {
        
        //Prints Grid Box
        for (int d = 0; d < boxGrid.length; d++) {
            //column
            for (int e = 0; e < boxGrid[d].length;e++) {
                
                System.out.print(boxGrid[d][e] + " ");
            }
            System.out.println();
        }
    }
    
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
    
    public static int[][] solveSudoku(int[][] bg){
        
        boxGrid = bg;
        solveSudoku();
        return boxGrid;
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
        
        return checkRow(col, num) && checkColumn(row, num) && checkBox(row, col, num);
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
        
        return isDone;
    }

}
