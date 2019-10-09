import java.util.*;

import javax.imageio.ImageIO;

import java.awt.image.*;
import java.awt.Color;
import java.io.*;

public class ImageUtil {
    public static void main(String[] args) {
        try {
            BufferedImage bi = ImageIO.read(new File("test2.png"));
            bi = grayscale(bi);
            int threshold = (int) (averageColor(bi).getRed() * 0.5);
            bi = thresholdFilter(bi, threshold);
            ImageIO.write(bi, "png", new File("test2out.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a sub-image from the BufferedImage parameter from [x1,x2) [y1,y2)
     * 
     * @param bi The BufferedImage to extract the sub-image from
     * @param x1 The initial x value, must be less than x2
     * @param x2 The final x value, where the last value of the output BufferedImage
     *           is the pixel x-1
     * @param y1 The initial y value, must be less than y2
     * @param y2 The final y value, where the last value of the output BufferedImage
     *           is the pixel y-1
     * @return The sub-image extracted from the input from x1 to x2(not inclusive)
     *         and y2 to y2(not inclusive)
     */
    public static BufferedImage splice(BufferedImage bi, int x1, int y1, int x2, int y2) {
        BufferedImage out = new BufferedImage(x2 - x1 + 1, y2 - y1 + 1, BufferedImage.TYPE_INT_RGB);
        for (int j = y1; j <= y2; j++) {
            for (int i = x1; i <= x2; i++) {
                out.setRGB(i - x1, j - y1, bi.getRGB(i, j));
            }
        }
        return out;
    }

    public static BufferedImage grayscale(BufferedImage bi) {
        BufferedImage out = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                Color c = new Color(bi.getRGB(i, j));
                int val = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                out.setRGB(i, j, new Color(val, val, val).getRGB());
            }
        }
        return out;
    }

    public static BufferedImage histogramEqualization(BufferedImage bi) {
        BufferedImage out = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
        double[] p = new double[256];
        int numPixels = bi.getWidth() * bi.getHeight();
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                int val = new Color(bi.getRGB(i, j)).getRed();
                p[val] += 1.0 / numPixels;
            }
        }
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                int val = (int) Math.floor((256 - 1) * sum(p, new Color(bi.getRGB(i, j)).getRed()));
                out.setRGB(i, j, new Color(val, val, val).getRGB());
            }
        }
        return out;
    }

    private static double sum(double[] vals, int end) {
        double out = 0;
        for (int i = 0; i <= end; i++) {
            out += vals[i];
        }
        return out;
    }

    public static BufferedImage thresholdFilter(BufferedImage bi, int threshold) {
        BufferedImage out = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                int val = new Color(bi.getRGB(i, j)).getRed();
                out.setRGB(i, j, (val >= threshold) ? 0xffffff : 0);
            }
        }
        return out;
    }

    public static BufferedImage thresholdFilter(BufferedImage bi) {
        double threshold = 0;
        int numPixels = bi.getWidth() * bi.getHeight();
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                int val = new Color(bi.getRGB(i, j)).getRed();
                threshold += (double) val / numPixels;
            }
        }
        return thresholdFilter(bi, (int) (0.5 * threshold));
    }

    public static Color averageColor(BufferedImage bi) {
        double red = 0;
        double green = 0;
        double blue = 0;
        int count = 0;
        double scalar = 1.0 / (bi.getWidth() * bi.getHeight());
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                Color temp = new Color(bi.getRGB(i, j));
                if (temp.getRed() != 255 && temp.getBlue() != 255 && temp.getGreen() != 255) {
                    red += temp.getRed()/255.0;
                    green += temp.getGreen()/255.0;
                    blue += temp.getBlue()/255.0;
                    count++;
                }
            }
        }
        red /= count;
        green /= count;
        blue /= count;
        red *= 255;
        green *= 255;
        blue *= 255;
        return new Color((int) red, (int) green, (int) blue);
    }
    
    public static BufferedImage convToBlackWhite(BufferedImage bi)
    {
        BufferedImage out = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
        ArrayList<Integer> colorsInRow = new ArrayList<>();
        ArrayList<Integer> colorFreq = new ArrayList<>();
        for (int x = 0; x < bi.getWidth(); x++) {
            
            int p = bi.getRGB(x,0);
            if (!colorsInRow.contains(p)) {
                colorsInRow.add(p);
                colorFreq.add(1);
                
            } else {
                
                for (int i = 0; i < colorsInRow.size(); i++) {
                    
                    if (colorsInRow.get(i) == p) {
                        
                        colorFreq.set(i, colorFreq.get(i) + 1);
                        
                    }
                }
            }
        }
        
        System.out.println(colorsInRow);
        System.out.println(colorFreq);
        
        for (int i = 0; i < colorFreq.size(); i++) {
            
            if (colorFreq.get(i) >= 25) {
                
                colorFreq.remove(i);
                colorsInRow.remove(i);
                i--;
            }
            
        }
        System.out.println("----");
        System.out.println(colorsInRow);
        System.out.println(colorFreq);
        for (int y = 0; y < bi.getHeight(); y++) {
            
            for (int x = 0; x < bi.getWidth(); x++) {
            
                int p = bi.getRGB(x,y);
                
                for (int i = 0; i < colorsInRow.size(); i++) {
                    
                    if (p == colorsInRow.get(i) || p == SudokuGraphicsProcessor.blackColor) {
                    
                        out.setRGB(x, y, SudokuGraphicsProcessor.blackColor);
                        break;
                        
                    } else {
                        out.setRGB(x, y, SudokuGraphicsProcessor.whiteColor);

                    }
                    
                }
                
            }
            
        }
        
        
        return out;
    }
    
}
