package postgresjobs;

import basejobs.BaseJob;
import basejobs.LogEntry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.Configuration;

/**
 * Metoda dostarczająca podstawowych mechanizmów dla wszystkich
 * zadań wykonywanych na bazie PostgreSQL
 * 
 * Wczytanie informacji o konfiguracji PostgreSQL z pliku konfiguracyjnego
 * 
 * Dostarcza metodę do zestawienia połączenia z bazą danych PostgreSQL
 * 
 * Dostarcza metodę do likwidacji połaczenia z bazą danych.
 * 
 * @author Kamil Szostakowski
 */

public abstract class PostgresBaseJob extends BaseJob
{
    protected Connection connection = null;
    protected Statement statement = null;   
    
    private String url = (String) Configuration.GetParam("postgresql:url"); //"jdbc:postgresql://localhost/testdb";
    private String user = (String) Configuration.GetParam("postgresql:user"); //"postgres";
    private String password = (String) Configuration.GetParam("postgresql:password"); //"kamil123";     
    
    /*
     * Metoda zestawiająca połaczenie z bazą danych PostgreSQL
     */     
    
    @Override
    public void Connect() 
    {
        try 
        {
            this.connection = DriverManager.getConnection(this.url, this.user, this.password);
            this.statement = this.connection.createStatement();        
            
            LogEntry connectionEntry = new LogEntry();
        
            connectionEntry.SetOperationType("ConnectionEstablished");
            connectionEntry.SetOperationTime(0);
            connectionEntry.SetThreadId(this.threadID);             
            
            this.WriteLog(connectionEntry);
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
    
    @Override
    public void Disconnect() 
    {
        try 
        {
            this.connection.close();
        } 
        
        catch (SQLException ex) 
        {
            Logger.getLogger(PostgresTestJob.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }    
}