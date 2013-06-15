/*
 * Copyright © 2013 EAzama <eazama001@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.skype.bot;

import com.skype.*;

/*
 * Queue for admin commands.
 * May remove in the future and implement in the SkypeBot class
 */
public final class AdminQueue extends AbstractEventQueue {

    /*
     * Constructor. Creates the queue using the list of Admin Commands registered in the bot
     */
    public AdminQueue() {
        super(SkypeBot.getAdminList());
    }

    /*
     * ID string for log files
     */
    @Override
    public String eventType() {
        return "Admin Event";
    }
}
