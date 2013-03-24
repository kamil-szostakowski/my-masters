package runs;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Interfejs pozwalający na zdefiniowanie przebiegu zadania
 * wykonywanego na bazie danych.
 * 
 * Dzięki przebiegom to samo zadanie może być wykonywane na wiele 
 * sposobów, oszczędzając tym samym czas potrzebny na pisanie zadań
 * oraz minimalizując konieczność przepisywania istniejacego już kodu.
 * 
 * @author Kamil Szostakowski
 */

public interface IDatabaseRun 
{
    /**
     * Metoda umozliwia zdefiniowanie procentowy udział operacji select
     * we wszystkich operacjach wykonywanych przez zadanie na bazie danych.
     */
    
    public int GetSelectRate();
    
    /**
     * Metoda umozliwia zdefiniowanie procentowy udział operacji insert
     * we wszystkich operacjach wykonywanych przez zadanie na bazie danych.
     */    
    
    public int GetInsertRate();
    
    /*
     * Metoda pozwalajaca na zdefiniowanie na ilu wątkach ma zostać
     * wykonane zdanie.
     */
    
    public int GetNumberOfThreads();
    
    /*
     * Metoda pozwalajaca na zdefiniowanie ilości operacji które mają
     * zostać wykonane przez zadanie w danym przebiegu.
     */
    
    public int GetRepeatCount();
    
    /*
     * Metoda pozwalająca zdefiniować ilość dokumentów jaka ma zostać
     * pobrana z bazy danych przy jednym zapytaniu select w tym przebiegu.
     */
    
    public int GetSelectDocumentCount();
    
    /*
     * Metoda powinna zwrócic content jaki ma zostać zapisany w bazie danych
     * podczas operacji insert wykonywanej przez zadanie w tym przebiegu.
     * 
     * W przyszłości należy rozwinąć tę metodę w podsystem DataSource który 
     * pozwoli zdefiniować źródło pochodzenia danych.
     */
    
    public String GetContent(int iter) throws FileNotFoundException, IOException;        
}