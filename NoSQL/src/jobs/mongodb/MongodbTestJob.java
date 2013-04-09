package jobs.mongodb;

import basejobs.IDatabaseJob;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Właściwe zadanie testowe dla bazy MongoDB mierzącej jej wydajność
 * podczas wykonywania operacji select/insert w proporcjach zdefiniowanych
 * przez przebieg przypisany tenu zadaniu.
 * 
 * @author Kamil Szostakowski
 */

public class MongodbTestJob extends MongodbBaseJob
{      
    /*
     * Metoda zwraca nazwę zadania, używana między innymi to tworzenia nazw plików
     * z logami.
     */
    
    public String GetName()
    {
        return "mongodb-test";
    }   
    
    /*
     * Metoda definiująca operację pobrania dokumentu z bazy danych.
     */
    
    public int PerformSelectOperation(int identifier)
    {            
        BasicDBObject query = new BasicDBObject("id", new BasicDBObject("$in", this.GetDocumentListForIter(identifier)));
        
        DBCursor cursor = this.collection.find(query);
        
        return cursor.count();
    }
    
    /*
     * Metoda definiująca operację wstawienie dokumentu do bazy danych.
     */     
    
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
    
    /*
     * Metoda pozwalająca stworzyć instancję tego zadania 
     * do uruchomienia w innym wątku.
     */     
    
    public IDatabaseJob Clone()
    {
        return new MongodbTestJob();
    }    
}