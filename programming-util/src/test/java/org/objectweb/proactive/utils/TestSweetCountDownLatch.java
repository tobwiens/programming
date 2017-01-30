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
package org.objectweb.proactive.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestSweetCountDownLatch {

    static final private Logger logger = Logger.getLogger(TestSweetCountDownLatch.class.getName());

    @Before
    public void setUp() throws Exception {
        logger.setLevel(Level.DEBUG);
    }

    @Test
    public void test() throws InterruptedException {
        final CountDownLatch latch = new SweetCountDownLatch(1, logger);

        Thread latchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    // eated by sweetcountdownlatch
                }
            }
        });
        T t = new T(latch, latchThread, 0);
        new Thread(t).start();

        latchThread.start();
        latchThread.join();

        Assert.assertEquals(0, latch.getCount());
    }

    @Test
    public void testTimeoutExpired() throws InterruptedException {
        final SweetCountDownLatch latch = new SweetCountDownLatch(1, logger);

        final boolean[] b = new boolean[1];
        Thread latchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                b[0] = latch.await(50, TimeUnit.MILLISECONDS);
            }
        });
        T t = new T(latch, latchThread, 10000);
        new Thread(t).start();

        latchThread.start();
        latchThread.join();

        Assert.assertFalse(b[0]);
    }

    @Test
    public void testTimeout() throws InterruptedException {
        final SweetCountDownLatch latch = new SweetCountDownLatch(1, logger);
        final boolean[] b = new boolean[1];
        Thread latchThread = new Thread(new Runnable() {
            @Override
            public void run() {
                b[0] = latch.await(10000, TimeUnit.MILLISECONDS);
            }
        });
        T t = new T(latch, latchThread, 0);
        new Thread(t).start();

        latchThread.start();
        latchThread.join();

        Assert.assertTrue(b[0]);
    }

    class T implements Runnable {
        private CountDownLatch latch;

        private Thread waiter;

        private long sleepms;

        public T(CountDownLatch latch, Thread waiter, long sleepms) {
            this.latch = latch;
            this.waiter = waiter;
            this.sleepms = sleepms;
        }

        public void run() {
            new Sleeper(this.sleepms, Logger.getLogger(Sleeper.class.getName())).sleep();

            this.waiter.interrupt();
            Thread.yield();
            this.latch.countDown();

        }
    }
}
