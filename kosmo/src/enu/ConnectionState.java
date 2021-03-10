package enu;

public enum ConnectionState {
	CONNECTED("verbunden"), 
	INITIALISATION("verbinden..."), 
	DISCONNECTED("getrennt");

	public String name;

	private ConnectionState(String n) {
		name = n;
	}

	public String toString() {
		return name;
	}

	public boolean isCONNECTED() {
		return this == CONNECTED;
		 
	}
	public boolean isINITIALISATION() {
		return this == INITIALISATION;
		 
	}
	public boolean isDISCONNECTEDD() {
		return this == DISCONNECTED;
		 
	}

}
