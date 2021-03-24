package klassen.arduino;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.Externalizable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import com.fazecast.jSerialComm.*;
import enu.Calibrate;
import enu.Commands;
import enu.ConnectionState;
import javax.swing.DefaultListModel;
import gui.MainWindow;
import gui.arduino.AlertBox;
import klassen.ArduinoInfosListModel;
import klassen.EinstellungsParameter;
import main.PaintingMaschine;

public class Arduino implements Externalizable {
	private static final long serialVersionUID = 1L;
	private MainWindow mainFrame;
	private SerialPort comPort;
	private String portDescription;
	public ConnectionState conState;
	private Calibrate calibrate;
	private ReadWriteData readWrite = new ReadWriteData(new DefaultListModel<>());
	private ArduinoInfosListModel arduinoInfos;
	private HashMap<String, EinstellungsParameter> einstellungen;

	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;

	public Arduino() {
		// Construktor fürs speichern.
		this.conState = ConnectionState.DISCONNECTED;
		this.calibrate = Calibrate.UNBESTIMMT;
	}

	public Arduino(MainWindow mainFrame) {
		// empty constructor if port undecided
		this.conState = ConnectionState.DISCONNECTED;
		this.calibrate = Calibrate.UNBESTIMMT;
		this.mainFrame = mainFrame;
	}

	public Arduino(MainWindow mainFrame, String portDescription) {
		// preferred constructor
		this.conState = ConnectionState.DISCONNECTED;
		this.portDescription = portDescription;
		createComPortFromDescription();
		comPort.setBaudRate(DATA_RATE);
		this.calibrate = Calibrate.UNBESTIMMT;
		this.mainFrame = mainFrame;
	}

	public HashMap<String, EinstellungsParameter> getEinstellungen() {
		return einstellungen;
	}

	public void setEinstellungen(HashMap<String, EinstellungsParameter> einstellungen) {
		this.einstellungen = einstellungen;
	}

	public boolean isConnected() {
		return this.conState.isCONNECTED() ? true : false;
	}

	public EinstellungsParameter getParameterByName(String name) {
		EinstellungsParameter parameter = new EinstellungsParameter();
		for (Map.Entry<String, EinstellungsParameter> entry : einstellungen.entrySet()) {
			if (entry.getKey() == name) {
				parameter = entry.getValue();
			}
		}
		return parameter;
	}

	public void createComPortFromDescription() {
		this.comPort = SerialPort.getCommPort(this.portDescription);
	}

