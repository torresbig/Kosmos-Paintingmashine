package main;

import java.io.File;
import java.io.Serializable;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;

import com.fazecast.jSerialComm.SerialPort;

import klassen.Bilddetails;
import klassen.PrintingProzess;
import klassen.SaveData;
import klassen.Statistik;
import klassen.arduino.Arduino;
import klassen.arduino.ReadWriteData;

public class Datenpool implements Serializable {
    private static final long serialVersionUID = 1L;

	private String version;
	private Bilddetails bildDetails;
	private Arduino arduino;
	private File dateiName;
	private File pfad;
	private Statistik statistik;
	private PrintingProzess printingProzess;
	public Logger logger;
	private FileHandler fh;  



	public PrintingProzess getPrintingProzess() {
		return printingProzess;
	}

	public void setPrintingProzess(PrintingProzess printingProzess) {
		this.printingProzess = printingProzess;
	}

	Datenpool(String version) {
		this.version = version;
		this.arduino = null;
		this.bildDetails = null;
		this.dateiName = null;
		this.pfad = null;
		this.statistik = null;
		this.printingProzess = null;
		this.logger = null;
		this.setFh(null);

	}
	
	public void loadData(SaveData sd) {
		this.arduino = sd.getArduino();
		this.arduino.createComPortFromDescription();
		this.arduino.setReadWrite(new ReadWriteData(new DefaultListModel<>()));
		this.dateiName = sd.getDateiName();
		this.pfad = sd.getPfad();
		this.statistik = sd.getStatistik();
	}
	
	public void initialize(String pfad, String dateiName) {
		this.dateiName = new File(dateiName);
		this.pfad = new File(pfad);
		this.logger= Logger.getLogger("logger");  

	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Bilddetails getBildDetails() {
		return bildDetails;
	}

	public void setBildDetails(Bilddetails bildDetails) {
		this.bildDetails = bildDetails;
	}

	public Arduino getArduino() {
		return arduino;
	}

	public void setArduino(Arduino arduino) {
		this.arduino = arduino;
	}

	public File getDateiName() {
		return dateiName;
	}

	public void setDateiName(File dateiName) {
		this.dateiName = dateiName;
	}
	
	public File getDateiPfad() {
		return pfad;
	}

	public void setDateiPfad(File pfad) {
		this.pfad = pfad;
	}
	
	public String getCompletFilePathName() {
		return this.pfad + File.separator + this.dateiName;
	}
	
	public Statistik getStatistik() {
		return statistik;
	}

	public void setStatistik(Statistik statistik) {
		this.statistik = statistik;
	}

	public FileHandler getFh() {
		return fh;
	}

	public void setFh(FileHandler fh) {
		this.fh = fh;
	}
}
