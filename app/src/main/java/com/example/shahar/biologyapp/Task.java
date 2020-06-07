package com.example.shahar.biologyapp;

/**
 * @author  Shahar Chen
 * @version 1.0
 */
public class Task {

    /** The body of the task */
    private String task;
    /** The completion status of the task */
    private boolean isDone;
    /** The object's uid */
    private String uid;
    /** The object's key */
    private String key;


    /**
     * This is the constructor.
     * @param task This is the name of the task
     * @param isDone This shows if the task was done or not
     * @return an object
     */
    public Task(String task, boolean isDone) {
        this.task = task;
        this.isDone = false;
        this.key="";
        this.uid="";
    }

    /**
     * This is the constructor.
     * @return an empty task that has not been completed
     */
    public Task()
    {
        this.task="";
        this.isDone=false;
        this.uid="";
        this.key="";
    }

    /**
     * getter
     * @return the body of the task
     */
    public String getTask() {
        return task;
    }

    /**
     * setter
     * sets the body of the task
     * @return nothing
     */
    public void setTask(String task) {
        this.task = task;
    }

    /**
     * getter
     * @return true if the task has been completed or false if it hasn't
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * setter
     * sets the completion status of the task
     * @return nothing
     */
    public void setDone(boolean done) {
        isDone = done;
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
     * @return a yes or not message according to the completion status of the task
     */
    public String isDoneMessage()
    {
        String message="";
        if (this.isDone==true)
            message= "yes";
        else
            message="no";
        return  message;
    }

    /**
     * This method describes the object
     * @return a sentence that describes the object
     */
    @Override
    public String toString() {
        return "Task{" +
                "task='" + task + '\'' +
                ", isDone=" + isDone +
                ", uid='" + uid + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
