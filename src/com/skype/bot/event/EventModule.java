/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
