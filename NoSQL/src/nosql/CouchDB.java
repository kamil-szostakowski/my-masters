/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nosql;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jcouchdb.db.Database;

/**
 *
 * @author test
 */

public class CouchDB extends BaseTest  
{
    // Database connection    
    private Database db; 
    
    protected void InitTest()
    {
        super.InitTest();
        
        this.WriteLog("CouchDB: test started");        
        this.db = new Database("127.0.0.1", "testdb");
        
        this.WriteLog("CouchDB: connection established");        
    }    
    
    public void PerformInsertOperation(int identifier)
    {
        try 
        {
            Map<String, String> document = new HashMap<>();
        
            document.put("title", String.format("Title %d", identifier));        
            document.put("content", this.test.GetContent(identifier));
            
            this.db.createDocument(document);
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(CouchDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}