/*
 * Copyright © 2013 EAzama <eazama001@gmail.com>
 * This work is free. You can redistribute it and/or modify it under the
 * terms of the Do What The Fuck You Want To Public License, Version 2,
 * as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
 */
package com.skype.bot.user;

import com.skype.bot.SkypeBot;
import java.util.*;

public class RecordTimer extends UserRecordModule {

    private Map<String, GregorianCalendar> timers = new HashMap<String, GregorianCalendar>();

    public RecordTimer() {
        for (String tmr : SkypeBot.getUserTimers()) {
            timers.put(tmr, new GregorianCalendar());
        }
    }

    @Override
    public String getName() {
        return "timers";
    }

    @Override
    public String showModule() {
        String retVal = "";
        for (Map.Entry<String, GregorianCalendar> tmr : timers.entrySet()) {
            retVal += timerString(tmr.getKey());
            retVal += "\n";
        }
        return retVal;
    }

    @Override
    public void loadModule(String skypeID) {
        String[] splitStr;
        for (String str : utilities.TextFileManager.readFile("SaveData/Timers/" + skypeID + ".txt")) {
            splitStr = str.split(" ");
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTimeInMillis(Long.parseLong(splitStr[1]));
            timers.put(splitStr[0], cal);
        }
    }

    @Override
    public void saveModule(String skypeID) {
        String filename = "SaveData/Timers/" + skypeID + ".txt";
        utilities.TextFileManager.clearFile(filename);
        for (Map.Entry<String, GregorianCalendar> tmr : timers.entrySet()) {
            utilities.TextFileManager.appendToFile(filename, tmr.getKey() + " " + tmr.getValue().getTimeInMillis());
        }
    }

    public GregorianCalendar getTimer(String nTimer) {
        return timers.get(nTimer);
    }

    public void setTimer(String nTimer, GregorianCalendar nCal) {
        timers.put(nTimer, nCal);
    }

    public String timerString(String tmr) {
        String retVal = tmr + " ";

        GregorianCalendar cal = timers.get(tmr);

        retVal += String.format("%02d/%02d/%02d %02d:%02d:%02d%s\n",
                cal.get(GregorianCalendar.MONTH) + 1, cal.get(GregorianCalendar.DAY_OF_MONTH), cal.get(GregorianCalendar.YEAR),
                cal.get(GregorianCalendar.HOUR), cal.get(GregorianCalendar.MINUTE), cal.get(GregorianCalendar.SECOND),
                cal.get(GregorianCalendar.AM_PM) == 1 ? "PM" : "AM");

        return retVal;
    }

    public final void setMidnight(String timer) {
        GregorianCalendar cal = getTimer(timer);
        cal.add(GregorianCalendar.DATE, 1);
        cal.set(GregorianCalendar.MILLISECOND, 0);
        cal.set(GregorianCalendar.SECOND, 0);
        cal.set(GregorianCalendar.MINUTE, 0);
        cal.set(GregorianCalendar.HOUR, 0);
        cal.set(GregorianCalendar.AM_PM, GregorianCalendar.AM);
    }

    public final void setNow(String timer) {
        getTimer(timer).setTime(new GregorianCalendar().getTime());
    }

    public final void setTime(String timer, GregorianCalendar msgTime) {
        GregorianCalendar cal = getTimer(timer);
        cal.setTimeInMillis(msgTime.getTimeInMillis());
    }

    public final void setTime(String timer, Date msgTime) {
        GregorianCalendar cal = getTimer(timer);
        cal.setTime(msgTime);
    }

    public final void addSeconds(String timer, int nSec) {
        GregorianCalendar cal = getTimer(timer);
        cal.add(GregorianCalendar.SECOND, nSec);
    }

    public final void addMinutes(String timer, int nMin) {
        GregorianCalendar cal = getTimer(timer);
        cal.add(GregorianCalendar.MINUTE, nMin);
    }

    public final void addHours(String timer, int nHour) {
        GregorianCalendar cal = getTimer(timer);
        cal.add(GregorianCalendar.HOUR, nHour);
    }

    public final void addDays(String timer, int nDay) {
        GregorianCalendar cal = getTimer(timer);
        cal.add(GregorianCalendar.DATE, nDay);
    }
}
