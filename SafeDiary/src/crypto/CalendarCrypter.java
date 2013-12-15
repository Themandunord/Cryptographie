package crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;

public class CalendarCrypter {
    private static final SecureRandom RAND = new SecureRandom();
    private static final int BUFFER_SIZE = 4096;
    private Cipher cipher = null;
    private boolean ivNeeded = false;
    // Une collection des noms d'algorithme n�cessitant un vecteur d'initialisation
    // typiquement ce sont des algorithmes de chiffrement en continu dont l'�tat doit
    // �tre explicitement initialis�, par exemple HC128, Salsa20
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

    /**
     * M�thode d'aide permettant la construction du nom standard complet de l'objet chiffrant
     * : AES/OFB/Pkcs5Padding,... La m�thode initialise un flag m�morisant si un vecteur
     * d'initialisation est n�cessaire.
     * @param algoName le nom standard de l'algorithme : AES, DES,...
     * @param modeName le mode d'utilisation : ECB, CBC,...
     * @param paddingName le nom du rembourrage utilis� : Pkcs5Padding,...
     * @return le nom standard complet de l'objet chiffrant.
     */
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
        if (!modeName.equalsIgnoreCase("ECB")) // mode distinct de ECB un vecteur d'initialisation est n�cessaire
        {
            ivNeeded = true;
        }
        if (paddingName == null) {
            return sb.toString();
        }
        sb.append('/').append(paddingName);
        return sb.toString();
    }

    /**
     * M�thode d'aide charg�e de crypter ou de d�crypter le fichier
     * @param key la cl� de chiffrement/d�chiffrement
     * @param mode le mode d'utilisation : chiffrage ou d�chiffrage
     * @param specs le vecteur d'initialisation, peut �tre <code>null</code>
     * @param in le flot entrant d'o� extraire les octets � chiffrer ou � d�chiffrer
     * @param out le flot sortant o� ins�rer les octets chiffr�s/d�chiffr�s
     * @throws GeneralSecurityException si l'op�ration de chiffrage ou d�chiffrage �choue
     * @throws IOException si une op�ration d'entr�e/sortie echoue
     */
    private void processFile(Key key, int mode, AlgorithmParameterSpec specs, InputStream in, OutputStream out)
            throws GeneralSecurityException, IOException {
        if (specs == null) {
            cipher.init(mode, key);
        } else {
            cipher.init(mode, key, specs);
        }
        // Construction du flot chiffrant/d�chiffrant
        CipherOutputStream cipherOut = new CipherOutputStream(out, cipher);
        byte[] buffer = new byte[BUFFER_SIZE];
        int nBytes = 0;
        // boucle de chiffrement/d�chiffrement
        while ((nBytes = in.read(buffer)) != -1) {
            cipherOut.write(buffer, 0, nBytes);
        }
        cipherOut.close();
    }

    public void cryptFile(Key key, File inFile, File outFile)
            throws GeneralSecurityException, IOException {
        // Le flot d'octets � chiffrer
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(inFile));
        // Le flot d'octets chiffr�s
        BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(outFile));
        if (!ivNeeded) {
            processFile(key, Cipher.ENCRYPT_MODE, null, bin, bout);
        } else {
            // Fabrication d'un nonce pour l'initialisation
            int ivSize = cipher.getBlockSize();
            // Cas sp�ciaux des algorithmes de chiffrement en continu
            // qui n�cessitent un vecteur d'initialisation.
            if (ivSize == 0) {
                Integer ivs = algoWithIV.get(cipher.getAlgorithm());
                if (ivs == null) {
                    throw new IllegalArgumentException("Algorithme non support�!..");
                }
                ivSize = ivs;
            }
            byte[] bytes = new byte[ivSize];
            RAND.nextBytes(bytes);
            // Construction d'un vecteur d'initialisation
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(bytes);
            // Insertion du vecteur d'initialisation en t�te du fichier chiffr�
            bout.write(bytes);
            // traitement du fichier
            processFile(key, Cipher.ENCRYPT_MODE, ivSpec, bin, bout);
        }
    }

    /**
     * Chiffrement d'un fichier.
     * @param key la cl� de chiffrement.
     * @param inFileName le nom du fichier � chiffrer.
     * @param outFileName le nom du fichier chiffr�.
     * @throws GeneralSecurityException si l'op�ration de chiffrage �choue.
     * @throws IOException si une op�ration d'entr�e/sortie echoue.
     */
    public void cryptFile(Key key, String inFileName, String outFileName)
            throws GeneralSecurityException, IOException {
        cryptFile(key, new File(inFileName), new File(outFileName));
    }

    public void decryptFile(Key key, File inFile, File outFile)
            throws GeneralSecurityException, IOException {
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(inFile));
        BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(outFile));
        if (!ivNeeded) {
            processFile(key, Cipher.DECRYPT_MODE, null, bin, bout);
        } else {
            int ivSize = cipher.getBlockSize();
            // Cas sp�ciaux des algorithmes de chiffrement en continu
            // qui n�cessitent un vecteur d'initialisation.
            if (ivSize == 0) {
                Integer ivs = algoWithIV.get(cipher.getAlgorithm());
                if (ivs == null) {
                    throw new IllegalArgumentException("Algorithme non support�!..");
                }
                ivSize = ivs;
            }
            byte[] bytes = new byte[ivSize];
            int nb = bin.read(bytes);
            if (nb != ivSize) {
                throw new IllegalBlockSizeException("Vecteur d'initialisation incorrect!..");
            }
            processFile(key, Cipher.DECRYPT_MODE, new IvParameterSpec(bytes), bin, bout);

        }
    }
    public void decryptFile(Key key, String inFileName, String outFileName)
            throws GeneralSecurityException, IOException {
        decryptFile(key, new File(inFileName), new File(outFileName));
    }
}

