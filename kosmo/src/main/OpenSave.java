package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import klassen.Bilddetails;
import klassen.SaveData;

public class OpenSave {

	public boolean geladen;

	public static Bilddetails bildOeffnen() {
		Bilddetails img = null;
		// Erstellung unseres FileFilters f�r Bilddateien
		FileFilter filter = new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp");

		// JFileChooser-Objekt erstellen
		JFileChooser chooser = new JFileChooser(System.getProperty("user.home") + "/Desktop");

		// Filter wird unserem JFileChooser hinzugef�gt
		chooser.addChoosableFileFilter(filter);

		// Dialog zum Oeffnen von Dateien anzeigen
		int rueckgabeWert = chooser.showDialog(new JDialog(), "Bild w�hlen");

		if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {
			try {
				BufferedImage bi = ImageIO.read(chooser.getSelectedFile());
				String p = chooser.getSelectedFile().getPath();
				String n = chooser.getSelectedFile().getName();
				img = new Bilddetails(bi, n, p);
			} catch (IOException ex) {
				ex.printStackTrace();
				System.out.println("Fehler beim Laden des Bildes!");
			}

		}

		return img;
	}

	public static Datenpool loadDatapool(String version, String pfad) {
		SaveData sd = null;
		Datenpool dp = new Datenpool(version);
		try {

			File datei = null;
			if (Files.exists(Paths.get(pfad))) {
				datei = new File(pfad);
				System.out.println("Datei existiert");

				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(datei));
				Object rawObject = ois.readObject();
				ois.close();

				if (rawObject instanceof SaveData) {
					sd = (SaveData) rawObject;
					System.out.println("Daten geladen");
					sd.getArduino();
					sd.getCompletFilePathName();
					sd.getStatistik();
					dp.loadData(sd);

				} else {
					System.err.println("Datei NICHT gelesen");
				}
			} else {
				System.out.println("Datei nicht vorhanden");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dp;
	}

	public static void saveDatapool() {
		Datenpool dp = PaintingMaschine.detectDatapool();
		SaveData sd = new SaveData(dp.getArduino(), dp.getDateiName(), dp.getDateiPfad(), dp.getStatistik());
		try {
			File datei = sd.getDateiName();
			File dir = sd.getPfad();
			sd.getArduino();
			if (!Files.exists(Paths.get(sd.getCompletFilePathName()))) {
				dir.mkdir();
				// dir.mkdirs();
				datei.createNewFile();
				System.out.println(
						"PFad erstellt" + "existiert jetzt?: " + Files.exists(Paths.get(sd.getCompletFilePathName())));
			}
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(new File(sd.getCompletFilePathName())));

			oos.writeObject(sd);
			oos.flush();
			oos.close();
			System.out.println("Daten gespeichert");

		} catch (IOException e) {
			System.out.println("Daten NICHT gespeichert | FEHLER: " + e.toString());
			dp.logger.info("OpenSave -  Exception " +e.getMessage());
			e.printStackTrace();
		}
	}

}
