package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class GuiPassword extends JFrame{
	private GuiCalendar gui;
	private File f;
	private String action;
	
	public GuiPassword(GuiCalendar calendar,File f,String action){
		this.gui=calendar;
		this.f=f;
		this.action=action;
		initComponents();
	}
	
	private void initComponents(){
		jLabel = new JLabel();
		jLabel.setText("Mot de passe :");
		jPasswordField = new JPasswordField();
		jButton = new JButton();
		jButton.setText("Valider");
		jButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				savePassword();
			}
		});
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(jLabel,BorderLayout.NORTH);
		panel.add(jPasswordField,BorderLayout.CENTER);
		panel.add(jButton,BorderLayout.SOUTH);
		this.add(panel);
		this.setPreferredSize(new Dimension(200,300));
		this.pack();
	}
	
	private void savePassword(){
		if(this.gui==null)
			try {
				throw new Exception("fichier null");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(action.equals("open"))
			gui.decrypt(jPasswordField.getPassword().toString(), this.f);
		else if(action.equals("save"))
			gui.crypt(jPasswordField.getPassword().toString(), this.f);
		this.dispose();
	}

	
	private JLabel jLabel;
	private JPasswordField jPasswordField;
	private JButton jButton;
	private JPanel panel;

}
