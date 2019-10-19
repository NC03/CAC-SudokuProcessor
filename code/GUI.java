import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class GUI extends JFrame {
    private String state = "mainScreen";
    private String[] buttons = { "Import from a picture", "Enter the problem", "Load from file" };
    private int[][][] coordinates;
    private int highlightedIdx;
    private int pressedIdx;
    private double hue = 0;

    public static void main(String[] args) {
        GUI g = new GUI();
    }

    public static boolean fontFits(int width, int height, String str, Graphics g) {
        return g.getFontMetrics().stringWidth(str) <= width && g.getFontMetrics().getHeight() <= height;
    }

    public static int findFontSize(int width, int height, String str, Graphics g, String font, int type) {
        int size = 1;
        g.setFont(new Font(font, type, size));
        while (fontFits(width, height, str, g)) {
            size++;
            g.setFont(new Font(font, type, size));
        }
        return size - 1;
    }

    public GUI() {
        super("Sudoku Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(600, 400);
        try {
            BufferedImage icon = ImageIO.read(GUI.class.getResourceAsStream("icon.png"));
            setIconImage(icon);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
                int idx = getIdx(e.getX(), e.getY());
                if (idx != -1) {
                    highlightedIdx = idx;
                } else {
                    highlightedIdx = -1;
                }
                hue += 0.005;
                if (hue > 1) {
                    hue %= 1;
                }
                repaint();
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
                    switch (idx) {
                    case 0:
                        JFileChooser fc = new JFileChooser();
                        fc.setDialogTitle("Load the Image");
                        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                            try {
                                BufferedImage bi = ImageIO.read(fc.getSelectedFile());
                                ImageCropper ic = new ImageCropper(bi);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error:",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        break;
                    case 1:
                        SudokuEntering se = new SudokuEntering();
                        break;
                    case 2:
                        JFileChooser fc2 = new JFileChooser();
                        fc2.setDialogTitle("Load the Board");
                        fc2.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        if (fc2.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                            try {
                                BufferedReader br = new BufferedReader(new FileReader(fc2.getSelectedFile()));
                                String[][] boardStr = new String[9][9];
                                for (int i = 0; i < 9; i++) {
                                    boardStr[i] = br.readLine().split(",");
                                }
                                br.close();
                                int[][] board = new int[9][9];
                                for (int i = 0; i < 9; i++) {
                                    for (int j = 0; j < 9; j++) {
                                        board[i][j] = Integer.parseInt(boardStr[i][j]);
                                    }
                                }
                                SudokuEntering se2 = new SudokuEntering(board);
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error:",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        break;
                    }
                }
                repaint();
            }

            public void mouseReleased(MouseEvent e) {
                pressedIdx = -1;
                repaint();
            }

            public void mousePressed(MouseEvent e) {
                int idx = getIdx(e.getX(), e.getY());
                if (idx != -1) {
                    pressedIdx = idx;
                }
            }
        });
    }

    public int getIdx(int x, int y) {
        for (int i = 0; i < buttons.length; i++) {
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
        g.setColor(new Color(Color.HSBtoRGB((float) hue, 1, 1)));
        g.setFont(new Font("Arial", Font.BOLD, 64));
        g.drawString("Sudoku Solver!", getWidth() / 2 - g.getFontMetrics().stringWidth("Sudoku Solver!") / 2,
                getHeight() / 4 + g.getFontMetrics().getAscent() / 2);
        paintButtons(g);
    }

    public void paintButtons(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        int itemWidth = width * 5 / 10;
        int itemHeight = height * 10 / 100;
        int fontsize = findFontSize(itemWidth, itemHeight, buttons[0], g, "Arial", Font.PLAIN);
        for (int i = 0; i < buttons.length; i++) {
            int size = findFontSize(itemWidth, itemHeight, buttons[i], g, "Arial", Font.PLAIN);
            if (size < fontsize) {
                fontsize = size;
            }
        }
        g.setFont(new Font("Arial", Font.PLAIN, fontsize));

        coordinates = new int[buttons.length][2][2];
        for (int i = 0; i < buttons.length; i++) {
            int x = width / 2;
            int y = (int) (height * 3 / 5 + i * (itemHeight + 0.05 * height));
            if (pressedIdx == i) {
                g.setColor(new Color(80, 80, 80));
            } else if (highlightedIdx == i) {
                g.setColor(new Color(65, 65, 65));
            } else {
                g.setColor(new Color(54, 54, 54));
            }
            g.fillRect(x - itemWidth / 2, y - itemHeight / 2, itemWidth, itemHeight);
            g.setColor(new Color(255, 255, 255));
            g.drawString(buttons[i], x - g.getFontMetrics().stringWidth(buttons[i]) / 2,
                    y + g.getFontMetrics().getAscent() / 2);
            coordinates[i][0][0] = x - itemWidth / 2;
            coordinates[i][0][1] = y - itemHeight / 2;
            coordinates[i][1][0] = x + itemWidth / 2;
            coordinates[i][1][1] = y + itemHeight / 2;
        }
    }
}
