package enu;

public enum Commands {
	
	MOTOREN_STOP		("Motoren stopp",1000, 24), //1
	MOTOREN_LINKS		("Motoren links",1500, 24), //1500
	MOTOREN_RECHTS		("Motoren rechts",2000, 24),// 2000
	MOTOREN_HOCH		("Motoren hoch",3000, 24), //3000
	MOTOREN_RUNTER		("Motoren runter",3500, 24),//3500
	MODUS_AUTO			("Modus: Automatik",4500, 24), // automatic an/aus --> select button 4500
	MODUS_MANUELL		("Modus: Manuell",4000, 24),// automatic an/aus --> select button 4000
	REFERENZWERT		("Referenzwert X=0 und Z=0-",5000, 24), //Counter nullen (X)  --> muss zweimal gedrückt werden!
	DRUCK				("Druck",6000, 24),//6000
	NA					("noch offen",7000, 24),
	UNBESTIMMT			("undefiniert",0,0);
	
	String name;
	int wert;
	int bitRate;
	
	private Commands(String n, int w, int b) {
		this.name = n;
		this.wert = w;
		this.bitRate = b;
	}
	
	public String toString() {
		return this.name;
	}
	
	public int getWert() {
		return this.wert;
	}
	
	
	public String getStringWert() {
		return String.valueOf("@"+this.wert+"#");
	}
	
	public int getBitrate() {
		return this.bitRate;
	}
}
