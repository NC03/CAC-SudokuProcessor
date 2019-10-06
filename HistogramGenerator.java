import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.awt.Color;

public class HistogramGenerator {
    
    

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