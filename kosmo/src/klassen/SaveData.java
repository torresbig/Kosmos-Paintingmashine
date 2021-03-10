package klassen;

import java.io.File;
import java.io.Serializable;

import gui.ArduinoEinstellungen;
import klassen.arduino.Arduino;

public class SaveData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Arduino arduino;
	private File dateiName;
	private File pfad;
	private Statistik statistik;



	public SaveData(Arduino arduino, File dateiName, File pfad, Statistik statistik) {
		
		this.arduino = arduino;
		this.dateiName = dateiName;
		this.pfad = pfad;
		this.statistik = statistik;
	}

	public Arduino getArduino() {
		System.out.println("LOAD arduino: " +arduino.getPortDescription() + " | " + arduino.getCalibrateStatus() + " | " + arduino.zahlenZusammen+ " | " + arduino.getEinstellungen());
		return arduino;
	}

	public void setArduino(Arduino arduino) {
		this.arduino = arduino;
	}

	public File getDateiName() {
		System.out.println("LOAD dateiName: " +dateiName);
		return dateiName;
	}

	public void setDateiName(File dateiName) {
		this.dateiName = dateiName;
	}

	public File getPfad() {
		System.out.println("LOAD pfad: " +pfad);
		return pfad;
	}

	public void setPfad(File pfad) {
		this.pfad = pfad;
	}

	public Statistik getStatistik() {
		System.out.println("LOAD statistik: " +statistik);
		return statistik;
	}

	public void setStatistik(Statistik statistik) {
		this.statistik = statistik;
	}
	public String getCompletFilePathName() {
		System.out.println("LOAD getCompletFilePathName: " +this.pfad + File.separator + this.dateiName);
		return this.pfad + File.separator + this.dateiName;
	}

}