package couchdbjobs;

import basejobs.BaseJob;
import org.jcouchdb.db.Database;
import tools.Configuration;

/**
 *
 * @author test
 */
public abstract class CouchdbBaseJob extends BaseJob
{
    // Database connection    
    protected Database db;     
    
    private String host = (String) Configuration.GetParam("couchdb:host");
    private String dbname = (String) Configuration.GetParam("couchdb:dbname");
    private Long port = (Long) Configuration.GetParam("couchdb:port");    
    
    @Override
    public void Connect() 
    {
        this.WriteLog("CouchDB: test started");        
        this.db = new Database(host, port.intValue(), dbname);
        
        this.WriteLog("CouchDB: connection established");
    }

    @Override
    public void Disconnect() 
    {        
    }    
}