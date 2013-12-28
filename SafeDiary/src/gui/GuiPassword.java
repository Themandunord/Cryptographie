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

/**
 * Classe représentant la fenêtre qui permet de rentrer le mot de passe.
 *
 */
public class GuiPassword extends JFrame{
	/**
	 * La fenêtre parente.
	 */
	private GuiCalendar gui;
	/**
	 * Un fichier pour lire/écrire les données cryptées
	 */
	private File f;
	/**
	 * Chaine de charactères représentant l'acion à effectuer : ouvrir ou enregistrer.
	 */
	private String action;
	
	/**
	 * Construit la fenêtre.
	 * @param calendar fenêtre parente pour callback
	 * @param f fichier pour lire/écrire les données, utilisé pour le callback
	 * @param action "open" ou "save" pour savoir quelle fonction appeler en callback
	 */
	public GuiPassword(GuiCalendar calendar,File f,String action){
		this.gui=calendar;
		this.f=f;
		this.action=action;
		initComponents();
	}
	
	/**
	 * Initialise les composants graphiques swing.
	 */
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
		this.setPreferredSize(new Dimension(200,110));
		this.pack();
	}
	
	/**
	 * Appelle la méthode decrypt() ou crypt() de la fenêtre parente suite à la validation du mot de passe.
	 */
	private void savePassword(){
		if(this.gui==null)
			try {
				throw new Exception("fichier null");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(action.equals("open"))
			gui.decrypt(new String(jPasswordField.getPassword()), this.f);
		else if(action.equals("save"))
			gui.crypt(new String(jPasswordField.getPassword()), this.f);
		//System.out.println(new String(jPasswordField.getPassword()));
		this.dispose();
	}

	
	private JLabel jLabel;
	private JPasswordField jPasswordField;
	private JButton jButton;
	private JPanel panel;

}
