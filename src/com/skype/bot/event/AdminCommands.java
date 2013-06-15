/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
