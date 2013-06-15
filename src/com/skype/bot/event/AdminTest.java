/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.skype.bot.event;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import java.util.*;

public class AdminTest extends EventModule{

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public void EventFunction(ChatMessage msg) throws SkypeException {
        msg.getChat().send("Admin Command Success");
        ChatMessage nMsg = msg.getChat().send("Admin Command Success");
        nMsg.setContent("DERP");
        
        GregorianCalendar msgTime = new GregorianCalendar();
        msgTime.setTime(msg.getTime());
        
        System.out.println("msgTime: " + msgTime.getTimeInMillis());
        System.out.println("curTime: " + new GregorianCalendar().getTimeInMillis());
    }

}
