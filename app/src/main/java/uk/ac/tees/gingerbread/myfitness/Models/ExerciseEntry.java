package uk.ac.tees.gingerbread.myfitness.Models;

/**
 * Created by 07mct on 07/03/2017.
 */

public class ExerciseEntry
{
    private String name;
    private String description;
    private int id;

    public ExerciseEntry(String name, String description, int id)
    {
        this.name = name;
        this.description = description;
        this.id = id;
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

    public int getId()
    {
        return id;
    }
}
