package jobs.mongodb;

import basejobs.IDatabaseJob;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Właściwe zadanie testowe dla bazy MongoDB mierzącej jej wydajność
 * podczas wykonywania operacji select/insert w proporcjach zdefiniowanych
 * przez przebieg przypisany tenu zadaniu.
 * 
 * http://127.0.0.1:9080/db/mongodb/job/test/run/singleload/datasource/file/id/mongodb-load
 * http://127.0.0.1:9080/db/mongodb/job/test/run/test75/datasource/file/id/mongodb-mixed
 * http://127.0.0.1:9080/db/mongodb/job/test/run/test100/datasource/file/id/mongodb-read
 * 
 * @author Kamil Szostakowski
 */

public class MongodbTestJob extends MongodbBaseJob
{           
    /*
     * Metoda definiująca operację pobrania dokumentu z bazy danych.
     */
    
    @Override
    public int PerformSelectOperation(int identifier)
    {         
        int thread = this.randomizer.nextInt(10);
        int operation = this.randomizer.nextInt(100000);
            
        String id = String.format("%d-%d", thread, operation); 
        
        BasicDBObject query = new BasicDBObject("id", id);
        
        DBCursor cursor = this.collection.find(query);
        
        return cursor.count();
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
     * Metoda definiująca operację wstawienie dokumentu do bazy danych.
     */     
    
    @Override
    public void PerformInsertOperation(int identifier)
    {
        try 
        {
            BasicDBObject document = new BasicDBObject();
            
            document.append("id", String.format("%d-%d", this.threadID, identifier));
            document.append("threadid", this.threadID);
            document.append("title", String.format("Title %d", identifier));
            document.append("content", this.dataSource.GetData(identifier));        
            
            this.collection.insert(document);            
        } 
        
        catch (Exception ex) 
        {
            Logger.getLogger(MongodbTestJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
     * Metoda pozwalająca stworzyć instancję tego zadania 
     * do uruchomienia w innym wątku.
     */     
    
    @Override
    public IDatabaseJob Clone()
    {
        return new MongodbTestJob();
    }    
}