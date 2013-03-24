package runs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa definiująca przebieg dla zadania PrepareDatabase.
 * 
 * Przebieg definiuje sposób w jaki ma zostać przygotowana
 * baza danych pod późniejsze testy.
 * 
 * @author Kamil Szostakowski
 */
public class DBPrepareRun implements IDatabaseRun
{
   private String _content;
    
   public int GetSelectDocumentCount()
   {
       return 0;
   }
   
    public int GetSelectRate()
    {
        return 0;
    }
    
    public int GetInsertRate()
    {
        return 100;
    }
    
    public int GetNumberOfThreads()
    {
        return 5;
    }
    
    public int GetRepeatCount()
    {
        return 200000;
    }
    
    public String GetContent(int iter) throws FileNotFoundException, IOException
    {
        if(_content == null)
        {
            this.ReadFile();
        }
        
        return String.format("Content %d: %s", iter, _content);
    }
    
    private void ReadFile() throws IOException
    {       
        try 
        {
            StringBuffer data = new StringBuffer();
            BufferedReader reader = new BufferedReader(new FileReader("input2.txt"));
            
            char[] buffer = new char[1024];
            int readCount = 0;
            
            while((readCount=reader.read(buffer)) != -1)
            {
                String readData = String.valueOf(buffer, 0, readCount);
                data.append(readData);
            }
            
            reader.close();
            
            _content = data.toString();
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(DBTestRun.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}