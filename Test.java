import java.util.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class Test
{
	public static void main(String[] args)
	{
		try{
		BufferedImage bi = new BufferedImage(600,600,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		g.setColor(new Color(255,255,255));
		g.fillRect(0,0,600,600);
		g.setColor(new Color(0,0,0));
		g.drawLine(200,0,200,600);
		g.drawLine(400,0,400,600);
		g.drawLine(0,200,600,200);
		g.drawLine(0,400,600,400);
		ImageIO.write(bi,"png",new File("test.png"));
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
