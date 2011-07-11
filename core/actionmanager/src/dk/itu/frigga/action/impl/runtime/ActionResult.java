package dk.itu.frigga.action.impl.runtime;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-11
 */
public class ActionResult
{
    private String value;
    private boolean resultIsVoid = true;

    public ActionResult()
    {
    }

    public ActionResult(final String value)
    {
        this.value = value;
        resultIsVoid = false;
    }

    public String getValue()
    {
        return value;
    }

    public boolean isVoid()
    {
        return resultIsVoid;
    }
}
