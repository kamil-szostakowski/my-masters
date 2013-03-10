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
public interface IDatabaseRun 
{
    public int GetSelectRate();
    
    public int GetInsertRate();
    
    public int GetNumberOfThreads();
    
    public int GetRepeatCount();
    
    public int GetSelectDocumentCount();
    
    public String GetContent(int iter) throws FileNotFoundException, IOException;        
}