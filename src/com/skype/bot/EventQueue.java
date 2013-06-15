/*
 * Copyright © 2013 EAzama <eazama001@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.skype.bot;

import com.skype.*;

public final class EventQueue extends AbstractEventQueue {

    public EventQueue() {
        super(SkypeBot.getEventList());
    }

    @Override
    public boolean validMsg(ChatMessage msg) {
        try {
            String msgtxt = msg.getContent();
            return msgtxt.startsWith(SkypeBot.CommandTrigger());
        } catch (Exception ex) {
        }
        return false;
    }



    @Override
    public String eventType() {
        return "Core Event";
    }
}
