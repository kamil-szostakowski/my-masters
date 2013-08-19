package jobs.postgresql;

import basejobs.IDatabaseJob;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Właściwe zadanie testowe dla bazy PostgreSQL mierzącej jej wydajność
 * podczas wykonywania operacji select/insert w proporcjach zdefiniowanych
 * przez przebieg przypisany tenu zadaniu.
 * 
 * http://127.0.0.1:9080/db/postgresql/job/test/run/singleload/datasource/file/id/postgresql-load
 * http://127.0.0.1:9080/db/postgresql/job/test/run/test75/datasource/file/id/postgresql-mixed
 * http://127.0.0.1:9080/db/postgresql/job/test/run/test100/datasource/file/id/postgresql-read
 * 
 * @author Kamil Szostakowski
 */

public class PostgresTestJob extends PostgresBaseJob
{    
    //private String url = "jdbc:postgresql://localhost/avilar";
    //private String user = "avilar";
    //private String password = "x11y85aa";       
    
    private String query = "INSERT INTO document (id, threadid, title, content) VALUES('%s', %d, '%s', '%s')";    
    private String select = "SELECT * FROM document where id = '%s'";                   
    
    /*
     * Metoda definiująca operację pobrania dokumentu z bazy danych.
     */    
    
    @Override
    public int PerformSelectOperation(int identifier)
    {
        try
        {           
            int thread = this.randomizer.nextInt(10);
            int operation = this.randomizer.nextInt(100000);
            
            String id = String.format("%d-%d", thread, operation);
            
            this.statement.execute(String.format(this.select, id));                                               
        }
        
        catch (SQLException ex) 
        {
            Logger.getLogger(PostgresTestJob.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        return 0;
    }
    
    /*
     * Metoda definiująca operację wstawienie dokumentu do bazy danych.
     */      
    
    @Override
    public void PerformInsertOperation(int identifier)
    {
        try 
        {
            String id = String.format("%d-%d", this.threadID, identifier);
            String title = String.format("Title %d", identifier);
            String content = (String) this.dataSource.GetData(identifier);        
            
            this.statement.execute(String.format(this.query, id, this.threadID, title, content));
        } 
        
        catch (Exception ex) 
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
        return new PostgresTestJob();
    }
}