package jobs.couchdb.street;

import basejobs.IDatabaseJob;
import basejobs.LogEntry;
import data.source.XmlRegion;
import data.source.XmlStreet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jcouchdb.db.Options;
import org.jcouchdb.document.ValueRow;
import org.jcouchdb.document.ViewResult;
import org.jcouchdb.exception.CouchDBException;

/**
 * http://127.0.0.1:9080/db/couchdb/job/cluster-feed/run/cluster-insert/datasource/xml-street/id/feed-cluster-insert
 * http://127.0.0.1:9080/db/couchdb/job/cluster-feed/run/cluster-update/datasource/xml-region/id/feed-cluster-update
 * http://127.0.0.1:9080/db/couchdb/job/cluster-feed/run/cluster-select/datasource/empty/id/feed-cluster-select
 * 
 * 02 - DOLNOŚLĄSKIE
 * 04 - KUJAWSKO-POMORSKIE
 * 06 - LUBELSKIE
 * 08 - LUBUSKIE
 * 10 - ŁÓDZKIE
 * 12 - MAŁOPOLSKIE
 * 14 - MAZOWIECKIE
 * 16 - OPOLSKIE
 * 18 - PODKARPACKIE
 * 20 - PODLASKIE
 * 22 - POMORSKIE
 * 24 - ŚLĄSKIE
 * 26 - ŚWIĘTOKRZYSKIE
 * 28 - WARMIŃSKO-MAZURSKIE
 * 30 - WIELKOPOLSKIE
 * 32 - ZACHODNIOPOMORSKIE
 * 
 * @author kamil
 */

public class CouchdbClusterFeed extends CouchdbClusterBaseJob
{           
    private int sequenceNumber = 0;
    
    private int debugRequestedRegions = 0;
    private int debugFoundRegions = 0;
    
    /**
     * Indeks węzła który obsługuje obsługuje operacje zapisu
     * dla danego województwa.
     */
    
    private final int MainNodeIndex = 0;
    
    private HashMap<String, String> supportedRegions;
    
    /**
     * Domyślny konstruktor budujący listę wspieranych baz danych.
     */
    
    public CouchdbClusterFeed()
    {
        this.supportedRegions = new HashMap<String, String>();
         
        this.supportedRegions.put("04", "woj-kujawsko-pomorskie");
        this.supportedRegions.put("08", "woj-lubuskie");
        this.supportedRegions.put("12", "woj-malopolskie");
        this.supportedRegions.put("14", "woj-mazowieckie");
        this.supportedRegions.put("18", "woj-podkarpackie");
        this.supportedRegions.put("20", "woj-podlaskie");
        this.supportedRegions.put("22", "woj-pomorskie");
        this.supportedRegions.put("28", "woj-warminsko-mazurskie");
        this.supportedRegions.put("30", "woj-wielkopolskie");
        this.supportedRegions.put("32", "woj-zachodnio-pomorskie");        
    }
    
    @Override
    public int PerformSelectOperation(int identifier)
    {     
        long documentSelectStartTime = System.currentTimeMillis();                
        
        int dbIndex = identifier % this.supportedRegions.size();            
        int nodeIndex = this.randomizer.nextInt(3);
        
        String db = this.GetDatabaseNameForIndex(dbIndex);
        String key = this.GetRandomCityKeyForIndex(dbIndex);
        
        if(db != null)
        {
            Map<String, Object> query = new HashMap<String, Object>();
        
            query.put ("key", key);            
        
            Options options = new Options(query);  
            
            ViewResult<Map> result;
            
            for(int iter=0; iter<3; iter++)
            {
                try
                {                                        
                    result = this.shards.get(db).GetConnection(nodeIndex+iter).queryView("streets/city-code", Map.class, options, null);
                    
                    System.out.println(String.format("%s: %s", key, result.getRows().size()));
            
                    LogEntry logEntry = new LogEntry();
            
                    logEntry.SetOperationType("doc-select");
                    logEntry.SetOperationTime(System.currentTimeMillis()-documentSelectStartTime);
                    logEntry.SetThreadId(this.threadID);
                    logEntry.SetParameter("database", db);
                    logEntry.SetParameter("node", String.format("%d", dbIndex));
                    logEntry.SetParameter("city-key", key);
                    logEntry.SetParameter("result-count", String.format("%d", result.getRows().size()));
                    logEntry.SetParameter("retry-count", String.format("%d", iter));

                    this.WriteLog(logEntry);            
            
                    return result.getRows().size();                    
                }
                
                catch(CouchDBException exception)
                {                    
                    System.out.println(String.format("RETRY CouchDBException %s:", exception.getMessage()));
                }
            }  
            
            System.out.println("Request failed: skipped");
        }
        
        return 0;
    }
    
