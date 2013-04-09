package jobs.postgresql;

import basejobs.IDatabaseJob;

/**
 * Zadanie mające na celu przygotowanie bazy danych PostgreSQL do 
 * wykonywania na niech innych testów.
 * 
 * Zadanie polaga na zainsertowaniu do bazy ilości dokumentów
 * zdefiniowanych w opisie przebiegu zadania. Na załadowanym
 * zbiorze dokumentów mogę być już przeprowadzane bardziej
 * finezyjne testy.
 * 
 * @author Kamil Szostakowski
 */

public class PostgresPrepareJob extends PostgresTestJob
{    
    /*
     * Metoda zwraca nazwę zadania, używana między innymi to tworzenia nazw plików
     * z logami.
     */
    
    public String GetName()
    {
        return "postgresql-prepare";
    }   
    
    /*
     * Metoda pozwalająca stworzyć instancję tego zadania 
     * do uruchomienia w innym wątku.
     */
    
    public IDatabaseJob Clone()
    {
        return new PostgresPrepareJob();
    }
}