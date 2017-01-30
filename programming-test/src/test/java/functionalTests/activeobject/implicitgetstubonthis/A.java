/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package functionalTests.activeobject.implicitgetstubonthis;

import java.io.Serializable;

import org.objectweb.proactive.api.PAActiveObject;
import org.objectweb.proactive.core.util.wrapper.StringWrapper;


public class A implements Serializable {

    public A() {
    }

    public String sayHello() {
        return "hello from " + PAActiveObject.getBodyOnThis().getNodeURL();
    }

    public StringWrapper sayWrappedHello() {
        return new org.objectweb.proactive.core.util.wrapper.StringWrapper("hello");
    }

    public A returnThis() {
        return this;
    }

    public B aInB() {
        return new B(this);
    }

    public boolean callTakeAOnB(B b) {
        return b.takeA(this);
    }

}
