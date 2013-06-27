/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jobs.couchdb.street;

import basejobs.BaseJob;
import basejobs.LogEntry;
import data.source.IDataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.jcouchdb.db.Database;
import org.jcouchdb.document.ValueRow;
import org.jcouchdb.document.ViewResult;
import tools.Configuration;

/**
 *
 * @author kamil
 */

public abstract class CouchdbClusterBaseJob extends BaseJob
{
    protected Database mainNode;     
    
    private String host = (String) Configuration.GetParam("couchdb-cluster:host");
    private String dbname = (String) Configuration.GetParam("couchdb-cluster:dbname");
    private Long port = (Long) Configuration.GetParam("couchdb-cluster:port");     
    
    protected IDataSource dataSource;
    
    protected HashMap<String, CouchdbShard> shards;
    
    public CouchdbClusterBaseJob()
    {
        super();
        
        this.shards = new HashMap<>();
    }
    
    /*
     * Metoda pozwala zdefiniować źródło danych dla zadania.
     */
    
    @Override
    public void SetDataSource(IDataSource dataSource)
    {
        this.dataSource = dataSource;
    } 

    /**
     * Metoda zestawiająca połaczenie z bazą danych CouchDB
     */    
    
    @Override
    public boolean Connect() 
    {
        this.mainNode = new Database(host, port.intValue(), dbname);
        
        LogEntry connectionEntry = new LogEntry();
        
        connectionEntry.SetOperationType("ConnectionEstablished");
        connectionEntry.SetOperationTime(0);
        connectionEntry.SetThreadId(this.threadID);        
        
        this.WriteLog(connectionEntry);
        
        this.PrepareClusterMetadata();
        
        return true;        
    }
    
    /**
     * Metoda która czyta metadane klastra i na ich podstawie
     * buduje indeks wszystkich węzłów i baz danych na nich 
     * się znajdujących.
     */
    
    private void PrepareClusterMetadata()
    {
        ViewResult<Map> result = this.mainNode.queryView("regions/all", Map.class, null, null);        
        
        for (ValueRow<Map> res : result.getRows())
        {
            Map document = res.getValue();
            
            this.PrepareShardCredentials(document);
        }      
    }
    
    /**
     * Metoda przygotowuje połączenia dla wszystkich węzłów klastra.
     * 
     * @param document 
     */
    
    private void PrepareShardCredentials(Map document)
    {                        
        String database = (String) document.get("database");
        ArrayList<Map> nodes = (ArrayList) document.get("nodes");
        
        if(!this.shards.containsKey(database))
        {
            CouchdbShard shard = new CouchdbShard(database);
            
            this.shards.put(database, shard);
        }
            
        for(Map node : nodes)
        {
            String nodeHost = (String) node.get("host");
            Long nodePort = Long.parseLong((String) node.get("port"));
            String nodeDbname = (String) node.get("dbname");
                
            CouchdbCredentials credentials = new CouchdbCredentials(nodeHost, nodePort, nodeDbname);   
            
            this.shards.get(database).AddCredentails(credentials);
        }
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
