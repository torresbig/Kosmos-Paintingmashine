package enu;

public enum Calibrate {

	CALIBRATED,
	REFERENZWERT,
	UNBESTIMMT;
	
	public boolean isCALIBRATED() {
		return this ==CALIBRATED;
	}
	public boolean isREFERENZWERT() {
		return this ==REFERENZWERT;
	}
	public boolean isUNBESTIMMT() {
		return this ==UNBESTIMMT;
	}
	
}
