/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.skype.bot;

import com.skype.*;

public final class AdminQueue extends AbstractEventQueue {

    public AdminQueue() {
        super(SkypeBot.getAdminList());
    }

    @Override
    public boolean validMsg(ChatMessage msg) {
        try {
            String msgtxt = msg.getContent();
            System.out.println("valid admin command");
            return msgtxt.startsWith(SkypeBot.AdminTrigger());
        } catch (Exception ex) {
        }
        return false;
    }

    @Override
    public String eventType() {
        return "Admin Event";
    }
}
