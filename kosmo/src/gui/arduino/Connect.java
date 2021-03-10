package gui.arduino;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;



public  class Connect extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;

	
	public JButton btnRefresh;
	private JFrame frame;

	public Connect(JFrame frame) {
		super();
		this.frame = frame;		
	}



	JPanel topPanel = new JPanel();

	private void initTopPanel() {
		initPortList();
		initBtnRefresh();
		initConnectButton();
		getContentPane().setLayout(null);
		topPanel.setBounds(0, 0, 250, 50);
		getContentPane().add(topPanel);
	}

	public final PortDropdownMenu portList = new PortDropdownMenu();

	private void initPortList() {
		portList.setBounds(10, 11, 65, 29);
		portList.refreshMenu();
		topPanel.setLayout(null);
		topPanel.add(portList);
	}

	public JButton connectButton = new JButton("Connect");

	public void initConnectButton() {
		connectButton.setBounds(139, 11, 101, 27);
		topPanel.add(connectButton);
	}

	ImageIcon refresh = new ImageIcon("src/gui/grafik/refresh.png");

	private void initBtnRefresh() {
		btnRefresh = new JButton(new ImageIcon(Connect.class.getResource("/gui/grafik/refresh.png")));
		btnRefresh.setBounds(85, 11, 44, 29);
		btnRefresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				portList.refreshMenu();

			}
		});
		topPanel.add(btnRefresh);
	}

	public void setUpGUI() {
		getContentPane().setPreferredSize(new Dimension(250, 50));
		initTopPanel();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Arduino verbinden");
		setResizable(false);
		setBackground(Color.black);
		setForeground(Color.black);
		setLocation(this.frame.getX() + 250, this.frame.getY() + 250);
		setVisible(true);
		pack();
		getContentPane();

	}
	


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}	
	

}
