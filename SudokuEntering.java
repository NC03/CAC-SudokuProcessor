
import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class SudokuEntering extends JFrame {
    private int[][] board;
    private int[][][] coordinates;
    private int selectedIdx = -1;
    private String[] buttonText = { "Solve", "Cancel" };
    private int[][][] buttonCoordinates;

    public SudokuEntering(int[][] board) {
        super("Sudoku Solver");
        this.board = board;
        System.out.println("SudokuEntering");
        setup();
    }

    public SudokuEntering() {
        super("Sudoku Solver");
        board = new int[9][9];
        System.out.println("SudokuEntering");
        setup();
    }

    public void setup() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setSize(600, 400);
        addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {

            }

            public void mouseDragged(MouseEvent e) {

            }
        });
        addMouseListener(new MouseListener() {
            public void mouseExited(MouseEvent e) {

            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseClicked(MouseEvent e) {
                int idx = getIdx(e.getX(), e.getY());
                if (idx != -1) {
                    selectedIdx = idx;
                } else {
                    selectedIdx = -1;
                }
                int btnIdx = getButtonIdx(e.getX(), e.getY());
                if (btnIdx != -1) {
                    switch (buttonText[btnIdx]) {
                    case "Solve":
                        try {
                            int[][] solvedBoard = SudokuSolver.solveSudoku(board);
                            ImageDisplayer id = new ImageDisplayer(solvedBoard,
                                    CreateFinalBoard.createBoard(solvedBoard, SudokuGraphicsProcessor.blankGrids));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        dispose();
                        break;
                    case "Cancel":
                        dispose();
                        break;
                    }
                }
                repaint();
            }

            public void mouseReleased(MouseEvent e) {

            }

            public void mousePressed(MouseEvent e) {

            }
        });
        addKeyListener(new KeyListener() {
            public void keyReleased(KeyEvent e) {

            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 37) {
                    selectedIdx--;
                } else if (e.getKeyCode() == 38) {
                    selectedIdx -= 9;
                } else if (e.getKeyCode() == 39) {
                    selectedIdx++;
                } else if (e.getKeyCode() == 40) {
                    selectedIdx += 9;
                }
                if (selectedIdx >= 81) {
                    selectedIdx -= 81;
                }
                if (selectedIdx < 0) {
                    selectedIdx += 81;
                }
                repaint();
            }

            public void keyTyped(KeyEvent e) {
                if (selectedIdx != -1) {
                    int i = selectedIdx / 9;
                    int j = selectedIdx % 9;
                    try{
                    board[i][j] = Integer.parseInt("" + e.getKeyChar());
                    }catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }
                repaint();
            }
        });
        repaint();
    }

    public int getIdx(int x, int y) {
        for (int i = 0; i < coordinates.length; i++) {
            int xMin = Math.min(coordinates[i][0][0], coordinates[i][1][0]);
            int xMax = Math.max(coordinates[i][0][0], coordinates[i][1][0]);
            int yMin = Math.min(coordinates[i][0][1], coordinates[i][1][1]);
            int yMax = Math.max(coordinates[i][0][1], coordinates[i][1][1]);
            if (x >= xMin && x <= xMax && y >= yMin && y <= yMax) {
                return i;
            }
        }
        return -1;
    }

    public void paint(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        paintGrid(g);
    }

    public void paintGrid(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        int dim = Math.min(width, height) / 2 / 9;
        int fontsize = GUI.findFontSize(dim, dim, "0", g, "Arial", Font.PLAIN);
        for (int i = 0; i < buttonText.length; i++) {
            int size = GUI.findFontSize(dim, dim, ""+i, g, "Arial", Font.PLAIN);
            if (size < fontsize) {
                fontsize = size;
            }
        }
        g.setFont(new Font("Arial", Font.PLAIN, fontsize));
        coordinates = new int[81][2][2];
        for (int n = 0; n < 81; n++) {
            int i = n / 9;
            int j = n % 9;
            int x = width / 2 - 4 * dim - dim / 2 + j * dim;
            int y = height / 2 - 4 * dim - dim / 2 + i * dim;
            g.setColor(new Color(255,255,255));
            g.fillRect(x,y,dim,dim);
            g.setColor(new Color(0, 0, 0));
            g.drawRect(x, y, dim, dim);
            if (board[i][j] != 0) {
                g.drawString("" + board[i][j], x+dim/2-g.getFontMetrics().stringWidth(""+board[i][j])/2, y + g.getFontMetrics().getAscent());
            }
            coordinates[n][0][0] = x;
            coordinates[n][0][1] = y;
            coordinates[n][1][0] = x + dim;
            coordinates[n][1][1] = y + dim;
            if (n == selectedIdx) {
                g.setColor(new Color(255, 180, 0));
                g.drawOval(x, y, dim, dim);
            }
        }
        drawButtons(g);
    }

    public int getButtonIdx(int x, int y) {
        for (int i = 0; i < buttonText.length; i++) {
            int xMin = Math.min(buttonCoordinates[i][0][0], buttonCoordinates[i][1][0]);
            int xMax = Math.max(buttonCoordinates[i][0][0], buttonCoordinates[i][1][0]);
            int yMin = Math.min(buttonCoordinates[i][0][1], buttonCoordinates[i][1][1]);
            int yMax = Math.max(buttonCoordinates[i][0][1], buttonCoordinates[i][1][1]);
            if (x >= xMin && x <= xMax && y >= yMin && y <= yMax) {
                return i;
            }
        }
        return -1;
    }
    public void drawButtons(Graphics g) {
        int y = getHeight() * 95 / 100;
        int btnHeight = getHeight() * 4 / 100;
        int btnWidth = getWidth() / 4;
        int fontsize = GUI.findFontSize(btnWidth, btnHeight, buttonText[0], g, "Arial", Font.PLAIN);
        for (int i = 0; i < buttonText.length; i++) {
            int size = GUI.findFontSize(btnWidth, btnHeight, buttonText[i], g, "Arial", Font.PLAIN);
            if (size < fontsize) {
                fontsize = size;
            }
        }
        g.setFont(new Font("Arial", Font.PLAIN, fontsize));
        buttonCoordinates = new int[buttonText.length][2][2];
        for (int i = 0; i < buttonText.length; i++) {
            int x = getWidth() * (i + 1) / (buttonText.length + 1);
            g.setColor(new Color(255, 255, 255));
            g.fillRect(x - btnWidth / 2, y - btnHeight / 2, btnWidth, btnHeight);
            g.setColor(new Color(0, 0, 0));
            g.drawString(buttonText[i], x - g.getFontMetrics().stringWidth(buttonText[i]) / 2,
                    y + g.getFontMetrics().getAscent() / 2);
            buttonCoordinates[i][0][0] = x - btnWidth / 2;
            buttonCoordinates[i][0][1] = y - btnHeight / 2;
            buttonCoordinates[i][1][0] = buttonCoordinates[i][0][0] + btnWidth;
            buttonCoordinates[i][1][1] = buttonCoordinates[i][0][1] + btnHeight;
        }
    }
}
