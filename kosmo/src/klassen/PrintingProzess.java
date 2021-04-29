package klassen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import enu.Calibrate;
import enu.Commands;
import enu.GuiComponente;
import gui.MainWindow;

import main.PaintingMaschine;

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

	/**
	 * Druckvorgang RESUME >> resume()
	 * <p>
	 * - start() wird aufgerufen <br>
	 * - Auto-Button wird aktiviert <br>
	 * - SerialWirte - MODUS_AUTO wird gesetzt
	 * <p>
	 * Gui Aktualisierung: GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL,
	 * GuiComponente.TABBEDPANEL
	 */
	public void resume() {
		start();
		this.mainFrame.tabbedPanels.tglbtnAuto.setSelected(true);
		this.mainFrame.getArduino().serialWrite(Commands.MODUS_AUTO);
		Helper.guiUpdater(this.mainFrame, new ArrayList<GuiComponente>(
				List.of(GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL)));
	}

	/**
	 * Druckvorgang PAUSE >> pause()
	 * <p>
	 * Alles wird auf Pause gesetzt <br>
	 * - Arduino Communication <br>
	 * - Manuel-Button wird aktiviert <br>
	 * - SerialWirte - MODUS_MANUELL wird gesetzt - PrintingProzess = PAUSE
	 * <p>
	 * Gui Aktualisierung: GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL,
	 * GuiComponente.TABBEDPANEL
	 */
	public void pause() {
		PaintingMaschine.detectStatistik().countPausePrinting();
		this.mainFrame.getArduino().setArduinoCommunication("Druckvorgang PAUSE");
		this.mainFrame.tabbedPanels.tglbtnManuel.setSelected(true);
		this.mainFrame.getArduino().serialWrite(Commands.MODUS_MANUELL);
		this.printingProzess = enu.PrintingProzess.PAUSE;
		Helper.guiUpdater(this.mainFrame, new ArrayList<GuiComponente>(
				List.of(GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL)));
	}

	/**
	 * Druckvorgang CANCEL >> cancel()
	 * <p>
	 * Alles wird auf Abbrechen gesetzt <br>
	 * - Arduino Communication <br>
	 * - PrintingProzess = IDLE <br>
	 * - Calibrate = UNBESTIMMT <br>
	 * - Manuel-Button wird aktiviert <br>
	 * - SerialWirte - MODUS_MANUELL wird gesetzt - Bildmap wird gelöscht
	 * <p>
	 * - Gui Aktualisierung: GuiComponente.ARDUINOPANEL,
	 * GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL
	 */
	public void cancel() {
		PaintingMaschine.detectStatistik().countCanceldPrinting();
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

	/**
	 * Druckvorgang START >> start()
	 * <p>
	 * Alles wird auf start gesetzt <br>
	 * - Arduino Communication <br>
	 * - PrintingProzess = RUN
	 * <p>
	 * - Gui Aktualisierung: GuiComponente.ARDUINOPANEL,
	 * GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL
	 */
	public void start() {
		this.mainFrame.getArduino().setArduinoCommunication("Druckvorgang START");
		this.printingProzess = enu.PrintingProzess.RUN;
		Helper.guiUpdater(this.mainFrame, new ArrayList<GuiComponente>(
				List.of(GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL)));
		this.mainFrame.datenPool.logger.info("PrintingProzess - Background   //  start() ");

	}

	/**
	 * Druckvorgang ENDE >> ende()
	 * <p>
	 * Alles wird auf Ende gesetzt <br>
	 * - Arduino Communication <br>
	 * - PrintingProzess = IDLE <br>
	 * - Bildmap wird gelöscht<br>
	 * <p>
	 * - Gui Aktualisierung: GuiComponente.ARDUINOPANEL,
	 * GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL
	 */
	public void ende() {
		PaintingMaschine.detectStatistik().countPrintedPicture();
		this.mainFrame.getArduino().setArduinoCommunication("Druckvorgang ENDE");
		this.bildMap = null;
		this.printingProzess = enu.PrintingProzess.IDLE;
		Helper.guiUpdater(this.mainFrame, new ArrayList<GuiComponente>(
				List.of(GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL)));
		this.mainFrame.datenPool.logger.info("PrintingProzess - Background   //  ende() ");

	}

}
