/*
 * Copyright © 2013 EAzama <eazama001@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.skype.bot;

import com.skype.*;
import com.skype.bot.event.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

public abstract class AbstractEventQueue extends LinkedBlockingQueue<ChatMessage> implements Runnable {

    private static final Logger logger = Logger.getLogger(AbstractEventQueue.class.getName());
    protected ChatMessage eventMsg;
    protected int lastID = 0;
    protected boolean doLoop = true;
    protected TreeSet<Integer> processedIDs = new TreeSet<Integer>();
    protected EventList eList;

    public AbstractEventQueue(EventList nList) {
        eList = nList;
    }

    @Override
    public final void run() {
        while (doLoop) {
            if (nextMsg()) {
                try {
                    processMessage(eventMsg);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                logger.log(Level.INFO, "{0}: {1} processed",
                        new Object[]{eventType(), eventMsg.getId()});
                processedIDs.add(Integer.parseInt(eventMsg.getId()));
                if (processedIDs.size() > 50) {
                    reduceIDs();
                }
            }
        }
    }

    protected void processMessage(ChatMessage msg) {
        try {
            new eventThread(msg).start();
        } catch (Exception ex) {
        }
    }

    public final void stop() {
        doLoop = false;
    }

    private boolean nextMsg() {
        try {
            eventMsg = take();
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, "Error in AbstractEventQueue.nextMsg()", ex);
            return false;
        }
        if (Integer.parseInt(eventMsg.getId()) <= lastID) {
            logger.log(Level.FINEST, "{0}: {1} rejected - {2}",
                    new Object[]{eventType(), eventMsg.getId(), lastID});
            return false;
        }
        if (processedIDs.contains(Integer.parseInt(eventMsg.getId()))) {
            logger.log(Level.FINEST, "{0}: {1} rejected - ID Processed",
                    new Object[]{eventType(), eventMsg.getId()});
            return false;
        }
        return true;

    }

    private void reduceIDs() {
        int i = lastID;
        while (processedIDs.size() > 25) {
            if (processedIDs.first() > lastID) {
                lastID = processedIDs.pollFirst();
            }
        }
    }

    public abstract String eventType();

    public abstract boolean validMsg(ChatMessage msg);

    private class eventThread extends Thread {

        ChatMessage msg;

        public eventThread(ChatMessage eventMsg) {
            msg = eventMsg;
        }

        @Override
        public void run() {
            eList.runEvent(msg);
        }
    }
}
