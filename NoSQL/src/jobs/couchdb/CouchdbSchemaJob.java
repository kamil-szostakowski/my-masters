package jobs.couchdb;

import basejobs.IDatabaseJob;
import java.util.HashMap;
import java.util.Map;

/**
 * Zadanie mające na celu zbudowanie w bazie danych CouchDB schematu 
 * na którym bedą operowały pozostałe testy.
 * 
 * Zadanie definiuje wszystkie widoki na podstawie których będzie można
 * dokonywać pobierania dokumentów z bazy.
 * 
 * @author Kamil Szostakowski
 */

public class CouchdbSchemaJob extends CouchdbBaseJob
{   
    /*
     * Metoda definiujące sposób wykonania metody insert dla tego zadania.
     * 
     * Do bazy dodawany jest dokument definiujacy trzy następujące widoku
     * 
     * - widok pozwalający na pobrania ze bazy danych wszystkich dokumentów
     * - widok pozwalający na pobranie z bazy danych dokumentów
     * o parzystych identyfikatorach.
     * - widok pozwalający na pobranie z bazy danych dokumentów o 
     * nieparzystych identyfikatorach.
     */
    
    @Override
    public void PerformInsertOperation(int identifier)
    {              
       Map<String, Object> viewAll = new HashMap<String, Object>(); viewAll.put("map", "function(doc) { emit(doc.id, null) }");
       Map<String, Object> viewOdds = new HashMap<String, Object>(); viewOdds.put("map", "function(doc) { if (doc.id % 2 == 0)  emit(doc.id, null) }");
       Map<String, Object> viewEven = new HashMap<String, Object>(); viewEven.put("map", "function(doc) { if (doc.id % 2  == 1)  emit(doc.id, null) }");
       
       Map<String, Object> views = new HashMap<String, Object>();
       
       views.put("all", viewAll);
       views.put("odds", viewOdds);
       views.put("even", viewEven);
       
       Map<String, Object> document = new HashMap<String, Object>();
        
       document.put("_id", "_design/queries");
       document.put("language", "javascript");        
       document.put("views", views);
            
       this.db.createDocument(document);                          
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
        return new CouchdbSchemaJob();
    }    
}