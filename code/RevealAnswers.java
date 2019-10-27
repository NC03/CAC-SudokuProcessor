import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * An interface with the user to edit the sudoku board values
 * 
 * @author Nick
 * @version 1.0
 */
public class RevealAnswers extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int[][] board;
    private int[][][] coordinates;
    private String[] buttonText = { "Show All", "Quit" };
    private int[][][] buttonCoordinates;
    private boolean[][] show = new boolean[9][9];
    private ArrayList<Integer> alreadyInputted;

    public RevealAnswers(int[][] board, ArrayList<Integer> alreadyInputted) {
        super("Sudoku Solver");
        this.board = board;
        this.alreadyInputted = alreadyInputted;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                show[i][j] = true;
            }
        }
        for (int n : alreadyInputted) {
            show[n / 9][n % 9] = false;
        }
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
                    show[idx / 9][idx % 9] = !show[idx / 9][idx % 9];
                }
                int btnIdx = getButtonIdx(e.getX(), e.getY());
                if (btnIdx != -1) {
                    switch (buttonText[btnIdx]) {
                    case "Show All":
                        try {
                            System.out.println("Solve");
                            new ImageDisplayer(board, CreateFinalBoard.createBoard(board, alreadyInputted));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        dispose();
                        break;
                    case "Quit":
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
                repaint();
            }

            public void keyTyped(KeyEvent e) {
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
            int size = GUI.findFontSize(dim, dim, "" + i, g, "Arial", Font.PLAIN);
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
            g.setColor(new Color(175, 175, 175));
            for (int a : alreadyInputted) {
                if (a == n) {
                    g.setColor(new Color(255, 255, 255));
                }
            }
            g.fillRect(x, y, dim, dim);
            g.setColor(new Color(0, 0, 0));
            g.drawRect(x, y, dim, dim);
            if (board[i][j] != 0 && show[i][j]) {
                g.drawString("" + board[i][j], x + dim / 2 - g.getFontMetrics().stringWidth("" + board[i][j]) / 2,
                        y + g.getFontMetrics().getAscent());
            }
            coordinates[n][0][0] = x;
            coordinates[n][0][1] = y;
            coordinates[n][1][0] = x + dim;
            coordinates[n][1][1] = y + dim;
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
