package main;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.Security;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.KeyGenerator;

import model.Calendar;
import model.CalendarData;
import model.Event;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import crypto.CalendarCrypter;

public class Main {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		try {
            Security.addProvider(new BouncyCastleProvider());
            CalendarCrypter crypter = new CalendarCrypter("Salsa20", null, null);
            KeyGenerator kg = KeyGenerator.getInstance("Salsa20");
            Key key = kg.generateKey();
            
            crypter.cryptFile(key, "src/model/Calendar.java", "Calendar.cry");
            crypter.decryptFile(key, "Calendar.cry", "Calendar.dat");
            
            long srcLength = new File("src/model/Calendar.java").length();
            long resultLength = new File("Calendar.dat").length();
            
            assert srcLength == resultLength : String.format("fichiers source et cible de longueur distincte %-8d : %-8d !..",srcLength, resultLength);
        } catch (Exception ex) {
            Logger.getLogger(CalendarCrypter.class.getName()).log(Level.SEVERE, null, ex);
        }
		
		Calendar c = new Calendar();
		c.add(new CalendarData(new Date(), new Event("Coucou", 50, 5)));
		c.add(new CalendarData(new Date(), new Event("Help", 25, 5)));
		
		try {
			System.out.println(new String(c.getBytes()));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		try {
			Calendar c1 = new Calendar("Sun Dec 15 17:18:27 CET 2013~Help~25~5;Sun Dec 15 17:18:28 CET 2013~Coucou~50~5");
			System.out.println(new String(c1.getBytes()));
			
			Date d = new Date(112, 11, 2);
			c1.setDataByDate(new CalendarData(d, new Event("beuh", 50, 20)));
			c1.setDataByDate(new CalendarData(d, new Event("beuh", 50, 30)));
			System.out.println(new String(c1.getBytes()));
			
		} catch (ParseException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		
		
		
	}

}
