/*
 * Copyright © 2013 EAzama <eazama001@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.skype.bot;

import com.skype.*;

public final class AdminQueue extends AbstractEventQueue {

    public AdminQueue() {
        super(SkypeBot.getAdminList());
    }

    @Override
    public boolean validMsg(ChatMessage msg) {
        try {
            String msgtxt = msg.getContent();
            System.out.println("valid admin command");
            return msgtxt.startsWith(SkypeBot.AdminTrigger());
        } catch (Exception ex) {
        }
        return false;
    }

    @Override
    public String eventType() {
        return "Admin Event";
    }
}
