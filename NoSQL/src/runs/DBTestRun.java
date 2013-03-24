package runs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Podstawowy przebieg testowy definiujący zachowanie
 * zadanie testowego podczas jego wykonywania na bazie 
 * danych.
 * 
 * @author Kamil Szostakowski
 */
public class DBTestRun implements IDatabaseRun
{
    private String _content;        
    
    public DBTestRun()
    {
        _content = null;        
    }
    
    /*
     * Pojedyńcza operacja select składa się z pobrania 100 losowych dokumentów.
     */
    
    @Override
    public int GetSelectDocumentCount()
    {
        return 100;
    }    
    
    /*
     * Nie testeujemy operacji insert.
     */
    
    public int GetSelectRate()
    {
        return 0;
    }   
    
    /*
     * Dokonujemy tylko operacji insert.
     */    
    
    public int GetInsertRate()
    {
        return 100;
    }
    
    /*
     * Przeprowadzam testy na 6 wątkach.
     */
    
    public int GetNumberOfThreads()
    {
        return 6;
    }
    
    /*
     * Każdy wątek wykona 100 operacji.
     */
    
    public int GetRepeatCount()
    {
        return 100;
    }
    
    /*
     * Treść insertowanych dokumentów zostanie wczytana z pliku.
     */
    
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