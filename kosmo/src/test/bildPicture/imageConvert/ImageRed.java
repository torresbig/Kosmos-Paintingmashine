package test.bildPicture.imageConvert;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImageRed {

	/*
	 * i. Algorithm for converting colored image to red hued:
	 * 
	 * Get the RGB estimation of the pixel. Set the RGB esteems as takes after: R:
	 * NO CHANGE G: Set to 0 B: Set to 0
	 * 
	 * Supplant the R, G and B estimation of the pixel with the qualities figured in
	 * stage 2. Rehash Step 1 to Step 3 for every pixel of the picture. Do you Know
	 * Method Overloading vs Overriding in Java
	 * 
	 * ii. Implementation of the algorithm
	 */

	public static void main(String args[]) throws IOException {
		BufferedImage img = null;
		File f = null;
		try {
			f = new File("./src/test/bildPicture/test_2.bmp");
			img = ImageIO.read(f);
		} catch (IOException e) {
			System.out.println(e);
		}
		int width = img.getWidth();
		int height = img.getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int p = img.getRGB(x, y);
				int a = (p >> 24) & 0xff;
				int r = (p >> 16) & 0xff;
				p = (a << 24) | (r << 16) | (0 << 8) | 0;
				img.setRGB(x, y, p);
			}
		}
		try {
			f = new File("./src/test/bildPicture/outRgb.jpg");
			ImageIO.write(img, "jpg", f);
			ImageToGrey.showResult(img);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}