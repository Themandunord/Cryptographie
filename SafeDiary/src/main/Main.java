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
            // Pour pouvoir utiliser l'API BouncyCastle au travers du m�canisme standard du JCE
            Security.addProvider(new BouncyCastleProvider());
            // Cr�ation d'un chiffreur/d�chiffreur bas� sur l'algorithme Salsa20
            CalendarCrypter crypter = new CalendarCrypter("Salsa20", null, null);
            // G�n�ration de la cl� secr�te pour l'algorithme
            KeyGenerator kg = KeyGenerator.getInstance("Salsa20");
            Key key = kg.generateKey();
            // Chiffrement fichier source
            crypter.cryptFile(key, "src/td2/FileCrypter.java", "FileCrypter.cry");
            // D�chiffrement
            crypter.decryptFile(key, "FileCrypter.cry", "FileCrypter.dat");
            // Comparaison des longueurs du fichier en clair et du fichier d�crypt�
            long srcLength = new File("src/td2/FileCrypter.java").length();
            long resultLength = new File("FileCrypter.dat").length();
            assert srcLength == resultLength : String.format(
                    "fichiers source et cible de longueur distincte %-8d : %-8d !..",
                    srcLength, resultLength);
        } catch (Exception ex) {
            Logger.getLogger(CalendarCrypter.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

}
