package gui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Classe représentant un filtre pour les extensions de fichiers dans le JFileChooser.
 *
 */
public class CustomFileFilter extends FileFilter
{
    private String extension;
    private String description;
   
    /**
     * Construit un nouveau filtre.
     * @param extension l'extension des fichiers filtrés
     * @param description la description du type de fichier
     */
    public CustomFileFilter(String extension, String description)
    {
        this.extension=extension;
        this.description=description;
    }
   
    /**
     * Vérifie que le fichier est conforme au filtre.
     */
    public boolean accept(File f)
    {
        if (f.isDirectory())
        {
            return true;
        }

        String ext = getFileExtension(f);
        if (ext != null)
        {
            if (ext.equals(this.extension))
                return true;
            else
                return false;
        }

        return false;
        }

    /**
     * retourne la description du filtre
     */
    public String getDescription()
    {
        return this.description;
    }
   
    /**
     * Retourne l'extension d'un fichier
     * @param f le fichier
     * @return l'extension du fichier par ex : "jpg"
     */
    public String getFileExtension(File f)
    {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    /**
     * Retourne l'extension filtrée.
     * @return l'extension filtrée
     */
    public String getExtension()
    {
        return this.extension;
    }
   
   
}