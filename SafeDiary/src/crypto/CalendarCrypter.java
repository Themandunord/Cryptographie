package crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;

import model.Calendar;

public class CalendarCrypter {
    private static final SecureRandom RAND = new SecureRandom();
    private static final int BUFFER_SIZE = 4096;
    private Cipher cipher = null;
    private boolean ivNeeded = false;
    private final static Map<String, Integer> algoWithIV = new HashMap<String, Integer>();
    static {
        algoWithIV.put("HC128", 16);
        algoWithIV.put("Salsa20", 8);
    }
    
    public CalendarCrypter(String algoName, String modeName, String paddingName)
            throws GeneralSecurityException {
        this.cipher = Cipher.getInstance(makeCipherName(algoName, modeName, paddingName));
    }
    public CalendarCrypter(String algoName, String modeName, String paddingName, String providerName)
            throws GeneralSecurityException {
        this.cipher = Cipher.getInstance(makeCipherName(algoName, modeName, paddingName), providerName);
    }

    private String makeCipherName(String algoName, String modeName, String paddingName) {
        StringBuilder sb = new StringBuilder(algoName);
        if (modeName == null) {
            // algorithme de chiffrement en continu type RC4
            if (algoWithIV.get(algoName) != null) {
                ivNeeded = true;
            }
            return sb.toString();
        }
        sb.append('/').append(modeName);
        if (!modeName.equalsIgnoreCase("ECB"))
        {
            ivNeeded = true;
        }
        if (paddingName == null) {
            return sb.toString();
        }
        sb.append('/').append(paddingName);
        return sb.toString();
    }

    public void processCalendar(Key key, int mode, AlgorithmParameterSpec specs, Calendar cal, OutputStream out) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException{
    	if (specs == null) {
            cipher.init(mode, key);
        } else {
            cipher.init(mode, key, specs);
        }
        CipherOutputStream cipherOut = new CipherOutputStream(out, cipher);
        cipherOut.write(cal.getBytes(), 0,cal.getBytes().length);
        cipherOut.close();
    }
    public String processCalendarDecrypt(Key key, int mode, AlgorithmParameterSpec specs, InputStream in) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, ParseException{
    	if (specs == null) {
            cipher.init(mode, key);
        } else {
            cipher.init(mode, key, specs);
        }
    	CipherInputStream cipherIn = new CipherInputStream(in, cipher);
    	byte[] b = new byte[in.available()];
    	cipherIn.read(b, 0, b.length);
    	cipherIn.close();
    	in.close();
    	return new String(b,"utf-8");
    }
    public void cryptCalendar(Key key, Calendar cal, File outFile) throws GeneralSecurityException, IOException{
    	BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(outFile));
    	if (!ivNeeded) {
            processCalendar(key, Cipher.ENCRYPT_MODE, null, cal, bout);
        } else {
            int ivSize = cipher.getBlockSize();
            if (ivSize == 0) {
                Integer ivs = algoWithIV.get(cipher.getAlgorithm());
                if (ivs == null) {
                    throw new IllegalArgumentException("Algorithme non supporté!..");
                }
                ivSize = ivs;
            }
            byte[] bytes = new byte[ivSize];
            RAND.nextBytes(bytes);
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(bytes);
            bout.write(bytes);
            processCalendar(key, Cipher.ENCRYPT_MODE, ivSpec, cal, bout);
        }
    }
    
    public Calendar decryptCalendar(Key key, File inFile) throws GeneralSecurityException, IOException, ParseException {
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(inFile));
        if (!ivNeeded) {
            return new Calendar(processCalendarDecrypt(key, Cipher.DECRYPT_MODE, null, bin));
        } else {
            int ivSize = cipher.getBlockSize();
            if (ivSize == 0) {
                Integer ivs = algoWithIV.get(cipher.getAlgorithm());
                if (ivs == null) {
                    throw new IllegalArgumentException("Algorithme non supporté!..");
                }
                ivSize = ivs;
            }
            byte[] bytes = new byte[ivSize];
            int nb = bin.read(bytes);
            if (nb != ivSize) {
                throw new IllegalBlockSizeException("Vecteur d'initialisation incorrect!..");
            }
            return new Calendar(processCalendarDecrypt(key, Cipher.DECRYPT_MODE, new IvParameterSpec(bytes), bin));
        }
    }
}

