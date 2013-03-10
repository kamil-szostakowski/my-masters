/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mongojobs;

import basejobs.IDatabaseJob;

/**
 *
 * @author test
 */
public class MongodbSchemaJob extends MongodbBaseJob
{
    public void PerformInsertOperation(int identifier)
    {        
    }   
    
    public IDatabaseJob Clone()
    {
        return new MongodbSchemaJob();
    }    
}