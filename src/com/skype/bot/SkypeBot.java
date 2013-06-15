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

        //Ensure that required directories and exist
        utilities.TextFileManager.MakeDirectory("Plugins");
        utilities.TextFileManager.MakeDirectory("SaveData");
        utilities.TextFileManager.MakeDirectory("Data");
        utilities.TextFileManager.MakeDirectory("SaveData/Timers");
        utilities.TextFileManager.MakeFile("SaveData/UserAlias.txt");
        utilities.TextFileManager.MakeFile("Data/Admins.txt");
        utilities.TextFileManager.MakeFile("Data/BlackList.txt");

        //Loads admins and banned users
        admins.addAll(utilities.TextFileManager.readFile("Data/Admins.txt"));
        blackList.addAll(utilities.TextFileManager.readFile("Data/BlackList.txt"));

        //Initiallizes public and admin commands with their trigger strings
        eventList = new EventList(commandTrigger);
        adminList = new EventList(adminTrigger);

        //Register default commands
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
        
        //Adds record module for timers
        addRecordModule(RecordTimer.class);

        //Load and intialize plugins
        for (SkypeBotPlugin plugin : new PluginFinder().search("Plugins")) {
            logger.log(Level.INFO, "Loading Plugin: {0}", plugin.getName());
            plugin.Init();
        }
        
        //Initialize message queues
        messageQueue = new EventQueue();
        adminQueue = new AdminQueue();

        //Add message listeners for events and admin events
        try {
            Skype.addChatMessageListener(new BotEventListener());
            Skype.addChatMessageListener(new BotAdminListener());
        } catch (SkypeException ex) {
            logger.log(Level.SEVERE, "SkypeError adding BotEventListener in SkypeBot.main", ex);
        }

        //Loads aliases for registered Users
        for (String str : utilities.TextFileManager.readFile("SaveData/UserAlias.txt")) {
            String[] splitName = str.split(" ");
            userRecords.add(new UserRecord(splitName[0], splitName[1]));
            logger.log(Level.INFO, "Registering Alias: {0}", str);
        }

        //Loads save data for users
        userRecords.LoadUsers();

        //Sets up list of Users in GUI
        rebuildComboBox();

        //Sets the bot to listen for commands
        new Thread(messageQueue).start();
        new Thread(adminQueue).start();

        //Shows the GUI
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

    /*
     * Removes an Event Module from the list of current commands
     * 
     * @param nClass - The command to be removed
     * returns true if successfully removed, false otherwise
     */
    public static boolean removeEventModule(EventModule nClass) {
        if (eventList.remove(nClass)) {
            logger.log(Level.INFO, "Event Module Removed: {0}", nClass.getName());
            return true;
        } else {
            logger.log(Level.INFO, "Error removing event: {0}", nClass.getName()
                    + "Are you sure it existed? Or was it already removed?");
            return false;
        }
    }


    /*
     * Returns the list of Record Modules
     */
    public static List<Class<? extends UserRecordModule>> getRecordModules() {
        return recordModuleClasses;
    }

    /*
     * Returns the list of Timer Modules
     */
    public static List<String> getUserTimers() {
        return userTimers;
    }

    /*
     * Returns the list containing the Event Commands
     */
    public static EventList getEventList() {
        return eventList;
    }

    /*
     * Returns the list containing the Admin Commands
     */
    public static EventList getAdminList() {
        return adminList;
    }

    /*
     * Returns the list of all Users
     */
    public static UserRecordList UserRecords() {
        return userRecords;
    }

    /*
     * Updates the list of users in the GUI.
     * 
     * No, I do not know why this is here.
     */
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


    /*
     * Sends a message to all chats the Bot's host account is currently in.
     * 
     * @param text - The message to send
     */
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

    
    /*
     * Returns the user ID of the Skype Account the bot is using
     */
    public static String getBotHost() {
        try {
            return Skype.getProfile().getId();
        } catch (SkypeException ex) {
            logger.log(Level.SEVERE, "SkypeError in SkypeBot.getBotHost()", ex);
            return null;
        }
    }

    /*
     * Returns the string of characters that prefixes Admin Commands
     */
    public static String AdminTrigger() {
        return adminTrigger;
    }

    /*
     * Returns the string of characters that prefixes public commands
     */
    public static String CommandTrigger() {
        return commandTrigger;
    }
}
