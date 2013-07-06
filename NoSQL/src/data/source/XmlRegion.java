package data.source;

import java.util.Map;

/**
 *
 * @author kamil
 */

public class XmlRegion 
{
    private String woj;
    private String pow;
    private String gmi;
    private String rodz;
    private String nazwa;
    private String nazwaOd;
    private String stanNa;
    
    public XmlRegion(Map<String, String> region)
    {
        if(region.containsKey("WOJ")){ this.woj = region.get("WOJ"); }
        if(region.containsKey("POW")){ this.pow = region.get("POW"); }
        if(region.containsKey("GMI")){ this.gmi = region.get("GMI"); }
        
        if(region.containsKey("RODZ")){ this.rodz = region.get("RODZ"); }
        if(region.containsKey("NAZWA")){ this.nazwa = region.get("NAZWA"); }
        if(region.containsKey("NAZDOD")){ this.nazwaOd = region.get("NAZDOD"); }
        
        if(region.containsKey("STAN_NA")){ this.stanNa = region.get("STAN_NA"); }
    }
    
    public String GetWoj() { return this.woj; }
    public String GetPow() { return this.pow; }
    public String GetGmi() { return this.gmi; }
    public String GetRodz() { return this.rodz; }
    public String GetNazwa() { return this.nazwa; }
    public String GetNazwaOd() { return this.nazwaOd; }
    public String GetStanNa() { return this.stanNa; }
 
    @Override
    public String toString()
    {
        return String.format("WOJ: %s, POW: %s, GMI: %s, RODZ: %s, NAZWA: %s, NAZDOD: %s, STAN_NA: %s", this.woj, this.pow, 
                this.gmi, this.rodz, this.nazwa, this.nazwaOd, this.stanNa);
    }
}
