package gui;

import javax.swing.JDialog;
import javax.swing.JFrame;

import klassen.Statistik;
import main.PaintingMaschine;
import javax.swing.JLabel;
import java.awt.Font;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class StatistikGui extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Statistik statistik;
	
	JLabel lblStatistik = new JLabel("Statistik:");
	JPanel panel = new JPanel();
	

	public StatistikGui(MainWindow main) {
		this.statistik = PaintingMaschine.detectStatistik();
		
		setBounds(new Rectangle(main.getX()+50, main.getY()+50, 240, 350));
		initFrame();
		createZeilen() ;
	}

	private void initFrame() {
		getContentPane().setLayout(null);
		this.setVisible(true);
		setTitle("Statistik");
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(ArduinoEinstellungen.class.getResource("/gui/grafik/arduino_22429_klein.png")));
		
		lblStatistik.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblStatistik.setBounds(10, 11, 126, 19);
		getContentPane().add(lblStatistik);
		
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel.setBounds(10, 41, 200, 260);
		panel.setLayout(null);
		getContentPane().add(panel);
		
	}
	
	private void createZeilen() {
		int abstand = 10;
		List<String> list = statistik.getStatistik();
		for(int i = 0; i < list.size();i++) {
			JLabel lblZeile = new JLabel(list.get(i));
			lblZeile.setBounds(10, abstand, 396, 14);
			panel.add("zeile"+i,lblZeile);
			abstand +=15;
		}
		
		
	}
}