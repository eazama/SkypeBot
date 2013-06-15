/*
 * Copyright © 2013 EAzama <eazama001@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.skype;

import com.skype.bot.*;

public class AbstractBotEventListener implements ChatMessageListener {

    private AbstractEventQueue mq;
    private String eventType;

    public AbstractBotEventListener(AbstractEventQueue nMQ, String nEventType) {
        mq = nMQ;
        eventType = nEventType;
    }

    @Override
    public final void chatMessageReceived(ChatMessage msg) throws SkypeException {
        try {
            if (msg.getTime().before(SkypeBot.startDate)) {
                return;
            }

            if (!msg.getType().equals(ChatMessage.Type.SAID)) {
                return;
            }
            if (allowMessage(msg)) {
                enqueue(mq, msg);
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public final void chatMessageSent(ChatMessage cm) throws SkypeException {
        try {
            chatMessageReceived(cm);
        } catch (Exception ex) {
        }
    }

    private void enqueue(AbstractEventQueue queue, ChatMessage msg) throws SkypeException {
        try {
            queue.put(msg);
            //System.out.println(eventType + ": " + msg.getId() + " queued");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public boolean allowMessage(ChatMessage msg){
        return true;
    }
}
