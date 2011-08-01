package dk.itu.frigga.action.impl.runtime.actions;

import dk.itu.frigga.action.impl.filter.FilterContext;
import dk.itu.frigga.action.impl.filter.FilterDeviceState;
import dk.itu.frigga.action.impl.filter.FilterOutput;
import dk.itu.frigga.action.impl.runtime.AbstractAction;
import dk.itu.frigga.action.impl.runtime.ActionResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-31
 */
public class FilterAction extends AbstractAction
{
    private String category = "";
    private String selection = "";

    @Override
    protected void loadAction(Map<String, String> attributes)
    {
        super.loadAction(attributes);

        if (attributes.containsKey("selection"))
        {
            selection = attributes.get("selection");
        }

        if (attributes.containsKey("category"))
        {
            category = attributes.get("category");
        }
    }

    @Override
    public Collection<FilterDeviceState> getListOfDevices(Collection<FilterDeviceState> devices, FilterContext context)
    {
        ArrayList<FilterDeviceState> states = new ArrayList<FilterDeviceState>(devices.size());

        if (!selection.isEmpty())
        {
            FilterOutput output = context.getStoredOutput(selection);
            for (FilterDeviceState state: output)
            {
                states.add(state);
            }
        }
        else if (!category.isEmpty())
        {
            String[] categories = category.split("\\s");

            for (FilterDeviceState state : devices)
            {
                for (String cat : categories)
                {
                    if (state.getDevice().isOfCategory(cat))
                    {
                        states.add(state);
                        break;
                    }
                }
            }
        }
        else
        {
            return devices;
        }

        return states;
    }

    @Override
    public ActionResult run(Collection<FilterDeviceState> deviceStates, FilterContext context)
    {
        return new ActionResult();
    }

  @Override
  public boolean canHaveChildActions() {
    return true;
  }

}
