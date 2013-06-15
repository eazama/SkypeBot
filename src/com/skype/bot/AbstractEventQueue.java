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


/*
 * Base class for event queues. Holds a list of events and checks messages that
 * are passed to it from the AbstractBotEventListener object containing it.
 * If the message starts with the 
 */
public abstract class AbstractEventQueue extends LinkedBlockingQueue<ChatMessage> implements Runnable {

    private static final Logger logger = Logger.getLogger(AbstractEventQueue.class.getName());
    protected ChatMessage eventMsg;
    protected int lastID = 0;
    protected boolean doLoop = true;
    protected TreeSet<Integer> processedIDs = new TreeSet<Integer>();
    protected EventList eList;

    /*
     * Constructor. Initializes the object with a list of commands to check
     * against when processing messages
     */
    public AbstractEventQueue(EventList nList) {
        eList = nList;
    }

    /*
     * Infinite loop that processes messages as they are queued up by an 
     * AbstractBotEventListener class. 
     */
    @Override
    public final void run() {
        while (doLoop) {
            //Checks if next message in queue is valid
            if (nextMsg()) {
                try {
                    //if so processes it
                    processMessage(eventMsg);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                //logger.log(Level.FINEST, "{0}: {1} processed",
                //        new Object[]{eventType(), eventMsg.getId()});
                //Adds the message ID to the list of prosessed IDs
                processedIDs.add(Integer.parseInt(eventMsg.getId()));
                //Trim the list of IDs if greater than 50
                if (processedIDs.size() > 50) {
                    reduceIDs();
                }
            }
        }
    }

    /*
     * Launches a new thread to process the message
     */
    protected void processMessage(ChatMessage msg) {
        new eventThread(msg).start();
    }

    /*
     * Ends the loop in run() and allows the queue to shut down.
     */
    public final void stop() {
        doLoop = false;
    }

    /*
     * Waits for a new chat message and determines if it has been processed already
     */
    private boolean nextMsg() {
        try {
            eventMsg = take();
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, "InterruptedException in AbstractEventQueue.nextMsg()", ex);
            return false;
        }
        //Rejects if the message is older than the last 50 queued messages
        if (Integer.parseInt(eventMsg.getId()) <= lastID) {
            logger.log(Level.FINEST, "{0}: {1} rejected - {2}",
                    new Object[]{eventType(), eventMsg.getId(), lastID});
            return false;
        }
        //Rejects if the message has the same ID as one of the last 50 queued messages
        if (processedIDs.contains(Integer.parseInt(eventMsg.getId()))) {
            logger.log(Level.FINEST, "{0}: {1} rejected - ID Processed",
                    new Object[]{eventType(), eventMsg.getId()});
            return false;
        }
        return true;
    }

    /*
     * Removes items from the TreeSet of previously queued items. Also sets
     * the cutoff ID for oldest allowable message
     */
    private void reduceIDs() {
        int i = lastID;
        while (processedIDs.size() > 25) {
            if (processedIDs.first() > lastID) {
                lastID = processedIDs.pollFirst();
            }
        }
    }

    //String to allow an Event Queue to identify itself in log messages
    public abstract String eventType();

    /*
     * Thread class to independently handle messages
     */
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
