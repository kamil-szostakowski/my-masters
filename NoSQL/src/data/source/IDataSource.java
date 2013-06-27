package data.source;

/**
 * Interfejs dla klas będących źródłami danych dla zadań
 * uruchamianych na bazie danych.
 * 
 * @author Kamil Szostakowski
 */

public interface IDataSource 
{
    /*
     * Metoda powinna zwrócic content jaki ma zostać zapisany w bazie danych
     * podczas operacji insert wykonywanej przez zadanie w tym przebiegu.
     * 
     */
    
    Object GetData(Object param) throws Exception;
    
    /*
     * Metoda zwracająca nazwę źródła danych, informacja ta potrzebna jest 
     * odpowiedniego nazwania pliku z logami dla zadania a tym samym do umożliwenia
     * poprawnego monitoringu stanu zadań.
     */
    
    String GetName();           
}