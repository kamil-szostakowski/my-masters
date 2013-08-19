package jobs.couchdb;

import basejobs.IDatabaseJob;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jcouchdb.db.Options;
import org.jcouchdb.document.ViewResult;

/**
 * Właściwe zadanie testowe dla bazy CouchDB mierzącej jej wydajność
 * podczas wykonywania operacji select/insert/update/delete w proporcjach zdefiniowanych
 * przez przebieg przypisany tenu zadaniu.
 * 
 * http://127.0.0.1:5984/singlemachine/_design/query/_view/all?keys=[%2213%22,%2217%22]
 * http://127.0.0.1:9080/db/couchdb/job/test/run/singleload/datasource/file/id/couchdbinsert
 * http://127.0.0.1:9080/db/couchdb/job/test/run/test75/datasource/file/id/couchdb-mixed
 * http://127.0.0.1:9080/db/couchdb/job/test/run/test100/datasource/file/id/couchdb-read
 * 
 * @author Kamil Szostakowski
 */

public class CouchdbTestJob extends CouchdbBaseJob  
{           
    /*
     * Metoda definiująca operację pobrania dokumentu z bazy danych.
     */
    
    //http://127.0.0.1:5984/testdb/_design/queries/_view/odds
    //http://127.0.0.1:5984/testdb/_design/queries/_view/even?keys=[%2213%22,%2217%22]
    
    @Override
    public int PerformSelectOperation(int identifier)
    {           
        Map<String, Object> query = new HashMap<String, Object>();
        
        int thread = this.randomizer.nextInt(10);
        int operation = this.randomizer.nextInt(100000);
            
        String id = String.format("%d-%d", thread, operation);        
        
        query.put ("key", id);        
        
        Options options = new Options(query);                      
        
        ViewResult<Map> result = this.db.queryView("query/all", Map.class, options, null);                                   
         
        return result.getRows().size();
    }    
    
    /*
     * Metoda definiująca operację wstawienie dokumentu do bazy danych.
     */    
    
    @Override
    public void PerformInsertOperation(int identifier)
    {
        try 
        {
            Map<String, String> document = new HashMap<String, String>();
        
            document.put("id", String.format("%d-%d", this.threadID, identifier));
            document.put("threadid", String.format("%d", this.threadID));
            document.put("title", String.format("Title %d", identifier));        
            document.put("content", (String) this.dataSource.GetData(identifier));
            
            this.db.createDocument(document);
        } 
        
        catch (Exception ex) 
        {
            Logger.getLogger(CouchdbTestJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    
    /*
     * Metoda definiująca jak ma wyglądać operacja update 
     * dla danego zadania.
     */      
    
    @Override
    public void PerformUpdateOperation(int identifier) 
    {    
    }

    /*
     * Metoda definiująca jak ma wyglądać operacja delete 
     * dla danego zadania.
     */    
    
    @Override
    public void PerformDeleteOperation(int identifier) 
    {    
    }    
    
    /*
     * Metoda pozwalająca stworzyć instancję tego zadania 
     * do uruchomienia w innym wątku.
     */     
    
    @Override
    public IDatabaseJob Clone()
    {
        return new CouchdbTestJob();
    }    
}