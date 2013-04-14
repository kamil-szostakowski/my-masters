package jobs.postgresql;

import basejobs.IDatabaseJob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.StringTools;

/**
 * Właściwe zadanie testowe dla bazy PostgreSQL mierzącej jej wydajność
 * podczas wykonywania operacji select/insert w proporcjach zdefiniowanych
 * przez przebieg przypisany tenu zadaniu.
 * 
 * @author Kamil Szostakowski
 */

public class PostgresTestJob extends PostgresBaseJob
{    
    //private String url = "jdbc:postgresql://localhost/avilar";
    //private String user = "avilar";
    //private String password = "x11y85aa";       
    
    private String query = "INSERT INTO document (id, threadid, title, content) VALUES(%d, %d, '%s', '%s')";    
    private String select = "SELECT * FROM document where id IN (%s)";           
    
    /*
     * Metoda zwraca nazwę zadania, używana między innymi to tworzenia nazw plików
     * z logami.
     */
    
    @Override
    public String GetName()
    {
        return "test";
    }      
    
    /*
     * Metoda definiująca operację pobrania dokumentu z bazy danych.
     */    
    
    @Override
    public int PerformSelectOperation(int identifier)
    {
        try
        {                                               
            this.statement.execute(String.format(this.select, StringTools.Join(this.GetDocumentListForIter(identifier), ",", false)));                                                  
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
            String title = String.format("Title %d", identifier);
            String content = this.dataSource.GetData(identifier);        
            
            this.statement.execute(String.format(this.query, identifier, this.threadID, title, content));
        } 
        
        catch (Exception ex) 
        {
            Logger.getLogger(PostgresTestJob.class.getName()).log(Level.SEVERE, null, ex);
        }
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