package gui;

import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.event.ActionEvent;

public class Kontakt extends JDialog {
	public Kontakt(MainWindow owner) {
		setBounds(owner.getX()+50,owner.getY()+50 ,333 , 270);
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(Releasnotes.class.getResource("/gui/grafik/arduino_22429_klein.png")));
		getContentPane().setLayout(null);
		
		JLabel lblThomasSchnborn = new JLabel("Thomas Sch\u00F6nborn");
		lblThomasSchnborn.setIcon(new ImageIcon(Kontakt.class.getResource("/gui/grafik/profil.jpg")));
		lblThomasSchnborn.setVerticalAlignment(SwingConstants.TOP);
		lblThomasSchnborn.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 15));
		lblThomasSchnborn.setBounds(10, 11, 265, 56);
		getContentPane().add(lblThomasSchnborn);
		
		JLabel lblThomasschoenborntonlinede_1 = new JLabel("0175/4133026");
		lblThomasschoenborntonlinede_1.setIcon(new ImageIcon(Kontakt.class.getResource("/gui/grafik/whatsapp_108042.png")));
		lblThomasschoenborntonlinede_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblThomasschoenborntonlinede_1.setBounds(10, 171, 160, 35);
		getContentPane().add(lblThomasschoenborntonlinede_1);
		
		JButton btnEmail = new JButton("thomas.schoenborn@t-online.de");
		btnEmail.setIcon(new ImageIcon(Kontakt.class.getResource("/gui/grafik/email_14410.png")));
		btnEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					open(new URI("mailto:thomas.schoenborn@t-online.de"));
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnEmail.setForeground(Color.BLUE);
		btnEmail.setBorder(null);
		btnEmail.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnEmail.setHorizontalAlignment(SwingConstants.LEFT);
		btnEmail.setBounds(10, 121, 264, 39);
		getContentPane().add(btnEmail);
		
		JButton btnInsta = new JButton("@thomas_sborn");
		btnInsta.setIcon(new ImageIcon(Kontakt.class.getResource("/gui/grafik/instagram_108043.png")));
		btnInsta.setHorizontalAlignment(SwingConstants.LEFT);
		btnInsta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					open(new URI("https://www.instagram.com/thomas_sborn/"));
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnInsta.setForeground(Color.BLUE);
		btnInsta.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnInsta.setBorder(null);
		btnInsta.setBounds(10, 78, 255, 32);
		getContentPane().add(btnInsta);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static void open(URI uri) {
	    if (Desktop.isDesktopSupported()) {
	      try {
	        Desktop.getDesktop().browse(uri);
	      } catch (IOException e) { /* TODO: error handling */ }
	    } else { /* TODO: error handling */ }
	  }
}
