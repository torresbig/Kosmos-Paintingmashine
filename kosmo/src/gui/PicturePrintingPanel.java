package gui;

import java.awt.Font;


import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import klassen.ImagePanel;
import javax.swing.JList;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

public class PicturePrintingPanel extends JTabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainWindow mainFrame;
	
	
	public PicturePrintingPanel(MainWindow main) {
		super();
		mainFrame = main;
		setBounds(0, 0, 766, 686);
		initPanelPicture() ;
		initPanelPrinting();
	}
	
	public JPanel panelPicture = new JPanel();
	public JPanel panelPrinting = new JPanel();
	public JLabel lblBildName = new JLabel("Bildname: << kein Bild geladen >>");
	public JLabel lblBildFarbenAnzahl = new JLabel();
	public ImagePanel panelBild = new ImagePanel(null);
	public JToggleButton btnFarbenReduzieren = new JToggleButton("Farben reduzieren");
	public JToggleButton btnBlackWhite = new JToggleButton("Schwarz/Wei\u00DF");
	public JToggleButton btnGraustufen = new JToggleButton("Graustufen");
	public JButton btnOriginalBild = new JButton("original Bild");
	public JButton btnOpenPicture = new JButton("Bild \u00F6ffnen");
	public JLabel lblPictureSize = new JLabel("Picturesize (px)");
	public JTextField textResolution = new JTextField();
	public JSlider sliderFarben = new JSlider();
	public JLabel lblFarbenReduzieren = new JLabel("<html><body>FUNKTINIERT NICHT RICHTIG <br>Farbe reduzieren <br> = um " +sliderFarben.getValue()*10 +"  Prozent</body></html>");
	public JButton btnFarbenReduzierenRegler = new JButton("reduzieren");
	public JToggleButton btnInvertColors = new JToggleButton("Farben invertieren");
	public JList<String> listArduinoOutput = new JList<String>();
	public JScrollPane scrollPane = new JScrollPane();
	public JButton btnDruckauftragAbbrechen = new JButton("<html>Druckvorgang<br>abbrechen</html> ");
	public JTextField textArduinoComand;

	
	private void initPanelPicture() {
		addTab("Bildbearbeitung", null, panelPicture, null);
		panelPicture.setLayout(null);

		panelBild = new ImagePanel(null);
		panelBild.setBounds(10, 60, 600, 577);
		panelPicture.add(panelBild);
		
		
		lblPictureSize.setBounds(524, 8, 84, 15);
		panelPicture.add(lblPictureSize);
		lblPictureSize.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textResolution.setBounds(524, 34, 86, 20);
		panelPicture.add(textResolution);
		textResolution.setColumns(10);
		
		sliderFarben.setValue(0);
		sliderFarben.setMaximum(10);
		sliderFarben.setToolTipText(String.valueOf(sliderFarben.getValue()));
		sliderFarben.setBounds(616, 434, 135, 26);
		panelPicture.add(sliderFarben);
		lblFarbenReduzieren.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblFarbenReduzieren.setBounds(616, 386, 145, 49);
		panelPicture.add(lblFarbenReduzieren);
		
		lblBildName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBildName.setBounds(166, 13, 191, 15);
		panelPicture.add(lblBildName);
		
		lblBildFarbenAnzahl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBildFarbenAnzahl.setBounds(166, 39, 105, 15);
		lblBildFarbenAnzahl.setText("Anzahl Farben: N/A");
		panelPicture.add(lblBildFarbenAnzahl);
		
		btnFarbenReduzierenRegler.setBounds(647, 471, 83, 23);
		panelPicture.add(btnFarbenReduzierenRegler);
		btnFarbenReduzieren.setToolTipText("Indexed Picture");
		
		btnFarbenReduzieren.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnFarbenReduzieren.setBounds(616, 276, 135, 49);
		panelPicture.add(btnFarbenReduzieren);
		
		btnBlackWhite.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnBlackWhite.setBounds(616, 60, 135, 49);
		panelPicture.add(btnBlackWhite);
		
		btnGraustufen.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnGraustufen.setBounds(616, 120, 135, 49);
		panelPicture.add(btnGraustufen);
		
		btnOriginalBild.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnOriginalBild.setBounds(616, 588, 135, 49);
		panelPicture.add(btnOriginalBild);
		
		btnOpenPicture.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnOpenPicture.setBounds(10, 14, 127, 42);
		panelPicture.add(btnOpenPicture);
		
		btnInvertColors.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnInvertColors.setBounds(616, 181, 135, 49);
		panelPicture.add(btnInvertColors);
		
	}
	
	public JButton btnSend = new JButton("Daten Schreiben");		
	public JLabel lblSerialWirte = new JLabel("Serial Write (String):");

	private void initPanelPrinting() {
		addTab("Printing", null, panelPrinting, null);
		panelPrinting.setBounds(0, 0, 766, 708);
		panelPrinting.setVisible(false);
		panelPrinting.setLayout(null);
		scrollPane.setForeground(Color.WHITE);
		scrollPane.setBackground(Color.BLACK);
		scrollPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		scrollPane.setBounds(10, 96, 741, 506);
		scrollPane.setViewportView(listArduinoOutput);
		listArduinoOutput.setLayoutOrientation(JList.VERTICAL);
		panelPrinting.add(scrollPane);
		btnDruckauftragAbbrechen.setBorder(new LineBorder(new Color(0, 0, 0)));
		btnDruckauftragAbbrechen.setIcon(new ImageIcon(PicturePrintingPanel.class.getResource("/gui/grafik/paint-spray_47194_abbrechen.png")));
		btnDruckauftragAbbrechen.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnDruckauftragAbbrechen.setToolTipText("<html>Druckauftrag stoppen<br>und abbrechen</html>");
		btnDruckauftragAbbrechen.setBounds(604, 10, 147, 75);
		panelPrinting.add(btnDruckauftragAbbrechen);
		
		textArduinoComand = new JTextField();
		textArduinoComand.setToolTipText("Hier ein Befehl eingeben, der via Serial Write als String an den Arduino gegebn wird");
		textArduinoComand.setBounds(157, 624, 423, 20);
		panelPrinting.add(textArduinoComand);
		textArduinoComand.setColumns(10);
		
		
		btnSend.setBounds(604, 623, 147, 23);
		panelPrinting.add(btnSend);
		
		lblSerialWirte.setHorizontalAlignment(SwingConstants.LEFT);
		lblSerialWirte.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblSerialWirte.setBounds(9, 619, 152, 28);
		panelPrinting.add(lblSerialWirte);
	}
	
	
	
	/**
	 * methoden um einzelne parameter zu ändern
	 * @param name
	 */
	
	public void changeBildName(String name) {
		String n = name;
		if (n == null || n == "") {
			n = "<< kein Bild geladen >>";
		}

		lblBildName.setText("Bildname: " + n);
	}

	public void changeBildFarbenAnzahl(String name) {
		String n = name;
		if (n == null || n == "") {
			n = "N/A";
		}
		lblBildFarbenAnzahl.setText("Anzahl Farben: " + n);
	}

	
	
	
	
	/**
	 * Update der Gui Printing und Picture Panel
	 * @param arduino
	 */
	
	public void updatePrintingPanel() {
		if(mainFrame.datenPool.getArduino().isConnected()) {
			if(mainFrame.datenPool.getPrintingProzess() != null) {
				if(mainFrame.datenPool.getPrintingProzess().printingProzess.isRUN()) {
					btnDruckauftragAbbrechen.setEnabled(true);
				}
				else if(mainFrame.datenPool.getPrintingProzess().printingProzess.isPAUSE()) {
					btnDruckauftragAbbrechen.setEnabled(true);
				}
				else {
					btnDruckauftragAbbrechen.setEnabled(false);
				}
			}
			else {
				btnDruckauftragAbbrechen.setEnabled(false);
			}
			this.setEnabledAt(1, true);
		} else {
			this.setEnabledAt(1, false);
		}
	}
}
