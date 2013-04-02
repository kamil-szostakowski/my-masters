package postgresjobs;

import basejobs.IDatabaseJob;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Zadanie mające na celu zbudowanie w bazie danych PostgreSQL schematu 
 * na którym bedą operowały pozostałe testy.
 * 
 * Utworzenie tabeli do któej będą insertowane później dokumenty przez
 * pozostałe zadania testowe.
 * 
 * @author Kamil Szostakowski
 */

public class PostgresSchemaJob extends PostgresBaseJob
{
    private String schema = "CREATE TABLE document(id integer NOT NULL, threadid integer, title character varying(200), content text)";
    
    /*
     * Metoda zwraca nazwę zadania, używana między innymi to tworzenia nazw plików
     * z logami.
     */
    
    public String GetName()
    {
        return "postgresql-schema";
    }      
    
    /*
     * Utworzenie tabeli na dokumenty.
     */    
    
    public void PerformInsertOperation(int identifier)   
    {
        try 
        {                                
            this.statement.execute(schema);                                    
        } 
        
        catch (SQLException ex) 
        {
            Logger.getLogger(PostgresTestJob.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    /*
     * Metoda pozwalająca stworzyć instancję tego zadania 
     * do uruchomienia w innym wątku.
     */    
    
    public IDatabaseJob Clone()
    {
        return new PostgresSchemaJob();
    }    
}