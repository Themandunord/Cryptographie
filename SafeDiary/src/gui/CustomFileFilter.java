package gui;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class CustomFileFilter extends FileFilter
{
    private String extension;
    private String description;
   
    public CustomFileFilter(String extension, String description)
    {
        this.extension=extension;
        this.description=description;
    }
   
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

    public String getDescription()
    {
        return this.description;
    }
   
    /*
     * Get the extension of a file.
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

    public String getExtension()
    {
        return this.extension;
    }
   
   
}