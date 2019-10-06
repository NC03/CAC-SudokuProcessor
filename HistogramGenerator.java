import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.awt.Color;

public class HistogramGenerator {
    
    public static void main(String[] args)
    {

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