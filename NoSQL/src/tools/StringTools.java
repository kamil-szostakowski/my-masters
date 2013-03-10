/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

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
}
