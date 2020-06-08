package com.example.shahar.biologyapp;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * @author Shahar Chen
 * @version 1.0
 */
public class Plant {

    /**
     * The  plant's name
     */
    private String name;
    /**
     * The  plant's position in the garden
     */
    private String position;
    /**
     * The  plant's watering needs
     */
    private String watering;
    /**
     * The  plant's type
     */
    private String type;
    /**
     * The  plant's physical state
     */
    private String state;
    /**
     * Pic number
     */
    private int picNum;
    /**
     * The object's key
     */
    private String key;
    /**
     * The time that passed since creation
     */
    private String remind;

    /**
     * This is the constructor.
     * The plant's pic will be added after the creation of the item
     *
     * @param name     This is the name of the plant
     * @param position This is the position of the plant in the garden
     * @param watering This is how much water the plant needs
     * @param type     This is what type of plant it is
     * @param state    This is the physical state of the plant
     * @param key      This is the key for the Firebase
     * @return an object
     */
    public Plant(String name, String position, String watering, String type, String state, String key) {
        //the plant's pic will be added after the creation of the item
        this.name = name;
        this.position = position;
        this.watering = watering;
        this.type = type;
        this.state = state;
        this.key = key;
        this.remind = "Hours since last edit:0";
        // set image as wheat
        this.picNum = 0;
    }

    public Plant() {

    }

    /**
     * getter
     *
     * @return the name of the plant
     */
    public String getName() {
        return name;
    }

    /**
     * setter
     * sets the name of the plant
     *
     * @return nothing
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter
     *
     * @return the position of the plant
     */
    public String getPosition() {
        return position;
    }

    /**
     * setter
     * sets the position of the plant
     *
     * @return nothing
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * getter
     *
     * @return the watering needs of the plant
     */
    public String getWatering() {
        return watering;
    }

    /**
     * setter
     * sets the watering needs of the plant
     *
     * @return nothing
     */
    public void setWatering(String watering) {
        this.watering = watering;
    }

    /**
     * getter
     *
     * @return the type of the plant
     */
    public String getType() {
        return type;
    }

    /**
     * setter
     * sets the type of the plant
     *
     * @return nothing
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getter
     *
     * @return the physical state of the plant
     */
    public String getState() {
        return state;
    }

    /**
     * setter
     * sets the physical state of the plant
     *
     * @return nothing
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * getter
     *
     * @return the key of the object
     */
    public String getKey() {
        return key;
    }

    /**
     * setter
     * sets the key of the object
     *
     * @return nothing
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * getter
     *
     * @return the time that passed since creation
     */
    public String getRemind() {
        return this.remind;
    }

    /**
     * setter
     * sets the key of the object
     *
     * @return nothing
     */
    public void setRemind(String timeSinceCreation) {
        this.remind = timeSinceCreation;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", watering='" + watering + '\'' +
                ", type='" + type + '\'' +
                ", state='" + state + '\'' +
                ", picNum=" + picNum +
                ", key='" + key + '\'' +
                ", remind='" + remind + '\'' +
                '}';
    }

    public int getPicNum() {
        return picNum;
    }

    public void setPicNum(int picNum) {
        this.picNum = picNum;
    }
}
