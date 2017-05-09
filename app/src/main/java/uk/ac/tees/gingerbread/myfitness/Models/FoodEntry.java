package uk.ac.tees.gingerbread.myfitness.Models;

/**
 * Created by 07mct on 09/05/2017.
 */

public class FoodEntry {

    private String name;
    private Long date;
    private int calories;
    private float protein;

    public FoodEntry(String name, Long date, int calories, float protein) {
        this.name = name;
        this.date = date;
        this.calories = calories;
        this.protein = protein;
    }

    public FoodEntry(String name, Long date, int calories) {
        this.name = name;
        this.date = date;
        this.calories = calories;
        this.protein = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }




}
