package enu;

public enum GuiComponente {

	TABBEDPANEL,
	PICTUREPRINTINGPANEL,
	PRINTINGPANEL,
	PICTUREPANEL,
	ARDUINOPANEL,
	MAINPANEL,
	CONFIGPANEL,
	MENU, ARDUINOCONF;
	

	
	public boolean isTABBEDPANEL() {
		return this == TABBEDPANEL;
	}
	public boolean isPICTUREPRINTINGPANEL() {
		return this == PICTUREPRINTINGPANEL;
	}
	public boolean isPRINTINGPANEL() {
		return this == PRINTINGPANEL;
	}
	public boolean isPICTUREPANEL() {
		return this == PICTUREPANEL;
	}
	public boolean isARDUINOPANEL() {
		return this == ARDUINOPANEL;
	}
	public boolean isMAINPANEL() {
		return this == MAINPANEL;
	}
	public boolean isCONFIGPANEL() {
		return this == CONFIGPANEL;
	}
	public boolean isMENU() {
		return this == MENU;
	}
	public boolean isARDUINOCONF() {
		return this == ARDUINOCONF;
	}
	
	
}
