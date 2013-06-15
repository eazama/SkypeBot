/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.skype.bot.event;

import com.skype.*;
import java.util.*;
import java.util.logging.*;

public class EventList extends TreeSet<EventModule> {

    private static final Logger logger = Logger.getLogger(EventList.class.getName());
    private String trigger = "!";

    public EventList() {
    }

    public EventList(String nTrigger){
        trigger = nTrigger;
    }
    
    public EventList(EventList evLst) {
        super(evLst);
        trigger = evLst.trigger;
    }

    public void runEvent(ChatMessage msg) {
        String msgText;
        try {
            msgText = msg.getContent();
        } catch (SkypeException ex) {
            logger.log(Level.SEVERE, "SkypeError in EventList.runEvent(): msg.getContent #1", ex);
            return;
        }
        int spacePos = msgText.indexOf(" ");
        if (spacePos == -1) {
            spacePos = msgText.length();
        }
        String s;
        try {
            s = msg.getContent().substring(0, spacePos);
        } catch (SkypeException ex) {
            logger.log(Level.SEVERE, "SkypeError in EventList.runEvent(): msg.getContent #2", ex);
            return;
        }
        for (EventModule ev : this) {
            if ((trigger+ev.getName()).equalsIgnoreCase(s)) {
                try {
                    ev.EventFunction(msg);
                } catch (SkypeException ex) {
                    logger.log(Level.SEVERE, "SkypeError in {0}.EventFunction()", ev.getName());
                }
            }
        }
    }

    public boolean runHelpEvent(ChatMessage msg) {
        String[] s;
        try {
            s = msg.getContent().split(" ");
        } catch (SkypeException ex) {
            logger.log(Level.SEVERE, "SkypeError in EventList.runHelpEvent()", ex);
            return false;
        }
        for (EventModule ev : this) {
            if ((trigger+ev.getName()).equalsIgnoreCase(s[1])) {
                try {
                    msg.getChat().send(ev.HelpString());
                } catch (SkypeException ex) {
                    logger.log(Level.SEVERE, "SkypeError in {0}.HelpFunction()", ev.getName());
                    return false;
                }
                return true;
            }
        }
        return false;
    }
}
