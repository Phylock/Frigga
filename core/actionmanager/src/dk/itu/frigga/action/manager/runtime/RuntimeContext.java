package dk.itu.frigga.action.manager.runtime;

import dk.itu.frigga.action.ConditionResult;
import dk.itu.frigga.action.runtime.*;

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
public class RuntimeContext
{
    public enum RuntimeMode
    {
        RUN_NORMAL, RUN_DEBUG
    }

    public enum RuntimeState
    {
        STATE_COMPLETED, STATE_STEP, STATE_BREAKPOINT
    }

    private enum StepMode
    {
        STEP_NONE, STEP_INTO, STEP_OVER, STEP_OUT
    }

    private RuntimeMode runtimeMode = RuntimeMode.RUN_NORMAL;
    private final CallStack callStack = new CallStack();
    private final List<Breakpoint> breakpoints = Collections.synchronizedList(new LinkedList<Breakpoint>());

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

    public void run(final ConditionResult result)
    {
        currentResult = result;
        //callStack.pushCall(new Call(executable));

        try
        {
            // In debug mode we have to handle break points.
            if (runtimeMode == RuntimeMode.RUN_DEBUG)
            {
                handleStep();
            }

            //resume();
        } finally
        {
            callStack.popCall();
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
    }

}
