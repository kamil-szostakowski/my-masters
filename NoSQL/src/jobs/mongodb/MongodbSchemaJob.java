package jobs.mongodb;

import basejobs.IDatabaseJob;

/**
 * Zadanie mające na celu zbudowanie w bazie danych MongoDB schematu 
 * na którym bedą operowały pozostałe testy.
 * 
 * @author Kamil Szostakowski
 */

public class MongodbSchemaJob extends MongodbBaseJob
{      
    /*
     * Na obecnym poziomie żadne dodatkowe definicje nie są potrzebne.
     */
    
    @Override
    public void PerformInsertOperation(int identifier)
    {        
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
        return new MongodbSchemaJob();
    }    
}