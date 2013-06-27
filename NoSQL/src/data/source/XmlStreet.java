/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
    private String woj;
    private String pow;
    private String gmi;
    private String rodz_gmi;
    private String sym;
    private String sym_ul;
    private String cecha;
    private String nazwa_1;
    private String nazwa_2;
    private String stan_na;
    
    public XmlStreet(HashMap<String, String> params)
    {
        if(params.containsKey("WOJ")){ this.woj = params.get("WOJ"); }
        else if(params.containsKey("POW")){ this.woj = params.get("POW"); }
        else if(params.containsKey("GMI")){ this.woj = params.get("GMI"); }
        else if(params.containsKey("RODZ_GMI")){ this.woj = params.get("RODZ_GMI"); }
        else if(params.containsKey("SYM")){ this.woj = params.get("SYM"); }
        
        else if(params.containsKey("SYM_UL")){ this.woj = params.get("SYM_UL"); }
        else if(params.containsKey("CECHA")){ this.woj = params.get("CECHA"); }
        else if(params.containsKey("NAZWA_1")){ this.woj = params.get("NAZWA_1"); }
        else if(params.containsKey("NAZWA_2")){ this.woj = params.get("NAZWA_2"); }
        else if(params.containsKey("STAN_NA")){ this.woj = params.get("STAN_NA"); }
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
