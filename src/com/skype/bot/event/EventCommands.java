/*
 * Copyright © 2013 EAzama <eazama001@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.skype.bot.event;

import com.skype.*;
import com.skype.bot.SkypeBot;
import java.util.logging.*;

public class EventCommands extends EventModule {

    public String getName() {
        return "Commands";
    }

    @Override
    public String HelpString() {
        String retString = "Usage:\n"
                + "commands: Lists the basic commands the bot with respond to.\n"
                + "Please use !help <command> to get more detailed information about the command.\n"
                + "Make sure you remember the '!' before the command name";
        return retString;
    }

    @Override
    public void EventFunction(ChatMessage msg) throws SkypeException {
        String retString = "Current commands are ";
        for (EventModule mod : SkypeBot.getEventList()) {
            if (mod.isVisible()) {
                retString += (SkypeBot.CommandTrigger()+mod.getName() + ", ");
            }
        }
        retString = retString.substring(0, retString.length() - 2);
        msg.getChat().send(retString);
    }
}
