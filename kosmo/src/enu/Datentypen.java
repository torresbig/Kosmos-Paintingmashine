package enu;

public enum Datentypen {
	
	STRING 	("String","Zeichenkette"),
	INTEGER ("Integer", "ganzheitliche Zahlen"),
	CHAR	("Character","Der Datentyp »char« ist ein elementarer Typ. Werte 0 bis 65535, meist Kennzahlen für Zeichen.");
	
	String name;
	String definition;
	
	private Datentypen(String n, String d) {
		this.definition = d;
		this.name = n;
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

}
