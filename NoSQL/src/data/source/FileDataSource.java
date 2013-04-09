package data.source;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.StringTools;

/**
 * Źródło danych pobierające dane z przygotowanego pliki.
 * 
 * @author Kamil Szostakowski
 */

public class FileDataSource implements IDataSource
{
    private String staticContent;    
    private String filename;    
    
    public FileDataSource(String filename)
    {
        this.filename = filename;
    }
    
    /*
     * Metoda zwraca jako dane treść zapisaną w pliku.
     */
    
    @Override
    public String GetData(Object param) 
    {
        if(this.staticContent == null)
        {
            try 
            {
                this.staticContent = StringTools.ReadFile(this.filename);
            }             
            catch (IOException ex) 
            {
                Logger.getLogger(FileDataSource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return this.staticContent;
    }    
}