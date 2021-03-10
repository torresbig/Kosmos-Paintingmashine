package klassen;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import main.PaintingMaschine;

import java.awt.Color;

public class Bilddetails implements Serializable {
    private static final long serialVersionUID = 1L;

	private BufferedImage picture;
	final protected BufferedImage origPicture;
	public String name;
	public String pfad;
	public Map<Color, Map<Integer, List<Integer>>> mapFarben; // Key = Zeile, Value = Liste der Reihe
	public Map<Integer, List<Integer>> mapBlackWhite;

	public Bilddetails(BufferedImage bild, String name, String pfad) {
		this.origPicture = bild;
		this.picture = bild;
		this.name = name;
		this.pfad = pfad;
		
	}
	public static Integer getColorCount(BufferedImage img) {
		return getColorList(getRGBFarben(img)).size();
	}
	public Integer getColorCountCurrentPicture() {
		return getColorCount(this.picture);
	}
	
	public void setCurrentPicture(BufferedImage img) {
		this.picture = img;
	}
	
	public BufferedImage getCurrentPicture() {
		return this.picture;
	}
	
	public BufferedImage getOriginalPicture() {
		return this.origPicture;
	}
	
	public void setOriginalPicture() {
		this.picture = this.origPicture;
	}
	

	
	public int getWidth() {
		return this.picture.getWidth();
	}
	public int getHeight() {
		return this.picture.getHeight();
	}
	
	public BufferedImage  getIndexedPicture() {
		BufferedImage output = new BufferedImage(this.picture.getWidth(), this.picture.getHeight(), BufferedImage.TYPE_BYTE_INDEXED); //Den Typ des Bildes kann man nach seinen Bedürfnissen anpassen
		output.getGraphics().drawImage(this.picture, 0, 0, null);
		return output;
	}
	
	public BufferedImage getGreyPicture() {
		BufferedImage output = new BufferedImage(this.picture.getWidth(), this.picture.getHeight(), BufferedImage.TYPE_BYTE_GRAY); //Den Typ des Bildes kann man nach seinen Bedürfnissen anpassen
		output.getGraphics().drawImage(this.picture, 0, 0, null);
		return output;
		
	}
	
	public BufferedImage getBlackWhitePicture() {
		BufferedImage output = new BufferedImage(this.picture.getWidth(), this.picture.getHeight(), BufferedImage.TYPE_BYTE_BINARY); //Den Typ des Bildes kann man nach seinen Bedürfnissen anpassen
		output.getGraphics().drawImage(this.picture, 0, 0, null);
		return output;
		
	}
	
	public BufferedImage getReducedColorPicture(int step) {
		BufferedImage output = this.picture;//new BufferedImage(this.picture.getWidth(), this.picture.getHeight(),BufferedImage.TYPE_BYTE_INDEXED);
		output.getGraphics().drawImage(ColorDifference.getReducedImage(step, this.picture), 0, 0, null);
		return output;
	}
	

	/*seperate the Color Channels
	 *
	 */
	public BufferedImage getInvertetColorPicture(){
		
		int imageWidth = this.picture.getWidth();
		int imageHeight = this.picture.getHeight();
		
		BufferedImage changedImage = new BufferedImage(imageWidth,imageHeight,this.picture.getType());
		Color color;
		
		for (int x = 0; x < imageWidth; x++) {
           for (int y = 0; y < imageHeight; y++) {
               
               color = new Color(this.picture.getRGB(x, y));
               
               int r = 255- color.getRed();
               int g = 255-color.getGreen();
               int b = 255-color.getBlue();
               
               changedImage.setRGB(x, y,   (new Color(r, g, b)).getRGB());

           }
       }      
       return changedImage;
	}
	
	
	

