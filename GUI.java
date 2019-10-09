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

    public GUI() {
        super("Sudoku Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(600, 400);
        addMouseMotionListener(new MouseMotionListener(){
                public void mouseMoved(MouseEvent e)
                {
                    int idx = getIdx(e.getX(),e.getY());
                    if(idx != -1)
                    {
                        highlightedIdx = idx;
                    }else{
                        highlightedIdx = -1;
                    }
                    hue += 0.005;
                    if(hue > 1)
                    {
                        hue%=1;
                    }
                    repaint();
                }

                public void mouseDragged(MouseEvent e)
                {

                }
            });
        addMouseListener(new MouseListener(){
                public void mouseExited(MouseEvent e)
                {

                }

                public void mouseEntered(MouseEvent e)
                {
                }

                public void mouseClicked(MouseEvent e)
                {
                    int idx = getIdx(e.getX(),e.getY());
                    if(idx != -1)
                    {
                        switch(idx)
                        {
                            case 0:
                            System.out.println("Import from Picture");
                            JFileChooser fc = new JFileChooser();
                            fc.setDialogTitle("Load the Image");
                            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                            if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                            {
                                try{
                                    File imgIn = fc.getSelectedFile();
                                    BufferedImage bi = ImageIO.read(imgIn);
                                    ImageCropper ic = new ImageCropper(bi);
                                }catch(Exception ex)
                                {
                                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error:", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                            break;
                            case 1:
                            System.out.println("Enter Raw Data");
                            SudokuEntering se = new SudokuEntering();
                            break;
                            case 2:
                            System.out.println("Load from file");
                            break;
                        }
                    }
                    repaint();
                }

                public void mouseReleased(MouseEvent e)
                {
                    pressedIdx = -1;
                    repaint();
                }

                public void mousePressed(MouseEvent e)
                {
                    int idx = getIdx(e.getX(),e.getY());
                    if(idx != -1)
                    {
                        pressedIdx = idx;
                    }
                }
            });
    }

    public int getIdx(int x, int y)
    {
        for(int i = 0; i < buttons.length; i++)
        {
            int xMin = Math.min(coordinates[i][0][0], coordinates[i][1][0]);
            int xMax = Math.max(coordinates[i][0][0], coordinates[i][1][0]);
            int yMin = Math.min(coordinates[i][0][1], coordinates[i][1][1]);
            int yMax= Math.max(coordinates[i][0][1], coordinates[i][1][1]);
            if(x >= xMin && x <= xMax && y >= yMin && y <= yMax)
            {
                return i;
            }
        }
        return -1;
    }

    public void paint(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        g.setColor(new Color(Color.HSBtoRGB((float)hue, 1, 1)));
        g.setFont(new Font("Arial",Font.BOLD,64));
        g.drawString("Sudoku Solver!",getWidth()/2-g.getFontMetrics().stringWidth("Sudoku Solver!")/2,getHeight()/4+g.getFontMetrics().getAscent()/2);
        paintButtons(g);
    }

    public void paintButtons(Graphics g) {
        g.setFont(new Font("Arial",Font.PLAIN,24));
        int width = getWidth();
        int height = getHeight();
        int itemWidth = width * 5 / 10;
        int itemHeight = height * 10 / 100;
        coordinates = new int[buttons.length][2][2];
        for (int i = 0; i < buttons.length; i++) {
            int x = width / 2;
            int y = (int) (height * 3 / 5 + i * (itemHeight + 0.05 * height));
            if(pressedIdx == i)
            {
                g.setColor(new Color(80, 80, 80));
            }
            else if(highlightedIdx == i)
            {
                g.setColor(new Color(65, 65, 65));
            }else
            {
                g.setColor(new Color(54, 54, 54));
            }
            g.fillRect(x - itemWidth / 2, y - itemHeight / 2, itemWidth, itemHeight);
            g.setColor(new Color(255, 255, 255));
            g.drawString(buttons[i], x - g.getFontMetrics().stringWidth(buttons[i]) / 2,
                y + g.getFontMetrics().getAscent() / 2);
            coordinates[i][0][0] = x-itemWidth/2;
            coordinates[i][0][1] = y-itemHeight/2;
            coordinates[i][1][0] = x+itemWidth/2;
            coordinates[i][1][1] = y+itemHeight/2;
        }
    }
}
