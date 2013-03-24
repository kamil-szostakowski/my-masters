package runs;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Przebieg definiujący zachowanie zadania tworzącego schemat
 * dla bazy danych.
 * 
 * @author Kamil Szostakowski
 */
public class DBSchemaRun implements IDatabaseRun
{
    /*
     * Nie będa pobierane żadne dane z bazy.
     */
    
    @Override
    public int GetSelectDocumentCount()
    {
        return 0;
    }
    
    /*
     * Nie potrzebuję żadnych selectów.
     */
    
    @Override
    public int GetSelectRate() 
    {
        return 0;
    }

    /*
     * Wykonujemy tylko operacje insert
     */
    
    @Override
    public int GetInsertRate() 
    {        
        return 100;
    }

    /*
     * Wystarczy jeden wątek.
     */
    
    @Override
    public int GetNumberOfThreads() 
    {
        return 1;
    }

    /*
     * Wystarczy jedna operacja
     */
    
    @Override
    public int GetRepeatCount() 
    {
        return 1;
    }

    /*
     * Nie dodajemy do bazy żadnego contentu.
     */
    
    @Override
    public String GetContent(int iter) throws FileNotFoundException, IOException 
    {
        return "";
    }    
}