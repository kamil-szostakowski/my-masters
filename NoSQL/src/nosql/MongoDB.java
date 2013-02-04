package nosql;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author test
 */
public class MongoDB extends BaseTest
{
    private MongoClient client;
    private DB db;
    private DBCollection collection;
    
    protected void InitTest()
    {
        try 
        {
            super.InitTest();
            
            this.client = new MongoClient("localhost", 27017);
                
            this.WriteLog("MongoDB: connection established");
                
            this.db = client.getDB("testdb");
                
            this.WriteLog("MongoDB: database selected");
                
            this.collection = this.db.getCollection("test_collection");        
                
            this.WriteLog("MongoDB: collection got");
            
        } 
        
        catch (UnknownHostException ex) 
        {
            Logger.getLogger(MongoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }      
    
    public void PerformSelectOperation()
    {
        
    }
    
    public void PerformInsertOperation(int identifier)
    {
        try 
        {
            BasicDBObject document = new BasicDBObject();
            
            document.append("title", String.format("Title %d", identifier));
            document.append("content", test.GetContent(identifier));        
            
            this.collection.insert(document);            
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(MongoDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}