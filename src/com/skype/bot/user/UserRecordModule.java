/*
 * Copyright © 2013 EAzama <eazama001@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
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
