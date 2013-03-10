/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package runs;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author test
 */
public class DBSchemaRun implements IDatabaseRun
{
    @Override
    public int GetSelectDocumentCount()
    {
        return 0;
    }
    
    @Override
    public int GetSelectRate() 
    {
        return 0;
    }

    @Override
    public int GetInsertRate() 
    {        
        return 100;
    }

    @Override
    public int GetNumberOfThreads() 
    {
        return 1;
    }

    @Override
    public int GetRepeatCount() 
    {
        return 1;
    }

    @Override
    public String GetContent(int iter) throws FileNotFoundException, IOException 
    {
        return "";
    }    
}