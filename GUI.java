import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class GUI {
    public static void main(String[] args) {
        try {
            String fileName = "board.png";
            File f = new File(fileName);
            BufferedImage bi = ImageIO.read(f);
            SudokuGraphicsProcessor sgp = new SudokuGraphicsProcessor(bi);
            int[][] board = sgp.getBoard();
            for(int[] row : board)
            {
                for(int e: row)
                {
                    System.out.print(e);
                }
                System.out.println();
            }
            SudokuSolver ss = new SudokuSolver(board);
            int[][] solvedBoard = ss.getBoard();
            for(int[] row : solvedBoard)
            {
                for(int e: row)
                {
                    System.out.print(e);
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
