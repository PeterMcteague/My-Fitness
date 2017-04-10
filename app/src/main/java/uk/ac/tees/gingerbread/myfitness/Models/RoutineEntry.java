package uk.ac.tees.gingerbread.myfitness.Models;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Joseph on 08/03/2017.
 */

public class RoutineEntry {

    private String day;
    private List<ExerciseEntry> exercises;
    private long date;

    public RoutineEntry(Long date, List<ExerciseEntry> exercises)
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        this.date = date;
        this.day = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
        this.exercises = exercises;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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

    //Gets day of date in and updates that too.
    public void setDateDay(long date)
    {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date);
        this.date = date;
        this.day = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
    }
}
