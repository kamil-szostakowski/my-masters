/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import runs.DatabaseRun;

/**
 *
 * @author test
 */
public class StringTools 
{
    public static String Join(int[] array, String separator, boolean withQuotationMarks)
    {
        String result = "";
        String format = ((withQuotationMarks) ? "%s\"%d\"" : "%s%d");
        
        for(int iter=0; iter<array.length; iter++)
        {
            if(iter==0){ result += array[iter]; }
            else { result += String.format(format, separator, array[iter]); }
        }
        
        return result;
    }   
    
    public static String ReadFile(String filename) throws IOException
    {                   
        StringBuffer data = new StringBuffer();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
            
        char[] buffer = new char[1024];
        int readCount = 0;
            
        while((readCount=reader.read(buffer)) != -1)
        {
            String readData = String.valueOf(buffer, 0, readCount);
            data.append(readData);
        }
            
        reader.close();
            
        return data.toString();
    }       
}
