package dk.itu.frigga.action.manager;

import dk.itu.frigga.action.ConditionResult;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.action.SidNotFoundException;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-05-31
 */
public class ConditionResultImpl implements ConditionResult
{


    @Override
    public int getScopeIdCount()
    {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getScopeId(int index) throws SidNotFoundException
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Device getDevice(String scopeId) throws SidNotFoundException
    {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
