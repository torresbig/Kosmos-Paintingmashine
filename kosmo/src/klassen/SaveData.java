package klassen;

import java.io.File;
import java.io.Serializable;


import klassen.arduino.Arduino;

public class SaveData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Arduino arduino;
	private File dateiName;
	private File pfad;



	public SaveData(Arduino arduino, File dateiName, File pfad) {
		
		this.arduino = arduino;
		this.dateiName = dateiName;
		this.pfad = pfad;
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

	public String getCompletFilePathName() {
		return this.pfad + File.separator + this.dateiName;
	}

}