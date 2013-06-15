/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.skype.bot.event;

import com.skype.ChatMessage;
import com.skype.SkypeException;
import com.skype.bot.SkypeBot;

public class AdminDemote extends EventModule {

    @Override
    public String getName() {
        return "Demote";
    }

    @Override
    public void EventFunction(ChatMessage msg) throws SkypeException {
                String[] s = msg.getContent().split(" ");
        
        if(s.length != 2){
            return;
        }
        
        if(!SkypeBot.UserRecords().ContainsUser(msg, s[1])){
            msg.getChat().send(s[1] + " is not in this chat");
            return;
        }
        
        String user = SkypeBot.UserRecords().GetUser(s[1]).getSkypeID();        
        
        if(SkypeBot.blackList.contains(user)){
            SkypeBot.admins.remove(user);
            msg.getChat().send(user + " is already blacklisted");
            saveLists();
            return;
        }
        
        if(SkypeBot.admins.contains(user)){
            SkypeBot.admins.remove(user);
            msg.getChat().send(user + " was removed from admins");
            saveLists();
            return;
        }
        
        SkypeBot.blackList.add(user);
        msg.getChat().send(user + " blacklisted");
        saveLists();
        

 
    }
    private void saveLists() {
        utilities.TextFileManager.writeFile("Data/Admins.txt", SkypeBot.admins.toArray());
        utilities.TextFileManager.writeFile("Data/Blacklist.txt", SkypeBot.blackList.toArray());
    }
}
