package uk.ac.tees.gingerbread.myfitness.Models;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Joseph on 08/03/2017.
 */

public class RoutineEntry {

    private List<ExerciseEntry> exercises;
    private long date;

    public RoutineEntry(Long date, List<ExerciseEntry> exercises)
    {
        this.date = date;
        this.exercises = exercises;
    }

    public String getDay() {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        return c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
    }

    public List<ExerciseEntry> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseEntry> exercises) {
        this.exercises = exercises;
    }

    public void addExercise(ExerciseEntry exercise)
    {
        this.exercises.add(exercise);
    }

    public void removeExercise(ExerciseEntry exercise)
    {
        this.exercises.remove(exercise);
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean containsExcercise(int excerciseId)
    {
        for (ExerciseEntry exercise : exercises)
        {
            if (exercise.getId() == excerciseId)
            {
                return true;
            }
        }
        return false;
    }

    public boolean containsExcercise(ExerciseEntry excercise)
    {
        return exercises.contains(excercise);
    }
}
