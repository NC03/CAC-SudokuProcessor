import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class GUI extends JFrame {
    private String state = "mainScreen";
    public static void main(String[] args) {
        GUI g = new GUI();
        while (true) {
            try {
                Thread.sleep(1000);
                g.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // try {
        // String fileName = "board.png";
        // File f = new File(fileName);
        // BufferedImage bi = ImageIO.read(f);
        // SudokuGraphicsProcessor sgp = new SudokuGraphicsProcessor(bi);
        // int[][] board = sgp.getBoard();
        // for(int[] row : board)
        // {
        // for(int e: row)
        // {
        // System.out.print(e);
        // }
        // System.out.println();
        // }
        // SudokuSolver ss = new SudokuSolver(board);
        // int[][] solvedBoard = ss.getBoard();
        // for(int[] row : solvedBoard)
        // {
        // for(int e: row)
        // {
        // System.out.print(e);
        // }
        // System.out.println();
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

    public GUI() {
        super("Sudoku Solver");
        

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(200, 200);
    }

    public void paint(Graphics g) {
        if(state.equals("mainScreen"))
        {
            g.setColor(new Color(0,0,0));
            g.fillRect(100, 100, 100, 100);
        }
    }
}
