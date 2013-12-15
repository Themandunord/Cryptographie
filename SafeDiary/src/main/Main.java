package main;

import java.io.File;
import java.security.Key;
import java.security.Security;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.KeyGenerator;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import crypto.CalendarCrypter;

public class Main {

	public static void main(String[] args) {
		try {
            Security.addProvider(new BouncyCastleProvider());
            CalendarCrypter crypter = new CalendarCrypter("Salsa20", null, null);
            KeyGenerator kg = KeyGenerator.getInstance("Salsa20");
            Key key = kg.generateKey();
            
            crypter.cryptFile(key, "src/td2/FileCrypter.java", "FileCrypter.cry");
            crypter.decryptFile(key, "FileCrypter.cry", "FileCrypter.dat");
            
            long srcLength = new File("src/td2/FileCrypter.java").length();
            long resultLength = new File("FileCrypter.dat").length();
            
            assert srcLength == resultLength : String.format("fichiers source et cible de longueur distincte %-8d : %-8d !..",srcLength, resultLength);
        } catch (Exception ex) {
            Logger.getLogger(CalendarCrypter.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

}
