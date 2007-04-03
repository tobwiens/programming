/*
 * ################################################################
 *
 * ProActive: The Java(TM) library for Parallel, Distributed,
 *            Concurrent computing with Security and Mobility
 *
 * Copyright (C) 1997-2007 INRIA/University of Nice-Sophia Antipolis
 * Contact: proactive@objectweb.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://www.inria.fr/oasis/ProActive/contacts.html
 *  Contributor(s):
 *
 * ################################################################
 */
package org.objectweb.proactive.core.body.http.util;

import java.io.IOException;
import java.util.HashMap;

import org.objectweb.proactive.ActiveObjectCreationException;
import org.objectweb.proactive.ProActive;
import org.objectweb.proactive.core.body.UniversalBody;
import org.objectweb.proactive.core.node.NodeException;


/**
 * An HTTP Registry that registers Bodies
 * @author vlegrand
 *
 */
public class HTTPRegistry {
    private static final String REGISTRY_NAME = "HTTP_REGISTRY";
    private static HTTPRegistry instance;
    private static HashMap<String, UniversalBody> bodyMap = new HashMap<String, UniversalBody>();

    private HTTPRegistry() {
    }

    /**
     * Gets the unique instance of the registry
     * @return the unique instance of the registry
     */
    public static HTTPRegistry getInstance() {
        if (instance == null) {
            instance = new HTTPRegistry();
            try {
                instance = (HTTPRegistry) ProActive.turnActive(instance);
                ProActive.register(instance, HTTPRegistry.REGISTRY_NAME);
            } catch (ActiveObjectCreationException e) {
                e.printStackTrace();
            } catch (NodeException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * Binds a body  with a name
     * @param name  the name of the body
     * @param body the body to be binded
     */
    public void bind(String name, UniversalBody body) {
        bodyMap.put(name, body);
        System.out.println(bodyMap.size());
    }

    /**
     * Unbinds a body from a  name
     * @param name the name binded with a body
     */
    public void unbind(String name) {
        bodyMap.remove(name);
    }

    /**
     * Gives all the names registered in this  registry
     * @return the names list
     */
    public String[] list() {
        String[] list = new String[bodyMap.size()];
        bodyMap.keySet().toArray(list);
        return list;
    }

    /**
     * Retrieves a body from a name
     * @param name The name of the body to be retrieved
     * @return the binded body
     */
    public UniversalBody lookup(String name) {
        return bodyMap.get(name);
    }
}
