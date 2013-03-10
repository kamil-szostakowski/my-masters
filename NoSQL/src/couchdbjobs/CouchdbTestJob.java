/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package couchdbjobs;

import basejobs.BaseJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import basejobs.IDatabaseJob;
import org.jcouchdb.db.Database;
import org.jcouchdb.db.Options;
import org.jcouchdb.document.ViewResult;
import org.jcouchdb.util.CouchDBUpdater;
import org.svenson.JSON;
import org.svenson.JSONParser;
import tools.Configuration;
import tools.StringTools;

/**
 *
 * @author test
 */

public class CouchdbTestJob extends CouchdbBaseJob  
{          
    //http://127.0.0.1:5984/testdb/_design/queries/_view/odds
    //http://127.0.0.1:5984/testdb/_design/queries/_view/even?keys=[%2213%22,%2217%22]
    
    public int PerformSelectOperation(int identifier)
    {   
        int[] keysArray = this.GetDocumentListForIter(identifier);
        String keysString = StringTools.Join(keysArray, ",", true);
        
        Map<String, Object> query = new HashMap<>();
        
        query.put ("keys", String.format("[%s]", keysString));        
        
        Options options = new Options(query);                      
        
         ViewResult<Map> result = this.db.queryView("queries/all", Map.class, options, null);                                   
         
         return result.getRows().size();
    }    
    
    public void PerformInsertOperation(int identifier)
    {
        try 
        {
            Map<String, String> document = new HashMap<>();
        
            document.put("id", String.format("%d", identifier));
            document.put("threadid", String.format("%d", this.threadID));
            document.put("title", String.format("Title %d", identifier));        
            document.put("content", this.test.GetContent(identifier));
            
            this.db.createDocument(document);
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(CouchdbTestJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    public IDatabaseJob Clone()
    {
        return new CouchdbTestJob();
    }    
}