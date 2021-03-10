package klassen;

import javax.swing.DefaultListModel;

import klassen.arduino.ArduinoInfos;

@SuppressWarnings("rawtypes")
public class ArduinoInfosListModel extends DefaultListModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String getElementAt(int index) {
		ArduinoInfos aI = (ArduinoInfos) super.getElementAt(index);
        return aI.getTextZeile();
    }
    
	
    @SuppressWarnings("unchecked")
	public void addElement(ArduinoInfos arduinoInfos) {
        if(!this.contains(arduinoInfos))
        {
            int i=0;
            
            while(i<this.size()&&((ArduinoInfos)this.get(i)).getIndex()
                <=((ArduinoInfos)arduinoInfos).getIndex()){
                    i++;
           }
            this.add(i, arduinoInfos);
        }
    }
	
	

}
