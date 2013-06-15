package com.skype.bot;

import com.skype.*;
import com.skype.bot.event.*;
import com.skype.bot.plugin.*;
import com.skype.bot.user.*;
import java.util.*;
import java.util.logging.*;

public class SkypeBot {

    public static final Logger logger = Logger.getLogger(SkypeBot.class.getName());
    /**
     * @param args the command line arguments
     */
    private static Random rand = new Random();

    public static Random getRand() {
        return rand;
    }
    private static UserRecordList userRecords = new UserRecordList();
    private static EventList eventList;
    private static EventList adminList;
    private static LinkedList<Class<? extends UserRecordModule>> recordModuleClasses =
            new LinkedList<Class<? extends UserRecordModule>>();
    private static LinkedList<String> userTimers =
            new LinkedList<String>();
    public static SkypeBotUI gui = new SkypeBotUI();
    public static EventQueue messageQueue;
    public static AdminQueue adminQueue;
    public static Date startDate;
    private static String commandTrigger = "!";
    private static String adminTrigger = "~";
    public static LinkedList<String> admins = new LinkedList<String>();
    public static LinkedList<String> blackList = new LinkedList<String>();

    public static void main(String[] args) {

        startDate = new Date();

        gui = new SkypeBotUI();

        utilities.TextFileManager.MakeDirectory("Plugins");
        utilities.TextFileManager.MakeDirectory("SaveData");
        utilities.TextFileManager.MakeDirectory("Data");
        utilities.TextFileManager.MakeDirectory("SaveData/Timers");
        utilities.TextFileManager.MakeFile("SaveData/UserAlias.txt");

        admins.addAll(utilities.TextFileManager.readFile("Data/Admins.txt"));
        blackList.addAll(utilities.TextFileManager.readFile("Data/BlackList.txt"));

        eventList = new EventList(commandTrigger);
        adminList = new EventList(adminTrigger);

        addEventModule(new EventCommands());
        addEventModule(new EventHelp());
        addEventModule(new EventRegisterAlias());
        addAdminModule(new AdminTest());
        addAdminModule(new AdminDemote());
        addAdminModule(new AdminPromote());
        addAdminModule(new AdminSetAlias());
        addAdminModule(new AdminTimerAdd());
        addAdminModule(new AdminTimerReset());
        addAdminModule(new AdminCommands());
        addRecordModule(RecordTimer.class);


        for (SkypeBotPlugin plugin : new PluginFinder().search("Plugins")) {
            logger.log(Level.INFO, "Loading Plugin: {0}", plugin.getName());
            plugin.Init();
        }
        messageQueue = new EventQueue();
        adminQueue = new AdminQueue();

        try {
            Skype.addChatMessageListener(new BotEventListener());
            Skype.addChatMessageListener(new BotAdminListener());
        } catch (SkypeException ex) {
            logger.log(Level.SEVERE, "SkypeError adding BotEventListener in SkypeBot.main", ex);
        }

        for (String str : utilities.TextFileManager.readFile("SaveData/UserAlias.txt")) {
            String[] splitName = str.split(" ");
            userRecords.add(new UserRecord(splitName[0], splitName[1]));
            logger.log(Level.INFO, "Registering Alias: {0}", str);
        }

        userRecords.LoadUsers();

        rebuildComboBox();

        new Thread(messageQueue).start();
        new Thread(adminQueue).start();

        gui.setVisible(true);

    }

    /*
     * Registers a new save data module for users
     * 
     * @param nClass - the save data module to be added. Must extend UserRecordModule.
     * returns true if the module was successfully registered, false otherwise
     */
    public static boolean addRecordModule(Class<? extends UserRecordModule> nClass) {
        if (recordModuleClasses.add(nClass)) {
            logger.log(Level.INFO, "Record Module Added: {0}", nClass.getName());
            return true;
        } else {
            logger.log(Level.INFO, "Error adding RecordModule: "
                    + nClass.getName() + "\nPlease check for record name conflicts");
            return false;
        }
    }

    /*
     * Registers a new public event module
     * 
     * @param nClass - the event module to be added.
     * returns true if the module was successfully registered, false otherwise
     */
    public static boolean addEventModule(EventModule nClass) {
        if (eventList.add(nClass)) {
            logger.log(Level.INFO, "Event Added: {0}", nClass.getName());
            return true;
        } else {
            logger.log(Level.INFO, "Error adding EventModule: "
                    + nClass.getName() + "\nPlease check for command name conflicts");
            return false;
        }
    }

    /*
     * Registers a new admin-only event module
     * 
     * @param nClass - the event module to be added.
     * returns true if the module was successfully registered, false otherwise
     */
    public static boolean addAdminModule(EventModule nClass) {
        if (adminList.add(nClass)) {
            logger.log(Level.INFO, "Admin Event Added: {0}", nClass.getName());
            return true;
        } else {
            logger.log(Level.INFO, "Error adding AdminModule: "
                    + nClass.getName() + "\nPlease check for command name conflicts");
            return false;
        }
    }

    /*
     * Registers a new timer module
     * 
     * @param nTimer - the name of the timer to be added
     * returns true if timer was added successfully, false otherwise
     */
    public static boolean addUserTimer(String nTimer) {
        if (userTimers.add(nTimer)) {
            logger.log(Level.INFO, "Timer Added: {0}", nTimer);
            return true;
        } else {
            logger.log(Level.INFO, "Error adding Timer: "
                    + nTimer + "\nPlease check for timer name conflicts");
            return false;
        }
    }

    public static void removeEventModule(EventModule nClass) {
        logger.log(Level.INFO, "Event Module Removed: {0}", nClass.getName());
        eventList.remove(nClass);
    }

    public static List<Class<? extends UserRecordModule>> getRecordModules() {
        return recordModuleClasses;
    }

    public static List<String> getUserTimers() {
        return userTimers;
    }

    public static EventList getEventList() {
        return eventList;
    }

    public static EventList getAdminList() {
        return adminList;
    }

    public static void rebuildComboBox() {
        LinkedList<String> list = new LinkedList<String>();
        for (int i = 0; i < gui.jComboBox1.getItemCount(); i++) {
            list.add(gui.jComboBox1.getItemAt(i).toString());
        }

        for (UserRecord usr : userRecords) {
            if (!list.contains(usr.getSkypeID())) {
                gui.jComboBox1.addItem(usr.getSkypeID());
            }
        }
    }

    public static UserRecordList UserRecords() {
        return userRecords;
    }

    public static void Announce(String text) {
        try {
            for (Chat cht : Skype.getAllChats()) {
                if (cht.getStatus().equals(Chat.Status.MULTI_SUBSCRIBED)) {
                    cht.send(text);
                }
            }
        } catch (SkypeException ex) {
            logger.log(Level.SEVERE, "SkypeError in SkypeBot.Announce()", ex);
        }
    }

    public static String getBotHost() {
        try {
            return Skype.getProfile().getId();
        } catch (SkypeException ex) {
            logger.log(Level.SEVERE, "SkypeError in SkypeBot.getBotHost()", ex);
            return null;
        }
    }

    public static String AdminTrigger() {
        return adminTrigger;
    }

    public static String CommandTrigger() {
        return commandTrigger;
    }
}
