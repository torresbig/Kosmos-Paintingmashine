package klassen.arduino;

import java.beans.PropertyChangeSupport;

import javax.swing.DefaultListModel;

public class ReadWriteData extends PropertyChangeSupport {

	public DefaultListModel<String> readWrite;

	public ReadWriteData(DefaultListModel<String> readWrite) {
		super(readWrite);
		this.readWrite = readWrite;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getElementAt(int index) {
		return readWrite.getElementAt(index);
	}

	public DefaultListModel<String> getListModel() {
		return this.readWrite;
	}

	public void addElement(String arduinoText) {
		this.readWrite.add(0, arduinoText);
	}

	public void addPropertyChangeListener(DefaultListModel<String> readWrite2) {
		// TODO Auto-generated method stub
		this.readWrite = readWrite2;
	}

}
