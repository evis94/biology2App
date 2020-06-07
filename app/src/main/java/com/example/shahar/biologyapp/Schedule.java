package com.example.shahar.biologyapp;

import java.util.WeakHashMap;

/**
 * @author  Shahar Chen
 * @version 1.0
 */
public class Schedule {

     /** The day */
    private String day;
    /** The hour */
    private String hour;
    /** What takes place during that time */
    private String body;
    /** The object's key */
    private String key;
    /** The object's uid */
    private String uid;

    /**
     * This is the constructor.
     * @param day This is the day
     * @param hour This is the hour
     * @param body This describes what takes place during that time
     * @param uid This is the object's uid
     * @param key This is the object's key
     * @return an object
     */
    public Schedule(String day, String hour, String body , String uid ,String key ) {
        this.day = day;
        this.hour = hour;
        this.body = body;
        this.key = key;
        this.uid = uid;
    }

    /**
     * This is the constructor.
     * @return an empty object
     */
    public Schedule()
    {
        this.day = "";
        this.hour = "";
        this.body = "";
        this.key = "";
        this.uid = "";
    }

    /**
     * getter
     * @return the day
     */
    public String getDay() {
        return day;
    }

    /**
     * setter
     * sets the day
     * @return nothing
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     * getter
     * @return the hour
     */
    public String getHour() {
        return hour;
    }

    /**
     * setter
     * sets the hour
     * @return nothing
     */
    public void setHour(String hour) {
        this.hour = hour;
    }

    /**
     * getter
     * @return the body of the schedule
     */
    public String getBody() {
        return body;
    }

    /**
     * setter
     * sets the body of the schedule
     * @return nothing
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * getter
     * @return the key of the object
     */
    public String getKey() {
        return key;
    }

    /**
     * setter
     * sets the key of the object
     * @return nothing
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * getter
     * @return the uid of the object
     */
    public String getUid() {
        return uid;
    }

    /**
     * setter
     * sets the uid of the object
     * @return nothing
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * This method describes the object
     * @return a sentence that describes the object
     */
    @Override
    public String toString() {
        return "Schedule{" +
                "time='" + day + '\'' +
                ", hour='" + hour + '\'' +
                ", body='" + body + '\'' +
                ", key='" + key + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
