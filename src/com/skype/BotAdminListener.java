/*
 * Copyright © 2013 EAzama <eazama001@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.skype;

import com.skype.bot.*;
import java.util.logging.*;

public final class BotAdminListener extends AbstractBotEventListener {

    private static final Logger logger = Logger.getLogger(BotAdminListener.class.getName());

    public BotAdminListener() {
        super(SkypeBot.adminQueue, "Admin Event");
    }

    @Override
    public boolean allowMessage(ChatMessage msg) {
        try {
            if (!SkypeBot.admins.contains(msg.getSender().getId().toLowerCase())) {
                return false;
            }
            if (msg.getContent().startsWith(SkypeBot.AdminTrigger())) {
                return true;
            }
        } catch (SkypeException ex) {
            logger.log(Level.SEVERE, "SkypeError in BotAdminListener.allowMessage()", ex);
            return false;
        }
        return false;
    }
}
