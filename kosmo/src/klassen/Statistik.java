package klassen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Statistik implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int printedLines;
	private int printedPixel;
	private int printedPictures;
	private int startPrintingProcess;
	private int openProgram;
	private int connectArduino;
	private int canceldPrinting;
	private int pausePrinting;
	private int openPicture;
	private int serialWrites;
	private int serialReads;

	public Statistik() {
		this.connectArduino = 0;
		this.openProgram = 0;
		this.printedLines = 0;
		this.printedPictures = 0;
		this.printedPixel = 0;
		this.startPrintingProcess = 0;
		this.canceldPrinting = 0;
		this.pausePrinting = 0;
		this.openPicture = 0;
		this.serialReads =0;
		this.serialWrites = 0;
	}

	public int getPrintedLines() {
		return printedLines;
	}

	public int getPrintedPixel() {
		return printedPixel;
	}

	public int getPrintedPictures() {
		return printedPictures;
	}

	public int getOpenProgram() {
		return openProgram;
	}

	public int getConnectArduino() {
		return connectArduino;
	}

	public void countLinePixel(int pixel) {
		countPrintedLine();
		this.printedPixel = pixel;
	}

	public void countPrintedLine() {
		this.printedLines++;
	}

	public void countPrintedPixel() {
		this.printedPixel++;
	}

	public void countPrintedPicture() {
		this.printedPictures++;
	}

	public void countOpenProgram() {
		this.openProgram++;
	}

	public void countConnectArduino() {
		this.connectArduino++;
	}

	public int getStartPrintingProcess() {
		return startPrintingProcess;
	}

	public void countStartPrintingProcess() {
		this.startPrintingProcess++;
	}

	public int getCanceldPrinting() {
		return canceldPrinting;
	}

	public void countCanceldPrinting() {
		this.canceldPrinting++;
	}

	public int getPausePrinting() {
		return pausePrinting;
	}

	public void countPausePrinting() {
		this.pausePrinting++;
	}

	public int getOpenPicture() {
		return openPicture;
	}

	public void countOpenPicture() {
		this.openPicture++;
	}
	
	public int getSerialWrites() {
		return serialWrites;
	}

	public void countSerialWrites() {
		this.serialWrites++;
	}

	public int getSerialReads() {
		return serialReads;
	}

	public void countSerialReads() {
		this.serialReads++;
	}
	
	public List<String> getStatistik() {
		List<String> st = new ArrayList<String>();
	
		st.add("<<  ALLGEMEIN  >>");
		st.add("- Programm geöffnet:   "+this.openProgram);
		st.add("- Bild geöffnet:   "+this.openPicture);
		st.add(" ");
		st.add("<<  DRUCKVORGANG  >>");
		st.add("- Drucken abgebrochen:   "+this.canceldPrinting);
		st.add("- Drucken gestartet:   "+this.startPrintingProcess);
		st.add("- Drucken pause:   "+this.pausePrinting);
		st.add("- gesendete Zeilen:   "+this.printedLines);
		st.add("- gesendete Pixel:   "+this.printedPixel);
		st.add("- gedruckte Bilder:   "+this.printedPictures);
		st.add(" ");
		st.add("<<  ARDUINO  >>");
		st.add("- Arduino verbunden:   "+this.connectArduino);
		st.add("- Arduino SerialRead:   "+this.serialReads);
		st.add("- Arduino SerialWrite:   "+this.serialWrites);
		return st;
	}

}
