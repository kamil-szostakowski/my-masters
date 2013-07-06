package runs;

import java.io.BufferedReader;
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
    private String filename;
    private String configContent;    
    private HashMap configObject;    
    
    public DatabaseRun(String filename)
    {
        this.filename = filename;
        
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
    
    /**
     * Metoda umozliwia zdefiniowanie procentowy udział operacji update
     * we wszystkich operacjach wykonywanych przez zadanie na bazie danych.
     */    
    
    @Override
    public int GetUpdateRate()
    {
        return ((Long)this.configObject.get("update_rate")).intValue();
    }
    
    /**
     * Metoda umozliwia zdefiniowanie procentowy udział operacji delete
     * we wszystkich operacjach wykonywanych przez zadanie na bazie danych.
     */    
    
    @Override
    public int GetDeleteRate()
    {
        return ((Long)this.configObject.get("delete_rate")).intValue();
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
     * Metoda zwraca nazwę przebiegu która jest uwzględniana w nazwie pliku
     * z logiem wykonania zadania. Umożliwia tym samym dokładną identyfikację
     * zadania i śledzenie jego wykonania.
     */
    
    @Override
    public String GetName()
    {
        return this.filename.replaceAll(".json", "");
    }
}