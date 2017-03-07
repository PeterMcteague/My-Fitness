package uk.ac.tees.gingerbread.myfitness.Classes;

import android.graphics.Bitmap;

/**
 * Created by 07mct on 07/03/2017.
 */

public class PictureEntry
{
    private Bitmap picture;
    private long date;

    public PictureEntry(long date, Bitmap picture)
    {
        this.picture = picture;
        this.date = date;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}

