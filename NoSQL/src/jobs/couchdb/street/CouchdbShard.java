package jobs.couchdb.street;

import java.util.LinkedList;
import org.jcouchdb.db.Database;

/**
 *
 * @author kamil
 */
public class CouchdbShard 
{
    private String shardName;
    private LinkedList<Database> connections;
    private LinkedList<CouchdbCredentials> credentials;
    
    public CouchdbShard(String name)
    {
        this.connections = new LinkedList<Database>();
        this.credentials = new LinkedList<CouchdbCredentials>();
        
        this.shardName = name;
    }
    
    public void AddCredentails(CouchdbCredentials credentials)
    {
        this.credentials.add(credentials);
        
        Database connection = new Database(credentials.GetHost(), credentials.GetPort().intValue(), credentials.GetDbname());
        
        this.connections.add(connection);
    }
    
    public Database GetConnection(int node)
    {
        int nodeNumber = node % credentials.size();
        
        return this.connections.get(nodeNumber);                
    } 
    
    @Override
    public String toString()
    {
        String repr = "";
        
        for(CouchdbCredentials credential : this.credentials)
        {
            repr += String.format("%s\n", credential.toString());
        }
        
        return repr;
    }    
}
