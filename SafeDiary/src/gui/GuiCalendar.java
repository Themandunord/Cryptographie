
package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import crypto.CalendarCrypter;

import model.Calendar;

/**
 *	Classe principale de l'interface graphique. C'est une JFrame qui affiche un calendrier.
 *	Un bouton permet d'afficher les évènements pour la date sélectionnée sur le calendrier.
 *	Un menu permet d'enregistrer et d'ouvrir un agenda. 
 * 
 */
public class GuiCalendar extends javax.swing.JFrame {

	/**
	 * La fenêtre d'édition des évènements pour une date donnée
	 */
    private GuiDay d;
    /**
     * Le modèle pour les données.
     */
    private Calendar c;
    /**
     * Objet permetant de crypter et décrypter le modèle dans un fichier.
     */
    private CalendarCrypter crypter;
    /**
     * Crée la JFrame, instancie le modèle pour les données et appelle la méthode {@link #initComponents()}
     */
    public GuiCalendar() {
        initComponents();
        c = new Calendar();
        try {
			crypter = new CalendarCrypter();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.setTitle("Safe Diary - Remy Kevin Quentin");
    }

    /**
     * Instancie et initialise les composants swing ( Components, layouts, ...)
     * et les placent dans la JFrame.
     */
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jCalendar2 = new org.freixas.jcalendar.JCalendar();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        dayButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Agenda ouvert :");

        jTextField1.setEditable(false);
        jTextField1.setMinimumSize(new Dimension(400,25)); // line 86

        dayButton.setText("Afficher/Editer les Evènements");
        dayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dayButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCalendar2, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(dayButton))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jCalendar2, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dayButton))
        );

        jMenu1.setText("Fichier");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Enregistrer");
        jMenuItem1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actionEnregistrer();
			}
		});
        jMenuItem1.setToolTipText("");
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Ouvrir");
        jMenuItem2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				actionOuvrir();
			}
		});
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edition");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }
    
    private void actionOuvrir(){
    	JFileChooser fileChooser = new JFileChooser();
    	fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new CustomFileFilter("krq","SafeDiary crypted file type (.krq)"));
        
        int returnVal = fileChooser.showOpenDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            File file = fileChooser.getSelectedFile();
            System.out.println(file.toString());
            GuiPassword gp = new GuiPassword(this,file,"open");
            gp.setVisible(true);
            this.jTextField1.setText(file.toString());
        }

    }
    
    private void actionEnregistrer(){
    	JFileChooser fileChooser = new JFileChooser();
    	fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new CustomFileFilter("krq","SafeDiary crypted file type (.krq)"));
        
        int returnVal = fileChooser.showSaveDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
        	File file = fileChooser.getSelectedFile();
            CustomFileFilter filter =(CustomFileFilter) fileChooser.getFileFilter();
           
            // we add the extension if not set
            String ext = filter.getFileExtension(file);
            if(ext == null || !ext.equals("krq"))
                file = new File(file.getAbsolutePath()+"."+filter.getExtension());

            GuiPassword gp = new GuiPassword(this,file,"save");
            gp.setVisible(true);
            this.jTextField1.setText(file.toString());
            
        }
    }
    
    public void crypt(String pass,File f){
    	try {
			crypter.cryptCalendar(pass,this.c,f);
		} catch (GeneralSecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void decrypt(String pass,File f){
    	try {
			this.c = crypter.decryptCalendar(pass, f);
		} catch (GeneralSecurityException | IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Methode appelée lors de l'appuie sur le bouton permettant d'afficher les évènements pour la date sélectionnée.
     * Ouvre une nouvelle JFrame permettant d'éditer les évènements.
     * @param evt
     */
    private void dayButtonActionPerformed(java.awt.event.ActionEvent evt) {                                          
        d = new GuiDay(jCalendar2.getDate(),this.c);
        d.show();
    }                                         

    /**
     * Methode main du programme.
     * @param args les arguments de la ligne de commande (non utilisés)
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GuiCalendar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GuiCalendar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GuiCalendar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GuiCalendar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GuiCalendar().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify                     
    private javax.swing.JButton dayButton;
    private org.freixas.jcalendar.JCalendar jCalendar2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration                   
}
