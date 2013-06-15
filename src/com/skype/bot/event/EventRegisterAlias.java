/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.skype.bot.event;

import com.skype.*;
import com.skype.bot.*;
import com.skype.bot.user.*;
import org.apache.commons.lang.StringUtils;

public class EventRegisterAlias extends EventModule {

    @Override
    public String getName() {
        return "RegisterAlias";
    }

    @Override
    public String HelpString() {
        String retString = "Usage:\n"
                + "RegisterAlias <alias>: Registers an alias to your username.\n"
                + "Allows other users to use the alias instead of your skype ID to refer to you in commands";
        return retString;
    }

    @Override
    public void EventFunction(ChatMessage msg) throws SkypeException {
        String[] s = msg.getContent().split(" ");

        if (s.length != 2) {
            return;
        }
        
        for (UserRecord u : SkypeBot.UserRecords()) {
            if (u.getAlias().equalsIgnoreCase(s[1])) {
                msg.getChat().send(s[1] + " is already registered to " + u.getSkypeID());
                return;
            }
        }

        UserRecord user = SkypeBot.UserRecords().GetUser(msg.getSenderId());

        if (!StringUtils.isAsciiPrintable(s[1])) {
            msg.getChat().send("Alphanumeric characters only please");
            return;
        }

        if (s[1].length() > 25) {
            msg.getChat().send("Alias must be less than 25 characters");
            return;
        }

        user.setAlias(s[1]);
        msg.getChat().send(user.getSkypeID() + "'s alias registered as " + user.getAlias());
        msg.getSender().setDisplayName(user.getDisplayName());

        SkypeBot.UserRecords().SaveAlias();

    }
}
