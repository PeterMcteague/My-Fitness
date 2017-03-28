package uk.ac.tees.gingerbread.myfitness.Models;

/**
 * Created by Joseph on 08/03/2017.
 */

public class InfoEntry {
    private float height;
    private float weight;
    private int activityLevel;
    private long date;
    private String goal;

    public InfoEntry(float height, float weight, int activityLevel , long date, String goal)
    {
        this.height = height;
        this.activityLevel = activityLevel;
        this.weight = weight;
        this.date = date;
        this.goal = goal;
    }


    public float getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }

    public int getActivityLevel() {
        return activityLevel;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setActivityLevel(int activityLevel) {
        this.activityLevel = activityLevel;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getGoal(){return goal;}

    public void setGoal(String goal){this.goal = goal;}
}

