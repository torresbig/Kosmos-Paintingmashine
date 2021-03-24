package klassen;

import java.util.List;
import java.util.Map;
import javax.swing.SwingWorker;

import enu.GuiComponente;
import main.PaintingMaschine;

public class BackgroundPrintLoop extends SwingWorker<Boolean, Integer> {

	PrintingProzess pp;

	public BackgroundPrintLoop(PrintingProzess pp) {
		this.pp = pp;
	}

	protected Boolean doInBackground() throws Exception {
		Boolean result = false;
		try {
			if (pp.bildMap == null) {
				pp.bildMap = Helper.getMapOhneLeerdruck(getBildMap());
			}
			printProcess(pp.bildMap);

		} catch (Exception e) {
			System.err.println("Background Prozess - exception: " + e);
		}
		result = true;
		return result;

	}

	protected void done() {
		try {
			Helper.guiUpdater(pp.mainFrame, GuiComponente.PRINTINGPANEL);
			System.out.println("Backgroundprozess >> DONE");
			pp.mainFrame.dataPool.logger.info("PrintingProzess - Background - DONE //  done()");

		} catch (Exception e) {
			e.printStackTrace();
			pp.mainFrame.dataPool.logger.info("PrintingProzess - Background - DONE FEHLER " + e.getMessage() + " //  done()");
		}
	}

	void printProcess(Map<Integer, List<String>> map) {
		try {
			PaintingMaschine.detectStatistik().countStartPrintingProcess();
			pp.start();
			for (int x = pp.bildMap.size(); x >= 0 && !pp.printingProzess.isIDLE(); x--) {
				String zeichenZeile = "";
				List<String> list = map.get(x);
				zeichenZeile = String.join("", list);
				loopWhilePause();
				pp.mainFrame.getArduino().setArduinoCommunication("Druck der Zeile: " + x + " gestartet");
				pp.mainFrame.getArduino().serialWritePictureLine(zeichenZeile);
				PaintingMaschine.detectStatistik().countLinePixel(zeichenZeile.length());
				pp.mainFrame.dataPool.logger.info("PrintingProzess - "+ pp.printingProzess.toString());
				while (pp.goToNextLine == false && !pp.printingProzess.isIDLE()) {
					loopWhilePause();
					try {
						Thread.sleep(200);
						if (pp.mainFrame.getArduino().serialReadPrintingProzess()) {
							pp.goToNextLine = true;
						}
					} catch (Exception e) {
						e.getStackTrace();
						pp.mainFrame.dataPool.logger.info("PrintingProzess - Background -Loop Exception " + e.toString());
					}
				}
				pp.goToNextLine = false;
			}
			pp.ende();
			
			pp.mainFrame.dataPool.logger.info("PrintingProzess - Background - ENDE");
		} catch (Exception e) {
			e.getStackTrace();
			pp.mainFrame.dataPool.logger.info("PrintingProzess - Background - GESAMT " + e.toString());

		}
	}

	void loopWhilePause() {
		if (pp.printingProzess.isPAUSE()) {
			boolean looping = true;
			int l = 0;
			System.out.println("PauseLOOP ANFANG - Aktueller Status: " + pp.printingProzess.toString());
			while (looping == true) {
				if (!pp.printingProzess.isPAUSE()) {
					looping = false;
				}
				l++;
				try {
					Thread.sleep(100);
					System.out.println("PauseLOOP Mittendrin -" + l);
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
			System.out.println("PauseLOOP ENDE - Aktueller Status: " + pp.printingProzess.toString());
		}
	}



	Map<Integer, List<String>> getBildMap() {
		Map<Integer, List<String>> result;
		if (pp.printingProzess.isPAUSE() && pp.bildMap != null) {
			result = pp.bildMap;
		} else {
			result = pp.mainFrame.getBildDetails().getBlackWhiteMap();
		}

		return result;
	}

}
