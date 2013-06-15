/*
 * Copyright © 2013 EAzama <eazama001@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
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
