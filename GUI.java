import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;

public class GUI {
    public static void main(String[] args) {
        GUI();

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

    public static void GUI() {
        JFrame frame = new JFrame("Sudoku Solver");
        JPanel panel = new JPanel();

        JButton chooseFileBtn = new JButton("Choose a File");
        chooseFileBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser fc = new JFileChooser();
                    if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        File f = fc.getSelectedFile();
                        BufferedImage bi = ImageIO.read(f);
                        SudokuGraphicsProcessor sgp = new SudokuGraphicsProcessor(bi);
                        int[][] board = sgp.getBoard();
                        SudokuSolver ss = new SudokuSolver(board);
                        System.out.println(ss);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        panel.add(chooseFileBtn);

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(200, 200);
    }
}
