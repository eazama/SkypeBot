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

public class AdminTimerReset extends EventModule {

    @Override
    public String getName() {
        return "TimerReset";
    }

    @Override
    public void EventFunction(ChatMessage msg) throws SkypeException {
        String[] s = msg.getContent().split(" ");
        
        if(s.length != 3){
            return;
        }
        
        if(!SkypeBot.UserRecords().ContainsUser(msg, s[1])){
            msg.getChat().send(s[1] + " is not in this chat");
            return;
        }
        
        UserRecord user = SkypeBot.UserRecords().GetUser(s[1]);
        RecordTimer userTimer = user.getModule(RecordTimer.class);
        
        if(userTimer.getTimer(s[2]) == null){
            msg.getChat().send(s[2] + " is not a registered timer");
            return;
        }
        
        userTimer.setNow(s[2]);
        
        msg.getChat().send(s[2] + " has been reset for " + s[1]);
        
        userTimer.saveModule(user.getSkypeID());
        
                
    }

}
