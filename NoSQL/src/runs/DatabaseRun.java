package runs;

import data.source.IDataSource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.svenson.JSONParser;
import tools.Configuration;
import tools.StringTools;

/**
 *
 * @author Kamil Szostakowski
 */

public class DatabaseRun implements IDatabaseRun
{
    private String configContent;
    private String content;
    private HashMap configObject;
    private IDataSource dataSource;
    
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
    
    /**
     * Metoda umozliwia zdefiniowanie procentowy udział operacji select
     * we wszystkich operacjach wykonywanych przez zadanie na bazie danych.
     */    
    
    @Override
    public int GetSelectRate() 
    {
        return ((Long)this.configObject.get("select_rate")).intValue();
    }

    /**
     * Metoda umozliwia zdefiniowanie procentowy udział operacji insert
     * we wszystkich operacjach wykonywanych przez zadanie na bazie danych.
     */     
    
    @Override
    public int GetInsertRate() 
    {
        return ((Long)this.configObject.get("insert_rate")).intValue();
    }

    /*
     * Metoda pozwalajaca na zdefiniowanie na ilu wątkach ma zostać
     * wykonane zdanie.
     */    
    
    @Override
    public int GetNumberOfThreads() 
    {
        return ((Long)this.configObject.get("threads")).intValue();
    }

    /*
     * Metoda pozwalajaca na zdefiniowanie ilości operacji które mają
     * zostać wykonane przez zadanie w danym przebiegu.
     */    
    
    @Override
    public int GetRepeatCount() 
    {
        return ((Long)this.configObject.get("iterations")).intValue();
    }

    /*
     * Metoda pozwalająca zdefiniować ilość dokumentów jaka ma zostać
     * pobrana z bazy danych przy jednym zapytaniu select w tym przebiegu.
     */    
    
    @Override
    public int GetSelectDocumentCount() 
    {
        return ((Long)this.configObject.get("documents_per_select")).intValue();
    }    
    
    /*
     * Metoda powinna zwrócic content jaki ma zostać zapisany w bazie danych
     * podczas operacji insert wykonywanej przez zadanie w tym przebiegu.
     * 
     * W przyszłości należy rozwinąć tę metodę w podsystem DataSource który 
     * pozwoli zdefiniować źródło pochodzenia danych.
     */    
    
    @Override
    public String GetContent(int iter) throws FileNotFoundException, IOException
    {
        if(this.dataSource == null)
        {
            throw new IOException("Data source not defined");            
        }
        
        if(content == null) 
        { 
            this.content = this.dataSource.GetData(iter);
        }
        
        return String.format("Content %d: %s", iter, content);        
    }    

    /*
     * Metoda pozwalająca na ustawienie źródła danych dla danego przebiegu.
     * Źródło danych musi zostać zdefiniowane.
     */    
    
    @Override
    public void SetDataSource(IDataSource datasource) 
    {
        this.dataSource = datasource;
    }
}