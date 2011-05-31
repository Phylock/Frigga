package dk.itu.frigga.action.manager.runtime;

import dk.itu.frigga.action.ConditionResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The runtime class is used to evaluate an option. It can be used for debugging by adding break points and stepping
 * through the code.
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-05-31
 */
public class RuntimeContext implements DebugContext
{
    public enum RuntimeMode { RUN_NORMAL, RUN_DEBUG }
    public enum RuntimeState { STATE_COMPLETED, STATE_STEP, STATE_BREAKPOINT }
    private enum StepMode { STEP_NONE, STEP_INTO, STEP_OVER, STEP_OUT }

    private RuntimeMode runtimeMode = RuntimeMode.RUN_NORMAL;
    private final CallStack callStack = new CallStack();
    private final List<Breakpoint> breakpoints = Collections.synchronizedList(new LinkedList<Breakpoint>());
    private final List<DebugListener> debugListeners = Collections.synchronizedList(new LinkedList<DebugListener>());

    private Executable currentExecutable;
    private long stepId;
    private int stepCallDepth;
    private ConditionResult currentResult;
    private boolean terminated = false;
    private StepMode stepMode = StepMode.STEP_NONE;
    private long currentStepId = 0l;
    private RuntimeState state;

    public RuntimeContext()
    {
    }

    public void setRuntimeMode(final RuntimeMode runtimeMode)
    {
        this.runtimeMode = runtimeMode;
    }

    public RuntimeMode getRuntimeMode()
    {
        return runtimeMode;
    }

    public void initialize()
    {
        terminated = false;
        currentStepId = 0l;
        stepMode = StepMode.STEP_NONE;
    }

    public void run(final Executable executable, final ConditionResult result)
    {
        currentExecutable = executable;
        currentResult = result;
        callStack.pushCall(new Call(executable));

        try
        {
            // In debug mode we have to handle break points.
            if (runtimeMode == RuntimeMode.RUN_DEBUG)
            {
                handleBreakpoints();
                handleStep();
            }

            resume();
        }
        finally
        {
            callStack.popCall();
        }
    }

    private void handleBreakpoints()
    {
        try
        {
            for (Breakpoint breakpoint : breakpoints)
            {
                breakpoint.evaluate(currentExecutable);
            }
        }
        catch (BreakpointEncounteredException ex)
        {
            for (DebugListener listener : debugListeners)
            {
                listener.breakpointReached(this, ex.getBreakpoint());
            }
        }
    }

    private void handleStep()
    {
        if (runtimeMode == RuntimeMode.RUN_DEBUG)
        {
            switch (stepMode)
            {
                case STEP_NONE:
                    // No stepping, just continue.
                    break;

                case STEP_INTO:
                    notifyStep();
                    stepMode = StepMode.STEP_NONE;
                    break;

                case STEP_OVER:
                    if (stepCallDepth >= callStack.getDepth() && stepId < currentStepId)
                    {
                        notifyStep();
                        stepMode = StepMode.STEP_NONE;
                    }
                    break;

                case STEP_OUT:
                    if (stepCallDepth > callStack.getDepth())
                    {
                        notifyStep();
                        stepMode = StepMode.STEP_NONE;
                    }
                    break;
            }
        }
    }

    private void notifyStep()
    {
        for (DebugListener listeners : debugListeners)
        {
            listeners.executionStepped(this, currentExecutable);
        }
    }

    @Override
    public CallStack getCallStack()
    {
        return callStack;
    }

    @Override
    public void stepOut()
    {
        stepMode = StepMode.STEP_OUT;
        stepCallDepth = callStack.getDepth();
        stepId = currentStepId;

        resume();
    }

    @Override
    public void stepOver()
    {
        stepMode = StepMode.STEP_OVER;
        stepCallDepth = callStack.getDepth();
        stepId = currentStepId;

        resume();
    }

    @Override
    public void stepInto()
    {
        stepMode = StepMode.STEP_INTO;
        stepId = currentStepId;

        resume();
    }

    @Override
    public void resume()
    {
        if (!terminated)
        {
            try
            {
                currentStepId++;
                currentExecutable.execute(this, currentResult);
            }
            catch (ExecutableException ex)
            {
                if (runtimeMode == RuntimeMode.RUN_DEBUG)
                {
                    for (DebugListener listener : debugListeners)
                    {
                        listener.exceptionCaught(this, currentExecutable, ex);
                    }
                }
            }
        }
    }

    @Override
    public void terminate()
    {
        terminated = true;
    }

    @Override
    public boolean isBreakpoint()
    {
        for (Breakpoint breakpoint : breakpoints)
        {
            if (breakpoint.test(currentExecutable))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void addBreakpoint(Breakpoint breakpoint)
    {
        assert(breakpoint != null) : "Breakpoint can not be null.";

        breakpoints.add(breakpoint);
    }

    @Override
    public void removeBreakpoint(Breakpoint breakpoint)
    {
        assert(breakpoint != null) : "Breakpoint can not be null.";

        breakpoints.remove(breakpoint);
    }

    @Override
    public void removeAllBreakpoints()
    {
        breakpoints.clear();
    }

    @Override
    public void addDebugListener(DebugListener debugListener)
    {
        assert(debugListener != null) : "DebugListener can not be null.";

        debugListeners.add(debugListener);
    }

    @Override
    public void removeDebugListener(DebugListener debugListener)
    {
        assert(debugListener != null) : "DebugListener can not be null.";

        debugListeners.remove(debugListener);
    }
}
