/*
 * Copyright © 2013 EAzama <eazama001@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.skype.bot.event;

import com.skype.*;

public abstract class EventModule implements Comparable<EventModule> {

    public abstract String getName();

    public abstract void EventFunction(ChatMessage msg) throws SkypeException ;

    public String HelpString(){
        return "No Help Documentation";
    }
    
    public boolean isVisible() {
        return true;
    }
    
    @Override
    public final int compareTo(EventModule nMod){
        return getName().compareToIgnoreCase(nMod.getName());
    }
}
