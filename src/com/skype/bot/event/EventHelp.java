/*
 * Copyright © 2013 EAzama <eazama001@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.skype.bot.event;

import com.skype.*;
import com.skype.bot.SkypeBot;

public class EventHelp extends EventModule {
    
    @Override
    public String getName() {
        return "help";
    }
    
    @Override
    public String HelpString() {
        String retString = "Usage:\n"
                + "help <command>: Displays the help message for the specified command.\n"
                + "Make sure you include the '!' before the command name";
        return retString;
    }
    
    @Override
    public void EventFunction(ChatMessage msg) throws SkypeException {
        String[] s = msg.getContent().split(" ");
        
        if (s.length == 1) {
            msg.getChat().send(HelpString());
            return;
        }
        
        if (s.length == 2) {
            if (!SkypeBot.getEventList().runHelpEvent(msg)) {
                msg.getChat().send(String.format("%s is not a known command", s[1]));
            }
        }
    }
}
