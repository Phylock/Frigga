/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.itu.frigga.action.impl;

import dk.itu.frigga.action.filter.FilterFailedException;
import dk.itu.frigga.action.filter.FilterSyntaxErrorException;
import dk.itu.frigga.action.impl.filter.*;
import dk.itu.frigga.device.model.Device;
import dk.itu.frigga.utility.XmlHelper;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Mikkel Wendt-Larsen (miwe@itu.dk)
 * @author Tommy Andersen (toan@itu.dk)
 */
public class Rule
{
    private final VariableContainer variableContainer = new VariableContainer();
    private final ActionContainer actionContainer = new ActionContainer();
    private final ConditionContainer conditionContainer;
    private final ReplacementContainer replacementContainer;
    private final FilterFactory filterFactory;
    private final Set<FilterDeviceState> validDevices = Collections.synchronizedSet(new LinkedHashSet<FilterDeviceState>());
    private String description;
    private String id;

    public Rule(final FilterFactory factory, final ReplacementContainer replacementContainer)
    {
        this.conditionContainer = new ConditionContainer();
        this.replacementContainer = replacementContainer;
        this.filterFactory = factory;
    }

    public void run(final FilterContext context) throws FilterFailedException
    {
        // Run all filters in a way so that filters depending on each other will be run in the right order.
        Collection<ConditionContainer.RootFilterInformation> dependencyResolvedFilters = conditionContainer.getPrioritizedFilterList();
        for (ConditionContainer.RootFilterInformation rootFilter : dependencyResolvedFilters)
        {
            if (rootFilter.isValidate()) context.allowValidation(rootFilter.getName());
            FilterOutput output = context.run(rootFilter.getFilter());

            context.storeOutput(rootFilter.getName(), output);
        }

        // Get the resulting output from all the filters.
        FilterOutput output = context.getValidateOutput();

        // Find out which devices to validate and which to invalidate. We store the validated items so we can always
        // find out whether an item is changed from valid to invalid or invalid to valid since last run.
        Collection<FilterDeviceState> validated = output.matchingDevices();

        Set<FilterDeviceState> invalidates = new LinkedHashSet<FilterDeviceState>();
        Set<FilterDeviceState> validates = new LinkedHashSet<FilterDeviceState>();

        // Find invalidated items.
        for (FilterDeviceState device : validDevices)
        {
            if (!validated.contains(device))
            {
                invalidates.add(device);
            }
        }

        // Find newly validated items.
        for (FilterDeviceState device : validated)
        {
            if (!validDevices.contains(device))
            {
                validates.add(device);
            }
        }

        // Update the list of valid devices for next time.
        validDevices.removeAll(invalidates);
        validDevices.addAll(validates);

        // Call the associated actions
        if (invalidates.size() > 0)
        {
            actionContainer.callEvent(variableContainer, "invalidate", invalidates, context, invalidates);
        }

        if (validates.size() > 0)
        {
            actionContainer.callEvent(variableContainer, "validate", validates, context, validates);
        }
    }

    public String getDescription()
    {
        return description;
    }

    public String getId()
    {
        return id;
    }

    public void parse(final Element element) throws FilterSyntaxErrorException
    {
        if (element == null) throw new IllegalArgumentException("Argument 'element' is null");

        if (!element.hasAttribute("id")) throw new FilterSyntaxErrorException();
        id = element.getAttribute("id");

        if (element.hasAttribute("description")) description = element.getAttribute("description");

        for (Element elem = XmlHelper.getFirstChildElement(element); elem != null; elem = XmlHelper.getNextSiblingElement(elem))
        {
            if (elem.getTagName().equals("variables"))
            {
                variableContainer.parse(elem);
            }

            else if (elem.getTagName().equals("actions"))
            {
                actionContainer.parse(elem);
            }

            else if (elem.getTagName().equals("condition"))
            {
                conditionContainer.parse(elem, filterFactory);
            }
        }
    }
}
