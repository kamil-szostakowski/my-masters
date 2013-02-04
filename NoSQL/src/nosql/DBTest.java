package nosql;

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
public class DBTest 
{
    private String _content;
    
    public DBTest()
    {
        _content = null;        
    }
    
    public int GetSelectRate()
    {
        return 25;
    }
    
    public int GetInsertRate()
    {
        return 75;
    }
    
    public int GetNumberOfThreads()
    {
        return 6;
    }
    
    public int GetRepeatCount()
    {
        return 10;
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
            Logger.getLogger(DBTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}