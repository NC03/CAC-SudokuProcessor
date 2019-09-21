import java.util.*;
import java.io.*;
import java.awt.image.*;

public class GUI
{
    public static void main(String[] args)
    {
        String fileName = "board.png";
        File f = new File(fileName);
        BufferedImage bi = ImageIO.read(f);
        int[][] board = SudokuGraphicsProcessor.boxGrid(bi);
        
    }
}
