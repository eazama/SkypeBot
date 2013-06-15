/*
 * Copyright © 2013 EAzama <eazama001@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.skype.bot.user;

import com.skype.*;
import com.skype.bot.SkypeBot;
import java.util.*;
import java.util.logging.*;

public class UserRecordList extends LinkedList<UserRecord> {

    private static final Logger logger = Logger.getLogger(UserRecordList.class.getName());

    public UserRecordList(UserRecordList usrRec) {
        super(usrRec);
    }

    public UserRecordList() {
    }

    public UserRecord GetUser(String name) {
        for (UserRecord usr : this) {
            if (usr.getSkypeID().equalsIgnoreCase(name)) {
                return usr;
            }
        }

        for (UserRecord usr : this) {
            if (usr.getAlias().equalsIgnoreCase(name)) {
                return usr;
            }
        }
        UserRecord user = new UserRecord(name);
        add(user);
        SaveAlias();
        SkypeBot.rebuildComboBox();
        return user;
    }

    public boolean ContainsUser(ChatMessage msg, String name) throws SkypeException {
        String lName = name;

        for (UserRecord usr : this) {
            if (usr.getAlias().equalsIgnoreCase(lName)) {
                lName = usr.getSkypeID();
            }
        }
        User[] chatUsers = msg.getChat().getAllMembers();
        for (int i = 0; i < chatUsers.length; i++) {
            if (chatUsers[i].getId().equalsIgnoreCase(lName)) {
                return true;
            }
        }
        return false;
    }

    public void SaveUsers() {
        SaveAlias();
        logger.log(Level.INFO, "Saving Users");
        for (UserRecord usr : this) {
            usr.SaveUser();
        }
        logger.log(Level.INFO, "Users Saved");
    }

    public void LoadUsers() {
        logger.log(Level.INFO, "Loading Users");
        for (UserRecord usr : this) {
            usr.LoadUser();
        }
        logger.log(Level.INFO, "Users Loaded");    }

    public void SaveAlias() {
        utilities.TextFileManager.clearFile("SaveData/UserAlias.txt");
        for (UserRecord usr : this) {
            utilities.TextFileManager.appendToFile("SaveData/UserAlias.txt", usr.getSkypeID() + " " + usr.getAlias());
        }
    }
}
