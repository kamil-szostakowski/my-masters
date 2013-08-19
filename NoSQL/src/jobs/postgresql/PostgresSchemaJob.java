package jobs.postgresql;

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
 * http://127.0.0.1:9080/db/postgresql/job/schema/run/schema/datasource/file/id/postgresql-schema
 * 
 * @author Kamil Szostakowski
 */

public class PostgresSchemaJob extends PostgresBaseJob
{
    private String schema = "CREATE TABLE document(id varchar(200) NOT NULL, threadid integer, title character varying(200), content text)";         
    
    /*
     * Utworzenie tabeli na dokumenty.
     */    
    
    @Override
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
     * Metoda definiująca jak ma wyglądać operacja update 
     * dla danego zadania.
     */      
    
    @Override
    public void PerformUpdateOperation(int identifier) 
    {    
    }

    /*
     * Metoda definiująca jak ma wyglądać operacja delete 
     * dla danego zadania.
     */    
    
    @Override
    public void PerformDeleteOperation(int identifier) 
    {    
    }    
    
    /*
     * Metoda pozwalająca stworzyć instancję tego zadania 
     * do uruchomienia w innym wątku.
     */    
    
    @Override
    public IDatabaseJob Clone()
    {
        return new PostgresSchemaJob();
    }    
}