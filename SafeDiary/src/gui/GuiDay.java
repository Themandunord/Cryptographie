/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import model.Calendar;
import model.CalendarData;
import model.Event;

/**
 *	Classe h�ritant de JFrame permettant l'�dition des �v�nements pour un jour s�lectionn�.
 * 
 */
public class GuiDay extends javax.swing.JFrame {
	/**
	 * Le mod�le pour les donn�es
	 */
	private Calendar c;
	/**
	 * La date pour laquelle on veut modifier/afficher les �v�nements
	 */
	private Date d;
	private ArrayList<CalendarData> cds;
	private ArrayList<Date> toDeletes;
    /**
     * Cr�ation de la fen�tre. Instancie et initialise les composants. Appelle la m�thode {@link GuiDay#initTable()}.
     * @param d Une date {@see Date}
     * @param c Un calendrier (mod�le de l'agenda) {@see Calendar}
     */
    public GuiDay(Date d,Calendar c){
        try {
            initComponents();
            MaskFormatter mask = new MaskFormatter("##:##");
            JFormattedTextField jftf = new JFormattedTextField(mask);
            jTable1.getColumn("Dur�e (HH:mm)").setCellEditor(new DefaultCellEditor(jftf));
            jTable1.getColumn("Heure (HH:mm)").setCellEditor(new DefaultCellEditor(jftf));
            String[] title = d.toGMTString().split(" ");
            this.setTitle(title[0]+" "+title[1]+" "+title[2]);
            this.c=c;
            this.d=d;
            d.setSeconds(0);
            d.setMinutes(0);
            d.setHours(0);
            this.cds=c.getDataByDay(d);
            this.toDeletes = new ArrayList<Date>();
            
            // r�solution du bug de modification de l'heure qui modif la cl� et qui donc cr�� une nouvelle entr�e.
            jTable1.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    // This is for double click event on anywhere on JTable
                    if (e.getClickCount() == 2) {
                        JTable target = (JTable) e.getSource();
                        int row = target.getSelectedRow();
                        int column = target.getSelectedColumn();
                        if(column==1)
                        {
                        	addToDelete((String) target.getValueAt(row, column));
                        }
                    }
                }

            });
            initTable();
        } catch (ParseException ex) {
            Logger.getLogger(GuiDay.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void addToDelete(String s){
    	String[] hs = s.split(":");
		int h = Integer.valueOf(hs[0]).intValue();
		int m = Integer.valueOf(hs[1]).intValue();
		
		Date date = (Date) d.clone();
		date.setHours(h);
		date.setMinutes(m);
		date.setSeconds(0);
    	toDeletes.add(date);
    }
    
    /**
     * Methode permettant de g�n�rer la liste des �v�nements (Nom,Heure,Dur�e) 
     * pour la date donn�e � partir du mod�le {@link GuiDay#c} {@see Calendar}
     */
    private void initTable()
    {
    	// obtenir tous les �v�nements pour la date s�lecionn�e.
    	ArrayList<CalendarData> list = c.getDataByDay(d);
    	
    	for(CalendarData cd : list)
    	{
    		Event e = cd.getEvent();
    		int hours = e.getHours();
    		int m = hours%60;
    		int h = hours/60;
    		int duration = e.getDuration();
    		int dm = duration%60;
    		int dh = duration/60;
    		((DefaultTableModel)jTable1.getModel()).addRow(new Object[]{e.getEvent(),h+":"+m,dh+":"+dm});
    	}
    	
    }

    /**
     * Initialisation des composants graphiques de la fen�tre.
     */
    private void initComponents() {

        addButton = new javax.swing.JButton();
        supprButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        addButton.setText("Ajouter");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        supprButton.setText("Supprimer");
        supprButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Annuler");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Ev�nement", "Heure (HH:mm)", "Dur�e (HH:mm)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setEditingRow(1);
        jTable1.setRowHeight(30);
        jScrollPane1.setViewportView(jTable1);

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(34, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(42, 42, 42)
                            .addComponent(okButton)
                            .addGap(18, 18, 18)
                            .addComponent(cancelButton))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(addButton)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(supprButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(supprButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cancelButton)
                            .addComponent(okButton)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }                

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        this.dispose();
    }                                            

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {                                          
        ((DefaultTableModel)jTable1.getModel()).addRow(new Object[]{"nom","00:00","00:00"});
    }                                         

    private void supprButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            
        int i = jTable1.getSelectedRow();
        if(i!=-1)
        {
        	// ajoute la date compl�te dans la liste de cl�s � suppr.
        	DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    		String hour = (String) model.getValueAt(i,1);
    		addToDelete(hour);
            model.removeRow(i);
        }
        
    }
    
    
    /**
     * Methode execut�e lors de l'appuie sur le bouton ok.
     * R�cup�re les donn�es de la liste et met � jour le mod�le.
     * @param evt (non utilis�)
     */
    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
    	/*
    	 * @TODO
    	 * Suppression du mod�le calendar lors de la suppression du model de la liste
    	 * Attention la mise � jour de l'heure change la cl� primaire et donc cr�ation d'un nouvel event.
    	 * Lors de la modification, il faut supprimer l'ancien et en ajouter un nouveau.
    	 * La methode setDataByDate() v�rifie la pr�sence de la cl� et si n'existe pas, cr�� une nouvelle entr�e.
    	 * Id�e de solution : getDataByDate()
    	 */
    	DefaultTableModel model = (DefaultTableModel)jTable1.getModel();
    	
    	for(Date date : toDeletes)
    	{
    		System.out.println("date to delete : "+date.toString());
    		c.removeByDate(date);
    	}
    	
    	for(int i=0;i<jTable1.getRowCount();i++)
    	{
    		String event = (String) model.getValueAt(i,0);
    		String hour = (String) model.getValueAt(i,1);
    		String duration = (String) model.getValueAt(i,2);
    		
    		String[] hs = hour.split(":");
    		int h = Integer.valueOf(hs[0]).intValue();
    		int m = Integer.valueOf(hs[1]).intValue();
    		
    		hs = duration.split(":");
    		int dh = Integer.valueOf(hs[0]).intValue();
    		int dm = Integer.valueOf(hs[1]).intValue();
    		
			Date date = new Date(d.getYear(),d.getMonth(),d.getDate(),d.getHours(),0,0);
    		date.setHours(h);
    		date.setMinutes(m);
    		date.setSeconds(0);
    		System.out.println("row : "+i+"  h: "+h+"m: "+m);
    		System.out.println(date.getTime());
    		System.out.println(d + "  cl� :" + date);
    		CalendarData cd = new CalendarData(date,new Event(event,dh*60+dm,h*60+m));
    		c.setDataByDate(cd);
    		System.out.println(c.getDatas());
    		System.out.println("");
    		for(Date dd : c.getDatas().keySet())
    		{
    			System.out.print(dd.getTime()+ " %%");
    		}
    		System.out.println("");
    	}
    	
    	this.dispose();
        
    }                                        
    
    // Variables declaration - do not modify                     
    private javax.swing.JButton addButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton okButton;
    private javax.swing.JButton supprButton;
    // End of variables declaration                   
}
