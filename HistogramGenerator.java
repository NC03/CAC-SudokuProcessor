import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.awt.Color;

public class HistogramGenerator {
    public static double[][][] model;

    public static void main(String[] args)
    {
        generateModel();
    }
    public static void generateModel()
    {
        for(int i = 0; i < 9; i++)
        {
            try{
                
            }catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
    public static int processImage(BufferedImage bi)
    {
        Color c = new Color(0,0,0);
        double[] rowHist = process(rowHistogram(bi, c));
        double[] colHist = process(colHistogram(bi, c));
        double[] errors = new double[9];
        for(int i = 0; i < 9; i++)
        {
            errors[i] = error(model[i][0],rowHist) + error(model[i][1],colHist);
        }
        return minIdx(errors)+1;
    }
    public static int minIdx(double vals)
    {
        int idx = 0;
        double val = vals[i];
        for(int i = 0; i < vals.length; i++)
        {
            if(vals[i] < val)
            {
                val = vals[i];
                idx = i;
            }
        }
        return idx;
    }
    public static double error(double[] a, double[]b)
    {
        double out = 0;
        for(int i = 0; i < a.length; i++)
        {
            out += Math.pow(a[i]-b[i],2);
        }
        return out;
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
    public static double[] process(int[] hist)
    {
        hist = removeZero(hist);
        double[] h = new double[hist.length];
        for(int i = 0; i < h.length; i++)
        {
            h[i] = (double)hist[i]/sum(hist);
        }
        return resize(h,100);
    }
    public static int[] removeZero(int[] arr)
    {
        int leftIdx = 0;
        int rightIdx = arr.length;
        boolean l = true;
        boolean r = true;
        for(int i = 0; i < arr.length; i++)
        {
            if(l && leftIdx < arr.length && arr[leftIdx] == 0)
            {
                leftIdx++;
            }else{
                l = false;
            }
            if(r && rightIdx > 0 && arr[rightIdx-1] == 0)
            {
                rightIdx--;
            }else{
                r = false;
            }
        }
        if(leftIdx < rightIdx)
        {
            return subarr(arr,leftIdx,rightIdx);
        }
        else{
            return null;
        }
    }
    public static int[] subarr(int[] arr, int min, int max)
    {
        int[] out = new int[max-min];
        for(int i = min; i < max; i++)
        {
            out[i-min] = arr[i];
        }
        return out;
    }
    public static double[] resize(double[] in, int length)
    {
        double[] out = new double[length];
        double scalar = (double)(in.length-1)/(out.length-1);
        for(int i = 0; i < out.length; i++)
        {
            double tempIdx = scalar*i;
            int minIdx = (int)Math.floor(tempIdx);
            int maxIdx = (int)Math.ceil(tempIdx);
            out[i] = linearInterpolation(in[minIdx],in[(maxIdx < in.length ? maxIdx : in.length-1)],tempIdx-minIdx);
        }
        return out;
    }
    public static double linearInterpolation(double min, double max, double percentage)
    {
        return (max-min)*percentage+min;
    }
    public static double sum(double[] arr)
    {
        double sum = 0;
        for(double v : arr)
        {
            sum += v;
        }
        return sum;
    }
    public static int sum(int[] arr)
    {
        int sum = 0;
        for(int v : arr)
        {
            sum += v;
        }
        return sum;
    }
    public static String toString(double[] arr)
    {
        String out = "";
        for(double v : arr)
        {
            out += v + ",";
        }
        return out.substring(0,out.length()-1);
    }
    public static void print(int[] arr)
    {
        for(int v : arr)
        {
            System.out.print(v);
        }
        System.out.println();
    }
    public static void print(double[] arr)
    {
        System.out.println(toString(arr));
    }
}