package uk.ac.tees.gingerbread.myfitness.Classes;

/**
 * Created by Joseph on 08/03/2017.
 */

public class InfoEntry {
    private int age;
    private float height;
    private float weight;
    private String name;
    private String gender;
    private int activityLevel;

    public InfoEntry(int age, float height, float weight, String name, String gender , int activityLevel)
    {
        this.age = age;
        this.height = height;
        this.activityLevel = activityLevel;
        this.gender = gender;
        this.weight = weight;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public float getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getActivityLevel() {
        return activityLevel;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setActivityLevel(int activityLevel) {
        this.activityLevel = activityLevel;
    }
}
