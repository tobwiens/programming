/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2009 INRIA/University of Nice-Sophia Antipolis
 * Contact: proactive@ow2.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version
 * 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s): ActiveEon Team - http://www.activeeon.com
 *
 * ################################################################
 * $$ACTIVEEON_CONTRIBUTOR$$
 */
package org.objectweb.proactive.ic2d.chartit.data;

import java.util.Iterator;
import java.util.List;

import org.objectweb.proactive.ic2d.chartit.Activator;
import org.objectweb.proactive.ic2d.chartit.data.provider.IDataProvider;
import org.objectweb.proactive.ic2d.chartit.util.Utils;
import org.objectweb.proactive.ic2d.console.Console;


/**
 * This class represents a model of a chart that can be updated.
 * <p>
 * The chart is associated to some data providers that will be 
 * asked for values.
 * <p>
 * To avoid any concurrency problems at runtime any user interactions
 * should be avoided.
 * <p>
 * The user should explicitly stop the runtime.
 * 
 * @author <a href="mailto:support@activeeon.com">ActiveEon Team</a>.
 */
public final class ChartModelValidator {

    /**
     * The types associated to this class of model
     */
    public static final String[] NUMBER_TYPE = new String[] { "byte", "java.lang.Byte", "short",
            "java.lang.Short", "int", "java.lang.Integer", "float", "java.lang.Float", "double",
            "java.lang.Double", "long", "java.lang.Long" };

    /**
     * The string array type name
     */
    public static final String[] STRING_ARRAY_TYPE = new String[] { "[Ljava.lang.String;" };

    /**
     * The number array type name
     */
    public static final String[] NUMBER_ARRAY_TYPE = new String[] { "[B", "[Ljava.lang.Byte;", "[S",
            "[Ljava.lang.Short;", "[I", "[Ljava.lang.Integer;", "[F", "[Ljava.lang.Float;", "[D",
            "[Ljava.lang.Double;", "[J", "[Ljava.lang.Long;" };

    /**
     * Validates a chart model. In other words verifies the following rules:
     * <p>
     * - Data providers are String[] are Number[] of the same length
     * <p> 
     * - Only number based data providers
     * 
     * @param model The model to validate
     * 
     * @return True if the model is semantically valid
     */
    public static final boolean validate(final ChartModel model) {
        final List<IDataProvider> providers = model.getProviders();
        // At least 1 provider is needed
        if (providers.size() < 1) {
            return false;
        }
        // Get the iterator
        final Iterator<IDataProvider> it = providers.iterator();
        // Get first element
        final IDataProvider firstElement = it.next();
        // If string array
        if (Utils.contains(STRING_ARRAY_TYPE, firstElement.getType())) {
            // size must not exceed 2 and the next element must be an array of
            // number
            if (it.hasNext()) {
                final IDataProvider next = it.next();
                if (Utils.contains(NUMBER_ARRAY_TYPE, next.getType())) {
                    initModel(model, firstElement, next);
                    return true;
                }
                return false;
            }
            return false;
            // If array of number
        } else if (Utils.contains(NUMBER_ARRAY_TYPE, firstElement.getType())) {
            // size must not exceed 2 and the next element must be a string
            // array
            if (it.hasNext()) {
                final IDataProvider next = it.next();
                if (Utils.contains(STRING_ARRAY_TYPE, next.getType())) {
                    initModel(model, next, firstElement);
                    return true;
                }
                return false;
            }
            return false;
            // If number type
        } else if (Utils.contains(NUMBER_TYPE, firstElement.getType())) {
            // Check that all other elements are of the same type
            if (providers.size() > 1) {
                while (it.hasNext()) {
                    if (!Utils.contains(NUMBER_TYPE, it.next().getType())) {
                        return false;
                    }
                }
            }
            initModel(model, providers);
            return true;
        } else {
            return false;
        }
    }

    private static final void initModel(final ChartModel model, final IDataProvider namesProvider,
            final IDataProvider valuesProvider) {
        final String[] names = (String[]) namesProvider.provideValue();
        final double[] values = new double[names.length];

        // Check and adapt names
        // If RRD4J chart type Datasource name must be less than 20 chars
        if (model.isChronological()) {
            for (int i = names.length; i-- > 0;) {
                String name = names[i];
                if (name.length() > 20) {
                    names[i] = name.substring(0, 19);
                }
            }
        }

        model.setRuntimeNames(names);
        model.setRuntimeValues(values);

        model.runtimeValuesUpdater = new IRuntimeValuesUpdater() {
            public final void updateValues(final double[] valuesToUpdate) {
                final Object values = valuesProvider.provideValue();
                final int length = java.lang.reflect.Array.getLength(values);
                // If values array length differs from names array length log a
                // message and return
                if (length != model.runtimeNames.length) {
                    Console.getInstance(Activator.CONSOLE_NAME).log(
                            "Cannot refresh the chart " + model.name +
                                " because its values length differs from names length.");
                    return;
                }
                for (int i = length; i-- > 0;) {
                    valuesToUpdate[i] = ((Number) java.lang.reflect.Array.get(values, i)).doubleValue();
                }
            }
        };
        model.runtimeValuesUpdater.updateValues(model.runtimeValues);
    }

    private static final void initModel(final ChartModel model, final List<IDataProvider> providers) {
        final String[] names = new String[providers.size()];
        final double[] values = new double[providers.size()];

        // If RRD4J chart type Datasource name must be less than 20 chars
        if (model.isChronological()) {
            int i = 0;
            for (final IDataProvider provider : providers) {
                String name = provider.getName();
                if (name.length() > 20) {
                    names[i] = name.substring(0, 19);
                } else {
                    names[i] = name;
                }
                i++;
            }
        } else {
            int i = 0;
            for (final IDataProvider provider : providers) {
                names[i++] = provider.getName();
            }
        }

        model.setRuntimeNames(names);
        model.setRuntimeValues(values);

        model.runtimeValuesUpdater = new IRuntimeValuesUpdater() {
            public final void updateValues(final double[] valuesToUpdate) {
                for (int i = providers.size(); i-- > 0;) {
                    IDataProvider provider = providers.get(i);
                    valuesToUpdate[i] = ((Number) provider.provideValue()).doubleValue();
                }
            }
        };
        model.runtimeValuesUpdater.updateValues(model.runtimeValues);
    }
}