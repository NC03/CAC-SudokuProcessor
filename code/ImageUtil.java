/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author akash
 */
import java.util.*;
import java.awt.image.*;
import java.awt.Color;

public class ImageUtil
{

    /**
     * Returns a sub-image from the BufferedImage parameter from [x1,x2) [y1,y2)
     * @param bi The BufferedImage to extract the sub-image from
     * @param x1 The initial x value, must be less than x2
     * @param x2 The final x value, where the last value of the output BufferedImage is the pixel x-1
     * @param y1 The initial y value, must be less than y2
     * @param y2 The final y value, where the last value of the output BufferedImage is the pixel y-1
     * @return The sub-image extracted from the input from x1 to x2(not inclusive) and y2 to y2(not inclusive)
     */
    public static BufferedImage splice(BufferedImage bi, int x1, int y1, int x2, int y2)
    {
        BufferedImage out = new BufferedImage(x2-x1+1,y2-y1+1,BufferedImage.TYPE_INT_RGB);
        for(int j = y1; j <= y2; j++)
        {
            for(int i = x1; i <= x2; i++)
            {
                out.setRGB(i-x1,j-y1,bi.getRGB(i,j));
            }
        }
        return out;
    }

    public static BufferedImage grayscale(BufferedImage bi)
    {
        BufferedImage out = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < bi.getWidth(); i++)
        {
            for(int j = 0; j < bi.getHeight(); j++)
            {
                Color c = new Color(bi.getRGB(i,j));
                int val = (c.getRed() + c.getGreen() + c.getBlue())/3;
                out.setRGB(i,j,new Color(val,val,val).getRGB());
            }
        }
        return out;
    }

    public static BufferedImage histogramEqualization(BufferedImage bi)
    {
        BufferedImage out = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
        double[] p = new double[256];
        int numPixels = bi.getWidth() * bi.getHeight();
        for(int i = 0; i < bi.getWidth(); i++)
        {
            for(int j = 0; j < bi.getHeight(); j++)
            {
                int val = new Color(bi.getRGB(i,j)).getRed();
                p[val] += 1.0/numPixels;
            }
        }
        for(int i = 0; i < bi.getWidth(); i++)
        {
            for(int j = 0; j < bi.getHeight(); j++)
            {
                int val = (int)Math.floor((256-1)*sum(p,new Color(bi.getRGB(i,j)).getRed()));
                out.setRGB(i,j,new Color(val,val,val).getRGB());
            }
        }
        return out;
    }
    private static double sum(double[] vals, int end)
    {
        double out = 0;
        for(int i = 0; i <= end; i++)
        {
            out += vals[i];
        }
        return out;
    }

    public static BufferedImage thresholdFilter(BufferedImage bi, int threshold)
    {
        BufferedImage out = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < bi.getWidth(); i++)
        {
            for(int j = 0; j < bi.getHeight(); j++)
            {
                int r = new Color(bi.getRGB(i,j)).getRed();
                int g = new Color(bi.getRGB(i,j)).getGreen();
                int b = new Color(bi.getRGB(i,j)).getBlue();
                
                
                out.setRGB(i,j,(r >= threshold || g >= threshold || b >= threshold) ? 0xffffff : 0);
            }
        }
        return out;
    }

    public static BufferedImage thresholdFilter(BufferedImage bi)
    {
        double threshold = 0;
        int numPixels = bi.getWidth() * bi.getHeight();
        for(int i = 0; i < bi.getWidth(); i++)
        {
            for(int j = 0; j < bi.getHeight(); j++)
            {
                int val = new Color(bi.getRGB(i,j)).getRed();
                threshold += (double)val / numPixels;
            }
        }
        return thresholdFilter(bi,(int)threshold);
    }
    
     
    public static BufferedImage convToBlackWhite(BufferedImage bi)
    {
        BufferedImage out = new BufferedImage(bi.getWidth(), bi.getWidth(), BufferedImage.TYPE_INT_RGB); //Makes a square: W=H
        ArrayList<Integer> colorsInRow = new ArrayList<>();
        ArrayList<Integer> colorFreq = new ArrayList<>();
        ArrayList<Integer> xCordInRow = new ArrayList<>();
        ArrayList<Integer> yCordInRow = new ArrayList<>();
        int colorIndex0;
        int colorFreq0;
        
        //Fills up arraylist colorsInRow and colorFreq
        for (int a = 0; a < bi.getWidth(); a++) {
            
            int p = new Color(bi.getRGB(a,0)).getRed();
            if (!colorsInRow.contains(p)) {
                colorsInRow.add(p);
                colorFreq.add(1);
                
            } else {
                
                for (int i = 0; i < colorsInRow.size(); i++) {
                    
                    if (colorsInRow.get(i) == p) {
                        
                        colorFreq.set(i, colorFreq.get(i) + 1);
                        break;
                    }
                }
            }
            
            int p2 = new Color(bi.getRGB(0,a)).getRed();
            if (!colorsInRow.contains(p2)) {
                colorsInRow.add(p2);
                colorFreq.add(1);
                
            } else {
                
                for (int i = 0; i < colorsInRow.size(); i++) {
                    
                    if (colorsInRow.get(i) == p2) {
                        
                        colorFreq.set(i, colorFreq.get(i) + 1);
                        break;
                    }
                }
            }
            
        }
        
        colorIndex0 = colorsInRow.get(0);
        colorFreq0 = colorFreq.get(0);
        
        System.out.println(colorsInRow);
        System.out.println(colorFreq);
        
        //Removes unnecessary colors from colorsInRow
        for (int i = 0; i < colorFreq.size(); i++) {
            
            if (colorFreq.get(i) >= 50) {
                
                colorFreq.remove(i);
                colorsInRow.remove(i);
                i--;
            }
            
        }
        System.out.println(colorIndex0);
        System.out.println(colorFreq0);
        
        //Sees which x and y coordinates occur < 50 times
        for (int a = 0; a < bi.getWidth(); a++) {
            int p = new Color(bi.getRGB(a,0)).getRed();
            int p2 = new Color(bi.getRGB(0,a)).getRed();
            boolean hasAddedX = false;
            boolean hasAddedY = false;
            for (int i = 0; i < colorsInRow.size(); i++) {
                    
                if (colorsInRow.get(i) == p && !hasAddedX) {
                    xCordInRow.add(a);
                    hasAddedX = true;
                }
                if (colorsInRow.get(i) == p2 && !hasAddedY) {
                    yCordInRow.add(a);
                    hasAddedY = true;
                }
                if (hasAddedX && hasAddedY) {
                    break;
                }
                
            }
            
        }
        
        System.out.println("--XY-Cordinates--");
        System.out.println(xCordInRow.size());
        System.out.println(yCordInRow.size());
        System.out.println("----------");
        for (int y = 0; y < bi.getWidth(); y++) {
            
            for (int x = 0; x < bi.getWidth(); x++) {
            
                int p = new Color(bi.getRGB(x,y)).getRed();
                
                int largerCordInRowSize = xCordInRow.size();
                if (yCordInRow.size() > largerCordInRowSize) {
                    largerCordInRowSize = yCordInRow.size();
                }
                
                for (int i = 0; i < largerCordInRowSize; i++) {
                    try{
                        if (x == xCordInRow.get(i) || y == yCordInRow.get(i) || (p < colorIndex0 && colorFreq0 > 70 && colorFreq0 < 180) || p < 100) {

                            out.setRGB(x, y, SudokuGraphicsProcessor.blackColor);
                            break;

                        } else {
                            out.setRGB(x, y, SudokuGraphicsProcessor.whiteColor);

                        }
                    }catch(Exception err){
                    }
                    
                }
            }
        }
        
        return out;
    }

    // public static BufferedImage convToBlackWhite(BufferedImage bi) {
    //     List<Color> colors = new ArrayList<Color>();
    //     List<Integer> nums = new ArrayList<Integer>();

    //     for (int i = 0; i < bi.getWidth(); i++) {
    //         for (int j = 0; j < bi.getHeight(); j++) {
    //             Color c = new Color(bi.getRGB(i, j));
    //             boolean flag = false;
    //             for (int k = 0; k < colors.size(); k++) {
    //                 if (colors.get(k).getRed() == c.getRed()) {
    //                     nums.set(k, nums.get(k) + 1);
    //                     flag = true;
    //                 }
    //             }
    //             if (!flag && c.getRed() < 128) {
    //                 colors.add(c);
    //                 nums.add(0);
    //             }
    //         }
    //     }
    //     int maxIdx = 0;
    //     for(int i = 0; i < nums.size(); i++)
    //     {
    //         if(nums.get(i)>nums.get(maxIdx)){
    //             maxIdx = i;
    //         }
    //     }

    //     for (int i = 0; i < bi.getWidth(); i++) {
    //         for (int j = 0; j < bi.getHeight(); j++) {
    //             Color c = new Color(bi.getRGB(i,j));
    //             if(c.getRed() == colors.get(maxIdx).getRed())
    //             {
    //                 bi.setRGB(i,j,new Color(0,0,0).getRGB());
    //             }else{
    //                 bi.setRGB(i,j,new Color(255,255,255).getRGB());
    //             }
    //         }
    //     }
    //     return bi;

    // }
    
}
