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
 *  Contributor(s):
 *
 * ################################################################
 */
package org.objectweb.proactive.extensions.gcmdeployment.GCMDeployment.group;

import javax.xml.xpath.XPath;

import org.objectweb.proactive.extensions.gcmdeployment.GCMParserHelper;
import org.w3c.dom.Node;


public class GroupMPIParser extends AbstractGroupParser {
    private static final String ATTR_COMMAND_OPTIONS = "commandOptions";
    private static final String ATTR_MACHINE_FILE = "machineFile";
    private static final String ATTR_EXEC_DIR = "execDir";
    private static final String ATTR_HOST_LIST = "hostList";
    private static final String ATTR_MPI_DISTRIBUTION_PATH = "distributionPath";
    static final String NODE_NAME = "mpiGroup";

    @Override
    public AbstractGroup parseGroupNode(Node groupNode, XPath xpath) {
        GroupMPI groupMPI = (GroupMPI) super.parseGroupNode(groupNode, xpath);

        // Mandatory attributes
        String hostList = GCMParserHelper.getAttributeValue(groupNode, ATTR_HOST_LIST);
        groupMPI.setHostList(hostList);

        String commandOptions = GCMParserHelper.getAttributeValue(groupNode, ATTR_COMMAND_OPTIONS);
        if (commandOptions != null) {
            groupMPI.setCommandOption(commandOptions);
        }

        String machineFile = GCMParserHelper.getAttributeValue(groupNode, ATTR_MACHINE_FILE);
        if (machineFile != null) {
            groupMPI.setMachineFile(machineFile);
        }

        String execDir = GCMParserHelper.getAttributeValue(groupNode, ATTR_EXEC_DIR);
        if (execDir != null) {
            groupMPI.setExecDir(execDir);
        }

        String mpiDistributionPath = GCMParserHelper.getAttributeValue(groupNode, ATTR_MPI_DISTRIBUTION_PATH);
        groupMPI.setMpiDistributionPath(mpiDistributionPath);

        return groupMPI;
    }

    @Override
    public AbstractGroup createGroup() {
        return new GroupMPI();
    }

    public String getNodeName() {
        return NODE_NAME;
    }
}