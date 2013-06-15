/*
 * Copyright © 2013 EAzama <eazama001@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.skype.bot.event;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import com.skype.bot.SkypeBot;

public class AdminCommands extends EventModule {

    @Override
    public String getName() {
        return "ACommands";
    }

    @Override
    public void EventFunction(ChatMessage msg) throws SkypeException {
        String retString = "Current commands are ";
        for (EventModule mod : SkypeBot.getAdminList()) {
            if (mod.isVisible()) {
                retString += (SkypeBot.AdminTrigger() + mod.getName() + ", ");
            }
        }
        retString = retString.substring(0, retString.length() - 2);
        msg.getSender().send(retString);
    }
}
