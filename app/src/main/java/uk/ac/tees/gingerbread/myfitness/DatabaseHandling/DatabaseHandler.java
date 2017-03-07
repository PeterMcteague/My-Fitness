package uk.ac.tees.gingerbread.myfitness.DatabaseHandling;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import uk.ac.tees.gingerbread.myfitness.Classes.DietEntry;

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
    private static final String COL_INFO_AGE = "AGE" ;
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

        //Generating a create SQL Statement
        String CREATE_TABLES = "CREATE TABLE "
                + TABLE_NAME_DIET
                + "(" + COL_DIET_DATE + "DATE,"
                + COL_DIET_CAL + "INTEGER,"
                + COL_DIET_CAL_GOAL + "INTEGER,"
                + COL_DIET_PROTEIN + "REAL,"
                + COL_DIET_PROTEIN_GOAL + "REAL" + ")"

                + TABLE_NAME_EXCERCISES
                + "(" + COL_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NAME + "TEXT,"
                + COL_DESCRIPTION + "TEXT" + ")"

                +TABLE_NAME_INFO
                + "(" + COL_INFO_NAME + "TEXT,"
                + COL_INFO_HEIGHT + "REAL,"
                + COL_INFO_AGE + "INTEGER,"
                + COL_INFO_WEIGHT + "REAL,"
                +COL_INFO_GENDER + "TEXT,"
                +COL_INFO_ACTIVITY_LEVEL + "INTEGER" + "CHECK activity_level <6 && > 0" + ")"

                +TABLE_NAME_ROUTINE
                + "(" + COL_DAY + "TEXT,"
                + COL_EXCERCISE_ID + "INTEGER" + ")"

                +TABLE_NAME_PICTURES
                + "(" + COL_PICTURES_IMAGE + "BLOB" + ")";

                //Execute/run the create SQL statement
                db.execSQL(CREATE_TABLES);

                Log.d("Database", "Database Created.");

                //Inserting default data into the exercise table
                String sql =
                        "INSERT INTO TABLE_NAME_EXERCISE(COL_ID , COL_NAME , COL_DESCRIPTION) VALUES('1' , 'SIT' , 'SIT DOWN')";
                    db.execSQL(sql);

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

    //--------------Accessor methods for diet table----------------------------------------------//

    /**Adds a diet entry to the diet table.
     *
     * @param dateIn The date
     * @param calories The calories eaten
     * @param caloriesGoal The calories goal
     * @param protein The protein eaten
     * @param proteinGoal The protein goal
     * @return ID for new record.
     */
    public long addDietEntry(long dateIn, int calories, int caloriesGoal, float protein, float proteinGoal)
    {
        // Open database connection (for write)
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DIET_DATE , dateIn);
        values.put(COL_DIET_CAL, calories);
        values.put(COL_DIET_CAL_GOAL, caloriesGoal);
        values.put(COL_DIET_PROTEIN, protein);
        values.put(COL_DIET_PROTEIN_GOAL, proteinGoal);

        // Add record to database and get id of new record (must long integer).
        long id = db.insert(TABLE_NAME_DIET, null, values);
        db.close(); // Closing database connection
        return id; // Return id for new record
    }

    /**Adds a diet entry to the diet table.
     *
     * @param dietEntry A diet entry object to add.
     * @return ID for new record.
     */
    public long addDietEntry(DietEntry dietEntry)
    {
        // Open database connection (for write)
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DIET_DATE , dietEntry.getDate());
        values.put(COL_DIET_CAL, dietEntry.getCalories());
        values.put(COL_DIET_CAL_GOAL, dietEntry.getCaloriesGoal());
        values.put(COL_DIET_PROTEIN, dietEntry.getProtein());
        values.put(COL_DIET_PROTEIN_GOAL, dietEntry.getProteinGoal());

        // Add record to database and get id of new record (must long integer).
        long id = db.insert(TABLE_NAME_DIET, null, values);
        db.close(); // Closing database connection
        return id; // Return id for new record
    }

    /**Gets a diet object for the day, from the database (Remember to get rid of time from date).
     *
     * @param dateIn - The date to get diet entry for.
     * @return - Returns a diet entry object if found.
     */
    public DietEntry getDietEntry(long dateIn)
    {
        // Connect to the database to read data
        SQLiteDatabase db = this.getReadableDatabase();
        // Generate SQL SELECT statement
        String selectQuery = "SELECT * FROM " + TABLE_NAME_DIET + " WHERE " + COL_DIET_DATE + " = " + dateIn;

        // Execute select statement
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) { // If data (records) available
            int idDate = cursor.getColumnIndex(COL_DIET_DATE);
            int idCalories = cursor.getColumnIndex(COL_DIET_CAL);
            int idCaloriesGoal = cursor.getColumnIndex(COL_DIET_CAL_GOAL);
            int idProtein = cursor.getColumnIndex(COL_DIET_PROTEIN);
            int idProteinGoal = cursor.getColumnIndex(COL_DIET_PROTEIN_GOAL);
            db.close();
            return new DietEntry(
                    cursor.getLong(idDate),
                    cursor.getInt(idCalories),
                    cursor.getInt(idCaloriesGoal),
                    cursor.getFloat(idProtein),
                    cursor.getFloat(idProteinGoal)
            );
        }
        return null;
    }

    /**Returns an arraylist containing all records in the diet entry database in the form of
     * DietEntry objects.
     *
     * @return An ArrayList of all diet entry objects in db.
     */
    public ArrayList<DietEntry> getAllDietEntries()
    {
        // Create empty list
        ArrayList<DietEntry> list = new ArrayList<DietEntry>();
        // Connect to the database to read data
        SQLiteDatabase db = this.getReadableDatabase();
        // Generate SQL SELECT statement
        String selectQuery = "SELECT * FROM " + TABLE_NAME_DIET;

        // Execute select statement
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) { // If data (records) available
            int idDate = cursor.getColumnIndex(COL_DIET_DATE);
            int idCalories = cursor.getColumnIndex(COL_DIET_CAL);
            int idCaloriesGoal = cursor.getColumnIndex(COL_DIET_CAL_GOAL);
            int idProtein = cursor.getColumnIndex(COL_DIET_PROTEIN);
            int idProteinGoal = cursor.getColumnIndex(COL_DIET_PROTEIN_GOAL);
            do {
                //Str,str,long,double,double,bitmap
                list.add(new DietEntry(
                        cursor.getLong(idDate),
                        cursor.getInt(idCalories),
                        cursor.getInt(idCaloriesGoal),
                        cursor.getFloat(idProtein),
                        cursor.getFloat(idProteinGoal)
                ));
            } while (cursor.moveToNext()); // repeat until there are no more records
        }
        db.close();
        return list;
    }
}