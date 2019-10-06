import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.awt.Color;

public class HistogramGenerator {
    public static void main(String[] args) {
        createHistogram();
        mergeText();
        for (int i = 1; i < 9; i++) {
            try {
                BufferedImage bi = ImageIO.read(new File("testImageNY/" + i + ".png"));
                System.out.println(evaluateImage(bi, new Color(0, 0, 0), 0.5, 0.5));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 1; i < 9; i++) {
            try {
                BufferedImage bi = ImageIO.read(new File("testImageCOM/" + i + ".png"));
                System.out.println(evaluateImage(bi, new Color(0, 0, 0), 0.5, 0.5));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static double[] merge(double[] a, double[] b, double w) {
        double[] out = new double[a.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = w * a[i] + (1-w) * b[i];
        }
        return out;
    }

    public static void mergeText() {
        for (int n = 1; n < 10; n++) {
            try {
                BufferedReader brNY = new BufferedReader(new FileReader(new File("NY" + n + ".txt")));
                BufferedReader brCOM = new BufferedReader(new FileReader(new File("COM" + n + ".txt")));
                BufferedWriter bw = new BufferedWriter(new FileWriter(new File("new" + n + ".txt")));
                String out = "";
                for (int k = 0; k < 2; k++) {
                    String[] NYStr = brNY.readLine().split(",");
                    String[] COMStr = brCOM.readLine().split(",");
                    double[] NYDouble = new double[NYStr.length];
                    double[] COMDouble = new double[COMStr.length];
                    for (int j = 0; j < NYDouble.length; j++) {
                        NYDouble[j] = Double.parseDouble(NYStr[j]);
                        COMDouble[j] = Double.parseDouble(COMStr[j]);
                    }
                    double[] o = merge(NYDouble, COMDouble,0.5);
                    for (int i = 0; i < o.length; i++) {
                        out += o[i] + (i < o.length - 1 ? "," : "\n");
                    }
                }
                bw.write(out);
                bw.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    public static void createHistogram() {
        for (int n = 1; n < 9; n++) {
            try {
                Color c = new Color(0, 0, 0);
                double rowWeight = 0.5;
                double colWeight = 0.5;
                BufferedImage bi = ImageIO.read(new File("testImageNY/" + n + ".png"));
                bi = ImageUtil.thresholdFilter(bi);
                ImageIO.write(bi, "png", new File("imageNYOut/" + n + ".png"));
                bi = ImageUtil.thresholdFilter(bi);

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
                BufferedWriter bw = new BufferedWriter(new FileWriter(new File("NY" + n + ".txt")));
                String o = "";
                for (int i = 0; i < rowHistDouble.length; i++) {
                    o += rowHistDouble[i] + (i < rowHistDouble.length - 1 ? "," : "\n");
                }
                for (int i = 0; i < colHistDouble.length; i++) {
                    o += colHistDouble[i] + (i < colHistDouble.length - 1 ? "," : "\n");
                }
                bw.write(o);
                bw.close();
            } catch (Exception e) {
                // TODO: handle exception
            }
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

    public static int evaluateImage(BufferedImage bi, Color c, double rowWeight, double colWeight) {
        bi = ImageUtil.thresholdFilter(bi);

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
                BufferedReader br = new BufferedReader(new FileReader(new File("new" + n + ".txt")));
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
        int center = arr.length / 2;
        double[] temp = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            temp[i] = arr[i] > 0.1 ? 1 : 0;
        }

        double[] t = new double[temp.length];
        for (int i = 0; i < t.length; i++) {
            t[i] = temp[i] * i;
        }
        int avg = (int) (sum(t) / sum(temp));
        int count = 0;
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] == 1) {
                count++;
            }
        }
        int oneSideCount = (int) (0.9 * count / 2);
        int idx1 = avg;
        int idx2 = avg;
        int c = 0;

        while (c < oneSideCount) {
            idx1--;
            idx2++;
            while (temp[idx1] != 1) {
                idx1--;
            }
            while (temp[idx2] != 1) {
                idx2++;
            }
            c++;
        }
        double[] out = new double[idx2 - idx1 + 1];
        for (int i = idx1; i <= idx2; i++) {
            out[i - idx1] = arr[i];
        }
        out = resize(out, arr.length);

        double max = out[0];
        for (int i = 0; i < out.length; i++) {
            if (out[i] > max) {
                max = out[i];
            }
        }
        for (int i = 0; i < out.length; i++) {
            out[i] /= max;
        }
        return out;
    }
}