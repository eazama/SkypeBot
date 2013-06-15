/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.skype;

import com.skype.bot.*;
import java.util.logging.*;

public final class BotEventListener extends AbstractBotEventListener {

    private static final Logger logger = Logger.getLogger(BotEventListener.class.getName());

    public BotEventListener() {
        super(SkypeBot.messageQueue, "Core Event");
    }

    @Override
    public boolean allowMessage(ChatMessage msg) {
        try {
            if (SkypeBot.blackList.contains(msg.getSenderId())) {
                return false;
            }
            if (msg.getContent().startsWith(SkypeBot.CommandTrigger())) {
                return true;
            }

        } catch (SkypeException ex) {
            logger.log(Level.SEVERE, "SkypeError in BotEventListener.allowMessage()", ex);
            return false;
        }
        return false;
    }
}
