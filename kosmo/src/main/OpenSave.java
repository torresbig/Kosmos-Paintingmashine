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
import klassen.Statistik;

public class OpenSave {

	public boolean geladen;

	public static Bilddetails bildOeffnen() {
		Bilddetails img = null;
		// Erstellung unseres FileFilters für Bilddateien
		FileFilter filter = new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp");

		// JFileChooser-Objekt erstellen
		JFileChooser chooser = new JFileChooser(System.getProperty("user.home") + "/Desktop");

		// Filter wird unserem JFileChooser hinzugefügt
		chooser.addChoosableFileFilter(filter);

		// Dialog zum Oeffnen von Dateien anzeigen
		int rueckgabeWert = chooser.showDialog(new JDialog(), "Bild wählen");

		if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {
			try {
				BufferedImage bi = ImageIO.read(chooser.getSelectedFile());
				String p = chooser.getSelectedFile().getPath();
				String n = chooser.getSelectedFile().getName();
				img = new Bilddetails(bi, n, p);
				PaintingMaschine.detectStatistik().countOpenPicture();

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
				System.out.println("Datenpool-Datei existiert");

				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(datei));
				Object rawObject = ois.readObject();
				ois.close();

				if (rawObject instanceof SaveData) {
					sd = (SaveData) rawObject;
					System.out.println("Datenpool-Daten geladen");
					sd.getArduino();
					sd.getCompletFilePathName();
					dp.loadData(sd);

				} else {
					System.err.println("Datenpool-Datei NICHT gelesen");
				}
			} else {
				System.out.println("Datenpool-Datei nicht vorhanden");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dp;
	}

	public static void saveDatapool() {
		Datenpool dp = PaintingMaschine.detectDatapool();
		SaveData sd = new SaveData(dp.getArduino(), dp.getDateiName(), dp.getDateiPfad());
		try {
			File datei = sd.getDateiName();
			File dir = sd.getPfad();
			sd.getArduino();
			if (!Files.exists(Paths.get(sd.getCompletFilePathName()))) {
				dir.mkdir();
				datei.createNewFile();
			}
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(new File(sd.getCompletFilePathName())));

			oos.writeObject(sd);
			oos.flush();
			oos.close();
			System.out.println("Datenpool-Daten gespeichert");

		} catch (IOException e) {
			System.out.println("Datenpool-Daten NICHT gespeichert | FEHLER: " + e.toString());
			dp.logger.info("OpenSave - Datenpool -  Exception " +e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	public static Statistik loadStatistik(String p) {
		Statistik st = null;
		String pfad = p + File.separator + "statistik.dat";
		try {

			File datei = null;
			if (Files.exists(Paths.get(pfad))) {
				datei = new File(pfad);
				System.out.println("Statistik-Datei existiert");

				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(datei));
				Object rawObject = ois.readObject();
				ois.close();

				if (rawObject instanceof Statistik) {
					st = (Statistik) rawObject;
					System.out.println("Statistik-Daten geladen");

				} else {
					
					System.err.println("Statistik-Datei NICHT gelesen << Instanz nicht vom Typ Statistik << Pfad: "+pfad);

				}
			} else {
				System.out.println("Statistik-Datei nicht vorhanden");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		if(st != null) {
			System.out.println("Statistik aufrufe Programm: " +st.getOpenProgram());
		}
		

		return st;
	}

	public static void saveStatistik(String p, Statistik statistik) {
		String pfad = p + File.separator + "statistik.dat";
		
		try {
			File file = new File(pfad);
			File dir = new File(p);
			if (!Files.exists(Paths.get(pfad))) {
				dir.mkdir();
				file.createNewFile();
			}
			ObjectOutputStream oos = new ObjectOutputStream(
					new FileOutputStream(new File(pfad)));

			oos.writeObject(statistik);
			oos.flush();
			oos.close();
			System.out.println("Statistik-Daten gespeichert");

		} catch (IOException e) {
			System.out.println("Daten NICHT gespeichert | FEHLER: " + e.toString());
			PaintingMaschine.detectDatapool().logger.info("OpenSave - Statistik -  Exception " +e.getMessage());
			e.printStackTrace();
		}
	}

	

}
