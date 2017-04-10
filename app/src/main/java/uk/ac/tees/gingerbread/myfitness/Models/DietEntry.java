package uk.ac.tees.gingerbread.myfitness.Models;

/**An object to hold data for a diet entry.
 * Created by 07mct on 07/03/2017.
 */

public class DietEntry
{
    private long date;
    private int calories;
    private int caloriesGoal;
    private float protein;
    private float proteinGoal;

    public DietEntry(long date, int calories, int caloriesGoal, float protein, float proteinGoal)
    {
        this.date = date;
        this.calories = calories;
        this.caloriesGoal = caloriesGoal;
        this.protein = protein;
        this.proteinGoal = proteinGoal;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getCaloriesGoal() {
        return caloriesGoal;
    }

    public void setCaloriesGoal(int caloriesGoal) {
        this.caloriesGoal = caloriesGoal;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getProteinGoal() {
        return proteinGoal;
    }

    public void setProteinGoal(float proteinGoal) {
        this.proteinGoal = proteinGoal;
    }
}
