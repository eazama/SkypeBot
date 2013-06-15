/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.skype.bot.user;

public abstract class UserRecordModule implements Comparable<UserRecordModule> {

    public abstract String getName();

    public abstract void saveModule(String skypeID);

    public abstract void loadModule(String skypeID);

    public abstract String showModule();
    
    @Override
    public final int compareTo(UserRecordModule nMod){
        return getName().compareTo(nMod.getName());
    }
}
