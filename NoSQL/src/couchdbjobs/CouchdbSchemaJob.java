package couchdbjobs;

import java.util.HashMap;
import java.util.Map;
import basejobs.IDatabaseJob;

/**
 *
 * @author test
 */

public class CouchdbSchemaJob extends CouchdbBaseJob
{
    public void PerformInsertOperation(int identifier)
    {              
       Map<String, Object> viewAll = new HashMap<>(); viewAll.put("map", "function(doc) { emit(doc.id, null) }");
       Map<String, Object> viewOdds = new HashMap<>(); viewOdds.put("map", "function(doc) { if (doc.id % 2 == 0)  emit(doc.id, null) }");
       Map<String, Object> viewEven = new HashMap<>(); viewEven.put("map", "function(doc) { if (doc.id % 2  == 1)  emit(doc.id, null) }");
       
       Map<String, Object> views = new HashMap<>();
       
       views.put("all", viewAll);
       views.put("odds", viewOdds);
       views.put("even", viewEven);
       
       Map<String, Object> document = new HashMap<>();
        
       document.put("_id", "_design/queries");
       document.put("language", "javascript");        
       document.put("views", views);
            
       this.db.createDocument(document);                          
    }
    
    public IDatabaseJob Clone()
    {
        return new CouchdbSchemaJob();
    }    
}