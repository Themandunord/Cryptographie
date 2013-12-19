package crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import model.Calendar;

public class CalendarCrypter {
    private static final SecureRandom RAND = new SecureRandom();
    private Cipher cipher = null;
    private boolean ivNeeded = false;
    private final static Map<String, Integer> algoWithIV = new HashMap<String, Integer>();
    static {
        algoWithIV.put("HC128", 16);
        algoWithIV.put("Salsa20", 8);
    }
    /**
     * Constructeur par defaut qui permet d'instancier le cipher
     * @throws GeneralSecurityException
     */
    public CalendarCrypter() throws GeneralSecurityException {
    	Security.addProvider(new BouncyCastleProvider());
        this.cipher = Cipher.getInstance(makeCipherName("Salsa20", null, null));
    }
    
    /**
     * Constructeur valué
     * @param algoName
     * @param modeName
     * @param paddingName
     * @throws GeneralSecurityException
     */
    public CalendarCrypter(String algoName, String modeName, String paddingName)
            throws GeneralSecurityException {
    	Security.addProvider(new BouncyCastleProvider());
        this.cipher = Cipher.getInstance(makeCipherName(algoName, modeName, paddingName));
    }
    
    /**
     * Constructeur valué avec nom du provider
     * @param algoName
     * @param modeName
     * @param paddingName
     * @param providerName
     * @throws GeneralSecurityException
     */
    public CalendarCrypter(String algoName, String modeName, String paddingName, String providerName)
            throws GeneralSecurityException {
    	Security.addProvider(new BouncyCastleProvider());
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

    private void processCalendar(Key key, int mode, AlgorithmParameterSpec specs, Calendar cal, OutputStream out) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException{
    	if (specs == null) {
            cipher.init(mode, key);
        } else {
            cipher.init(mode, key, specs);
        }
        CipherOutputStream cipherOut = new CipherOutputStream(out, cipher);
        cipherOut.write(cal.getBytes(), 0,cal.getBytes().length);
        cipherOut.close();
    }
    private String processCalendarDecrypt(Key key, int mode, AlgorithmParameterSpec specs, InputStream in) throws IOException, InvalidKeyException, InvalidAlgorithmParameterException, ParseException{
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
    /**
     * Méthode permettant de crypter un Calendar
     * @param key - Le mot de passe de l'utilisateur
     * @param cal - Le calendar à crypter
     * @param outFile - Le fichier de sortie encrypté
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public void cryptCalendar(String key, Calendar cal, File outFile) throws GeneralSecurityException, IOException{
    	BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(outFile));
    	if (!ivNeeded) {
            processCalendar(this.generateKey(key), Cipher.ENCRYPT_MODE, null, cal, bout);
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
            processCalendar(this.generateKey(key), Cipher.ENCRYPT_MODE, ivSpec, cal, bout);
        }
    }
    /**
     * Méthode permettant de décrypter un fichier
     * @param key - Le mot de passe permettant de décrypter le fichier
     * @param inFile - Le fichier à décrypter
     * @return - Un calendar généré à partir du fichier crypté
     * @throws GeneralSecurityException
     * @throws IOException
     * @throws ParseException
     */
    public Calendar decryptCalendar(String key, File inFile) throws GeneralSecurityException, IOException, ParseException {
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(inFile));
        if (!ivNeeded) {
            return new Calendar(processCalendarDecrypt(this.generateKey(key), Cipher.DECRYPT_MODE, null, bin));
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
            return new Calendar(processCalendarDecrypt(this.generateKey(key), Cipher.DECRYPT_MODE, new IvParameterSpec(bytes), bin));
        }
    }
    private Key generateKey(String password){
    	SecretKeySpec secretKeySpec = null;
		try {
			
			byte[] keyBytes = (password).getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			keyBytes = sha.digest(keyBytes);
			keyBytes = Arrays.copyOf(keyBytes, 16); // use only first 128 bit

			secretKeySpec = new SecretKeySpec(keyBytes, "AES");
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return secretKeySpec;
    }
}

