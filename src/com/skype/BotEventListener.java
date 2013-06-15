/*
 * Copyright © 2013 EAzama <eazama001@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
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
