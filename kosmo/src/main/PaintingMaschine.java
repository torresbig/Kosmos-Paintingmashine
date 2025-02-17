package main;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

import gui.MainWindow;
import klassen.Statistik;
import sun.awt.shell.ShellFolder;

public class PaintingMaschine implements ActionListener {

	private static Datenpool dp;
	private static Statistik statistik;
	private final static String version = "v1.0_BETA";
	private final static String pfad = ShellFolder.get("fileChooserDefaultFolder") + File.separator
			+ "PaintingMaschine";
	private final static String dateiName = "paintingData.dat";

	public static void main(String[] args) {

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				OpenSave.saveDatapool();
				OpenSave.saveStatistik(pfad, statistik);
				System.out.println("Das Programm wurde beendet");
			}
		}));
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					dp = detectDatapool();
					dp.initialize(pfad, dateiName);
					try {

						// This block configure the logger with handler and formatter
						dp.setFh(new FileHandler(pfad + File.separator + "logger.log"));
						dp.logger.addHandler(dp.getFh());
						SimpleFormatter formatter = new SimpleFormatter();
						dp.getFh().setFormatter(formatter);

						// the following statement is used to log any messages
						dp.logger.info("Kosmos Paintingmaschine - Logger");
						dp.logger.info("Version:" + version);

					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					MainWindow window = new MainWindow(dp);
					window.setVisible(true);
				} catch (Exception e) {
					PaintingMaschine.detectDatapool().logger.info("PaintingMaschine -  Exception " + e.getMessage());
					e.getStackTrace();
				}

				try {
					statistik = detectStatistik();
					statistik.countOpenProgram();
				} catch (Exception e) {

				}
			}
		});

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
	
	public static Statistik detectStatistik() {
		if (statistik != null) {
			return statistik;
		} else {
			if (Files.exists(Paths.get(pfad + File.separator + "statistik.dat"))) {
				return OpenSave.loadStatistik(pfad);
			} else {
				new File(pfad).mkdir();
				return new Statistik();
			}
		}
	}

	public static Datenpool detectDatapool() {
		if (dp != null) {
			return dp;
		} else {
			if (Files.exists(Paths.get(getCompletFilePathName()))) {
				return OpenSave.loadDatapool(version, getCompletFilePathName());
			} else {
				new File(pfad).mkdir();
				return new Datenpool(version);
			}

		}
	}

	public static String getCompletFilePathName() {
		return pfad + File.separator + dateiName;
	}

}
