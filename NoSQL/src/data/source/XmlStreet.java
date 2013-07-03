package data.source;

import java.util.HashMap;

/**
 *
 * 
 * 
 * @author kamil
 */

public class XmlStreet 
{
    private String woj = "empty";
    private String pow = "empty";
    private String gmi = "empty";
    private String rodz_gmi = "empty";
    private String sym = "empty";
    private String sym_ul = "empty";
    private String cecha = "empty";
    private String nazwa_1 = "empty";
    private String nazwa_2 = "empty";
    private String stan_na = "empty";
    
    public XmlStreet(HashMap<String, String> params)
    {                       
        if(params.containsKey("WOJ")){ this.woj = params.get("WOJ"); }
        if(params.containsKey("POW")){ this.pow = params.get("POW"); }
        if(params.containsKey("GMI")){ this.gmi = params.get("GMI"); }
        if(params.containsKey("RODZ_GMI")){ this.rodz_gmi = params.get("RODZ_GMI"); }
        if(params.containsKey("SYM")){ this.sym = params.get("SYM"); }
        
        if(params.containsKey("SYM_UL")){ this.sym_ul = params.get("SYM_UL"); }
        if(params.containsKey("CECHA")){ this.cecha = params.get("CECHA"); }
        if(params.containsKey("NAZWA_1")){ this.nazwa_1 = params.get("NAZWA_1"); }
        if(params.containsKey("NAZWA_2")){ this.nazwa_2 = params.get("NAZWA_2"); }
        if(params.containsKey("STAN_NA")){ this.stan_na = params.get("STAN_NA"); }        
    }
    
    public String GetWoj() { return this.woj; }
    
    public String GetPow() { return this.pow; }
    
    public String GetGmi() { return this.gmi; }
    
    public String GetRodzGmi() { return this.rodz_gmi; }
    
    public String GetSym() { return this.sym; }
    
    public String GetSymUl() { return this.sym_ul; }
    
    public String GetCecha() { return this.cecha; }
    
    public String GetNazwa1() { return this.nazwa_1; }
   
    public String GetNazwa2() { return this.nazwa_2; }
    
    public String GetStanNa() { return this.stan_na; }
}
