package jobs.couchdb.street;

import basejobs.IDatabaseJob;
import data.source.XmlStreet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * 127.0.0.1:9080/db/couchdb/job/cluster-feed/run/prepare/datasource/xml-street/id/feed-cluster-1
 * 
 * @author kamil
 */
public class CouchdbClusterFeed extends CouchdbClusterBaseJob
{           
    private int sequenceNumber = 0;
    
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
            int connectioNodeIndex = 0;
            
            XmlStreet street = (XmlStreet) this.dataSource.GetData(0);
            
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
            
            this.shards.get("wielkopolskie").GetConnection(connectioNodeIndex).createDocument(document);
        } 
        
        catch (Exception ex) 
        {
            Logger.getLogger(CouchdbClusterFeed.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void PerformUpdateOperation(int identifier) 
    {        
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
    
}
