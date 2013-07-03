package basejobs;

import java.util.HashMap;
import java.util.Map;

/**
 * Klasa enkapsulujaca logowanie informacji z wykonywanych zadań do plików
 * z logami.
 * 
 * @author Kamil Szostakowski
 */

public class LogEntry implements ILogEntry
{
    private int threadId;
    private long executionTime;
    private String operationType;
    
    private Map<String, String> parameters;
    
    public LogEntry()
    {        
        this.parameters = new HashMap<String, String>();
    }
    
    /*
     * Metoda służy do definiowania identyfikatora wątku
     * który zapisuje log.
     */
    
    public void SetThreadId(int id)
    {
        this.threadId = id;
    }
    
    /*
     * Metoda definiująca czas wykonania operacji.
     */
    
    public void SetOperationTime(long time)
    {
        this.executionTime = time;
    }
    
    /*
     * Metoda definiująca typ operacji.
     */
    
    public void SetOperationType(String type)
    {
        this.operationType = type;
    }      
    
    public void SetParameter(String key, String value)
    {
        this.parameters.put(key, value);        
    }
    
    /*
     * Metoda zwracająca tekstową reprezentację loga.
     */
    
    @Override
    public String toString()
    {
        String params = "";
        
        for(Map.Entry<String, String> entry : this.parameters.entrySet())
        {
            params += String.format("<%s>%s</%s>", entry.getKey(), entry.getValue(), entry.getKey());            
        }
        
        return String.format("<operation type=\"%s\" time=\"%d\">%s</operation>", this.operationType, this.executionTime, params);
    }
}


