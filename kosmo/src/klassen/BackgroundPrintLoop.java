package klassen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.SwingWorker;

import enu.Commands;
import enu.GuiComponente;

public class BackgroundPrintLoop extends SwingWorker<Boolean, Integer> {

	PrintingProzess pp;

	public BackgroundPrintLoop(PrintingProzess pp) {
		this.pp = pp;
	}

	protected Boolean doInBackground() throws Exception {
		Boolean result = false;
		try {
			System.out.println("CurrentLine: " + pp.currentLine);
			pp.mainFrame.dataPool.logger
					.info("PrintingProzess - Background - CurrentLine: " + pp.currentLine +"STATUS: " + pp.printingProzess.toString() +" //  doInBackground()");
			if (pp.bildMap == null) {
				pp.bildMap = Helper.getMapOhneLeerdruck(getBildMap());
			}

			System.out.println("Bildmap: " + pp.bildMap.toString());
			pp.mainFrame.dataPool.logger
					.info("PrintingProzess - Background - Bildmap: " + pp.bildMap.toString() + " //  doInBackground()");
			System.out.println("ARDUINO CONNECTIONSTATE: "+pp.mainFrame.dataPool.getArduino().isConnected()+ " / STATUS: "  + pp.printingProzess.toString());
				System.out.println("REIN IN DIE OLGA! STATUS: " + pp.printingProzess.toString());
				printProcess(pp.bildMap);
			
		} catch (Exception e) {
			System.out.println("Background Prozess - exception: " + e);
			pp.mainFrame.dataPool.logger
					.info("PrintingProzess - Background - exception: " + e.getMessage() + " //  doInBackground()");
			pp.mainFrame.dataPool.logger
					.info("PrintingProzess - Background - " + this.toString() + " //  doInBackground()");

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
			pp.mainFrame.dataPool.logger
					.info("PrintingProzess - Background - DONE FEHLER " + e.getMessage() + " //  done()");
		}
	}

	void printProcess(Map<Integer, List<String>> map) {
		try {
			if (map != null) {
				if (pp.printingProzess.isPAUSE()) {
					while (pp.goToNextLine == false && pp.printingProzess.isPAUSE()) {
						if (pp.mainFrame.getArduino().serialReadPrintingProzess()) {
								pp.goToNextLine = true;
								pp.mainFrame.dataPool.logger.info(
										"PrintingProzess - Background - RESUME - 1 ERKANNT!  / nextLine:"
												+ pp.goToNextLine + "   //  printProcess(Map<Integer, List<String>> map) ");
							}
							else {
								try {
									Thread.sleep(50);
									pp.mainFrame.dataPool.logger.info(
											"PrintingProzess - Background - RESUME - Sleep 5  / nextLine:"
													+ pp.goToNextLine + "   //  printProcess(Map<Integer, List<String>> map) ");
								} catch (Exception e) {
									e.getStackTrace();
									pp.mainFrame.dataPool.logger.info("PrintingProzess - Background - RESUME "+ e.getMessage() + "   //  printProcess(Map<Integer, List<String>> map) ");

								}
							}
												
					}
					pp.goToNextLine = false;
				}
				int line = getStartLine();
				start();
				for (int x = line; x >= 0 && pp.printingProzess.isRUN(); x--) {
					String zeichenZeile = "";
					pp.currentLine = x;
					List<String> list = map.get(x);
					zeichenZeile = String.join("", list);
					pp.mainFrame.getArduino().setArduinoCommunication("Druck der Zeile: " + x + " gestartet");
					pp.mainFrame.getArduino().serialWritePictureLine(zeichenZeile);
					while (pp.goToNextLine == false && pp.printingProzess.isRUN()) {
							if (pp.mainFrame.getArduino().serialReadPrintingProzess()) {
								pp.goToNextLine = true;
								pp.mainFrame.dataPool.logger.info(
										"PrintingProzess - Background - RUN - 1 ERKANNT! / nextLine:"
												+ pp.goToNextLine + "   //  printProcess(Map<Integer, List<String>> map) ");
							}
							else {
								try {
									Thread.sleep(50);
									pp.mainFrame.dataPool.logger.info(
											"PrintingProzess - Background - RUN - Sleep 5 - nextLine:"
													+ pp.goToNextLine + "   //  printProcess(Map<Integer, List<String>> map) ");

								} catch (Exception e) {
									e.getStackTrace();
									pp.mainFrame.dataPool.logger.info("PrintingProzess - Background -RUN " + e.getMessage()
											+ "   //  printProcess(Map<Integer, List<String>> map) ");

								}
							}

					}
					pp.goToNextLine = false;
				}
				if (pp.printingProzess.isRUN()) {
					ende();
					pp.mainFrame.dataPool.logger.info(
							"PrintingProzess - Background - ENDE //  printProcess(Map<Integer, List<String>> map) ");

				}
			}
		} catch (Exception e) {
			e.getStackTrace();
			pp.mainFrame.dataPool.logger.info("PrintingProzess - Background -ENDE " + e.getMessage()
					+ "   //  printProcess(Map<Integer, List<String>> map) ");

		}
	}

	int getStartLine() {
		int line = 0;
		if (pp.printingProzess.isPAUSE() && pp.currentLine > 0) {
			line = pp.currentLine - 1;
			pp.mainFrame.dataPool.logger.info("PrintingProzess - Background - Startline: " + line
					+ "   //  getStartLine() --> verwendet: pp.currentLine - 1");
		} else {
			line = pp.bildMap.size() - 1;
			pp.mainFrame.dataPool.logger.info("PrintingProzess - Background - Startline: " + line
					+ "   //  getStartLine() --> verwendet: pp.bildMap.size() - 1");
		}
		return line;
	}

	Map<Integer, List<String>> getBildMap() {
		Map<Integer, List<String>> result;
		if (pp.printingProzess.isPAUSE() && pp.bildMap != null) {
			result = pp.bildMap;
			pp.mainFrame.dataPool.logger.info("PrintingProzess - Background - Startline: " + result
					+ "   //  getBildMap() --> verwendet: pp.bildMap");

		} else {
			result = pp.mainFrame.getBildDetails().getBlackWhiteMap();
			pp.mainFrame.dataPool.logger.info("PrintingProzess - Background - Startline: " + result
					+ "   //  getBildMap() --> verwendet: pp.mainFrame.getBildDetails().getBlackWhiteMap()");

		}

		return result;
	}

	public void start() {
		pp.mainFrame.getArduino().setArduinoCommunication("Druckvorgang START");
		pp.printingProzess = enu.PrintingProzess.RUN;
		Helper.guiUpdater(this.pp.mainFrame, new ArrayList<GuiComponente>(
				List.of(GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL)));
		pp.mainFrame.dataPool.logger.info("PrintingProzess - Background   //  start() ");

	}

	public void ende() {
		pp.mainFrame.getArduino().setArduinoCommunication("Druckvorgang ENDE");
		pp.bildMap = null;
		pp.currentLine = 0;
		pp.printingProzess = enu.PrintingProzess.IDLE;
		Helper.guiUpdater(this.pp.mainFrame, new ArrayList<GuiComponente>(
				List.of(GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL)));
		pp.mainFrame.dataPool.logger.info("PrintingProzess - Background   //  ende() ");

	}

}
