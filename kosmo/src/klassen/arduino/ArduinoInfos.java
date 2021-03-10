package klassen.arduino;

import java.io.Serializable;

public class ArduinoInfos implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int index;
	public String name;
	public String wert;
	
	ArduinoInfos(int index, String name, String wert){
		this.index = index;
		this.name = name;
		this.wert = wert;
	}
	
	public String getTextZeile() {
		return this.index + ": " + this.name + " >> "+ this.wert;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getWert() {
		return this.wert;
	}

	public int getIndex() {
		return this.index;
	}

}
