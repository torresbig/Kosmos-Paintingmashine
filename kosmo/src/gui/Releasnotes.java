package gui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JScrollPane;

import main.Datenpool;
import main.PaintingMaschine;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.util.GregorianCalendar;

import javax.swing.JTextArea;
import javax.swing.SwingConstants;


public class Releasnotes extends JDialog {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private final String datum = "10.03.2021";
	private Datenpool dp;
	
	
	
	public Releasnotes(MainWindow mainFrame) {
		this.dp =  PaintingMaschine.detectDatapool();
		initFrame() ;
		setBounds(mainFrame.getX()+50, mainFrame.getY()+50, 450, 550);

	}
	

	
	
	private void initFrame() {
		setTitle("Releasnotes" + dp.getVersion());
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(Releasnotes.class.getResource("/gui/grafik/arduino_22429_klein.png")));
		getContentPane().setLayout(null);

		JLabel lblReleasenotes = new JLabel("Releasenotes:");
		lblReleasenotes.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblReleasenotes.setBounds(10, 11, 133, 19);
		getContentPane().add(lblReleasenotes);

		JLabel lblVersion = new JLabel(dp.getVersion());
		lblVersion.setToolTipText("Versionsnummer");
		lblVersion.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblVersion.setBounds(153, 11, 149, 19);
		getContentPane().add(lblVersion);

			initTextArea();


		JLabel lblDate = new JLabel(this.datum);
		lblDate.setFont(new Font("Tahoma", Font.ITALIC, 15));
		lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDate.setBounds(291, 13, 133, 14);
		getContentPane().add(lblDate);
	}
	
	JTextArea textArea = new JTextArea();
	JScrollPane sp = new JScrollPane(textArea);
	
	private void initTextArea() {
		sp.setBounds(10, 50, 414, 450);
		sp.setWheelScrollingEnabled(true);
		textArea.setBounds(10, 50, 414, 450);
		BufferedReader br;
		br = new BufferedReader(new InputStreamReader( getClass().getResourceAsStream("file/releasnotes.txt"))); 
		try {
			StringBuffer zeilen = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				zeilen.append(line+ "\n");
			
			}
			textArea.setText(zeilen.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		textArea.setCaretPosition(0);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		getContentPane().add(sp);
		
	}

}
