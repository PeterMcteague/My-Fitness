package uk.ac.tees.gingerbread.myfitness.Classes;

/**
 * Created by Joseph on 08/03/2017.
 */

public class RoutineEntry {

    private String day;
    private int exerciseId;

    public RoutineEntry(String day, int exerciseId)
    {
       this.day = day;
       this.exerciseId = exerciseId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }
}
