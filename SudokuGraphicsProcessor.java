import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.awt.Color;
import javax.imageio.ImageIO;

public class SudokuGraphicsProcessor {
    public int[][] boxGrid = new int[9][9];
    public ArrayList<Integer> blankGrids = new ArrayList<Integer>();
    public int startX = 0;
    public int startY = 0;
    public int endX = 0;
    public int endY = 0;
    public int blackColor = -16777216; // black
    public ArrayList<ArrayList<Integer>> colorsInEachGrid = new ArrayList<ArrayList<Integer>>();

    public SudokuGraphicsProcessor(BufferedImage img2) {
        // Set up boxGridInitial Array
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                boxGrid[i][j] = -1;
            }
        }
        // Temporarily fills multi-dem arraylist
        for (int i = 0; i < 81; i++) {
            colorsInEachGrid.add(new ArrayList<Integer>());
        }
        // ImageUtil iU = new ImageUtil(); ImageUtil has class methods so you don't need
        // to instantiate an object, like Math.abs() doesn't require Math m = new Math()
        // and m.abs()
        BufferedImage img = ImageUtil.thresholdFilter(img2);
        // For testing purposes
        /////
        int overallWidth = img.getWidth();
        int overallHeight = img.getHeight();
        // Finds starting and ending points of the grid itself
        int sBordColX = 0;
        int sBordColY = 0;
        boolean stageOneCompS = false;
        boolean isDoneS = false;
        for (int y = 0; y < overallHeight; y++) {
            for (int x = 0; x < overallWidth; x++) {
                int p = img.getRGB(x, y);
                if (p != img.getRGB(0, 0) && !stageOneCompS) {
                    sBordColX = x;
                    sBordColY = y;
                    stageOneCompS = true;
                    continue;
                }
                if (stageOneCompS && p != blackColor && (x > sBordColX && x < sBordColX + 20)
                        && (y > sBordColY && y < sBordColY + 20)) {
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
        for (int y = overallHeight - 1; y >= 0; y--) {
            for (int x = overallWidth - 1; x >= 0; x--) {
                int p = img.getRGB(x, y);
                if (p == blackColor && !stageOneCompE) {
                    eBordColX = x;
                    eBordColY = y;
                    stageOneCompE = true;
                    continue;
                }
                if (stageOneCompE && p != blackColor && (x < eBordColX && x > eBordColX - 20)
                        && (y < eBordColY && y > eBordColY - 20)) {
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
        // For storing x & y coordinates of each grid
        ArrayList<Integer> startPointsX = new ArrayList<Integer>();
        ArrayList<Integer> startPointsY = new ArrayList<Integer>();
        ArrayList<Integer> endPointsX = new ArrayList<Integer>();
        ArrayList<Integer> endPointsY = new ArrayList<Integer>();
        // Finds x end coordinates of each grid
        for (int x = startX; x < endX; x++) {
            int p = img.getRGB(x, startY);
            if (p == blackColor) {
                endPointsX.add(x - 1);
                x += 10;
            }
            if (x == endX - 1) {
                endPointsX.add(x);
            }
        }
        // Finds y end coordinates of each grid
        for (int y = startY; y < endY; y++) {
            int p = img.getRGB(startX, y);
            if (p == blackColor) {
                endPointsY.add(y - 1);
                y += 10;
            }
            if (y == endY - 1) {
                endPointsY.add(y);
            }
        }
        // Finds x start coordinates of each grid
        for (int x = endX - 1; x >= startX; x--) {
            int p = img.getRGB(x, startY);
            if (p == blackColor) {
                startPointsX.add(x + 1);
                x -= 10;
            }
            if (x == startX) {
                startPointsX.add(x);
            }
        }
        // Finds y start coordinates of each grid
        for (int y = endY - 1; y >= startY; y--) {
            int p = img.getRGB(startX, y);
            if (p == blackColor) {
                startPointsY.add(y + 1);
                y -= 10;
            }
            if (y == startY) {
                startPointsY.add(y);
            }
        }
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                int p = img.getRGB(x,y);
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
        // Used to loop through all the number squares and recognize them
        for (int yOverall = 0; yOverall < 9; yOverall++) {
            for (int xOverall = 0; xOverall < 9; xOverall++) {
                int xGrid = xOverall;
                int yGrid = yOverall;
                int gridNum = (yGrid * 9) + xGrid;
                if (!blankGrids.contains(gridNum)) {
                    int sPointX = startPointsX.get(startPointsX.size() - 1 - xGrid);
                    int sPointY = startPointsY.get(startPointsY.size() - 1 - yGrid);
                    BufferedImage indivGridImg = new BufferedImage(endPointsX.get(xGrid) - sPointX + 1,
                            endPointsY.get(yGrid) - sPointY + 1, BufferedImage.TYPE_INT_ARGB);
                    for (int y = sPointY; y <= endPointsY.get(yGrid); y++) {
                        for (int x = sPointX; x <= endPointsX.get(xGrid); x++) {
                            int p = img.getRGB(x, y);
                            indivGridImg.setRGB(x - sPointX, y - sPointY, p);
                        }
                    }
                    
                }
            }
        }
    }

    public int[][] getBoard() {
        return boxGrid;
    }
}
