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

public class AdminPromote extends EventModule {

    @Override
    public String getName() {
        return "Promote";
    }

    @Override
    public void EventFunction(ChatMessage msg) throws SkypeException {
        String[] s = msg.getContent().split(" ");

        if (s.length != 2) {
            return;
        }

        if (!SkypeBot.UserRecords().ContainsUser(msg, s[1])) {
            msg.getChat().send(s[1] + " is not in this chat");
            return;
        }

        String user = SkypeBot.UserRecords().GetUser(s[1]).getSkypeID();

        if (SkypeBot.admins.contains(user)) {
            SkypeBot.blackList.remove(user);
            msg.getChat().send(user + " is already an admin");
            saveLists();
            return;
        }

        if (SkypeBot.blackList.contains(user)) {
            SkypeBot.blackList.remove(user);
            msg.getChat().send(user + " was removed from the blacklist");
            saveLists();
            return;
        }

        SkypeBot.admins.add(user);
        msg.getChat().send(user + " promoted to admin");

    }

    private void saveLists() {
        utilities.TextFileManager.writeFile("Data/Admins.txt", SkypeBot.admins.toArray());
        utilities.TextFileManager.writeFile("Data/Blacklist.txt", SkypeBot.blackList.toArray());
    }
}
