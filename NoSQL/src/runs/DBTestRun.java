package runs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author test
 */
public class DBTestRun implements IDatabaseRun
{
    private String _content;        
    
    public DBTestRun()
    {
        _content = null;        
    }
    
    @Override
    public int GetSelectDocumentCount()
    {
        return 100;
    }    
    
    public int GetSelectRate()
    {
        return 100;
    }
    
    public int GetInsertRate()
    {
        return 0;
    }
    
    public int GetNumberOfThreads()
    {
        return 6;
    }
    
    public int GetRepeatCount()
    {
        return 100;
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