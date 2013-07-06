package jobs.couchdb.street;

import basejobs.IDatabaseJob;
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

/**
 * http://127.0.0.1:9080/db/couchdb/job/cluster-feed/run/cluster-insert/datasource/xml-street/id/feed-cluster-insert
 * http://127.0.0.1:9080/db/couchdb/job/cluster-feed/run/cluster-update/datasource/xml-region/id/feed-cluster-update
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
            
            System.out.println(String.format("Update operation performed: %s %d", db, result.getRows().size()));
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
        for(ValueRow<Map> document : documents)
        {            
            try 
            {
                Map doc = document.getValue();                                                
                
                HashMap<String, String> request = new HashMap<String, String>();
                
                request.put("WOJ", (String) doc.get("WOJ"));
                request.put("POW", (String) doc.get("POW"));
                request.put("GMI", (String) doc.get("GMI"));
                request.put("RODZ", (String) doc.get("RODZ_GMI"));
                
                this.debugRequestedRegions++;
                
                 XmlRegion region = (XmlRegion) this.dataSource.GetData(request);                
                 
                 if(region != null)
                 {
                     this.debugFoundRegions++;
                     
                    doc.put("MIEJSCOWOSC_NAZWA", region.GetNazwa());
                    doc.put("MIEJSCOWOSC_TYP", region.GetNazwaOd());                     
                    
                    this.shards.get(db).GetConnection(this.MainNodeIndex).updateDocument(doc);
                 }                                                  
            } 
            
            catch (Exception ex) 
            {
                Logger.getLogger(CouchdbClusterFeed.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
