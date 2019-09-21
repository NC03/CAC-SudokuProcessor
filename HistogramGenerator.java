import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.awt.Color;

public class HistogramGenerator {
    public static void main(String[] args) {
        double[] a = new double[100];
        double[] b = new double[100];
        for(int i = 0; i < a.length; i++)
        {
            a[i] = Math.sin((double)i /100 * 2 * Math.PI);
            b[i] = Math.sin((double)(i-10) /100 * 2 * Math.PI);
        }
        System.out.println(error(normalize(a),normalize(b)));


        histogramImages();
        trainDataset();
    }

    public static void outputHistogram(File f, double[] rowHistDouble, double[] colHistDouble) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            for (int k = 0; k < rowHistDouble.length; k++) {
                bw.write(rowHistDouble[k] + (k < rowHistDouble.length - 1 ? "," : "\n"));
            }
            for (int k = 0; k < colHistDouble.length; k++) {
                bw.write(colHistDouble[k] + (k < colHistDouble.length - 1 ? "," : "\n"));
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void trainDataset() {
        try {
            int trials = 100;
            double[] score = new double[trials];
            for (int n = 0; n < trials; n++) {
                /*
                 * for (int i = 1; i < 10; i++) { BufferedImage bi = ImageIO.read(new File(i +
                 * ".png")); double weight = (double)n/trials * 1; int num =
                 * evaluateImage(bi,new Color(0,0,0),weight,1-weight); if(num == i){ score[n] +=
                 * 1.0/9; } }
                 */
                double weight = (double) n / trials * 1;
                BufferedImage bi = ImageIO.read(new File("boardOut.png"));
                int num = evaluateImage(bi, new Color(0, 0, 0), weight, 1 - weight);
                if (num == 5) {
                    score[n] = 1.0;
                }
            }
            for (int i = 0; i < score.length; i++) {
                System.out.println("Weight: " + (double) i / trials + ", score:" + (int) (score[i] * 10000) / 100.0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double error(double[] a, double[] b) {
    double maxSum = 0;
    double sum = 0;
    for (int i = 0; i < a.length; i++) {
    maxSum += Math.pow(Math.max(b[i], a[i]), 2);
    sum += Math.pow((b[i] - a[i]), 2);
    }
    return sum / maxSum;
    }

    // public static double error(double[] a, double[] b)
    // {
    //     double error = 0;
    //     for(int i = 0; i < a.length; i++)
    //     {
    //         error += Math.abs(a[i]-b[i]);
    //     }
    //     return error;
    // }

    public static void histogramImages() {
        for (int n = 1; n < 10; n++) {
            try {
                Color c = new Color(0, 0, 0);
                BufferedImage bi = ImageIO.read(new File(n + ".png"));
                int[] rowHist = rowHistogram(bi, c);
                int[] colHist = colHistogram(bi, c);
                int rowMax = rowHist[0];
                for (int r : rowHist) {
                    if (r > rowMax) {
                        rowMax = r;
                    }
                }
                double[] rowHistDouble = new double[rowHist.length];
                for (int i = 0; i < rowHist.length; i++) {
                    rowHistDouble[i] = (double) rowHist[i] / rowMax;
                }
                rowHistDouble = resize(rowHistDouble, 100);

                int colMax = colHist[0];
                for (int r : colHist) {
                    if (r > colMax) {
                        colMax = r;
                    }
                }
                double[] colHistDouble = new double[colHist.length];
                for (int i = 0; i < colHist.length; i++) {
                    colHistDouble[i] = (double) colHist[i] / colMax;
                }
                colHistDouble = resize(colHistDouble, 100);

                rowHistDouble = normalize(rowHistDouble);
                colHistDouble = normalize(colHistDouble);
                outputHistogram(new File(n + ".txt"), rowHistDouble, colHistDouble);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void generateImages() {
        for (int i = 1; i < 10; i++) {
            try {
                BufferedImage bi = ImageIO.read(new File("Boards/" + i + ".png"));
                bi = ImageSplicer.splice(bi, 667, 729, 792, 854);
                ImageIO.write(bi, "png", new File(i + ".png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int evaluateImage(BufferedImage bi, Color c, double rowWeight, double colWeight) {
        int[] rowHist = rowHistogram(bi, c);
        int[] colHist = colHistogram(bi, c);
        int rowMax = rowHist[0];
        for (int r : rowHist) {
            if (r > rowMax) {
                rowMax = r;
            }
        }
        double[] rowHistDouble = new double[rowHist.length];
        for (int i = 0; i < rowHist.length; i++) {
            rowHistDouble[i] = (double) rowHist[i] / rowMax;
        }
        rowHistDouble = resize(rowHistDouble, 100);

        int colMax = colHist[0];
        for (int r : colHist) {
            if (r > colMax) {
                colMax = r;
            }
        }
        double[] colHistDouble = new double[colHist.length];
        for (int i = 0; i < colHist.length; i++) {
            colHistDouble[i] = (double) colHist[i] / colMax;
        }
        colHistDouble = resize(colHistDouble, 100);

        rowHistDouble = normalize(rowHistDouble);
        colHistDouble = normalize(colHistDouble);

        double[][][] values = new double[9][2][];
        double[] errors = new double[9];
        for (int n = 1; n < 10; n++) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(new File(n + ".txt")));
                String[] rowStrs = br.readLine().split(",");
                String[] colStrs = br.readLine().split(",");
                br.close();
                double[] rowDoubls = new double[rowStrs.length];
                double[] colDoubls = new double[rowStrs.length];
                for (int i = 0; i < rowStrs.length; i++) {
                    rowDoubls[i] = Double.parseDouble(rowStrs[i]);
                }
                for (int i = 0; i < colStrs.length; i++) {
                    colDoubls[i] = Double.parseDouble(colStrs[i]);
                }
                values[n - 1][0] = rowDoubls;
                values[n - 1][1] = colDoubls;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 9; i++) {
            errors[i] = rowWeight * error(rowHistDouble, values[i][0]) + colWeight * error(colHistDouble, values[i][1]);
        }
        int minIdx = 0;
        double minErr = errors[0];
        for (int i = 0; i < 9; i++) {
            if (errors[i] < minErr) {
                minIdx = i;
                minErr = errors[i];
            }
        }

        outputHistogram(new File("boardOutTxt.txt"), rowHistDouble, colHistDouble);

        return minIdx + 1;
    }

    public static int[] rowHistogram(BufferedImage bi, Color c) {
        int[] out = new int[bi.getHeight()];
        for (int j = 0; j < bi.getHeight(); j++) {
            int count = 0;
            for (int i = 0; i < bi.getWidth(); i++) {
                if (bi.getRGB(i, j) == c.getRGB()) {
                    count++;
                }
            }
            out[j] = count;
        }
        return out;
    }

    public static int[] colHistogram(BufferedImage bi, Color c) {
        int[] out = new int[bi.getWidth()];
        for (int i = 0; i < bi.getWidth(); i++) {
            int count = 0;
            for (int j = 0; j < bi.getHeight(); j++) {
                if (bi.getRGB(i, j) == c.getRGB()) {
                    count++;
                }
            }
            out[i] = count;
        }
        return out;
    }

    public static double averageX(BufferedImage bi, Color c) {
        int count = 0;
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                if (bi.getRGB(i, j) == c.getRGB()) {
                    count++;
                }
            }
        }
        double avgX = 0;
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                if (bi.getRGB(i, j) == c.getRGB()) {
                    avgX += (double) i / count;
                }
            }
        }
        return avgX;
    }

    public static double averageY(BufferedImage bi, Color c) {
        int count = 0;
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                if (bi.getRGB(i, j) == c.getRGB()) {
                    count++;
                }
            }
        }
        double avgY = 0;
        for (int i = 0; i < bi.getWidth(); i++) {
            for (int j = 0; j < bi.getHeight(); j++) {
                if (bi.getRGB(i, j) == c.getRGB()) {
                    avgY += (double) j / count;
                }
            }
        }
        return avgY;
    }

    public static double[] resize(double[] arr, int length) {
        double[] out = new double[length];
        for (int i = 0; i < out.length; i++) {
            double idx = (double) i / (out.length - 1) * (arr.length - 1);
            int i1 = (int) Math.floor(idx);
            int i2 = (int) Math.ceil(idx);
            double slope = arr[i2] - arr[i1];
            double val = arr[i1] + (idx - i1) * slope;
            out[i] = val;
        }
        return out;
    }

    public static double average(double[] vals) {
        return sum(vals) / vals.length;
    }

    public static double stdDev(double[] vals) {
        double accum = 0;
        for (double v : vals) {
            accum += Math.pow(v - average(vals), 2);
        }
        return Math.sqrt(accum / vals.length);
    }

    public static void print(int[] arr) {
        for (int e : arr) {
            System.out.print(e + ",");
        }
        System.out.println();
    }

    public static void print(double[] arr) {
        for (double e : arr) {
            System.out.print(e + ",");
        }
        System.out.println();
    }

    public static double sum(double[] vals) {
        double out = 0;
        for (double v : vals) {
            out += v;
        }
        return out;
    }

    public static double[] normalize(double[] arr) {
        double[] out = new double[arr.length];
        int center = out.length / 2;
        double[] temp = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            temp[i] = arr[i] * i;
        }
        double avg = sum(temp) / sum(arr);
        for (int i = 0; i < out.length; i++) {
            int idx = (int) (i + (avg - center));
            if (idx >= 0 && idx < arr.length) {
                out[i] = arr[idx];
            }
        }
        int idx1 = (int) (center - (2 * stdDev(temp)));
        int idx2 = (int) (center + (2 * stdDev(temp)));
        double[] t = new double[idx2 - idx1];
        for (int i = idx1; i < idx2; i++) {
            t[i - idx1] = out[i];
        }

        out = resize(t, out.length);

        double max = out[0];
        for (int i = 0; i < out.length; i++) {
            if (out[i] > max) {
                max = out[i];
            }
        }
        for (int i = 0; i < out.length; i++) {
            out[i] /= max;
        }

        // Average out maximum values: divide by maxiumum value, like mass spectrometry,
        // 0.2, 0.2, 0.3, 0.5, 1, 0.7, 0.4, 0.1

        return out;
    }
}