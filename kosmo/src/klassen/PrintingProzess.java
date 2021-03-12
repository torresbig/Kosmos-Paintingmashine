package klassen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import enu.Calibrate;
import enu.Commands;
import enu.GuiComponente;
import gui.MainWindow;
import main.Datenpool;

public class PrintingProzess {

	MainWindow mainFrame;;
	Map<Integer, List<String>> bildMap;
	boolean goToNextLine;
	public enu.PrintingProzess printingProzess;
	
	private BackgroundPrintLoop bgpl;

	public PrintingProzess(MainWindow mainFrame) {
		this.mainFrame = mainFrame;
		this.goToNextLine = false;
		this.printingProzess = enu.PrintingProzess.IDLE;
	}

	public void excecute() {
		bgpl = new BackgroundPrintLoop(this);
		bgpl.execute();

	}

	public void resume() {
		start();
		this.mainFrame.tabbedPanels.tglbtnAuto.setSelected(true);
		this.mainFrame.getArduino().serialWrite(Commands.MODUS_AUTO);
		Helper.guiUpdater(this.mainFrame, new ArrayList<GuiComponente>(
				List.of(GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL)));
	}

	public void pause() {
		this.mainFrame.getArduino().setArduinoCommunication("Druckvorgang PAUSE");
		this.mainFrame.tabbedPanels.tglbtnManuel.setSelected(true);
		this.mainFrame.getArduino().serialWrite(Commands.MODUS_MANUELL);
		this.printingProzess = enu.PrintingProzess.PAUSE;
		Helper.guiUpdater(this.mainFrame, new ArrayList<GuiComponente>(
				List.of(GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL)));
	}

	public void cancel() {
		this.mainFrame.getArduino().setArduinoCommunication("Druckvorgang ABBRUCH");
		this.mainFrame.getArduino().setCalibrateStatus(Calibrate.UNBESTIMMT);
		this.mainFrame.tabbedPanels.tglbtnManuel.setSelected(true);
		this.mainFrame.getArduino().serialWrite(Commands.MODUS_MANUELL);
		bgpl.cancel(true);
		this.bildMap = null;
		this.printingProzess = enu.PrintingProzess.IDLE;
		Helper.guiUpdater(this.mainFrame, new ArrayList<GuiComponente>(
				List.of(GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL)));
	}
	
	public void start() {
		this.mainFrame.getArduino().setArduinoCommunication("Druckvorgang START");
		this.printingProzess = enu.PrintingProzess.RUN;
		Helper.guiUpdater(this.mainFrame, new ArrayList<GuiComponente>(
				List.of(GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL)));
		this.mainFrame.dataPool.logger.info("PrintingProzess - Background   //  start() ");

	}
	
	public void ende() {
		this.mainFrame.getArduino().setArduinoCommunication("Druckvorgang ENDE");
		this.bildMap = null;
		this.printingProzess = enu.PrintingProzess.IDLE;
		Helper.guiUpdater(this.mainFrame, new ArrayList<GuiComponente>(
				List.of(GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL)));
		this.mainFrame.dataPool.logger.info("PrintingProzess - Background   //  ende() ");

	}

//    public void printProcess() {
//		if (dp.getArduino().isConnected() && bildMap != null) {
//			
//			startPrinting();
//			for (int x = bildMap.size() - 1; x >= 0; x--) {
//				String zeichenZeile = "";
//				List<String> list = bildMap.get(x);
//				zeichenZeile = String.join("", list);
//				dp.getArduino().serialWritePictureLine(zeichenZeile);
//				dp.getArduino().setArduinoCommunication("Druck der Zeile: " + x + " gestartet");
//				while (dp.getArduino().goToNextLine == false) {
//					dp.getArduino().serialRead();
//				}
//				dp.getArduino().goToNextLine = false;
//			}
//		}
//	}

//    public static void printProcess(boolean stop) {
//		Datenpool dp = PaintingMaschine.detectDatapool();
//		int currentLine = 0;
//		if (dp.getArduino().isConnected() && dp.getBildDetails() != null) {
//			dp.getArduino().startPrinting();
//			Map<Integer, List<String>> map = dp.getBildDetails().getBlackWhiteMap();
//			if(dp.currentLine >0 && dp.getArduino().getState().isPAUSE()) {
//				currentLine = dp.currentLine;
//			}
//			else {
//				currentLine = map.size() - 1;
//			}
//			
//			for (int x = currentLine; x >= 0; x--) {
//				dp.currentLine = x;
//				String zeichenZeile = "";
//				List<String> list = map.get(x);
//				zeichenZeile = String.join("", list);
//				dp.getArduino().serialWritePictureLine(zeichenZeile);
//				dp.getArduino().setArduinoCommunication("Druck der Zeile: " + x + " gestartet");
//				// boolean nextLine = false;
//				// hier anpassen, wieviel durchgänge überprüft werden soll. TODO: noch ein
//				// Sleeptimer einbauen
//				while (dp.getArduino().nextLine == false || stop) {
//					dp.getArduino().serialRead();
//				}
//				
//				dp.getArduino().nextLine = false;
//			}
//		}
//		
//	}

}
