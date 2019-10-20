import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * An interface with the user to display the solved board as an image
 * 
 * @author Nick
 * @version 1.0
 */
public class ImageDisplayer extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private BufferedImage inputImage;
    private BufferedImage scaledImage;
    private int[][] bounds;
    private boolean show;
    private String[] buttonText = { "Save Image", "Export Board", "Close" };
    private int[][][] buttonCoordinates;
    private int[][] board;

    public int getIdx(int x, int y) {
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

    public boolean getStatus() {
        return show;
    }

    public ImageDisplayer(int[][] b, BufferedImage bi) {
        super("Image Cropper");
        this.board = b;
        show = true;
        inputImage = bi;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(600, 400);
        addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent e) {
            }

            public void mouseDragged(MouseEvent e) {
                repaint();
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
                    switch (buttonText[idx]) {
                    case "Save Image":
                        try {
                            JFileChooser fc = new JFileChooser();
                            fc.setDialogTitle("Save the Image");
                            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                                ImageIO.write(inputImage, "png", fc.getSelectedFile());
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case "Export Board":
                        try {
                            JFileChooser fc = new JFileChooser();
                            fc.setDialogTitle("Save the Image");
                            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                            if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                                BufferedWriter bw = new BufferedWriter(new FileWriter(fc.getSelectedFile()));
                                for (int[] row : board) {
                                    for (int i = 0; i < row.length; i++) {
                                        bw.write(row[i] + (i < row.length - 1 ? "," : "\n"));
                                    }
                                }
                                bw.close();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case "Close":
                        dispose();
                        break;

                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
                repaint();
            }

            public void mousePressed(MouseEvent e) {
                repaint();
            }

        });
    }

    public boolean inViewport(int x, int y) {
        int xMin = Math.min(bounds[0][0], bounds[1][0]);
        int xMax = Math.max(bounds[0][0], bounds[1][0]);
        int yMin = Math.min(bounds[0][1], bounds[1][1]);
        int yMax = Math.max(bounds[0][1], bounds[1][1]);
        if (x >= xMin && x <= xMax && y >= yMin && y <= yMax) {
            return true;
        } else {
            return false;
        }
    }

    public static BufferedImage resize(BufferedImage bi, int width, int height) {
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        double yScalar = (double) (bi.getHeight() - 1) / (out.getHeight() - 1);
        double xScalar = (double) (bi.getWidth() - 1) / (out.getWidth() - 1);
        for (int i = 0; i < out.getHeight(); i++) {
            for (int j = 0; j < out.getWidth(); j++) {
                double tempY = yScalar * i;
                double tempX = xScalar * j;
                int minX = (int) Math.floor(tempX);
                int maxX = (int) Math.ceil(tempX);
                int minY = (int) Math.floor(tempY);
                int maxY = (int) Math.ceil(tempY);
                Color[] colors = { new Color(bi.getRGB(minX, minY)), new Color(bi.getRGB(maxX, minY)),
                        new Color(bi.getRGB(minX, maxY)), new Color(bi.getRGB(maxX, maxY)) };
                Color top = linearInterpolation(colors[0], colors[1], tempX - minX);
                Color bottom = linearInterpolation(colors[2], colors[3], tempX - minX);
                Color c = linearInterpolation(top, bottom, tempY - minY);
                out.setRGB(j, i, c.getRGB());
            }
        }
        return out;
    }

    public static Color linearInterpolation(Color min, Color max, double percentage) {
        int r1 = min.getRed();
        int g1 = min.getGreen();
        int b1 = min.getBlue();
        int r2 = max.getRed();
        int g2 = max.getGreen();
        int b2 = max.getBlue();
        int r3 = (int) linearInterpolation(r1, r2, percentage);
        int g3 = (int) linearInterpolation(g1, g2, percentage);
        int b3 = (int) linearInterpolation(b1, b2, percentage);
        return new Color(r3, g3, b3);
    }

    public static double linearInterpolation(double min, double max, double percentage) {
        return (max - min) * percentage + min;
    }

    public void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        double yscalar = height * 0.8 / inputImage.getHeight();
        double xscalar = width * 0.8 / inputImage.getWidth();
        double scalar = Math.min(xscalar, yscalar);
        scaledImage = resize(inputImage, (int) (inputImage.getWidth() * scalar),
                (int) (inputImage.getHeight() * scalar));

        bounds = new int[2][2];
        int xExtra = width * 8 / 10 - scaledImage.getWidth();
        int yExtra = height * 8 / 10 - scaledImage.getHeight();

        bounds[0][0] = width / 10 + xExtra / 2;
        bounds[0][1] = height / 10 + yExtra / 2;
        bounds[1][0] = width * 9 / 10 - xExtra / 2;
        bounds[1][1] = height * 9 / 10 - yExtra / 2;

        g.clearRect(0, 0, width, height);
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, width, height);
        g.clearRect(bounds[0][0], bounds[0][1], bounds[1][0] - bounds[0][0], bounds[1][1] - bounds[0][1]);

        drawButtons(g);

        g.drawImage(scaledImage, bounds[0][0], bounds[0][1], new Color(0, 0, 0), null);

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
