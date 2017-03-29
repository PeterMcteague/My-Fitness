package uk.ac.tees.gingerbread.myfitness.Models;

/**
 * Created by 07mct on 28/03/2017.
 */

public class PersistentInfoEntry
{
    private long birthDate;
    private String name;
    private String gender;

    public PersistentInfoEntry(long birthDate, String name, String gender) {
        this.birthDate = birthDate;
        this.name = name;
        this.gender = gender;
    }

    public long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(long birthDate) {
        this.birthDate = birthDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
