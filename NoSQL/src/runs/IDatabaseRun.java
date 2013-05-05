package runs;

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
     * Metoda zwraca nazwę przebiegu która jest uwzględniana w nazwie pliku
     * z logiem wykonania zadania. Umożliwia tym samym dokładną identyfikację
     * zadania i śledzenie jego wykonania.
     */
    
        public String GetName();
}