package uk.ac.tees.gingerbread.myfitness.DatabaseHandling;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

/**A class for interacting with the sqlite database tables.*/
public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "Fitness.db";
    // Contacts table name
    private static final String TABLE_NAME_EXCERCISES = "Exercises";
    private static final String TABLE_NAME_ROUTINE = "Routine";
    private static final String TABLE_NAME_DIET = "Diet";
    private static final String TABLE_NAME_PICTURES = "ProgressPictures";
    private static final String TABLE_NAME_INFO = "UserInfo";
    // Exercises table column names
    private static final String COL_ID = "_id"; // Primary key column must be _id
    private static final String COL_NAME = "name";
    private static final String COL_DESCRIPTION = "description";
    //Routine column names
    private static final String COL_DAY = "day";
    private static final String COL_EXCERCISE_ID = "exercise_id";
    //Diet column names
    private static final String COL_DIET_DATE = "date";
    private static final String COL_DIET_CAL = "calories";
    private static final String COL_DIET_CAL_GOAL = "calories_goal";
    private static final String COL_DIET_PROTEIN = "protein";
    private static final String COL_DIET_PROTEIN_GOAL = "protein_goal";
    //Progress picture column names
    private static final String COL_PICTURES_IMAGE = "image";
    //Personal info columns
    private static final String COL_INFO_NAME = "name";
    private static final String COL_INFO_HEIGHT = "height";
    private static final String COL_INFO_WEIGHT = "weight";
    private static final String COL_INFO_GENDER = "gender";
    private static final String COL_INFO_ACTIVITY_LEVEL = "activity_level";

    /**Constructor for database handler object
     *
     * @param context Context required for database handler object.
     */
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /**Called when the database is created.
     * TODO: Fill in
     *
     * @param db A database object.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create tables
        //Fill in default values
    }

    /**Called to update the database.
     *
     * @param db The database object
     * @param oldNum The old version number
     * @param newNum The new version number
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldNum, int newNum) {
        /* Drop older table if exists and create fresh (deletes all data) */
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DIET);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EXCERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ROUTINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PICTURES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INFO);
        onCreate(db);
        db.close();
    }

    //TODO: Accessor methods
}