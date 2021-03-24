package gui;

import javax.swing.JFrame;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import enu.Calibrate;
import enu.Commands;
import enu.GuiComponente;
import enu.SerialIcon;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.Image;
import gui.arduino.Connect;
import klassen.Bilddetails;
import klassen.PrintingProzess;
import klassen.Helper;
import klassen.ImagePanel;
import klassen.arduino.Arduino;
import main.OpenSave;
import main.Datenpool;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Toolkit;

public class MainWindow extends JFrame
		implements ActionListener, MouseListener, WindowListener, SerialPortDataListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Datenpool dataPool;
	public Connect conDialog;
	public TabbedPanels tabbedPanels;
	public PicturePrintingPanel picPrintPanel;
	public MenuBar menuBar;
	public ArduinoEinstellungen arduinoConf;

	public MainWindow(Datenpool dp) {
		this.dataPool = dp;
		if (this.getArduino() == null) {
			this.dataPool.setArduino(new Arduino(this));
		}
		initialize();
		setUpActionListener();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		new JFrame();
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(MainWindow.class.getResource("/gui/grafik/arduino_22429_klein.png")));
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setTitle("Kosmos Painting Machine");
		setSize(new Dimension(1024, 768));
		setMinimumSize(new Dimension(1024, 768));
		setBounds(100, 100, 800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		this.menuBar = new MenuBar();
		setJMenuBar(menuBar);

		this.conDialog = new Connect(this);
		this.arduinoConf = new ArduinoEinstellungen(this);
		this.arduinoConf.setPreferredSize(new Dimension(500, 538));
		this.picPrintPanel = new PicturePrintingPanel(this);
		picPrintPanel.lblBildName.setBounds(166, 13, 336, 15);
		picPrintPanel.lblBildFarbenAnzahl.setBounds(166, 39, 336, 15);

		picPrintPanel.setBounds(0, 11, 766, 686);
		this.picPrintPanel.btnFarbenReduzieren.setLocation(616, 241);
		this.picPrintPanel.setEnabledAt(1, false);
		this.picPrintPanel.setSelectedIndex(0);
		getContentPane().add(this.picPrintPanel);
		this.tabbedPanels = new TabbedPanels(this);

		tabbedPanels.setBounds(776, 11, 222, 686);
		this.tabbedPanels.setEnabledAt(1, false);
		this.tabbedPanels.setSelectedIndex(2);
		getContentPane().add(this.tabbedPanels);
	}

	public void initBild(BufferedImage img) {
		Image perview = img;
		if (img != null) {
			Integer[] x = Helper.getNewImgSize(img);
			perview = img.getScaledInstance(x[0], x[1], Image.SCALE_DEFAULT);
			this.picPrintPanel.changeBildFarbenAnzahl(String.valueOf(Bilddetails.getColorCount(img)));
			this.getBildDetails().setCurrentPicture(img);
		}

		if (this.picPrintPanel.panelBild != null) {
			this.picPrintPanel.panelPicture.remove(this.picPrintPanel.panelBild);
		}
		this.picPrintPanel.panelBild = new ImagePanel(perview);
		this.picPrintPanel.panelBild.setBounds(10, 76, 600, 600);
		this.picPrintPanel.panelPicture.add(this.picPrintPanel.panelBild);
	}

	private void setUpActionListener() {

		this.conDialog.connectButton.addActionListener(this);

		this.menuBar.mntmArduinoConnect.addActionListener(this);
//		this.menuBar.mntmOpen.addActionListener(this);
		this.menuBar.mntmSave.addActionListener(this);
		this.menuBar.mntmKommandosFestlegen.addActionListener(this);
		this.menuBar.mntmReleasNotes.addActionListener(this);
		this.menuBar.mntmKontakt.addActionListener(this);

		this.picPrintPanel.btnFarbenReduzieren.addActionListener(this);
		this.picPrintPanel.btnBlackWhite.addActionListener(this);
		this.picPrintPanel.btnGraustufen.addActionListener(this);
		this.picPrintPanel.btnOriginalBild.addActionListener(this);
		this.picPrintPanel.btnOpenPicture.addActionListener(this);
		this.picPrintPanel.btnFarbenReduzierenRegler.addActionListener(this);
		this.picPrintPanel.btnInvertColors.addActionListener(this);
		this.picPrintPanel.btnDruckauftragAbbrechen.addActionListener(this);

		this.tabbedPanels.btnDown.addActionListener(this);
		this.tabbedPanels.btnUp.addActionListener(this);
		this.tabbedPanels.btnLeft.addActionListener(this);
		this.tabbedPanels.btnRight.addActionListener(this);
		this.tabbedPanels.btnDown.addActionListener(this);
		this.tabbedPanels.btnBreak.addActionListener(this);
		this.tabbedPanels.tglbtnAuto.addActionListener(this);
		this.tabbedPanels.tglbtnManuel.addActionListener(this);
		this.tabbedPanels.btnPrintOneTime.addActionListener(this);
		this.tabbedPanels.btnCalibrate.addActionListener(this);
		this.tabbedPanels.btnDown.addMouseListener(this);
		this.tabbedPanels.btnLeft.addMouseListener(this);
		this.tabbedPanels.btnRight.addMouseListener(this);
		this.tabbedPanels.btnUp.addMouseListener(this);
		this.tabbedPanels.btnConnect.addActionListener(this);
		this.tabbedPanels.btnStartPrinting.addActionListener(this);

		this.arduinoConf.rdbtnAllesGetrennt.addActionListener(this);
		this.arduinoConf.rdbtnKomplett.addActionListener(this);
		this.arduinoConf.rdbtnVorzeichenEineln.addActionListener(this);

		this.getArduino().getSerialPort().addDataListener(this);
		
		this.arduinoConf.addWindowListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == this.picPrintPanel.btnOpenPicture) {

			Bilddetails bd = OpenSave.bildOeffnen();
			if (bd != null) {
				this.dataPool.setBildDetails(bd);
				this.picPrintPanel.changeBildName(bd.name);

				initBild(bd.getCurrentPicture());
				this.picPrintPanel.textResolution
						.setText(String.valueOf(bd.getWidth()) + " x " + String.valueOf(bd.getHeight()));
			}

		}

		/*
		 * Arduino Einstellungen
		 */

		if (e.getSource() == this.arduinoConf.rdbtnAllesGetrennt) {
			if (this.arduinoConf.rdbtnKomplett.isSelected()) {
				this.getArduino().zahlenZusammen = 0;
			}
			if (this.arduinoConf.rdbtnVorzeichenEineln.isSelected()) {
				this.getArduino().zahlenZusammen = 2;
			}
			if (this.arduinoConf.rdbtnAllesGetrennt.isSelected()) {
				this.getArduino().zahlenZusammen = 1;
			}

		}
		if (e.getSource() == this.arduinoConf.rdbtnKomplett) {
			if (this.arduinoConf.rdbtnKomplett.isSelected()) {
				this.getArduino().zahlenZusammen = 0;
			}
			if (this.arduinoConf.rdbtnVorzeichenEineln.isSelected()) {
				this.getArduino().zahlenZusammen = 2;
			}
			if (this.arduinoConf.rdbtnAllesGetrennt.isSelected()) {
				this.getArduino().zahlenZusammen = 1;
			}

		}
		if (e.getSource() == this.arduinoConf.rdbtnVorzeichenEineln) {
			if (this.arduinoConf.rdbtnKomplett.isSelected()) {
				this.getArduino().zahlenZusammen = 0;
			}
			if (this.arduinoConf.rdbtnVorzeichenEineln.isSelected()) {
				this.getArduino().zahlenZusammen = 2;
			}
			if (this.arduinoConf.rdbtnAllesGetrennt.isSelected()) {
				this.getArduino().zahlenZusammen = 1;
			}

		}

		/*
		 * ActionListener für die Bildbearbeitungsbuttons
		 */

		if (e.getSource() == this.picPrintPanel.btnGraustufen) {
			if (this.getBildDetails() != null) {
				if (this.picPrintPanel.btnGraustufen.isSelected()) {
					initBild(this.getBildDetails().getGreyPicture());

				} else {
					this.dataPool.setBildDetails(Helper.getLastPicture(this.getBildDetails(), this.picPrintPanel));
					initBild(this.getBildDetails().getCurrentPicture());

				}
			}
		}
		if (e.getSource() == this.picPrintPanel.btnFarbenReduzieren) {
			if (this.getBildDetails() != null) {
				if (this.picPrintPanel.btnFarbenReduzieren.isSelected()) {
					initBild(this.getBildDetails().getIndexedPicture());

				} else {
					this.dataPool.setBildDetails(Helper.getLastPicture(this.getBildDetails(), this.picPrintPanel));
					initBild(this.getBildDetails().getCurrentPicture());
				}
			}
		}
		if (e.getSource() == this.picPrintPanel.btnBlackWhite) {
			if (this.getBildDetails() != null) {
				if (this.picPrintPanel.btnBlackWhite.isSelected()) {
					initBild(this.getBildDetails().getBlackWhitePicture());

				} else {
					this.dataPool.setBildDetails(Helper.getLastPicture(this.getBildDetails(), this.picPrintPanel));
					initBild(this.getBildDetails().getCurrentPicture());
				}
			}
		}
		if (e.getSource() == this.picPrintPanel.btnInvertColors) {
			if (this.getBildDetails() != null) {
				if (this.picPrintPanel.btnInvertColors.isSelected()) {
					initBild(this.getBildDetails().getInvertetColorPicture());
				} else {
					this.dataPool.setBildDetails(Helper.getLastPicture(this.getBildDetails(), this.picPrintPanel));
					initBild(this.getBildDetails().getCurrentPicture());
				}
			}
		}
		if (e.getSource() == this.picPrintPanel.btnOriginalBild) {
			System.out.println("Original Picture aktiviert");
			this.getBildDetails().setOriginalPicture();
			initBild(this.getBildDetails().getOriginalPicture());
			this.picPrintPanel.btnFarbenReduzieren.setSelected(false);
			this.picPrintPanel.btnGraustufen.setSelected(false);
			this.picPrintPanel.btnBlackWhite.setSelected(false);
			this.picPrintPanel.sliderFarben.setValue(0);
		}

		if (e.getSource() == this.picPrintPanel.btnFarbenReduzierenRegler) {
			initBild(this.getBildDetails().getIndexedPicture());
			initBild(this.getBildDetails().getReducedColorPicture(this.picPrintPanel.sliderFarben.getValue()));

		}

		/*
		 * ActionListener für die Drucken Komponenten
		 */

		if (e.getSource() == this.tabbedPanels.btnStartPrinting) {
			if (this.getArduino().isConnected()) {
				Helper.guiUpdater(this, new ArrayList<GuiComponente>(
						List.of(GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL)));
				this.picPrintPanel.setSelectedIndex(1);
				if (this.getBildDetails() != null) {
					if (getPrintingProzess() == null) {
						dataPool.setPrintingProzess(new PrintingProzess(this));
					}

					if (getPrintingProzess().printingProzess.isPAUSE()) {
						getPrintingProzess().resume();

					} else if (getPrintingProzess().printingProzess.isRUN()) {
						getPrintingProzess().pause();
					} else {
						getPrintingProzess().excecute();
					}

					Helper.guiUpdater(this, new ArrayList<GuiComponente>(
							List.of(GuiComponente.TABBEDPANEL, GuiComponente.PRINTINGPANEL)));

				} else {
					this.picPrintPanel.setSelectedIndex(0);
				}

			} else {
				this.tabbedPanels.setSelectedIndex(2);
				this.conDialog.setUpGUI();
				Helper.guiUpdater(this, new ArrayList<GuiComponente>(
						List.of(GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL, GuiComponente.MENU)));
			}

		}

		if (e.getSource() == this.picPrintPanel.btnDruckauftragAbbrechen) {
			this.getPrintingProzess().cancel();
			Helper.guiUpdater(this, new ArrayList<GuiComponente>(
					List.of(GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL)));
		}

		/*
		 * 
		 * ActionListener für die Navigation bzw. das Config Menü
		 */

		if (e.getSource() == this.tabbedPanels.btnPrintOneTime) {
			this.getArduino().serialWrite(Commands.DRUCK);
			Helper.guiUpdater(this, GuiComponente.PRINTINGPANEL);
		}

		if (e.getSource() == this.tabbedPanels.btnBreak) {
			this.getArduino().serialWrite(Commands.MOTOREN_STOP);
			Helper.guiUpdater(this, GuiComponente.PRINTINGPANEL);
		}

		if (e.getSource() == this.tabbedPanels.btnCalibrate) {
			this.getArduino().serialWrite(Commands.REFERENZWERT);
			Helper.guiUpdater(this,
					new ArrayList<GuiComponente>(List.of(GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL)));
		}

		if (e.getSource() == this.tabbedPanels.tglbtnAuto) {
			if (this.getArduino().isConnected()) {
				if (this.tabbedPanels.tglbtnAuto.isSelected()) {
					if (this.getPrintingProzess() != null) {
						if (this.getPrintingProzess().printingProzess.isPAUSE()) {
							getPrintingProzess().resume();
						} else {
							this.getArduino().serialWrite(Commands.MODUS_AUTO);
						}
					} else {
						this.getArduino().serialWrite(Commands.MODUS_AUTO);
					}

				} else {
					this.getArduino().serialWrite(Commands.MODUS_MANUELL);
				}

				Helper.guiUpdater(this,
						new ArrayList<GuiComponente>(List.of(GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL)));
			}
		}
		if (e.getSource() == this.tabbedPanels.tglbtnManuel) {
			if (this.getArduino().isConnected()) {
				if (this.tabbedPanels.tglbtnManuel.isSelected()) {
					if (this.getPrintingProzess() != null) {
						if (this.getPrintingProzess().printingProzess.isRUN()) {
							this.getPrintingProzess().pause();
						} else {
							this.getArduino().serialWrite(Commands.MODUS_MANUELL);
						}

					} else {
						this.getArduino().serialWrite(Commands.MODUS_MANUELL);
					}

				} else {
					this.getArduino().serialWrite(Commands.MODUS_AUTO);
				}
				Helper.guiUpdater(this,
						new ArrayList<GuiComponente>(List.of(GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL)));
			} else {
				this.tabbedPanels.setSelectedIndex(2);
			}

		}

		/*
		 * ActionListener für den Arduino/Connect teil
		 */

		if (e.getSource() == this.conDialog.connectButton) {
			if (!getArduino().isConnected()) {
				this.dataPool.setArduino(new Arduino(this, conDialog.portList.getSelectedItem().toString()));
				if (this.getArduino().openConnection()) {
					Helper.guiUpdater(this,
							new ArrayList<GuiComponente>(List.of(GuiComponente.TABBEDPANEL, GuiComponente.ARDUINOPANEL,
									GuiComponente.PRINTINGPANEL, GuiComponente.ARDUINOCONF, GuiComponente.MENU)));
					conDialog.dispose();
				}
			} else {
				conDialog.connectButton.setText("Disconnect");
				conDialog.portList.setEnabled(false);
				conDialog.btnRefresh.setEnabled(false);
				Helper.guiUpdater(this, new ArrayList<GuiComponente>(List.of(GuiComponente.ARDUINOPANEL,
						GuiComponente.PRINTINGPANEL, GuiComponente.TABBEDPANEL, GuiComponente.ARDUINOCONF)));
				conDialog.pack();
			}

		}

		if (e.getSource() == this.tabbedPanels.btnConnect) {

			if (this.getArduino().isConnected()) {
				this.getArduino().closeConnection();
				Helper.guiUpdater(this,
						new ArrayList<GuiComponente>(List.of(GuiComponente.TABBEDPANEL, GuiComponente.ARDUINOPANEL,
								GuiComponente.PRINTINGPANEL, GuiComponente.ARDUINOCONF, GuiComponente.MENU)));
			}

			else {
				if (Helper.getTheOneComPort() != "false") {
					this.dataPool.setArduino(new Arduino(this, Helper.getTheOneComPort()));
					if (this.getArduino().openConnection()) {
						Helper.guiUpdater(this,
								new ArrayList<GuiComponente>(List.of(GuiComponente.TABBEDPANEL,
										GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL,
										GuiComponente.ARDUINOCONF, GuiComponente.MENU)));
					}
				} else {
					this.conDialog.setUpGUI();
					Helper.guiUpdater(this,
							new ArrayList<GuiComponente>(List.of(GuiComponente.TABBEDPANEL, GuiComponente.ARDUINOPANEL,
									GuiComponente.PRINTINGPANEL, GuiComponente.ARDUINOCONF, GuiComponente.MENU)));
				}

			}

		}

		/*
		 * ActionListener für das Menü
		 */
		if (e.getSource() == this.menuBar.mntmArduinoConnect) {
			if (getArduino().isConnected()) {
				getArduino().closeConnection();
				Helper.guiUpdater(this, new ArrayList<GuiComponente>(
						List.of(GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL, GuiComponente.MENU)));
			}

			else {
				conDialog.setUpGUI();
				Helper.guiUpdater(this, new ArrayList<GuiComponente>(
						List.of(GuiComponente.ARDUINOPANEL, GuiComponente.PRINTINGPANEL, GuiComponente.MENU)));
			}

		}
		if (e.getSource() == this.menuBar.mntmSave) {
			OpenSave.saveDatapool();

		}

