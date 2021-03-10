package enu;

public enum PrintingProzess {
	
	RUN,
	IDLE,
	PAUSE;
	

	public boolean isRUN() {
		return this == RUN;
	}
	public boolean isIDLE() {
		return this == IDLE;
	}
	public boolean isPAUSE() {
		return this == PAUSE;
	}

}
