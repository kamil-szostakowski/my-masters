package mongojobs;

import basejobs.IDatabaseJob;

/**
 *
 * @author test
 */
public class MongodbPrepareJob extends MongodbTestJob
{    
    public IDatabaseJob Clone()
    {
        return new MongodbPrepareJob();
    }    
}