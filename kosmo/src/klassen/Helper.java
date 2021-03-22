package klassen;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fazecast.jSerialComm.SerialPort;
import enu.GuiComponente;
import gui.ArduinoEinstellungen;
import gui.MainWindow;
import gui.MenuBar;
import gui.PicturePrintingPanel;
import gui.TabbedPanels;
import main.Datenpool;
import main.PaintingMaschine;

public class Helper {

	public static Integer[] getNewImgSize(BufferedImage img) {
		double w = 0;
		double h = 0;

		if (img.getWidth() >= img.getHeight()) {

			double a = (img.getWidth() * 100) / 550;
			w = ((img.getWidth() * (100 - a)) / a) + img.getWidth();
			h = ((img.getHeight() * (100 - a)) / a) + img.getHeight();
		} else {
			int a = (img.getHeight() * 100) / 550;
			h = ((img.getHeight() * (100 - a)) / a) + img.getHeight();
			w = ((img.getWidth() * (100 - a)) / a) + img.getWidth();
		}
		Integer[] result = { (int) w, (int) h };

		return result;
	}

	public static Bilddetails getLastPicture(Bilddetails bildDetails, PicturePrintingPanel ppp) {
		Bilddetails result = bildDetails;
		String printOut = "";
		result.setOriginalPicture();

		if (ppp.btnFarbenReduzieren.isSelected()) {
			result.setCurrentPicture(bildDetails.getIndexedPicture());
			printOut = printOut == "" ? "Indexed Picture aktiviert" : printOut + " | Indexed Picture aktiviert";
		}
		if (ppp.btnBlackWhite.isSelected()) {
			result.setCurrentPicture(bildDetails.getBlackWhitePicture());
			printOut = printOut == "" ? "Black/White Picture aktiviert"
					: printOut + " | Black/White  Picture aktiviert";
		}
		if (ppp.btnGraustufen.isSelected()) {
			result.setCurrentPicture(bildDetails.getGreyPicture());
			printOut = printOut == "" ? "Graustufen Picture aktiviert" : printOut + " | Graustufen Picture aktiviert";
		}
		if (ppp.btnInvertColors.isSelected()) {
			result.setCurrentPicture(bildDetails.getInvertetColorPicture());
			printOut = printOut == "" ? "Inverted Picture aktiviert" : printOut + " | Inverted Picture aktiviert";
		}

		System.out.println(printOut);

		return result;
	}

	@SuppressWarnings("unused")
	private static void listInDatei(List<String> list, File datei) {
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new FileWriter(datei));
			int i = list.size();
			Iterator<String> iterator = list.iterator();
			while (iterator.hasNext()) {
				String s = iterator.next();
				printWriter.println("ZEILE " + i + ": " + s);
				i--;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (printWriter != null)
					printWriter.close();
			} catch (Exception ioe) {
				PaintingMaschine.detectDatapool().logger.info("HELPER Exception " +ioe.getMessage());
				ioe.getStackTrace();
			}
		}
	}

	public static void guiUpdater(MainWindow mainFrame, GuiComponente komponente) {
		guiUpdater(mainFrame.tabbedPanels, mainFrame.picPrintPanel, mainFrame.menuBar, mainFrame.arduinoConf,
				komponente);

	}

	public static void guiUpdater(TabbedPanels tabbedPanels, PicturePrintingPanel picPrintPanel, MenuBar menuBar,
			ArduinoEinstellungen arduinoConf, GuiComponente komponente) {
		Datenpool dp = PaintingMaschine.detectDatapool();
		switch (komponente) {
		case ARDUINOPANEL:
			tabbedPanels.refreshArduinoConnectPanel();
			break;
		case TABBEDPANEL:
			tabbedPanels.refreshMainPanel();
			tabbedPanels.refreshConfig();
			tabbedPanels.refreshArduinoConnectPanel();
			break;
		case CONFIGPANEL:
			tabbedPanels.refreshConfig();
			break;
		case MAINPANEL:
			tabbedPanels.refreshMainPanel();
			break;
		case PICTUREPANEL:
			break;
		case PICTUREPRINTINGPANEL:
			picPrintPanel.updatePrintingPanel();
			break;
		case PRINTINGPANEL:
			picPrintPanel.updatePrintingPanel();
			break;
		case MENU:
			menuBar.updateMenuBar(dp.getArduino());
			break;
		case ARDUINOCONF:
			arduinoConf.generateZeilen();
			arduinoConf.updateDruckformat();
			break;
		default:
			break;
		}
	}

	public static void guiUpdater(PicturePrintingPanel picPrintPanel) {
		guiUpdater(null, picPrintPanel, null, null, GuiComponente.PRINTINGPANEL);
	}

	public static void guiUpdater(TabbedPanels tabbedPanel) {
		guiUpdater(tabbedPanel, null, null, null, GuiComponente.TABBEDPANEL);
	}

	public static void guiUpdater(MainWindow mainFrame, ArrayList<GuiComponente> komponenten) {
		for (GuiComponente kom : komponenten) {
			guiUpdater(mainFrame, kom);
		}
	}

	// returns "false" falls mehr als ein Comport zurück kommt.
	public static String getTheOneComPort() {
		String result = "false";
		SerialPort[] portNames = SerialPort.getCommPorts();
		if (portNames.length == 1) {
			try {
				result = portNames[0].getSystemPortName();
			}
			catch (Exception e) {
				result = "false";
				PaintingMaschine.detectDatapool().logger.info("Helper - getTheOneComPort Exception: "+ e.getMessage());
			}
			
		} else {
			result = "false";
		}
		return result;
	}

	// methode um die leeren Zeilen beim drucken raus zu filtern

	public static Map<Integer, List<String>> getMapOhneLeerdruck(Map<Integer, List<String>> map) {
		Map<Integer, List<String>> m = new HashMap<Integer, List<String>>();
		int mapKey = 1;
		for (int i = 0; i <= map.size(); i++) {
			
			int whileI =i;
			boolean weiter = true;
			List<String> nl = new ArrayList<>();
			while (weiter && map.get(whileI) != null) {
				
				if (map.get(whileI).contains("1")) {
					weiter = false;
					if(nl.isEmpty()) {
						nl = map.get(whileI);
					}
				} else {
					whileI++;
					if(nl.isEmpty()) {
						nl.add("0");
					}
					else {
						nl.add("><0");
					}

				}
				System.out.println("Map schleife: " + weiter + " / i: " + i + " / whileI: " + whileI + " / Liste: "+ nl.toString());
			}
			if(nl.size()>=1) {
				m.put(mapKey, nl);
				PaintingMaschine.detectDatapool().logger.info("Map komprimiert: Map Size "+ m.size() + "  mapKey: " + mapKey +" <<< Map keys "+ m.keySet());
				mapKey++;
			}
			i = whileI;
		}
		return m;
	}

}
