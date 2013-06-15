/*
 * Copyright © 2013 EAzama <eazama001@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.skype.bot;

import com.skype.*;

/*
 * Event queue for public commands.
 * May remove in the future and implement in SkypeBot class
 */
public final class EventQueue extends AbstractEventQueue {

    /*
     * Constructor. Creates the queue using the list of Public Commands registered in the bot
     */
    public EventQueue() {
        super(SkypeBot.getEventList());
    }

    /*
     * ID sting for logs
     */
    @Override
    public String eventType() {
        return "Core Event";
    }
}
