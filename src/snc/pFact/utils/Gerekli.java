package snc.pFact.utils;

import java.util.Random;

/**
 * Gerekli
 */
public class Gerekli {

    public static final Random random = new Random();

    public static String getRemainingTime(long date) {

        int days = (int) Math.floorDiv(date, 24 * 60 * 60 * 1000);
        date -= days * 24 * 60 * 60 * 1000;
        int hours = (int) Math.floorDiv(date, 60 * 60 * 1000);
        date -= hours * 60 * 60 * 1000;
        int minutes = (int) Math.floorDiv(date, 60 * 1000);
        date -= minutes * 60 * 1000;
        int seconds = (int) Math.floorDiv(date, 1000);
        String s = days + ":";
        s += hours < 10 ? "0" : "";
        s += hours + ":";
        s += minutes < 10 ? "0" : "";
        s += minutes + ":";
        s += seconds < 10 ? "0" : "";
        s += seconds;
        return s;
    }
}