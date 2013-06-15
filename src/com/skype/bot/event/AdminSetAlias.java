/*
 * Copyright © 2013 EAzama <eazama001@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.skype.bot.event;

import com.skype.ChatMessage;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.bot.SkypeBot;
import com.skype.bot.user.*;
import org.apache.commons.lang.StringUtils;

public class AdminSetAlias extends EventModule {

    @Override
    public String getName() {
        return "SetAlias";
    }

    @Override
    public void EventFunction(ChatMessage msg) throws SkypeException {
        String[] s = msg.getContent().split(" ");

        if (s.length != 3) {
            return;
        }
        
        if(!SkypeBot.UserRecords().ContainsUser(msg, s[1])){
            msg.getChat().send(s[1] + " isn't in this chat");
        }

        for (UserRecord u : SkypeBot.UserRecords()) {
            if (u.getAlias().equalsIgnoreCase(s[2])) {
                msg.getChat().send(s[2] + " is already registered to " + u.getSkypeID());
                return;
            }
        }

        if (!StringUtils.isAsciiPrintable(s[2])) {
            msg.getChat().send("Alphanumeric characters only please");
            return;
        }

        if (s[2].length() > 25) {
            msg.getChat().send("Alias must be less than 25 characters");
            return;
        }

        UserRecord user = SkypeBot.UserRecords().GetUser(s[1]);
        user.setAlias(s[2]);
        msg.getChat().send(user.getSkypeID() + "'s alias registered as " + user.getAlias());
        Skype.getUser(user.getSkypeID()).setDisplayName(user.getDisplayName());

        SkypeBot.UserRecords().SaveAlias();
    }
}
