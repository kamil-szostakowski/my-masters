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
     * Metoda zwraca dane pobrane ze źródła.     
     */
    
    String GetData(Object param);
}