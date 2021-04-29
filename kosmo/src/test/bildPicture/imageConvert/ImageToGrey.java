package test.bildPicture.imageConvert;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ImageToGrey {
	/*
	 * https://data-flair.training/blogs/convert-colored-image/
	 * 2. Convert Colored Image to Greyscale in Java To convert colored image to
	 * greyscale in Java, the Alpha part of the picture will be same as the first
	 * picture. However, the RGB will be changed i.e., every one of the three RGB
	 * segments will have the same incentive for every pixel.
	 * 
	 * Let’s revise Java Character Class Methods with Syntax and Examples
	 * 
	 * i. Algorithm for the following Java Image Proecssing–
	 * 
	 * Get the RGB estimation of the pixel. Locate the normal of RGB i.e., Avg =
	 * (R+G+B)/3 Supplant the R, G and B estimation of the pixel with normal (Avg)
	 * ascertained in stage 2. Rehash Step 1 to Step 3 for every pixel of the
	 * picture. ii. Program to implement the following algorithm
	 */

	static int width;
	static int height;

	public static void main(String args[]) throws IOException {
		BufferedImage img = null;
		File f = null;
		try {
			f = new File("smal2.jpg");
			img = ImageIO.read(f);
		} catch (IOException e) {
			System.out.println(e);
		}
		width = img.getWidth();
		height = img.getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int p = img.getRGB(x, y);
				int a = (p >> 24) & 0xff;
				int r = (p >> 16) & 0xff;
				int g = (p >> 8) & 0xff;
				int b = p & 0xff;
				int avg = (r + g + b) / 3;
				p = (a << 24) | (avg << 16) | (avg << 8) | avg;
				img.setRGB(x, y, p);
			}
		}
		try {
			createAndSave(img);
			showResult(img);

		} catch (IOException e) {
			System.out.println(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void showResult(BufferedImage image) {
		JFrame jf = new JFrame();
		JLabel jl = new JLabel();
		;
		ImageIcon ii = new ImageIcon(image);
		jl.setIcon(ii);
		jf.add(jl);
		jf.pack();
		jf.setVisible(true);
	}

	public static void createAndSave(BufferedImage image) throws Exception {

		File outputfile = new File("out.png");
		ImageIO.write(image, "png", outputfile);
		System.out.println("wo speicherst du arsch? " + outputfile.toString());
	}

}