package dk.itu.frigga.action.manager.runtime;

import dk.itu.frigga.action.runtime.Executable;

import java.util.Collections;
import java.util.Stack;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-06-01
 */
public class ExecutionPlan
{
    private final Stack<Executable> plan = new Stack<Executable>();

    public void addExecutable(final Executable executable)
    {
        plan.push(executable);
    }
}
