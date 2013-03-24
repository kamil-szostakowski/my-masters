package basejobs;

import java.io.FileNotFoundException;
import java.io.IOException;
import runs.IDatabaseRun;

/**
 * Iterfejs dzięki któremu Job Manager będzie się komuniował z 
 * wykonywanymi przez sibie zadaniami. Interfejs umozliwia zdefiniowanie
 * pliku do którego zostaną zapisane logi zadania. Identyfikatora wątku
 * który będzie to zadanie wykonywał. Oraz konfiguracji przebiegu zadania.
 * 
 * Ponadto interfejs wymaga zdefiniowania przebiegu zadania oraz sposobu 
 * wykonania operacji insert oraz select.
 * 
 * @author Kamil Szostakowski
 */
public interface IDatabaseJob 
{
    /*
     * Metoda w której nalezy umieścić definicję wykonania 
     * zadania przeciążającego interfejs IDatabaseJob.
     */
    
    public void RunTest() throws FileNotFoundException, IOException;
    
    /**
     * Metoda umożliwiająca ustawienie konfuguracji przebiegu
     * dla aktualnego wykonania zadania implementujacego ten
     * interfejs.
     * 
     * @param config
     */
    
    public void SetConfiguration(IDatabaseRun config);
    
    /*
     * Metoda umożliwiajaca zdefiniowanie własnego identyfikatora
     * dla wątku który będzie wykonywal to zadanie.
     */
    
    public void SetThreadID(int identifier);   
    
    /*
     * Metoda umozliwiająca zdefiniowanie nazwy pliku do któego
     * będzie logował wątek wykonujący dane zadanie.     
     */
    
    public void SetLogFile(String filename);
    
    /*
     * Metoda zestawiająca polączenie z bazą danych.
     */
    
    public void Connect();
    
    /*
     * Metoda odpowiadająca za likwidację połączenia
     * z bazą danych.
     */
    
    public void Disconnect();            
    
    /*
     * Metoda definiująca jak ma wyglądać operacja select 
     * dla danego zadania.
     */
    
    public int PerformSelectOperation(int identifier);
    
    /*
     * Metoda definiująca jak ma wyglądać operacja insert 
     * dla danego zadania.
     */    
    
    public void PerformInsertOperation(int identifier);   
    
    /*
     * Metoda pozwala skolonować instancję zadania w celu przekazania
     * jej do wykonania w osobnym wątku.
     */
    
    public IDatabaseJob Clone();
}