	public Map<Integer, List<String>> getBlackWhiteMap() {
		Map<Integer, List<String>> f = new HashMap<Integer, List<String>>();
		List<String> l;
		
		for (int y = 0; y < this.picture.getHeight(); y++) {
			l = new ArrayList<String>();
			for (int x = 0; x < this.picture.getWidth(); x++) {
				Color c = new Color(this.picture.getRGB(x, y));
				if (c.getBlue() < 128) {
					l.add("1");
				} else {
					l.add("0");
				}
			}
			f.put(y, l);
		}
		//mapInDatei(f, new File("getBWFarben.txt"));
		return f;

	}
	
	

	/*
	 * Map aufbau wäre am besten Farbe --> Reihe --> Ja/Nein
	 * 
	 */
	private static Map<Integer, List<Color>> getRGBFarben(BufferedImage img) {
		Map<Integer, List<Color>> f = new HashMap<Integer, List<Color>>();
		List<Color> l;
		for (int y = 0; y < img.getHeight(); y++){
			l = new ArrayList<Color>();
			for (int x = 0; x < img.getWidth(); x++) {
				l.add(new Color(img.getRGB(x, y))); // Wenn alle Farben übernommen werden sollen
			}
			f.put(y, l);
		}

		return f;
	}

	public Map<Color, Map<Integer, List<Integer>>> getColorMap() {
		Map<Color, Map<Integer, List<Integer>>> result = new HashMap<Color, Map<Integer, List<Integer>>>();
		Map<Integer, List<Color>> rgbMap = getRGBFarben(this.picture);
		List<Color> colorSet = getColorList(rgbMap);

		if (colorSet.isEmpty()) {
			System.out.println("Keine Farben ermittelt");
			PaintingMaschine.detectDatapool().logger.info("Bilddateils - Keine Farben ermittelt  //  getColorMap() ");

		} else {
			for (Color rgb : colorSet) {
				Map<Integer, List<Integer>> m = new HashMap<Integer, List<Integer>>();
				for (Map.Entry<Integer, List<Color>> entry : rgbMap.entrySet()) {
					List<Integer> l = new ArrayList<Integer>();
					boolean found = false;
					for (int i = 0; i < entry.getValue().size(); i++) {
						if (entry.getValue().get(i).equals(rgb)) {
							l.add(1);
							found = true;
						} else {
							l.add(0);
						}
					}

					if (found == true) {
						m.put(entry.getKey(), l);
					}
				}
				result.put(rgb, m);

			}
		}
		return result;
	}

	private static List<Color> getColorList(Map<Integer, List<Color>> rgbMap) {
		List<Color> colorSet = new ArrayList<Color>();

		for (Map.Entry<Integer, List<Color>> entry : rgbMap.entrySet()) {
			for (Color c : entry.getValue()) {
				if (!colorSet.contains(c)) {
					colorSet.add(c);
				}
			}
		}
		return colorSet;

	}

	// nur zum testen welche werte hier raus kommen, da Liste zu lang für Console
	// ---> listInDatei(colorSet, new File("list.txt")); <<< muss an beliebige
	// stelle eingefügt werden

	private static void mapInDatei(Map<Integer, List<String>> map, File datei) {
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new FileWriter(datei));

			Iterator<Map.Entry<Integer, List<String>>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<Integer, List<String>> entry = iterator.next();
				printWriter.println("ZEILE " + entry.getKey() + ": " + entry.getValue());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (printWriter != null)
					printWriter.close();
			} catch (Exception ioe) {
				PaintingMaschine.detectDatapool().logger.info("BildDatails Exception " +ioe.getMessage());
				ioe.getStackTrace();
			}
		}
	}
//	
//	private static void farbenInDatei(List<Color> colorset, File datei) {
//		PrintWriter printWriter = null;
//		try {
//			printWriter = new PrintWriter(new FileWriter(datei));
//			Iterator<Color> iterator = colorset.iterator();
//			while (iterator.hasNext()) {
//				Color c = iterator.next();
//				printWriter.println(c.toString());
//				
//			}
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (printWriter != null)
//					printWriter.close();
//			} catch (Exception ioe) {
//			}
//		}
//	}

}
