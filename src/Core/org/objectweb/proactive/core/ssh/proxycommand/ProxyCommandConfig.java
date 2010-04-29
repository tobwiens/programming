/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2010 INRIA/University of
 *              Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
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
 * If needed, contact us to obtain a release under GPL Version 2
 * or a different license than the GPL.
 *
 *  Initial developer(s):               The ActiveEon Team
 *                        http://www.activeeon.com/
 *  Contributor(s):
 *
 * ################################################################
 * $$ACTIVEEON_INITIAL_DEV$$
 */
package org.objectweb.proactive.core.ssh.proxycommand;

import org.objectweb.proactive.core.config.PAPropertyBoolean;
import org.objectweb.proactive.core.config.PAPropertyString;
import org.objectweb.proactive.core.config.PAProperties.PAPropertiesLoaderSPI;


public class ProxyCommandConfig implements PAPropertiesLoaderSPI {

    public static PAPropertyString PA_SSH_PROXY_GATEWAY = new PAPropertyString(
        "proactive.communication.ssh.proxy.gateway", false);

    public static PAPropertyString PA_SSH_PROXY_USE_GATEWAY_OUT = new PAPropertyString(
        "proactive.communication.ssh.proxy.out_gateway", false);

    public static PAPropertyBoolean PA_RMISSH_TRY_PROXY_COMMAND = new PAPropertyBoolean(
        "proactive.communication.ssh.try_proxy_command", false, false);

}