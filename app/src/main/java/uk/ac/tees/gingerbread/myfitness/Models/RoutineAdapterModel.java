package uk.ac.tees.gingerbread.myfitness.Models;

/**
 * Created by Peter on 14/04/2017.
 */

//Sadly this has to exist because I can't figure out how to do adapters with a single object containing arrays of stuff.
public class RoutineAdapterModel {
    private int exerciseId;
    private boolean active;
    private String description;
    private String name;
    private RoutineEntry routine;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RoutineEntry getRoutine() {
        return routine;
    }

    public void setRoutine(RoutineEntry routine) {
        this.routine = routine;
    }

    public RoutineAdapterModel(RoutineEntry routine, int exerciseId, String name, String description) {
        this.exerciseId = exerciseId;
        this.name = name;
        this.description = description;
        this.routine = routine;
    }
}
