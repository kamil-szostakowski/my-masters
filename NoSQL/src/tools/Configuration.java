/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.svenson.JSONParser;

/**
 *
 * @author test
 */
public class Configuration 
{
    private static String configContent;
    private static HashMap configObject;
    
    public static void Prepare()
    {
        try 
        {
            StringBuffer data = new StringBuffer();
            BufferedReader reader = new BufferedReader(new FileReader("config.json"));
            
            char[] buffer = new char[1024];
            int readCount = 0;
            
            while((readCount=reader.read(buffer)) != -1)
            {
                String readData = String.valueOf(buffer, 0, readCount);
                data.append(readData);
            }
            
            reader.close();
            
            Configuration.configContent = data.toString();                        
            
            JSONParser parser = new JSONParser();
            Configuration.configObject = (HashMap)parser.parse(Configuration.configContent);                            
        } 
        
        catch (IOException ex) 
        { 
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Object GetParam(String key)
    {
        HashMap current = Configuration.configObject;
        
        String[] keys = key.split(":");
        
        if(keys.length >= 2)
        {
            String first = keys[0];
            String second = keys[1];
            
            HashMap object = (HashMap) Configuration.configObject.get(first);
            
            return object.get(second);
        }
        
        return Configuration.configObject.get(key);
    }
}
