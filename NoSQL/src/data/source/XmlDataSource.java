package data.source;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
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
 * @author kamil
 */

public class XmlDataSource implements IDataSource
{
    private LinkedList<XmlStreet> streets;
    private HashMap<String, String> currentRow;
    
    private String currentField;      
    
    public XmlDataSource()
    {
        try 
        {
            this.streets = new LinkedList<>();
            this.currentRow = new HashMap<>();
            
            SAXParserFactory factory = SAXParserFactory.newInstance();        
            SAXParser saxParser = factory.newSAXParser();        

            DefaultHandler handler = new DefaultHandler() {
 
                boolean rowName = false;
                boolean colName = false;
 
                @Override
                public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException 
                { 		
                    if (qName.equalsIgnoreCase("COL")) 
                    {			                       
                        this.colName = true;
                        
                        currentField = attributes.getValue(0);                             
                    }
                }
 
                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException 
                {                    
                    if(qName.equalsIgnoreCase("ROW"))
                    {
                        XmlStreet street = new XmlStreet(currentRow);
                        
                        streets.add(street);
                        
                        currentRow.clear();
                        
                        this.colName = false;
                    }
                }
                
                @Override
                public void characters(char ch[], int start, int length) throws SAXException 
                {
                    if(this.colName)
                    {
                        if(!currentRow.containsKey(currentField))
                        {
                            String fieldName = currentField;
                            String fieldVal = new String(ch, start, length);
                                             
                            currentRow.put(fieldName, fieldVal);
                        }
                    }
                }
            };

            saxParser.parse("ULIC.xml", handler);
            
            System.out.println(String.format("FINISHED %d", this.streets.size()));
        } 
        
        catch (ParserConfigurationException | IOException | SAXException ex ) 
        {
            Logger.getLogger(XmlDataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public Object GetData(Object param) throws Exception 
    {
        int requestedIndex = (int)param;        
        int dataIndex = requestedIndex % this.streets.size();
        
        return this.streets.get(dataIndex);
    }

    @Override
    public String GetName() 
    {
        return "street-xml";
    }
    
}