	public boolean openConnection() {
		if (comPort != null) {
			conState = ConnectionState.INITIALISATION;
			if (comPort.openPort()) {
				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}
				conState = ConnectionState.CONNECTED;
				PaintingMaschine.detectStatistik().countConnectArduino();
				setArduinoCommunication("Status >> Arduino an " + comPort.getSystemPortName() + " verbunden");
				setArduinoCommunication("Status >> Arduino Baudrate " + String.valueOf(comPort.getBaudRate()));
				this.setArduinoInfos();
				return true;
			} else {
				AlertBox alert = new AlertBox(new Dimension(400, 100), "Error Connecting", "Try Another port");
				alert.display();
				conState = ConnectionState.DISCONNECTED;
				return false;
			}
		} else {
			AlertBox alert = new AlertBox(new Dimension(400, 100), "Error Connecting", "Try Another port");
			alert.display();
			conState = ConnectionState.DISCONNECTED;
			return false;
		}
	}

	public void setArduinoInfos() {
		this.arduinoInfos = new ArduinoInfosListModel();
		if (comPort != null) {
			this.arduinoInfos.addElement(new ArduinoInfos(1, "Port", comPort.getSystemPortName()));
			this.arduinoInfos.addElement(new ArduinoInfos(2, "Info", comPort.getDescriptivePortName()));
			this.arduinoInfos.addElement(new ArduinoInfos(3, "Baudrate", String.valueOf(comPort.getBaudRate())));
			this.arduinoInfos.addElement(new ArduinoInfos(4, "Verbindung", conState.toString()));
			this.arduinoInfos.addElement(new ArduinoInfos(5, "Calibrate", calibrate.toString()));
			klassen.PrintingProzess state = this.mainFrame.getPrintingProzess();
			if (state != null) {
				this.arduinoInfos.addElement(new ArduinoInfos(6, "Status", state.printingProzess.toString()));
			}
		}

	}

	public void setArduinoCommunication(String text) {
		System.out.println(text);
		mainFrame.datenPool.logger.info("Arduino - SetCommunication Text: " + text);
		this.readWrite.addElement(text);
		this.mainFrame.picPrintPanel.listArduinoOutput.setModel(this.readWrite.getListModel());
	}

	public DefaultListModel<String> getArduinoCommunication() {
		return this.readWrite.getListModel();
	}

	public ArduinoInfosListModel getArduinoListModel() {
		if (this.arduinoInfos != null) {
			setArduinoInfos();
		}

		return this.arduinoInfos;
	}

	public void closeConnection() {
		conState = ConnectionState.DISCONNECTED;
		calibrate = Calibrate.UNBESTIMMT;
		this.arduinoInfos.removeAllElements();
		comPort.closePort();
		this.comPort = null;
		this.portDescription = null;
		this.readWrite = new ReadWriteData(new DefaultListModel<>());
		setArduinoCommunication("Status >> Verbindung zum Arduino getrennt!");
	}

	public void setPortDescription(String portDescription) {
		this.portDescription = portDescription;
		comPort = SerialPort.getCommPort(this.portDescription);
	}

	public String getPortDescription() {
		return portDescription;
	}

	public SerialPort getSerialPort() {
		return comPort;
	}

	public Calibrate getCalibrateStatus() {
		return calibrate;
	}

	public void setCalibrateStatus(Calibrate calibrate) {
		this.calibrate = calibrate;
	}

	public String serialRead() {
		// will be an infinite loop if incoming data is not bound
		String out = null;
		try {
			comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, TIME_OUT, TIME_OUT);

			BufferedReader input = new BufferedReader(new InputStreamReader(comPort.getInputStream()));
			if (input.ready()) {
				out = input.readLine();   //TODO: input.lines().map(line -> line + "\n").collect(Collectors.joining());
				mainFrame.datenPool.logger.info("SerialRead Output ist: " + out);
			}
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.toString());
			System.err.println("SCANNER ERROR");
			mainFrame.datenPool.logger.info("Arudino - SerialRead Exception " + e.toString());

		}
		if (out != "" && out != null) {
			setArduinoCommunication("Serial read >> " + out);
			PaintingMaschine.detectStatistik().countSerialReads();


		}
		return out;
	}

	public boolean serialReadPrintingProzess() {
		boolean result = false;
		try {
			String as = serialRead();

			if (as != null) {

				String[] s = as.split("/n");
				for (String x : s) {
					x.trim();
					if (x.equals("1")) {
						result = true;
					} else {
						if (x.contains("1")) {
							System.out.println("WARNUNG CONTAINS GENOMMEN!");
							mainFrame.datenPool.logger
									.info("Arudino - serialReadPrintingProzess - WARNUNG CONTAINS GENOMMEN!");
							result = true;
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.toString());
		}

		return result;

	}

	public String serialRead(int limit) {
		// in case of unlimited incoming data, set a limit for number of readings
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
		String out = "";
		int count = 0;
		Scanner in = new Scanner(comPort.getInputStream());
		try {
			while (in.hasNext() && count <= limit) {
				out += (in.next() + "\n");
				count++;
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			mainFrame.datenPool.logger.info("Arudino - SerialRead Exception " + e.getMessage());

		}
		if (out != "") {
			setArduinoCommunication("Serial read >> " + out);
			PaintingMaschine.detectStatistik().countSerialReads();

		}
		return out;
	}

	public void serialWrite(String s) {
		// writes the entire string at once.
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
		try {
			Thread.sleep(5);
		} catch (Exception e) {
			e.printStackTrace();
			mainFrame.datenPool.logger.info("Arudino - serialWrite Exception " + e.getMessage());
		}
		PrintWriter pout = new PrintWriter(comPort.getOutputStream());
		if (s != "") {
			setArduinoCommunication("Serial write >> " + s);
			System.out.println("gesendete Zeichen: " + s);
			mainFrame.datenPool.logger.info("SerialWrite: " + s + " // Methode serialWrite(String s)");
			PaintingMaschine.detectStatistik().countSerialWrites();
			pout.print(s);
			pout.flush();
		}
		

	}

	public void serialWrite(Commands enumCommands) {
		if (enumCommands == Commands.REFERENZWERT) {
			if (this.calibrate == Calibrate.UNBESTIMMT) {
				setCalibrateStatus(Calibrate.REFERENZWERT);
			} else if (this.calibrate == Calibrate.REFERENZWERT) {
				setCalibrateStatus(Calibrate.CALIBRATED);
			}
		}
		if (this.mainFrame.getPrintingProzess() != null) {
			if (enumCommands == Commands.MODUS_MANUELL
					&& this.mainFrame.getPrintingProzess().printingProzess.isPAUSE()) {
				setCalibrateStatus(Calibrate.REFERENZWERT);
			}
		}

		serialWrite(enumCommands.getStringWert());
		System.out.println("serialWrite --> " + enumCommands.toString());
		mainFrame.datenPool.logger
				.info("SerialWrite: " + enumCommands.toString() + " // serialWrite(Commands enumCommands)");

	}

	public Integer zahlenZusammen = 0;

	public void serialWritePictureLine(String s) {
		// writes the entire string at once.
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
		try {
			Thread.sleep(5);
		} catch (Exception e) {
			mainFrame.datenPool.logger.info("Arudino - serialWritePictureLine Exception " + e.getMessage());
			e.getStackTrace();

		}
		PrintWriter pout = new PrintWriter(comPort.getOutputStream());
		mainFrame.datenPool.logger.info("Arudino - serialWritePictureLine Serial write >> " + "<" + s + ">");
		setArduinoCommunication("Serial write >> " + "<" + s + ">");
		PaintingMaschine.detectStatistik().countSerialWrites();
		pout.print("<" + s + ">");
		pout.flush();
		
	}

	public void serialWrite(String s, int noOfChars, int delay) {
		// writes the entire string, 'noOfChars' characters at a time, with a delay of
		// 'delay' between each send.
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
		try {
			Thread.sleep(5);
		} catch (Exception e) {
			mainFrame.datenPool.logger.info("Arudino - serialWrite Exception " + e.getMessage());
			e.getStackTrace();
		}
		PrintWriter pout = new PrintWriter(comPort.getOutputStream());
		int i = 0;
		for (; i < s.length() - noOfChars; i += noOfChars) {
			pout.write(s.substring(i, i + noOfChars));
			pout.flush();
			try {
				Thread.sleep(delay);
			} catch (Exception e) {
			}
		}
		if (s != "") {
			setArduinoCommunication("Serial write >> " + s.substring(i) + " (Delay: " + delay);
			PaintingMaschine.detectStatistik().countSerialWrites();

			pout.write(s.substring(i));
			pout.flush();
		}
		

	}

	public void serialWrite(char c) {
		// writes the character to output stream.
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
		try {
			Thread.sleep(5);
		} catch (Exception e) {
			mainFrame.datenPool.logger.info("Arudino - serialWrite Exception " + e.getMessage());
			e.getStackTrace();
		}
		PrintWriter pout = new PrintWriter(comPort.getOutputStream());
		PaintingMaschine.detectStatistik().countSerialWrites();
		setArduinoCommunication("Serial write >> " + c);

		pout.write(c);
		pout.flush();
	}

	public void serialWrite(char c, int delay) {
		// writes the character followed by a delay of 'delay' milliseconds.
		comPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
		try {
			Thread.sleep(5);
		} catch (Exception e) {
			mainFrame.datenPool.logger.info("Arudino - serialWrite Exception " + e.getMessage());
			e.getStackTrace();
		}
		PrintWriter pout = new PrintWriter(comPort.getOutputStream());
		setArduinoCommunication("Serial write >> " + c + " (Delay: " + delay);
		PaintingMaschine.detectStatistik().countSerialWrites();

		pout.write(c);
		pout.flush();
		try {
			Thread.sleep(delay);
		} catch (Exception e) {
			mainFrame.datenPool.logger.info("Arudino - serialWrite Exception " + e.getMessage());
			e.getStackTrace();
		}
	}

	public void setReadWrite(ReadWriteData rw) {
		this.readWrite = rw;
	}

	public SerialPort getComPort() {
		return this.comPort;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		this.portDescription = ((String) in.readObject());
		this.einstellungen = ((HashMap<String, EinstellungsParameter>) in.readObject());
		this.zahlenZusammen = ((Integer) in.readObject());
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(this.portDescription);
		out.writeObject(this.einstellungen);
		out.writeObject(this.zahlenZusammen);
	}

}
