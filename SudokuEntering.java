
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
    
    public SudokuEntering(int[][] board)
    {
        super("Sudoku Solver");
        this.board = board;
        setup();
    }

    public SudokuEntering() {
        super("Sudoku Solver");
        setup();
    }
    
    public void setup()
    {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setSize(600, 400);
        board = new int[9][9];
        addMouseMotionListener(new MouseMotionListener(){
                public void mouseMoved(MouseEvent e)
                {

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
                        selectedIdx = idx;
                    }else{
                        selectedIdx = -1;
                    }
                    repaint();
                }

                public void mouseReleased(MouseEvent e)
                {

                }

                public void mousePressed(MouseEvent e)
                {

                }
            });
        addKeyListener(new KeyListener(){
                public void keyReleased(KeyEvent e)
                {

                }

                public void keyPressed(KeyEvent e)
                {
                    if(e.getKeyCode() == 37)
                    {
                        selectedIdx--;
                    }else if(e.getKeyCode() == 38)
                    {
                        selectedIdx-=9;
                    }else if(e.getKeyCode() == 39)
                    {
                        selectedIdx++;
                    }else if(e.getKeyCode() == 40)
                    {
                        selectedIdx+=9;
                    }
                    if(selectedIdx >= 81)
                    {
                        selectedIdx -= 81;
                    }
                    if(selectedIdx < 0)
                    {
                        selectedIdx += 81;   
                    }
                    repaint();
                }

                public void keyTyped(KeyEvent e)
                {
                    if(selectedIdx != -1)
                    {
                        int i = selectedIdx % 9;
                        int j = selectedIdx / 9;
                        board[i][j] = Integer.parseInt(""+e.getKeyChar());
                    }
                    repaint();
                }
            });
        repaint();
    }

    public int getIdx(int x, int y)
    {
        for(int i = 0; i < coordinates.length; i++)
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
        paintGrid(g);
    }

    public void paintGrid(Graphics g) {
        g.setFont(new Font("Arial",Font.PLAIN,24));
        int width = getWidth();
        int height = getHeight();
        int dim = Math.min(width,height)/2/9;
        coordinates = new int[81][2][2];
        for (int n = 0; n < 81; n++) {
            int i = n % 9;
            int j = n / 9;
            int x = width / 2 - 4*dim - dim/2 + i * dim;
            int y = height / 2 - 4*dim - dim/2 + j * dim;
            g.setColor(new Color(0,0,0));
            g.drawRect(x,y,dim,dim);
            if(board[i][j] != 0){
                g.drawString(""+board[i][j], x, y+g.getFontMetrics().getAscent());
            }
            coordinates[n][0][0] = x;
            coordinates[n][0][1] = y;
            coordinates[n][1][0] = x+dim;
            coordinates[n][1][1] = y+dim;
            if(n == selectedIdx)
            {
                g.setColor(new Color(255,180,0));
                g.drawOval(x, y, dim, dim);
            }
        }
    }
}