//		if (e.getSource() == this.menuBar.mntmOpen) {
////			OpenSave.loadDatapool(version);
//		}
		if (e.getSource() == this.menuBar.mntmKommandosFestlegen) {
			this.arduinoConf.setUpGui();
		}
		if (e.getSource() == this.menuBar.mntmReleasNotes) {
			new Releasnotes(this).setVisible(true);
		}
		if (e.getSource() == this.menuBar.mntmKontakt) {
			new Kontakt(this).setVisible(true);
		}
	}

	/*
	 * window listener
	 */

	/*
	 * Mouselistener
	 */

	private boolean mousePressed;

	public void mousePressed(MouseEvent m) {
		mousePressed = true;
		new Thread() {
			public void run() {
				if (m.getSource() == tabbedPanels.btnDown) {
					getArduino().serialWrite(Commands.MOTOREN_RUNTER);
					if (getArduino().getCalibrateStatus().isCALIBRATED()) {
						getArduino().setCalibrateStatus(Calibrate.REFERENZWERT);
					}
					while (mousePressed) {
						try {
							Thread.sleep(150);
						} catch (Exception e) {
							dataPool.logger.info("MainWindow - mousePressed Exception " + e.getMessage()
									+ "  MouseEvent: " + m.toString());
							e.getStackTrace();
						}
					}
					getArduino().serialWrite(Commands.MOTOREN_STOP);
					Helper.guiUpdater(tabbedPanels);
					Helper.guiUpdater(picPrintPanel);
				}

				if (m.getSource() == tabbedPanels.btnUp) {
					getArduino().serialWrite(Commands.MOTOREN_HOCH);
					if (getArduino().getCalibrateStatus().isCALIBRATED()) {
						getArduino().setCalibrateStatus(Calibrate.REFERENZWERT);
					}
					while (mousePressed) {
						try {
							Thread.sleep(150);
						} catch (Exception e) {
							dataPool.logger.info("MainWindow - mousePressed Exception " + e.getMessage()
									+ "  MouseEvent: " + m.toString());
							e.getStackTrace();
						}
					}
					getArduino().serialWrite(Commands.MOTOREN_STOP);
					Helper.guiUpdater(tabbedPanels);
					Helper.guiUpdater(picPrintPanel);
				}
				if (m.getSource() == tabbedPanels.btnLeft) {
					getArduino().serialWrite(Commands.MOTOREN_LINKS);
					if (getArduino().getCalibrateStatus().isCALIBRATED()) {
						getArduino().setCalibrateStatus(Calibrate.REFERENZWERT);
					}
					while (mousePressed) {
						try {
							Thread.sleep(150);
						} catch (Exception e) {
							dataPool.logger.info("MainWindow - mousePressed Exception " + e.getMessage()
									+ "  MouseEvent: " + m.toString());
							e.getStackTrace();
						}
					}
					getArduino().serialWrite(Commands.MOTOREN_STOP);
					Helper.guiUpdater(tabbedPanels);
					Helper.guiUpdater(picPrintPanel);
				}
				if (m.getSource() == tabbedPanels.btnRight) {
					getArduino().serialWrite(Commands.MOTOREN_RECHTS);
					if (getArduino().getCalibrateStatus().isCALIBRATED()) {
						getArduino().setCalibrateStatus(Calibrate.REFERENZWERT);
					}
					while (mousePressed) {
						try {
							Thread.sleep(150);
						} catch (Exception e) {
							dataPool.logger.info("MainWindow - mousePressed Exception " + e.getMessage()
									+ "  MouseEvent: " + m.toString());
							e.getStackTrace();
						}
					}
					getArduino().serialWrite(Commands.MOTOREN_STOP);
					Helper.guiUpdater(tabbedPanels);
					Helper.guiUpdater(picPrintPanel);
				}
			}
		}.start();

	}

	public void mouseReleased(MouseEvent e) {
		mousePressed = false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		if (e.getSource() == this.arduinoConf) {
			this.getArduino().setEinstellungen(this.arduinoConf.getEinstellungen());
		}

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	public Arduino getArduino() {
		return this.dataPool.getArduino();
	}

	public PrintingProzess getPrintingProzess() {
		return this.dataPool.getPrintingProzess();
	}

	public Bilddetails getBildDetails() {
		return this.dataPool.getBildDetails();
	}

	@Override
	public int getListeningEvents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	
//	 LISTENING_EVENT_DATA_AVAILABLE	1
//	 LISTENING_EVENT_DATA_RECEIVED	16
//	 LISTENING_EVENT_DATA_WRITTEN	256
	public void serialEvent(SerialPortEvent arg0) {

			if(arg0.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
				System.out.println("<<<< Datalistener: " +arg0.getEventType());
				this.tabbedPanels.flashSerialIcon(SerialIcon.READ);
			}
			if (arg0.getEventType() == SerialPort.LISTENING_EVENT_DATA_WRITTEN) {
				this.tabbedPanels.flashSerialIcon(SerialIcon.WRITE);
				System.out.println("<<<< Datalistener: " +arg0.getEventType());
			}
		}
		
	
}
