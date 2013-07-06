package data.source;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Kamil Szostakowski
 */

public class XmlRegionsDataSource implements IDataSource
{
    private LinkedList<XmlRegion> regions;
    private HashMap<String, String> currentRow;    
    
    public XmlRegionsDataSource()
    {
        try 
        {
            this.regions = new LinkedList<XmlRegion>();
            this.currentRow = new HashMap<String, String>();            
            
            SAXParserFactory factory = SAXParserFactory.newInstance();        
            SAXParser saxParser = factory.newSAXParser();         
            
            DefaultHandler handler = new DefaultHandler()
            {
                boolean rowName = false;
                boolean colName = false;
                
                private String currentField;
                
                @Override
                public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException
                {       
                    if (qName.equalsIgnoreCase("COL")) 
                    {			                       
                        this.colName = true;                        
                        this.currentField = attributes.getValue(0);                             
                    }                    
                }
                
                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException
                {
                    if(qName.equalsIgnoreCase("ROW"))
                    {
                        XmlRegion region = new XmlRegion(currentRow);
                        
                        regions.add(region);                        
                        currentRow.clear();
                        
                        this.colName = false;
                    }                    
                }
                
                @Override
                public void characters(char ch[], int start, int length) throws SAXException
                {    
                    if(this.colName)
                    {                        
                        if(!currentRow.containsKey(this.currentField))
                        {
                            String fieldName = this.currentField;
                            String fieldVal = new String(ch, start, length);
                            
                            currentRow.put(fieldName, fieldVal);                            
                        }
                    }                    
                }
            };
            
            saxParser.parse("TERC.xml", handler);
            
            System.out.println(String.format("FINISHED %d", this.regions.size()));            
        } 
        
        catch (ParserConfigurationException ex) 
        {
            Logger.getLogger(XmlRegionsDataSource.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        catch (SAXException ex) 
        {
            Logger.getLogger(XmlRegionsDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        catch(IOException ex)
        {
            Logger.getLogger(XmlStreetsDataSource.class.getName()).log(Level.SEVERE, null, ex);
            
            System.err.println(ex.toString());
        }         
    }
    
    
    @Override
    public Object GetData(Object param) throws Exception 
    {
        Map<String, String> request = (Map<String, String>)param;
        
        String woj = request.get("WOJ");
        String pow = request.get("POW");
        String gmi = request.get("GMI");
        String rodz = request.get("RODZ");
        
        for(int iter=0; iter<this.regions.size(); iter++)
        {
            XmlRegion region = this.regions.get(iter);
            
            if(region.GetWoj().equals(woj)  && region.GetPow().equals(pow) 
                    && region.GetGmi().equals(gmi) && region.GetRodz().equals(rodz))
            {
                return region;
            }
        }
        
        return null;
    }

    @Override
    public String GetName() 
    {
        return "region-xml";
    }
    
}
