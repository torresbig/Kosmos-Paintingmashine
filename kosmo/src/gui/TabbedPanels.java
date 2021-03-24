package gui;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import enu.Calibrate;

import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JList;

public class TabbedPanels extends JTabbedPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MainWindow main;

	public TabbedPanels(MainWindow main) {
		super();
		this.main = main;
		initPanel_overview();
		initPanel_Config();
		initPanel_Arduino();
		setBounds(776, 11, 222, 686);
	}

	public JPanel panel_overview = new JPanel();
	public JButton btnStartPrinting = new JButton("      Print");

	private void initPanel_overview() {

		addTab("Main", null, panel_overview, null);
		initLblMode();
		initLblSelectColor();
		btnStartPrinting.setIcon(new ImageIcon(TabbedPanels.class.getResource("/gui/grafik/paint-spray_47194.png")));
		btnStartPrinting.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnStartPrinting.setBounds(24, 27, 163, 47);
		btnStartPrinting.setEnabled(false);
		panel_overview.add(btnStartPrinting);
	}

	public JPanel panel_Config = new JPanel();

	private void initPanel_Config() {
		addTab("Config", null, panel_Config, null);
		initBtnCalibrate();
		initPanel_navigate();
		panel_Config.setLayout(null);

	}

	public JPanel panel_Arduino = new JPanel();
	public JLabel lblStatusGrafik = new JLabel("");
	public JButton btnConnect = new JButton("verbinden");
	public JLabel lblStatusArduino = new JLabel("Status: ");
	public JList<String> listArduinoInfos = new JList<String>();

	private void initPanel_Arduino() {
		addTab("Arduino", null, panel_Arduino, null);

		btnConnect.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnConnect.setHorizontalAlignment(SwingConstants.LEFT);
		btnConnect.setIcon(new ImageIcon(TabbedPanels.class.getResource("/gui/grafik/arduino_22429_klein.png")));
		btnConnect.setBounds(10, 11, 196, 85);
		panel_Arduino.add(btnConnect);

		lblStatusGrafik.setBounds(110, 111, 50, 50);
		lblStatusGrafik
				.setIcon(new ImageIcon(TabbedPanels.class.getResource("/gui/grafik/exit_close_error_50x50.png")));
		panel_Arduino.add(lblStatusGrafik);

		lblStatusArduino.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblStatusArduino.setBounds(24, 124, 55, 25);
		panel_Arduino.add(lblStatusArduino);

		panel_Arduino.setLayout(null);
		listArduinoInfos.setBounds(10, 243, 197, 386);

		panel_Arduino.add(listArduinoInfos);

	}

	public JLabel lblMode = new JLabel("Mode");
	public JToggleButton tglbtnManuel = new JToggleButton("Manuell");
	public JToggleButton tglbtnAuto = new JToggleButton("Auto");

	private void initLblMode() {
		panel_overview.setLayout(null);
		lblMode.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMode.setBounds(24, 139, 30, 15);
		panel_overview.add(lblMode);
		tglbtnManuel.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (tglbtnManuel.isSelected()) {
					tglbtnAuto.setSelected(false);
				} else {
					tglbtnAuto.setSelected(true);
				}
			}
		});
		tglbtnManuel.setSelected(true);
		tglbtnManuel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tglbtnManuel.setBounds(10, 160, 99, 47);
		panel_overview.add(tglbtnManuel);
		tglbtnAuto.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (tglbtnAuto.isSelected()) {
					tglbtnManuel.setSelected(false);

				} else {
					tglbtnManuel.setSelected(true);

				}
			}
		});
		tglbtnAuto.setFont(new Font("Tahoma", Font.PLAIN, 12));

		tglbtnAuto.setBounds(109, 160, 98, 47);
		panel_overview.add(tglbtnAuto);
	}

	public JLabel lblSelectColor = new JLabel("Select Color");
	public JPanel colorSelect = new JPanel();
	public JLabel lblCurrentColor = new JLabel("color in process");

	private void initLblSelectColor() {
		lblSelectColor.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSelectColor.setBounds(10, 331, 65, 15);
		panel_overview.add(lblSelectColor);
		colorSelect.setBounds(10, 262, 197, 58);
		panel_overview.add(colorSelect);
		lblCurrentColor.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCurrentColor.setBounds(10, 236, 84, 15);
		panel_overview.add(lblCurrentColor);
	}

	public JButton btnCalibrate = new JButton(" Referenzwert");
	public JButton btnBreak = new JButton("Motoren Stopp");

	private void initBtnCalibrate() {
		btnCalibrate.setHorizontalAlignment(SwingConstants.LEFT);
		btnCalibrate.setIcon(new ImageIcon(TabbedPanels.class.getResource("/gui/grafik/player_start_1082 (1).png")));

		btnCalibrate.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCalibrate.setBounds(10, 11, 196, 67);
		panel_Config.add(btnCalibrate);
		btnBreak.setHorizontalAlignment(SwingConstants.LEFT);
		btnBreak.setIcon(new ImageIcon(TabbedPanels.class.getResource("/gui/grafik/playpause_jugado_1086.png")));
		btnBreak.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnBreak.setBounds(10, 89, 196, 67);
		panel_Config.add(btnBreak);
	}

	public JPanel panel_navigate = new JPanel();
	public JButton btnDown = new JButton("");
	public JButton btnRight = new JButton("");
	public JButton btnLeft = new JButton("");
	public JButton btnUp = new JButton("");
	public JButton btnPrintOneTime = new JButton("Farbe abgeben");

	private void initPanel_navigate() {
		btnPrintOneTime.setIcon(new ImageIcon(TabbedPanels.class.getResource("/gui/grafik/paint-spray_47194.png")));
		btnPrintOneTime.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnPrintOneTime.setBounds(10, 465, 196, 47);
		panel_Config.add(btnPrintOneTime);
		panel_navigate.setBounds(10, 228, 200, 181);
		panel_Config.add(panel_navigate);
		panel_navigate.setLayout(null);
		btnUp.setHorizontalAlignment(SwingConstants.LEFT);
		btnUp.setBorder(null);
		btnUp.setIcon(new ImageIcon(TabbedPanels.class.getResource("/gui/grafik/arrow_top_15603.png")));
		btnUp.setBounds(75, 11, 50, 57);
		panel_navigate.add(btnUp);
		btnLeft.setBorder(null);
		btnLeft.setIcon(new ImageIcon(TabbedPanels.class.getResource("/gui/grafik/arrow_left_15601.png")));
		btnLeft.setBounds(0, 70, 100, 40);
		panel_navigate.add(btnLeft);
		btnRight.setBorder(null);
		btnRight.setIcon(new ImageIcon(TabbedPanels.class.getResource("/gui/grafik/arrow_right_15600.png")));
		btnRight.setBounds(100, 70, 100, 40);
		panel_navigate.add(btnRight);
		btnDown.setHorizontalAlignment(SwingConstants.LEFT);
		btnDown.setBorder(null);
		btnDown.setIcon(new ImageIcon(TabbedPanels.class.getResource("/gui/grafik/arrow_bottom_15602.png")));
		btnDown.setBounds(75, 115, 50, 57);
		panel_navigate.add(btnDown);
	}

	/**
	 * Refresh der Gui
	 */

	private void refreshLblStatusGrafik() {
		String grafik = "/gui/grafik/exit_close_error_50x50.png";
		if (main.dataPool.getArduino() != null) {
			if (main.dataPool.getArduino().conState.isCONNECTED()) {
				grafik = "/gui/grafik/ok_accept_50x50.png";
			} else if (main.dataPool.getArduino().conState.isDISCONNECTEDD()) {
				grafik = "/gui/grafik/exit_close_error_50x50.png";
			}
		} else {
			System.out.println("Arduino nicht gefunden!");
			main.dataPool.logger.info("Arduino nicht gefunden! // Methode refreshLblStatusGrafik() ");
		}

		lblStatusGrafik.setIcon(new ImageIcon(TabbedPanels.class.getResource(grafik)));

	}

	private void refreshBtnConnect() {
		String text = "verbinden";
		if (main.dataPool.getArduino() != null) {
			if (main.dataPool.getArduino().conState.isCONNECTED()) {
				text = "trennen";
			}
		}
		btnConnect.setText(text);
	}

	@SuppressWarnings("unchecked")
	public void refreshArduinoConnectPanel() {
		if (main.dataPool.getArduino().isConnected()) {
			setEnabledAt(1, true);
		} else {
			setEnabledAt(1, false);
		}
		refreshLblStatusGrafik();
		refreshBtnConnect();
		refreshMainPanel();
		if (main.dataPool.getArduino().getArduinoListModel() != null) {
			listArduinoInfos.setModel(main.dataPool.getArduino().getArduinoListModel());
		}

	}

	public void refreshMainPanel() {
		if (this.tglbtnManuel.isSelected()) {
			if (main.dataPool.getArduino().isConnected()) {
				setEnabledAt(1, true);
			} else {
				setEnabledAt(1, false);
			}
			if (main.dataPool.getPrintingProzess() != null) {
				if (main.dataPool.getPrintingProzess().printingProzess.isPAUSE()) {
					btnStartPrinting.setIcon(
							new ImageIcon(TabbedPanels.class.getResource("/gui/grafik/paint-spray_47194_resume.png")));
					btnStartPrinting.setText(" Resume");
					btnStartPrinting.setEnabled(true);
				} else if (main.dataPool.getPrintingProzess().printingProzess.isRUN()){
					btnStartPrinting.setIcon(
							new ImageIcon(TabbedPanels.class.getResource("/gui/grafik/paint-spray_47194_pause.png")));
					btnStartPrinting.setText("  Pause");
					btnStartPrinting.setEnabled(false);
				}
				else {
					btnStartPrinting.setIcon(
							new ImageIcon(TabbedPanels.class.getResource("/gui/grafik/paint-spray_47194.png")));
					btnStartPrinting.setText("  Print");
					btnStartPrinting.setEnabled(false);
				}
			}

			else {
				btnStartPrinting
						.setIcon(new ImageIcon(TabbedPanels.class.getResource("/gui/grafik/paint-spray_47194.png")));
				btnStartPrinting.setText("  Print");
				btnStartPrinting.setEnabled(false);
			}
		} else {
			this.tglbtnAuto.setSelected(true);
			if (main.dataPool.getPrintingProzess() != null) {
				if (main.dataPool.getPrintingProzess().printingProzess.isRUN()) {
					btnStartPrinting.setIcon(
							new ImageIcon(TabbedPanels.class.getResource("/gui/grafik/paint-spray_47194_pause.png")));
					btnStartPrinting.setText("  Pause");
					btnStartPrinting.setEnabled(true);
				} else if (main.dataPool.getPrintingProzess().printingProzess.isPAUSE()) {
					btnStartPrinting.setIcon(
							new ImageIcon(TabbedPanels.class.getResource("/gui/grafik/paint-spray_47194_resume.png")));
					btnStartPrinting.setText("  Resume");
					btnStartPrinting.setEnabled(true);
				} else {
					btnStartPrinting.setIcon(
							new ImageIcon(TabbedPanels.class.getResource("/gui/grafik/paint-spray_47194.png")));
					btnStartPrinting.setText("  Print");
					btnStartPrinting.setEnabled(true);
				}
			}

			else {
				btnStartPrinting
						.setIcon(new ImageIcon(TabbedPanels.class.getResource("/gui/grafik/paint-spray_47194.png")));
				btnStartPrinting.setText("  Print");
				btnStartPrinting.setEnabled(true);
			}
			if (main.dataPool.getArduino().isConnected()) {
				setEnabledAt(1, false);
			} else {
				setEnabledAt(1, false);
			}
		}

	}

	public void refreshTabbedPanel() {
		if (this.tglbtnManuel.isSelected()) {
			if (main.dataPool.getArduino().isConnected()) {
				this.btnStartPrinting.setEnabled(false);
				setEnabledAt(1, true);
			} else {
				this.btnStartPrinting.setEnabled(false);
				setEnabledAt(1, false);
			}
		} else {
			this.tglbtnAuto.setSelected(true);
			if (main.dataPool.getArduino().isConnected()) {
				this.btnStartPrinting.setEnabled(true);
				setEnabledAt(1, false);
			} else {
				this.btnStartPrinting.setEnabled(false);
				setEnabledAt(1, false);
			}
		}
	}

	public void refreshConfig() {

		if (this.main.dataPool.getArduino().getCalibrateStatus() == Calibrate.UNBESTIMMT) {
			btnCalibrate.setText("Referenzwert");
			btnCalibrate.setEnabled(true);
		}
		if (this.main.dataPool.getArduino().getCalibrateStatus() == Calibrate.REFERENZWERT) {
			btnCalibrate.setText("Kalibrieren");
			btnCalibrate.setEnabled(true);
		}
		if (this.main.dataPool.getArduino().getCalibrateStatus() == Calibrate.CALIBRATED) {
			btnCalibrate.setText("Kalibriert");
			btnCalibrate.setEnabled(false);
		}
	}

}
