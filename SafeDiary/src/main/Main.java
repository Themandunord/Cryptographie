package main;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.Security;
import java.text.ParseException;
import java.util.Date;

import javax.crypto.KeyGenerator;

import model.Calendar;
import model.CalendarData;
import model.Event;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import crypto.CalendarCrypter;

public class Main {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		Security.addProvider(new BouncyCastleProvider());
		Calendar c = new Calendar();
		Calendar outCal;
		c.add(new CalendarData(new Date(), new Event("Coucou", 50, 5)));
		c.add(new CalendarData(new Date(112,11,2), new Event("Help", 25, 5)));
		
		CalendarCrypter crypterCustom;
		try {
			crypterCustom = new CalendarCrypter();
	        crypterCustom.cryptCalendar("test", c, new File("Calendar.cry"));
	        
	        outCal = crypterCustom.decryptCalendar("test", new File("Calendar.cry"));
	        System.out.println("####"+new String(outCal.getBytes()));
		} catch (GeneralSecurityException e2) {
			e2.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			System.out.println(new String(c.getBytes()));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		
		try {
			Calendar c1 = new Calendar("Sun Dec 15 17:18:27 CET 2013~Help~25~5;Sun Dec 15 17:18:28 CET 2013~Coucou~50~5");
			System.out.println(new String(c1.getBytes()));
			
			Date d = new Date(112, 11, 2);
			c1.setDataByDate(new CalendarData(d, new Event("beuh", 50, 20)));
			c1.setDataByDate(new CalendarData(new Date(112,11,2,5,2), new Event("test", 50, 30)));
			System.out.println(new String(c1.getBytes()));
			
			System.out.println(c1.getDataByDay(d).get(1).getEvent().getEvent());
			
		} catch (ParseException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		
		
		
	}

}
