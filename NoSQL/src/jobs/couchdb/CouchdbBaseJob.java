package jobs.couchdb;

import basejobs.BaseJob;
import basejobs.LogEntry;
import org.jcouchdb.db.Database;
import tools.Configuration;

/**
 * Metoda dostarczająca podstawowych mechanizmów dla wszystkich
 * zadań wykonywanych na bazie CouchDB
 * 
 * Wczytanie informacji o konfiguracji CouchDB z pliku konfiguracyjnego
 * 
 * Dostarcza metodę do zestawienia połączenia z bazą danych CouchDB
 * 
 * Dostarcza metodę do likwidacji połaczenia z bazą danych.
 * 
 * @author Kamil Szostakowski
 */
public abstract class CouchdbBaseJob extends BaseJob
{     
    protected Database db;     
    
    private String host = (String) Configuration.GetParam("couchdb:host");
    private String dbname = (String) Configuration.GetParam("couchdb:dbname");
    private Long port = (Long) Configuration.GetParam("couchdb:port");    
    
    /*
     * Metoda zestawiająca połaczenie z bazą danych CouchDB
     */
    
    @Override
    public boolean Connect() 
    {               
        this.db = new Database(host, port.intValue(), dbname);
        
        LogEntry connectionEntry = new LogEntry();
        
        connectionEntry.SetOperationType("ConnectionEstablished");
        connectionEntry.SetOperationTime(0);
        connectionEntry.SetThreadId(this.threadID);        
        
        this.WriteLog(connectionEntry);
        
        return true;
    }

    /*
     * Metoda dokonująca likwidacji połączenia z CouchDB. Nie jest
     * wykorzystywana ze względu na charakter samej bazy danych.
     */
    
    @Override
    public void Disconnect() 
    {        
    }    
}