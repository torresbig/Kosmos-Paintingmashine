package gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import klassen.arduino.Arduino;

public class MenuBar extends JMenuBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MenuBar() {
		super();
		initMenuStart();
		initMenuConfig();
	}


	public JMenu mnStart = new JMenu("Start");
	//public JMenuItem mntmOpen = new JMenuItem("\u00D6ffnen");
	public JMenuItem mntmSave = new JMenuItem("Speichern");
	public JMenuItem mntmClose = new JMenuItem("Beenden");
	public JMenu mnConfig = new JMenu("Einstellungen");
	public JMenuItem mntmArduinoConnect = new JMenuItem("Arduino verbinden");
	public JMenuItem mntmKommandosFestlegen = new JMenuItem("Kommandos festlegen");
	public JMenu mnInfo = new JMenu("Info");
	public JMenuItem mntmKontakt = new JMenuItem("Kontakt");
	public JMenuItem mntmStatistik = new JMenuItem("Statistik");
	public JMenuItem mntmReleasNotes = new JMenuItem("Releasenotes");
	

	private void initMenuStart() {
//		mntmOpen.setIcon(null);
//		mnStart.add(mntmOpen);
		
		mntmSave.setIcon(null);
		mnStart.add(mntmSave);
		
		mntmClose.setIcon(null);
		mnStart.add(mntmClose);
		
		add(mnStart);
	}


	private void initMenuConfig() {
		mntmArduinoConnect.setIcon(null);
		mnConfig.add(mntmArduinoConnect);
		mnConfig.add(mntmKommandosFestlegen);
		add(mnConfig);
		
		add(mnInfo);
		
		mnInfo.add(mntmStatistik);
		
		mnInfo.add(mntmReleasNotes);
		
		mnInfo.add(mntmKontakt);
	}
	
	public void updateMenuBar(Arduino arduino) {
		
		if(arduino.isConnected()) {
			mntmArduinoConnect.setText("Arduino trennen");
		}
		else {
			mntmArduinoConnect.setText("Arduino verbinden");
		}
	}

}
