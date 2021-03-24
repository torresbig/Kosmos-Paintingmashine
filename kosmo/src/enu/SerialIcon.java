package enu;

public enum SerialIcon {

	WRITE,
	READ;
	
	public boolean isWrite() {
		return this == WRITE;
	}

	public boolean isRead() {
		return this == READ;
	}
}
