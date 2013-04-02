package runs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.svenson.JSONParser;
import tools.Configuration;

/**
 *
 * @author Kamil Szostakowski
 */

public class DatabaseRun implements IDatabaseRun
{
    private String configContent;
    private String content;
    private HashMap configObject;
    
    public DatabaseRun(String filename)
    {
        try
        {
            StringBuffer data = new StringBuffer();
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            
            char[] buffer = new char[1024];
            int readCount = 0;
            
            while((readCount=reader.read(buffer)) != -1)
            {
                String readData = String.valueOf(buffer, 0, readCount);
                data.append(readData);
            }
            
            reader.close();
            
            this.configContent = data.toString();                        
            
            JSONParser parser = new JSONParser();
            
            this.configObject = (HashMap)parser.parse(this.configContent);                            
        } 
        
        catch (IOException ex) 
        { 
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }        
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
            
            this.content = data.toString();
        } 
        catch (FileNotFoundException ex) 
        {            
            Logger.getLogger(DatabaseRun.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @Override
    public int GetSelectRate() 
    {
        return ((Long)this.configObject.get("select_rate")).intValue();
    }

    @Override
    public int GetInsertRate() 
    {
        return ((Long)this.configObject.get("insert_rate")).intValue();
    }

    @Override
    public int GetNumberOfThreads() 
    {
        return ((Long)this.configObject.get("threads")).intValue();
    }

    @Override
    public int GetRepeatCount() 
    {
        return ((Long)this.configObject.get("iterations")).intValue();
    }

    @Override
    public int GetSelectDocumentCount() 
    {
        return ((Long)this.configObject.get("documents_per_select")).intValue();
    }

    @Override
    public String GetContent(int iter) throws FileNotFoundException, IOException 
    {
        if(content == null) { this.ReadFile(); }
        
        return String.format("Content %d: %s", iter, content);        
    }    
}