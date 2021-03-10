package klassen;

import java.io.Serializable;

import enu.Datentypen;

public class EinstellungsParameter implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String vorzeichen;
	private String abschlusszeichen;
	private String wert;


	private Datentypen typ;
	
	public EinstellungsParameter(String name, String vorzeichen, String abschlusszeichen, Datentypen typ) {
		this.abschlusszeichen = abschlusszeichen;
		this.name= name;
		this.typ = typ;
		this.vorzeichen = vorzeichen;
	}
	public EinstellungsParameter() {

	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVorzeichen() {
		return vorzeichen;
	}

	public void setVorzeichen(String vorzeichen) {
		this.vorzeichen = vorzeichen;
	}

	public String getAbschlusszeichen() {
		return abschlusszeichen;
	}

	public void setAbschlusszeichen(String abschlusszeichen) {
		this.abschlusszeichen = abschlusszeichen;
	}

	public Datentypen getTyp() {
		return typ;
	}

	public void setTyp(Datentypen typ) {
		this.typ = typ;
	}
	
	public String getWert() {
		return wert;
	}
	public void setWert(String wert) {
		this.wert = wert;
	}
}
