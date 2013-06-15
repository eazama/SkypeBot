/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.skype.bot.user;

import com.skype.bot.SkypeBot;
import java.util.*;
import java.util.logging.*;

public class UserRecord extends LinkedList<UserRecordModule> {

    private static final Logger logger = Logger.getLogger(UserRecord.class.getName());
    
    private String skypeID;
    private String alias;

    public String getSkypeID() {
        return skypeID;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String nAlias) {
        alias = nAlias;
    }

    public String getDisplayName() {
        if (alias.equals("NullAlias")) {
            return skypeID;
        } else if (alias.equalsIgnoreCase(skypeID)) {
            return alias;
        } else {
            return alias + "(" + skypeID + ")";
        }
    }

    public <UserRecordModule> UserRecordModule getModule(Class<? extends UserRecordModule> clazz) {
        for (Object mod : this) {
            if (mod.getClass() == clazz) {
                return clazz.cast(mod);
            }
        }
        return null;
    }

    public UserRecord(String nID) {
        skypeID = nID;
        alias = "NullAlias";
        for (Class<? extends UserRecordModule> cls : SkypeBot.getRecordModules()) {
            try {
                add(cls.newInstance());
            } catch (Exception ex) {
            }
        }
    }

    public UserRecord(String nID, String nAlias) {
        skypeID = nID;
        alias = nAlias;
        for (Class<? extends UserRecordModule> cls : SkypeBot.getRecordModules()) {
            try {
                add(cls.newInstance());
            } catch (Exception ex) {
            }
        }
    }

    public void SaveUser() {
        logger.log(Level.INFO, "Saving {0}", getSkypeID());
        for (UserRecordModule mdl : this) {
            mdl.saveModule(skypeID);
        }
        logger.log(Level.INFO, "{0} Saved", getSkypeID());
    }

    public void LoadUser() {
        logger.log(Level.INFO, "Loading {0}", getSkypeID());
        for (UserRecordModule mdl : this) {
            mdl.loadModule(skypeID);
        }
        logger.log(Level.INFO, "{0} Loaded", getSkypeID());
    }

    public String displayUserRecord() {
        String retVal = getDisplayName();
        for (UserRecordModule mdl : this) {
            retVal += "\n********************************\n" + mdl.showModule();
        }
        return retVal;
    }

    public void SendMessage(String message) {
    }
}