    @Override
    public void PerformInsertOperation(int identifier)
    {        
        try 
        {
            //int connectioNodeIndex = this.randomizer.nextInt(3);
            int connectioNodeIndex = this.MainNodeIndex;
            
            XmlStreet street = (XmlStreet) this.dataSource.GetData(identifier);
            
            Map<String, String> document = new HashMap<String, String>();
            
            document.put("id", String.format("%d", identifier));
            document.put("threadid", String.format("%d", this.threadID));
            
            document.put("WOJ", street.GetWoj());        
            document.put("POW", street.GetPow());
            document.put("GMI", street.GetGmi());        
            document.put("RODZ_GMI", street.GetRodzGmi());
            document.put("SYM", street.GetSym());
            
            document.put("SYM_UL", street.GetSymUl());        
            document.put("CECHA", street.GetCecha());
            document.put("NAZWA_1", street.GetNazwa1());        
            document.put("NAZWA_2", street.GetNazwa2());
            document.put("STAN_NA", street.GetStanNa());            
            
            String dbName = this.GetDatabaseNameForCode(street.GetWoj());
            
            if(dbName != null)
            {
                this.shards.get(dbName).GetConnection(connectioNodeIndex).createDocument(document);                
            }            
        } 
        
        catch (Exception ex) 
        {
            Logger.getLogger(CouchdbClusterFeed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void PerformUpdateOperation(int identifier) 
    {          
        ViewResult<Map> result;

        int offset = 0;
        int limit = 100;
        int counter = 0;
        
        String db = this.GetDatabaseNameForIndex(identifier);               
        
        do
        {
            result = this.GetDocumentsBulkForDb(db, offset, limit);
            
            counter++;
            offset = counter*limit;
            
            this.SupplyDocumentsWithRegionInfo(db, result.getRows());                        
        } 
        
        while(result.getRows().size() > 0);     
        
        System.out.println(String.format("Requested: %d, Found: %d", this.debugRequestedRegions, this.debugFoundRegions));
    }

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
        return new CouchdbClusterFeed();
    }
    
    /**
     * Metoda zwraca nazwę bazy danych dla indeksu zdefiniowanego
     * jako parametr. Pierwsza baza na liście wpsieranych baz danych
     * zaostanie zwrócona jako wynik dla indeksu 0, druga dla indeksu
     * 1 itd.
     * 
     * @param index identyfikator operacji insert
     * @return nazwa bazy danych
     */
    
    private String GetDatabaseNameForIndex(int index)
    {                        
        String[] keys = this.supportedRegions.keySet().toArray(new String[0]);
        
        if(index < keys.length)
        {            
            return this.GetDatabaseNameForCode(keys[index]);                    
        }
        
        return null;
    }
    
    /**
     * Metoda zwraca losowo generowany kod miejscowości na podstawie
     * przekazanego jako parametr indeksu.           
     * 
     * @param index numer bazy danych do której ma zostać wykonane zapytanie
     * @return losowy kod miejscowości
     */
    
    private String GetRandomCityKeyForIndex(int index)
    {
        String[] keys = this.supportedRegions.keySet().toArray(new String[0]);
        
        if(index < keys.length)
        {
            String woj = keys[index];
            String pow = String.format("%02d", this.randomizer.nextInt(15));
            String gmi = String.format("%02d", this.randomizer.nextInt(15));
            String rodzGmi = String.format("%d", this.randomizer.nextInt(5));
            
            return String.format("%s|%s|%s|%s", woj, pow, gmi, rodzGmi);
        }
        
        return null;
    }
    
    /***
     * Metoda tłumacząca kod województwa na nazwę bazy danych
     * w której przechowywane są informacje o ulicach w tym
     * regionie.
     * 
     * @param code kod województwa
     * @return  nazwa bazy danych
     */
    
    private String GetDatabaseNameForCode(String code)
    {        
        if(this.supportedRegions.containsKey(code))
        {
            return this.supportedRegions.get(code);
        }
        
        return null;
    }
    
    /***
     * Metoda zwracająca listę dokumentów które będziemy aktualizować
     * podczas operacji update.
     * 
     * @param offset ilość dokumentów które chcemy pominąć
     * @param limit ilość dokumentów które chcemy pobrać
     * @return  lista pobranych dokumentów
     */
    
    private ViewResult<Map> GetDocumentsBulkForDb(String db, int offset, int limit)
    {                        
        Map<String, Object> query = new HashMap<String, Object>();
        
        query.put ("skip", offset);
        query.put ("limit", limit);
        
        Options options = new Options(query);                      
        
        ViewResult<Map> result = this.shards.get(db).GetConnection(this.MainNodeIndex).queryView("streets/all", Map.class, options, null);
         
        return result;
    }
    
    /**
     * Metoda uzupełnia dokumentom zawierającym informacje o ulicach
     * w kraju, informacje na temat miast w których te ulice się znajdują
     * 
     * @param documents lista dokumentów do obsłużenia
     */
    
    private void SupplyDocumentsWithRegionInfo(String db, List<ValueRow<Map>> documents) 
    {   
        long bulkUpdateStartTime = System.currentTimeMillis();
        
        for(ValueRow<Map> document : documents)
        {            
            try 
            {
                long documentUpdateStartTime = System.currentTimeMillis();
                
                Map doc = document.getValue();                                                
                
                HashMap<String, String> request = new HashMap<String, String>();
                
                request.put("WOJ", (String) doc.get("WOJ"));
                request.put("POW", (String) doc.get("POW"));
                request.put("GMI", (String) doc.get("GMI"));
                request.put("RODZ", (String) doc.get("RODZ_GMI"));
                
                this.debugRequestedRegions++;
                
                 XmlRegion region = (XmlRegion) this.dataSource.GetData(request);                
                 
                 if(region != null && !doc.containsKey("MIEJSCOWOSC_NAZWA") && !doc.containsKey("MIEJSCOWOSC_TYP"))
                 {
                     this.debugFoundRegions++;
                     
                    doc.put("MIEJSCOWOSC_NAZWA", region.GetNazwa());
                    doc.put("MIEJSCOWOSC_TYP", region.GetNazwaOd());                     
                    
                    this.shards.get(db).GetConnection(this.MainNodeIndex).updateDocument(doc);
                    
                    LogEntry documentEntry = new LogEntry();
            
                    documentEntry.SetOperationType("doc-update");
                    documentEntry.SetOperationTime(System.currentTimeMillis()-documentUpdateStartTime);
                    documentEntry.SetThreadId(this.threadID);

                    this.WriteLog(documentEntry);
                 }                                                  
            } 
            
            catch (Exception ex) 
            {
                Logger.getLogger(CouchdbClusterFeed.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        LogEntry bulkEntry = new LogEntry();
            
        bulkEntry.SetOperationType("bulk-update");
        bulkEntry.SetOperationTime(System.currentTimeMillis()-bulkUpdateStartTime);
        bulkEntry.SetThreadId(this.threadID);
        bulkEntry.SetParameter("size", String.format("%d", documents.size()));
        
        this.WriteLog(bulkEntry);
    }
}
