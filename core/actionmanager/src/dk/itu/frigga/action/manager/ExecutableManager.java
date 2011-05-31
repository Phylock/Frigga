package dk.itu.frigga.action.manager;

import dk.itu.frigga.action.manager.runtime.Executable;

import java.util.*;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-05-31
 */
public class ExecutableManager
{
    private final Map<String, Executable> namedExecutables = Collections.synchronizedMap(new HashMap<String, Executable>());

    public ExecutableManager()
    {
    }

    public void addExecutable(final Executable executable)
    {
        if (executable.hasId()) namedExecutables.put(executable.getId(), executable);
    }

    public Executable findExecutable(final String id) throws ExecutableNotFoundException
    {
        if (namedExecutables.containsKey(id))
        {
            return namedExecutables.get(id);
        }

        throw new ExecutableNotFoundException(id);
    }
}
