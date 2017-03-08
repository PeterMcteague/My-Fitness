package uk.ac.tees.gingerbread.myfitness.Classes;

/**
 * Created by 07mct on 07/03/2017.
 */

public class ExerciseEntry
{
    private String name;
    private String description;

    public ExerciseEntry(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
