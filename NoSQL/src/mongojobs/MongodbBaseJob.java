package mongojobs;

import basejobs.BaseJob;
import basejobs.LogEntry;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.Configuration;

/**
 * Metoda dostarczająca podstawowych mechanizmów dla wszystkich
 * zadań wykonywanych na bazie MongoDB
 * 
 * Wczytanie informacji o konfiguracji MongoDB z pliku konfiguracyjnego
 * 
 * Dostarcza metodę do zestawienia połączenia z bazą danych CouchDB
 * 
 * Dostarcza metodę do likwidacji połaczenia z bazą danych.
 * 
 * @author Kamil Szostakowski
 */

public abstract class MongodbBaseJob extends BaseJob
{
    protected MongoClient client;
    protected DB db;
    protected DBCollection collection;
    
    private String host = (String) Configuration.GetParam("mongodb:host");
    private Long port = (Long) Configuration.GetParam("mongodb:port");
    private String dbname = (String) Configuration.GetParam("mongodb:dbname");
    private String collectionName = (String) Configuration.GetParam("mongodb:collection");
    
    /*
     * Metoda zestawiająca połaczenie z bazą danych MongoDB
     */    
    
    @Override
    public void Connect() 
    {
        try 
        {              
            LogEntry entry = new LogEntry();
        
            entry.SetOperationType("ConnectionEstablished");
            entry.SetOperationTime(0);
            entry.SetThreadId(this.threadID);             
            
            this.client = new MongoClient(host, port.intValue());
                
            this.WriteLog(entry);
                
            this.db = client.getDB(dbname);
                
            entry.SetOperationType("DatabaseSelected");
            
            this.WriteLog(entry);
                
            this.collection = this.db.getCollection(collectionName);        
            
            entry.SetOperationType("CollectionSelected");
                
            this.WriteLog(entry);
            
        } 
        
        catch (UnknownHostException ex) 
        {
            Logger.getLogger(MongodbTestJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Metoda pozwalająca stworzyć instancję tego zadania 
     * do uruchomienia w innym wątku.
     */     
    
    @Override
    public void Disconnect() 
    {        
    }    
}