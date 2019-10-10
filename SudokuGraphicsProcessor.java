/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author akash
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class SudokuGraphicsProcessor {
    
    public static final int blackColor = -16777216; //black
    public static final int whiteColor = -1; //white
    public static ArrayList<Integer> blankGrids = new ArrayList<Integer>();
    
    public static int[][] parseImage(BufferedImage imgInit) throws IOException { 
        
        int[][] boxGrid = new int[9][9];

        int startX = 0;
        int startY = 0;
        int endX = 0;
        int endY = 0;

        ArrayList<ArrayList<Integer>> colorsInEachGrid = new ArrayList<ArrayList<Integer>>();
    
        //Set up boxGridInitial Array
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                boxGrid[i][j] = -1;
            }
        }
        
        //Temporarily fills multi-dem arraylist
        for (int i = 0; i < 81; i++) {
            colorsInEachGrid.add(new ArrayList<Integer>());
        }

        BufferedImage img = ImageUtil.grayscale(imgInit);
        int overallWidth = img.getWidth();
        int overallHeight = img.getHeight();
        
        //Finds starting and ending points of the grid itself
        int sBordColX = 0;
        int sBordColY = 0;
        int borderCol = 0;
        
        boolean stageOneCompS = false;
        boolean isDoneS = false;
        
        //Find startX and startY
        for (int y = 0; y < overallHeight; y++) {
            
            for (int x = 0; x < overallWidth; x++) {
            
                int p = img.getRGB(x,y);
                
                if (Math.abs(p - img.getRGB(0,0)) >= 8000000 && !stageOneCompS) {
                    
                    borderCol = p;
                    sBordColX = x;
                    sBordColY = y;
                    stageOneCompS = true;
                    continue;
                }
                
                if (stageOneCompS && Math.abs(p - borderCol) >= 8000000 && (x > sBordColX && x < sBordColX + 20) && (y > sBordColY && y < sBordColY + 20)) {
                    
                    isDoneS = true;
                    startX = x;
                    startY = y;
                    break;
                }
            }  
            if (isDoneS) {
                break;
                
            }
        }
        
        int eBordColX = 0;
        int eBordColY = 0;
        boolean stageOneCompE = false;
        boolean isDoneE = false;
        
        //Finds endX and endY
        for (int y = overallHeight - 1; y >= 0; y--) {
            
            for (int x = overallWidth - 1; x >= 0; x--) {
            
                int p = img.getRGB(x,y);
                if (Math.abs(p - img.getRGB(0,0)) >= 8000000 && !stageOneCompE) {
                    
                    eBordColX = x;
                    eBordColY = y;
                    stageOneCompE = true;
                    continue;
                }
                
                if (stageOneCompE && Math.abs(p - borderCol) >= 8000000 && (x < eBordColX && x > eBordColX - 20) && (y < eBordColY && y > eBordColY - 20)) {
                    
                    isDoneE = true;
                    endX = x;
                    endY = y;
                    break;
                }
            }  
            if (isDoneE) {
                break;
                
            }
        }
        
        //USE THIS AS THE NEW THRESHOLD THING -- The rest wont work until it converts all the grid borders to black
        BufferedImage imgSpliced = ImageUtil.splice(img, startX, startY, endX, endY);
        BufferedImage imgBW = ImageUtil.convToBlackWhite(imgSpliced);
        //For testing purposes
        try{
            File fGrid = new File("/Users/akash/Desktop/imgBW.png");
            ImageIO.write(imgBW, "png", fGrid);
          }catch(IOException e){
            System.out.println(e);
          }
        try{
            File fGrid = new File("/Users/akash/Desktop/imgSpliced.png");
            ImageIO.write(imgSpliced, "png", fGrid);
          }catch(IOException e){
            System.out.println(e);
          }
        
        ArrayList<Integer> startPointsX = new ArrayList<Integer>();
        ArrayList<Integer> startPointsY = new ArrayList<Integer>();
        ArrayList<Integer> endPointsX = new ArrayList<Integer>();
        ArrayList<Integer> endPointsY = new ArrayList<Integer>();
        
        //Finds x end coordinates of each grid
        for (int x = 0; x < imgBW.getWidth(); x++) {
           
            int p = imgBW.getRGB(x,0);

            if (p == blackColor) {

                endPointsX.add(x - 1);
                x += 10;
            }
            
            if (x == imgBW.getWidth() - 1) {
                
                endPointsX.add(x);
                
            }
        }
        
        //Finds y end coordinates of each grid
        for (int y = 0; y < imgBW.getHeight(); y++) {
           
            int p = imgBW.getRGB(0,y);

            if (p == blackColor) {

                endPointsY.add(y - 1);
                y += 10;
            }
            
            if (y == imgBW.getHeight() - 1) {
                
                endPointsY.add(y);
                
            }
        }
        
        //Finds x start coordinates of each grid
        for (int x = imgBW.getWidth() - 1; x >= 0; x--) {
           
            int p = imgBW.getRGB(x,0);
            
            if (p == blackColor) {

                startPointsX.add(x + 1);
                x -= 10;
            }
            
            if (x == 0) {
                
                startPointsX.add(x);
                
            }
        }
        
        //Finds y start coordinates of each grid
        for (int y = imgBW.getHeight() - 1; y >= 0; y--) {
           
            int p = imgBW.getRGB(0,y);

            if (p == blackColor) {

                startPointsY.add(y + 1);
                y -= 10;
            }
            
            if (y == 0) {
                
                startPointsY.add(y);
                
            }
        }
        
        //Determines which grid x and y are in
        for (int y = 0; y < imgBW.getHeight(); y++) {
            
            for (int x = 0; x < imgBW.getWidth(); x++) {
                
                int p = imgBW.getRGB(x,y);
                int xLoc = -1;
                int yLoc = -1;
                int gridNum = 0;
                boolean xIsFound = false;
                boolean yIsFound = false;
                
                for (int i = 0; i < 9; i++) {
                    
                    if (x <= endPointsX.get(i) && !xIsFound && x >= startPointsX.get(8-i)) {
                        
                        xLoc = i;
                        xIsFound = true;
                        
                    }
                    
                    if (y <= endPointsY.get(i) && !yIsFound && y >= startPointsY.get(8-i)) {
                        
                        yLoc = i;
                        yIsFound = true;
                        
                    }
                    
                }
                if (xLoc != -1 && yLoc != -1) {
                    
                    gridNum = (yLoc * 9) + xLoc;
                
                    if (!colorsInEachGrid.get(gridNum).contains(p)) {
                        
                        colorsInEachGrid.get(gridNum).add(p);
                    }
                }
                
            
            }
            
        }
        
        for (int i = 0; i < colorsInEachGrid.size(); i++) {
            
            if (!colorsInEachGrid.get(i).contains(blackColor)) {
                boxGrid[i / 9][i % 9] = 0;
                blankGrids.add(i);
                
            }
            
        }
        
        //Deletes all previous grid images in this folder (temporary)
        File file = new File("/Users/akash/Desktop/GridImgs/");      
        String[] myFiles;    
        if (file.isDirectory()) {
            myFiles = file.list();
            for (int i = 0; i < myFiles.length; i++) {
                File myFile = new File(file, myFiles[i]); 
                myFile.delete();
            }
        }
        
        //Used to loop through all the number squares and recognize them
        for (int yOverall = 0; yOverall < 9; yOverall++) {
            
            for (int xOverall = 0; xOverall < 9; xOverall++) {
                
                int xGrid = xOverall;
                int yGrid = yOverall;
                int gridNum = (yGrid * 9) + xGrid;
                
                if (!blankGrids.contains(gridNum)) {
                    
                    int sPointX = startPointsX.get(startPointsX.size() - 1 - xGrid);
                    int sPointY = startPointsY.get(startPointsY.size() - 1 - yGrid);
                    
                    BufferedImage indivGridImg = ImageUtil.splice(imgSpliced, sPointX, sPointY, endPointsX.get(xGrid), endPointsY.get(yGrid));
                    int x = HistogramGenerator.processImage(indivGridImg);
                    boxGrid[yGrid][xGrid] = x;
                    //write image: not needed (just for testing purposes)
                    try{
                      File fGrid = new File("/Users/akash/Desktop/GridImgs/gridImg" + xGrid + "-" + yGrid + ".png");
                      ImageIO.write(indivGridImg, "png", fGrid);
                      
                    }catch(IOException e){
                      System.out.println(e);
                    }
                    
                }
            
            }
            
        }
        
        return boxGrid;
    }
}
