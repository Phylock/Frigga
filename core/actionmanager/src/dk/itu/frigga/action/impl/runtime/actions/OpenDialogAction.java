package dk.itu.frigga.action.impl.runtime.actions;

import dk.itu.frigga.action.impl.filter.FilterContext;
import dk.itu.frigga.action.impl.filter.FilterDeviceState;
import dk.itu.frigga.action.impl.runtime.AbstractAction;
import dk.itu.frigga.action.impl.runtime.ActionResult;
import dk.itu.frigga.device.Parameter;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.device.model.Variable;

import java.security.Policy;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class description here...
 *
 * @author Tommy Andersen (toan@itu.dk)
 * @version 1.00, 2011-07-17
 */
public class OpenDialogAction extends AbstractAction
{
    private String selectionClients = "**";
    private String selectionDevices = "**";

    @Override
    protected void loadAction(Map<String, String> attributes)
    {
        super.loadAction(attributes);

        if (attributes.containsKey("clients"))
        {
            selectionClients = attributes.get("clients");
        }

        if (attributes.containsKey("devices"))
        {
            selectionDevices = attributes.get("devices");
        }
    }

    private Parameter[] handleDeviceTv(final Device device)
    {
        String current = "off";
        String subject = "On";
        String description = "Turn on television.";
        String type = "onoff";
        String significance = "100";
        String selectionValue = "/device:tv/id:" + device.getSymbolic() + "/function:";


        for (Variable variable : device.getVariables())
        {
            if (variable.getPrimaryKey().getVariabletype().getName().equals("OnOffState"))
            {
                current = variable.getValue();
            }
        }

        if ("on".equals(current))
        {
            selectionValue = selectionValue + "off";
        }
        else
        {
            selectionValue = selectionValue + "on";
        }

        Parameter[] parameters = new Parameter[9];
        parameters[0] = new Parameter("id", "dialogCall1");
        parameters[1] = new Parameter("type", type);
        parameters[2] = new Parameter("subject", subject);
        parameters[3] = new Parameter("description", description);
        parameters[4] = new Parameter("current", current);
        parameters[5] = new Parameter("significance", significance);
        parameters[6] = new Parameter("selection.0.value", selectionValue);
        parameters[7] = new Parameter("selection.0.subject", "Television in " + device.getLocalLocations().get(0).getRoom());
        parameters[8] = new Parameter("selection.0.selected", "true");

        return parameters;
    }

    @Override
    public ActionResult run(Collection<FilterDeviceState> deviceStates, FilterContext context)
    {
        String resultStr = "";

        Set<Device> clients = new LinkedHashSet<Device>();
        Set<Device> devices = new LinkedHashSet<Device>();

        parseSelection(selectionClients, clients, context, context.getStoredOutput(selectionClients).matchingDevices());
        parseSelection(selectionDevices, devices, context, deviceStates);

        if (clients.size() > 0)
        {
            for (Device device : devices)
            {
                Parameter[] parameters = handleDeviceTv(device);

                context.getDeviceManager().callFunction("openDialog", clients.toArray(new Device[clients.size()]), parameters);
            }
        }

        return new ActionResult(resultStr);
    }
}
