package dk.itu.frigga.action.manager.runtime;

import com.sun.java.browser.plugin2.liveconnect.v1.Result;

import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-05-31
 */
public class CallStack
{
    private final Stack<Call> callStack = new Stack<Call>();

    public CallStack()
    {
    }

    void pushCall(final Call call)
    {
        synchronized (callStack)
        {
            callStack.push(call);
        }
    }

    void popCall()
    {
        synchronized (callStack)
        {
            if (callStack.size() > 0)
            {
                callStack.pop();
            }
        }
    }

    public Call[] getCallStackElements()
    {
        Call[] result;

        synchronized (callStack)
        {
            result = (Call[])callStack.toArray();
        }

        return result;
    }

    public int getDepth()
    {
        return callStack.size();
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        synchronized (callStack)
        {
            for (Call call : callStack)
            {
                sb.append(call.toString());
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
