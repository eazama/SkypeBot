/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.skype.bot;

import com.skype.*;

public final class EventQueue extends AbstractEventQueue {

    public EventQueue() {
        super(SkypeBot.getEventList());
    }

    @Override
    public boolean validMsg(ChatMessage msg) {
        try {
            String msgtxt = msg.getContent();
            return msgtxt.startsWith(SkypeBot.CommandTrigger());
        } catch (Exception ex) {
        }
        return false;
    }



    @Override
    public String eventType() {
        return "Core Event";
    }
}
