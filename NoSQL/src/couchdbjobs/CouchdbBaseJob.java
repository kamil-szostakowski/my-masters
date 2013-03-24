package couchdbjobs;

import basejobs.BaseJob;
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
    public void Connect() 
    {
        this.WriteLog("CouchDB: test started");        
        this.db = new Database(host, port.intValue(), dbname);
        
        this.WriteLog("CouchDB: connection established");
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