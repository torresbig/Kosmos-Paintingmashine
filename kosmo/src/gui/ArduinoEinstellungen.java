package gui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import java.awt.Toolkit;
import enu.Datentypen;
import klassen.EinstellungsParameter;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.SystemColor;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class ArduinoEinstellungen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MainWindow main;
	private HashMap<String, EinstellungsParameter> einstellungen;
	
	JPanel panelAuflistung = new JPanel();
	JLabel lblBezeichnung = new JLabel("Bezeichnung");
	JLabel lblAktion = new JLabel("Aktion");
	JLabel lblAz_1 = new JLabel("Az");
	JLabel lblVz_1 = new JLabel("Vz");
	JLabel lblDatenTyp = new JLabel("Datentyp");
	JLabel lblVorundNachzeichen = new JLabel("Vor-und Nachzeichen");
	JLabel lblBezeichnung_1 = new JLabel("Bezeichnung");
	JButton btnEintragen = new JButton("");
	JPanel panelDateneingabe = new JPanel();
	JLabel lblZeichen = new JLabel("Zeichen");
	JLabel lblWert = new JLabel("Wert");

	public ArduinoEinstellungen(MainWindow mainFrame) {
		this.einstellungen = mainFrame.dataPool.getArduino().getEinstellungen() == null ? new HashMap<>()
				: mainFrame.dataPool.getArduino().getEinstellungen();
		this.main = mainFrame;
		initThis();
	}

	public void setUpGui() {
		
		getContentPane().setPreferredSize(new Dimension(500, 538));
		setVisible(true);
		generateZeilen();
		repaint();
		setLocation(this.main.getX() + 250, this.main.getY() + 250);
		pack();
		getContentPane();
	}

	private void initThis() {
		setTitle("Kommandos an Arduino");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(ArduinoEinstellungen.class.getResource("/gui/grafik/arduino_22429_klein.png")));
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		initPanelDateneingabe();
		initPanelAuflistung();

	}

	private void initPanelAuflistung() {
		panelAuflistung.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelAuflistung.setBounds(10, 104, 464, 304);
		getContentPane().add(panelAuflistung);
		panelAuflistung.setLayout(null);

		lblWert.setBounds(211, 2, 47, 14);
		panelAuflistung.add(lblWert);

		lblZeichen.setBounds(154, 2, 47, 14);
		panelAuflistung.add(lblZeichen);
		lblBezeichnung.setBounds(10, 2, 73, 14);
		panelAuflistung.add(lblBezeichnung);

		lblAktion.setBounds(391, 2, 47, 14);
		panelAuflistung.add(lblAktion);
		
		initDruckformat();

	}

	private void addZeile() {
		EinstellungsParameter ep = new EinstellungsParameter(this.einBezeichnung.getText(), this.einVz.getText(),
				this.einAz.getText(), (Datentypen) this.choiceDatenTyp.getSelectedItem());
		this.einstellungen.put(this.einBezeichnung.getText(), ep);

	}

	public void generateZeilen() {
		panelAuflistung.removeAll();
		initPanelAuflistung();
		int yWert = 22;

		if (einstellungen != null && einstellungen.size() > 0) {
			for (Map.Entry<String, EinstellungsParameter> entry : einstellungen.entrySet()) {
				String tooltip = entry.getValue().getVorzeichen() + " WERT " + entry.getValue().getAbschlusszeichen()
						+ "  (Typ: " + entry.getValue().getTyp() + ")";

				JTextField textBezeichnung = new JTextField();
				textBezeichnung.setBounds(10, yWert, 150, 23);
				textBezeichnung.setColumns(10);
				textBezeichnung.setText(entry.getKey());
				textBezeichnung.setEnabled(false);
				panelAuflistung.add("textBz" + entry.getKey(), textBezeichnung);

				JTextField textField = new JTextField();
				if(entry.getValue().getWert()!= null) {
					textField.setText(entry.getValue().getWert());
				}
				else {
					textField.setText("");
				}
				textField.setBounds(211, yWert, 144, 23);
				textField.setColumns(10);
				panelAuflistung.add("textField" + entry.getKey(), textField);
				
				JButton btnWrite = new JButton("");

				if (main.dataPool.getArduino().isConnected()) {
					btnWrite.setEnabled(true);
					btnWrite.setToolTipText("Daten schreiben");
				} else {
					btnWrite.setEnabled(false);
					btnWrite.setToolTipText("Daten schreiben <> Arduino NICHT verbunden");
				}

				btnWrite.setBorderPainted(false);
				btnWrite.setIcon(new ImageIcon(ArduinoEinstellungen.class.getResource("/gui/grafik/update.png")));
				btnWrite.setBounds(365, yWert, 23, 23);

				panelAuflistung.add("btnWrite" + entry.getKey(), btnWrite);
				btnWrite.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String toWrite = entry.getValue().getVorzeichen() + textField.getText()+ entry.getValue().getAbschlusszeichen();
						entry.getValue().setWert(textField.getText());
						if (main.dataPool.getArduino().isConnected()) {
							main.dataPool.getArduino().serialWrite(toWrite);
						}
					}
				});

				JButton btnEdit = new JButton("");
				btnEdit.setBorderPainted(false);
				btnEdit.setIcon(new ImageIcon(ArduinoEinstellungen.class.getResource("/gui/grafik/edit3.png")));
				btnEdit.setToolTipText("Editieren");
				btnEdit.setBounds(398, yWert, 23, 23);
				panelAuflistung.add("btnEdit" + entry.getKey(), btnEdit);
				btnEdit.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						entry.getValue().setWert(textField.getText());
						einAz.setText(entry.getValue().getAbschlusszeichen());
						einBezeichnung.setText(entry.getKey());
						einVz.setText(entry.getValue().getVorzeichen());
						choiceDatenTyp.setSelectedItem(entry.getValue().getTyp());
						repaint();
					}
				});

				JButton btnDelete = new JButton("");
				btnDelete.setBorderPainted(false);
				btnDelete.setIcon(new ImageIcon(ArduinoEinstellungen.class.getResource("/gui/grafik/delete2.png")));
				btnDelete.setToolTipText("Delete");
				btnDelete.setBounds(431, yWert, 23, 23);
				panelAuflistung.add("btnDelete" + entry.getKey(), btnDelete);
				btnDelete.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						einstellungen.remove(entry.getKey());
						generateZeilen();
					}
				});

				JButton btnZeichenInfo = new JButton("");
				btnZeichenInfo.setIcon(new ImageIcon(ArduinoEinstellungen.class.getResource("/gui/grafik/info.png")));
				btnZeichenInfo.setBorderPainted(false);
				btnZeichenInfo.setBounds(170, yWert, 23, 23);
				btnZeichenInfo.setToolTipText(tooltip);
				panelAuflistung.add("btnZeichenInfo" + entry.getKey(), btnZeichenInfo);
				yWert = yWert + 25;
				
				
			}
		}

	}
	JPanel panelDruckFormat = new JPanel();
	JLabel lblDruckformat = new JLabel("Druckformat:");
	JRadioButton rdbtnKomplett = new JRadioButton("Komplett --> <01100101> --> senden");
	JRadioButton rdbtnAllesGetrennt = new JRadioButton("Alles einzeln --> \"<\" --> senden\" 0\"--> senden \"1\"--> senden  \">\" --> senden");
	JRadioButton rdbtnVorzeichenEineln = new JRadioButton("Vorzeichen einzeln -> \"<\" -> senden \"01100101\" -> senden \">\" -> senden");
	ButtonGroup buttonGroup = new ButtonGroup();


	private void initDruckformat() {
		
		panelDruckFormat.setBounds(10, 419, 464, 69);
		getContentPane().add(panelDruckFormat);
		panelDruckFormat.setLayout(null);
		
		lblDruckformat.setBounds(0, 11, 101, 14);
		panelDruckFormat.add(lblDruckformat);
		
		buttonGroup.add(rdbtnKomplett);
		rdbtnKomplett.setBounds(74, 7, 372, 23);
		panelDruckFormat.add(rdbtnKomplett);
		
		buttonGroup.add(rdbtnAllesGetrennt);
		rdbtnAllesGetrennt.setToolTipText("Alles einzeln --> \"<\" --> senden\" 0\"--> senden \"1\"--> senden  \">\" --> senden");
		rdbtnAllesGetrennt.setBounds(74, 29, 384, 14);
		panelDruckFormat.add(rdbtnAllesGetrennt);
		
		buttonGroup.add(rdbtnVorzeichenEineln);
		rdbtnVorzeichenEineln.setToolTipText("Vorzeichen einzeln -> \"<\" -> senden \"01100101\" -> senden \">\" -> senden");
		rdbtnVorzeichenEineln.setBounds(74, 46, 372, 16);
		panelDruckFormat.add(rdbtnVorzeichenEineln);
		updateDruckformat();
		
		// da dieser Teil eigenltich nciht gebraucht wird und unter Arduino serial write schon gestrichen wurde deaktiviert!
		rdbtnVorzeichenEineln.setEnabled(false);
		rdbtnKomplett.setEnabled(false);
		rdbtnAllesGetrennt.setEnabled(false);
		panelDruckFormat.setEnabled(false);
		
	}

	public void updateDruckformat() {
		if(main.dataPool.getArduino().zahlenZusammen == 1) {
			rdbtnVorzeichenEineln.setSelected(false);
			rdbtnAllesGetrennt.setSelected(true);
			rdbtnKomplett.setSelected(false);
		}
		else if(main.dataPool.getArduino().zahlenZusammen == 2) {
			rdbtnVorzeichenEineln.setSelected(true);
			rdbtnAllesGetrennt.setSelected(false);
			rdbtnKomplett.setSelected(false);
		}
		else {
			rdbtnVorzeichenEineln.setSelected(false);
			rdbtnAllesGetrennt.setSelected(false);
			rdbtnKomplett.setSelected(true);
		}
	}
	
	private void initPanelDateneingabe() {
		initButton();
		initEingabefelder();
		panelDateneingabe.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDateneingabe.setLayout(null);
		panelDateneingabe.setBounds(10, 11, 464, 91);
		getContentPane().add(panelDateneingabe);

		lblBezeichnung_1.setBounds(10, 11, 73, 14);
		panelDateneingabe.add(lblBezeichnung_1);

		lblVorundNachzeichen.setToolTipText("Vorzeichen");
		lblVorundNachzeichen.setBounds(10, 39, 122, 14);
		panelDateneingabe.add(lblVorundNachzeichen);

		lblDatenTyp.setBounds(10, 66, 153, 14);
		panelDateneingabe.add(lblDatenTyp);

		lblVz_1.setToolTipText("Vorzeichen");
		lblVz_1.setBounds(163, 39, 24, 14);
		panelDateneingabe.add(lblVz_1);

		lblAz_1.setToolTipText("Abschlusszeichen");
		lblAz_1.setBounds(270, 39, 24, 14);
		panelDateneingabe.add(lblAz_1);

	}

	private void initButton() {
		btnEintragen.setBorderPainted(false);
		btnEintragen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addZeile();
				generateZeilen();
				resetEingabefelder();
				repaint();
			}
		});
		btnEintragen.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		btnEintragen.setBackground(SystemColor.control);
		btnEintragen.setIcon(new ImageIcon(ArduinoEinstellungen.class.getResource("/gui/grafik/add.png")));
		btnEintragen.setBounds(398, 29, 44, 37);
		panelDateneingabe.add(btnEintragen);
	}

	private JTextField einBezeichnung = new JTextField();
	private JTextField einVz = new JTextField();
	private JTextField einAz = new JTextField();
	private JComboBox<Datentypen> choiceDatenTyp = new JComboBox<Datentypen>();

	private void resetEingabefelder() {
		einBezeichnung.setText("");
		einVz.setText("");
		einAz.setText("");
	}

	private void initEingabefelder() {
		einBezeichnung.setColumns(10);
		einBezeichnung.setBounds(163, 8, 211, 20);
		panelDateneingabe.add(einBezeichnung);

		einVz.setColumns(10);
		einVz.setBounds(194, 36, 30, 20);
		panelDateneingabe.add(einVz);

		einAz.setColumns(10);
		einAz.setBounds(290, 36, 30, 20);
		panelDateneingabe.add(einAz);

		choiceDatenTyp.setBounds(163, 63, 211, 20);
		generateChoiceList();
		panelDateneingabe.add(choiceDatenTyp);
	}

	private void generateChoiceList() {
		for (Datentypen dt : Datentypen.values()) {
			this.choiceDatenTyp.addItem(dt);
		}
	}
	
	public HashMap<String, EinstellungsParameter> getEinstellungen() {
		return einstellungen;
	}

	public void setEinstellungen(HashMap<String, EinstellungsParameter> einstellungen) {
		this.einstellungen = einstellungen;
	}
}
