/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mongojobs;

import basejobs.BaseJob;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import runs.IDatabaseRun;
import tools.Configuration;

/**
 *
 * @author test
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
    
    @Override
    public void Connect() 
    {
        try 
        {                     
            this.client = new MongoClient(host, port.intValue());
                
            this.WriteLog("MongoDB: connection established");
                
            this.db = client.getDB(dbname);
                
            this.WriteLog("MongoDB: database selected");
                
            this.collection = this.db.getCollection(collectionName);        
                
            this.WriteLog("MongoDB: collection got");
            
        } 
        
        catch (UnknownHostException ex) 
        {
            Logger.getLogger(MongodbTestJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void Disconnect() 
    {        
    }    
}