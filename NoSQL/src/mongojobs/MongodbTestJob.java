package mongojobs;

import basejobs.IDatabaseJob;
import basejobs.BaseJob;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.util.JSONSerializers;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import tools.Configuration;

/**
 *
 * @author test
 */

public class MongodbTestJob extends MongodbBaseJob
{            
    public int PerformSelectOperation(int identifier)
    {            
        BasicDBObject query = new BasicDBObject("id", new BasicDBObject("$in", this.GetDocumentListForIter(identifier)));
        
        DBCursor cursor = this.collection.find(query);
        
        return cursor.count();
    }
    
    public void PerformInsertOperation(int identifier)
    {
        try 
        {
            BasicDBObject document = new BasicDBObject();
            
            document.append("id", identifier);
            document.append("threadid", this.threadID);
            document.append("title", String.format("Title %d", identifier));
            document.append("content", test.GetContent(identifier));        
            
            this.collection.insert(document);            
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(MongodbTestJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public IDatabaseJob Clone()
    {
        return new MongodbTestJob();
    }    
}