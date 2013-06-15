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
import com.skype.bot.user.*;

public class AdminTimerAdd extends EventModule {
    
    @Override
    public String getName() {
        return "TimerAdd";
    }
    
    @Override
    public void EventFunction(ChatMessage msg) throws SkypeException {
        String[] s = msg.getContent().split(" ");
        
        if (s.length != 5) {
            return;
        }
        
        if (!SkypeBot.UserRecords().ContainsUser(msg, s[1])) {
            msg.getChat().send(s[1] + " is not in this chat");
            return;
        }
        
        UserRecord user = SkypeBot.UserRecords().GetUser(s[1]);
        RecordTimer userTimer = user.getModule(RecordTimer.class);
        
        if (userTimer.getTimer(s[2]) == null) {
            msg.getChat().send(s[2] + " is not a registered timer");
            return;
        }
        
        int num;
        try {
            num = Integer.parseInt(s[4]);
        } catch (NumberFormatException ex) {
            msg.getChat().send(s[4] + " is not a valid number");
            return;
        }
        
        if (s[4].equalsIgnoreCase("days")) {
            userTimer.addDays(s[2], num);
        } else if (s[4].equalsIgnoreCase("hours")) {
            userTimer.addHours(s[2], num);
        } else if (s[4].equalsIgnoreCase("minutes")) {
            userTimer.addMinutes(s[2], num);
        } else if (s[4].equalsIgnoreCase("seconds")) {
            userTimer.addSeconds(s[2], num);
        }
        
        msg.getChat().send("Time Adjusted");
    }
}
