package couchdbjobs;

import basejobs.IDatabaseJob;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jcouchdb.db.Options;
import org.jcouchdb.document.ViewResult;
import tools.StringTools;

/**
 * Właściwe zadanie testowe dla bazy CouchDB mierzącej jej wydajność
 * podczas wykonywania operacji select/insert w proporcjach zdefiniowanych
 * przez przebieg przypisany tenu zadaniu.
 * 
 * @author Kamil Szostakowski
 */

public class CouchdbTestJob extends CouchdbBaseJob  
{       
    /*
     * Metoda zwraca nazwę zadania, używana między innymi to tworzenia nazw plików
     * z logami.
     */
    
    public String GetName()
    {
        return "couchdb-test";
    }
    
    /*
     * Metoda definiująca operację pobrania dokumentu z bazy danych.
     */
    
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
    
    /*
     * Metoda definiująca operację wstawienie dokumentu do bazy danych.
     */    
    
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
    
    /*
     * Metoda pozwalająca stworzyć instancję tego zadania 
     * do uruchomienia w innym wątku.
     */     
    
    public IDatabaseJob Clone()
    {
        return new CouchdbTestJob();
    }    
}