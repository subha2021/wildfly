/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2017, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.wildfly.extension.messaging.activemq.broadcast;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.wildfly.extension.messaging.activemq.logging.MessagingLogger;

/**
 * @author Paul Ferraro
 */
public class QueueBroadcastManager implements BroadcastManager {
    private final BlockingQueue<byte[]> broadcasts = new LinkedBlockingDeque<>();
    private final String name;

    public QueueBroadcastManager(String name) {
        this.name = name;
    }

    @Override
    public void receive(byte[] broadcast) {
        if (MessagingLogger.ROOT_LOGGER.isDebugEnabled()) {
            MessagingLogger.ROOT_LOGGER.debugf("Received broadcast from group %s: %s", this.name, Arrays.toString(broadcast));
        }
        this.broadcasts.add(broadcast);
    }

    @Override
    public byte[] getBroadcast() throws InterruptedException {
        return this.broadcasts.take();
    }

    @Override
    public byte[] getBroadcast(long timeout, TimeUnit unit) throws InterruptedException {
        return this.broadcasts.poll(timeout, unit);
    }

    @Override
    public void clear() {
        this.broadcasts.clear();
    }
}
