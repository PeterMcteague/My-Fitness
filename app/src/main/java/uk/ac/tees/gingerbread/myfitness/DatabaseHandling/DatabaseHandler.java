package uk.ac.tees.gingerbread.myfitness.DatabaseHandling;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import uk.ac.tees.gingerbread.myfitness.Classes.DietEntry;
import uk.ac.tees.gingerbread.myfitness.Classes.ExerciseEntry;
import uk.ac.tees.gingerbread.myfitness.Classes.PictureEntry;
import uk.ac.tees.gingerbread.myfitness.Classes.InfoEntry;
import uk.ac.tees.gingerbread.myfitness.Classes.RoutineEntry;

/**A class for interacting with the sqlite database tables.*/
public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "Fitness.db";
    // Contacts table name
    private static final String TABLE_NAME_EXERCISES = "Exercises";
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
    private static final String COL_EXERCISE_ID = "exercise_id";
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
     *
     * @param db A database object.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Generating a create SQL Statement
        String CREATE_TABLE_DIET = "CREATE TABLE "
                + TABLE_NAME_DIET
                + "("  + COL_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," + COL_DIET_DATE + "DATE,"
                + COL_DIET_CAL + "INTEGER,"
                + COL_DIET_CAL_GOAL + "INTEGER,"
                + COL_DIET_PROTEIN + "REAL,"
                + COL_DIET_PROTEIN_GOAL + "REAL" + ")";

        String CREATE_TABLE_EXERCISES = "CREATE TABLE " +
                TABLE_NAME_EXERCISES
                + "(" + COL_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NAME + "TEXT NOT NULL UNIQUE,"
                + COL_DESCRIPTION + "TEXT" + ")";

        String CREATE_TABLE_INFO = TABLE_NAME_INFO +
                "(" + COL_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," + COL_INFO_NAME + "TEXT,"
                + COL_DIET_DATE + " DATE NOT NULL UNIQUE,"
                + COL_INFO_HEIGHT + "REAL,"
                + COL_INFO_AGE + "INTEGER,"
                + COL_INFO_WEIGHT + "REAL,"
                +COL_INFO_GENDER + "TEXT,"
                +COL_INFO_ACTIVITY_LEVEL + "INTEGER" + "CHECK activity_level <6 && > 0" + ")";

        String CREATE_TABLE_ROUTINE = TABLE_NAME_ROUTINE
                + "(" + COL_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," + COL_DAY + "TEXT,"
                + COL_EXERCISE_ID + "INTEGER" +
                "FOREIGN KEY(" + COL_EXERCISE_ID + ") REFERENCES " + TABLE_NAME_EXERCISES + "(" + COL_EXERCISE_ID + "))";

        String CREATE_TABLE_PICTURE = TABLE_NAME_PICTURES
                + "(" + COL_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_DIET_DATE + " DATE,"
                + COL_PICTURES_IMAGE + " BLOB" + ")";

        //Execute/run the create SQL statement
        db.execSQL(CREATE_TABLE_DIET);
        db.execSQL(CREATE_TABLE_EXERCISES);
        db.execSQL(CREATE_TABLE_INFO);
        db.execSQL(CREATE_TABLE_ROUTINE);
        db.execSQL(CREATE_TABLE_PICTURE);

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ROUTINE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PICTURES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_INFO);
        onCreate(db);
        db.close();
    }
    
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
            DietEntry returnValue = new DietEntry(
                    cursor.getLong(idDate),
                    cursor.getInt(idCalories),
                    cursor.getInt(idCaloriesGoal),
                    cursor.getFloat(idProtein),
                    cursor.getFloat(idProteinGoal)
            );
            db.close();
            return returnValue;
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

    //--------------Accessor methods for exercises table------------------------------------------//

    /**Gets an excercise by it's name.
     *
     * @param name The name of the excercise to get.
     * @return An excercise entry object.
     */
    public ExerciseEntry getExcerciseByName(String name)
    {
        // Connect to the database to read data
        SQLiteDatabase db = this.getReadableDatabase();
        // Generate SQL SELECT statement
        String selectQuery = "SELECT * FROM " + TABLE_NAME_EXERCISES + " WHERE " + COL_NAME + " = " + name;

        // Execute select statement
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) { // If data (records) available
            int idName = cursor.getColumnIndex(COL_NAME);
            int idDescription = cursor.getColumnIndex(COL_DESCRIPTION);
            db.close();
            ExerciseEntry returnValue = new ExerciseEntry(
                    cursor.getString(idName),
                    cursor.getString(idDescription)
            );
            db.close();
            return returnValue;
        }
        return null;
    }

    /**Gets an excercise by id.
     * Useful for the routine stuff, as routine references an excercise ID
     *
     * @param id The id to get.
     * @return An excercise entry object.
     */
    public ExerciseEntry getExcerciseByID(int id)
    {
        // Connect to the database to read data
        SQLiteDatabase db = this.getReadableDatabase();
        // Generate SQL SELECT statement
        String selectQuery = "SELECT * FROM " + TABLE_NAME_EXERCISES + " WHERE " + COL_ID + " = " + id;

        // Execute select statement
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) { // If data (records) available
            int idName = cursor.getColumnIndex(COL_NAME);
            int idDescription = cursor.getColumnIndex(COL_DESCRIPTION);
            ExerciseEntry returnValue = new ExerciseEntry(
                    cursor.getString(idName),
                    cursor.getString(idDescription)
            );
            db.close();
            return returnValue;
        }
        return null;
    }

    /**Gets all excercises from the excercise table.
     *
     * @return An arraylist of ExerciseEntry containing all entries.
     */
    public ArrayList<ExerciseEntry> getAllExercises()
    {
        // Create empty list
        ArrayList<ExerciseEntry> list = new ArrayList<ExerciseEntry>();
        // Connect to the database to read data
        SQLiteDatabase db = this.getReadableDatabase();
        // Generate SQL SELECT statement
        String selectQuery = "SELECT * FROM " + TABLE_NAME_EXERCISES;

        // Execute select statement
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) { // If data (records) available
            int idName = cursor.getColumnIndex(COL_NAME);
            int idDescription = cursor.getColumnIndex(COL_DESCRIPTION);
            do {
                //Str,str,long,double,double,bitmap
                list.add(new ExerciseEntry(
                        cursor.getString(idName),
                        cursor.getString(idDescription)
                ));
            } while (cursor.moveToNext()); // repeat until there are no more records
        }
        db.close();
        return list;
    }

    /**Adds an excercise entry using a name and description.
     *
     * @param name The name for the excercise entry.
     * @param description The description for the entry.
     * @return The id of the entry.
     */
    public long addExerciseEntry(String name, String description)
    {
        // Open database connection (for write)
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME , name);
        values.put(COL_DESCRIPTION, description);

        // Add record to database and get id of new record (must long integer).
        long id = db.insert(TABLE_NAME_EXERCISES, null, values);
        db.close(); // Closing database connection
        return id; // Return id for new record
    }

    /**Adds an excercise entry using a excercise entry object.
     *
     * @param exerciseEntry The exercise object to add.
     * @return The id of the entry.
     */
    public long addExerciseEntry(ExerciseEntry exerciseEntry)
    {
        // Open database connection (for write)
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME , exerciseEntry.getName());
        values.put(COL_DESCRIPTION, exerciseEntry.getDescription());

        // Add record to database and get id of new record (must long integer).
        long id = db.insert(TABLE_NAME_EXERCISES, null, values);
        db.close(); // Closing database connection
        return id; // Return id for new record
    }

    //--------------------------Progress pictures table methods-----------------------------------//

    /**Converts image as read from database to a bitmap.
     *
     * @param imgBytes The image in byte form (As read using cursor.getBlob(columnForImage))
     * @return The image as a bitmap
     */
    public static Bitmap imageBytesToBitmap(byte[] imgBytes)
    {
        if (imgBytes != null)
        {
            return BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
        }
        return null;
    }

    /**Converts a bitmap to a form which can be inserted into the database.
     *
     * @param imgBmp The bitmap to convert
     * @return The image in byteform for storing in db
     */
    public static byte[] imageBitmapToBytes(Bitmap imgBmp)
    {
        if (imgBmp != null)
        {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            imgBmp.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            return outputStream.toByteArray();
        }
        return null;
    }

    /**Adds a progress picture entry to the database.
     *
     * @param dateIn The date to put in the date column.
     * @param imageIn The image to put in the image column (In bitmap form).
     * @return Returns the id of the record.
     */
    public long addPictureEntry(long dateIn, Bitmap imageIn)
    {
        // Open database connection (for write)
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DIET_DATE , dateIn);
        values.put(COL_PICTURES_IMAGE, imageBitmapToBytes(imageIn));

        // Add record to database and get id of new record (must long integer).
        long id = db.insert(TABLE_NAME_PICTURES, null, values);
        db.close(); // Closing database connection
        return id; // Return id for new record
    }

    /**Gets all pictures in the progress pictures table.
     *
     * @return A list of all picture entries.
     */
    public ArrayList<PictureEntry> getAllPictures()
    {
        // Create empty list
        ArrayList<PictureEntry> list = new ArrayList<PictureEntry>();
        // Connect to the database to read data
        SQLiteDatabase db = this.getReadableDatabase();
        // Generate SQL SELECT statement
        String selectQuery = "SELECT * FROM " + TABLE_NAME_PICTURES;

        // Execute select statement
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) { // If data (records) available
            int idDate = cursor.getColumnIndex(COL_DIET_DATE);
            int idPicture = cursor.getColumnIndex(COL_PICTURES_IMAGE);
            do {
                //Str,str,long,double,double,bitmap
                list.add(new PictureEntry(
                        cursor.getLong(idDate),
                        imageBytesToBitmap(cursor.getBlob(idPicture))
                ));
            } while (cursor.moveToNext()); // repeat until there are no more records
        }
        db.close();
        return list;
    }

    /**Gets all pictures for a date
     *
     * @param dateIn The date to get pictures for. Ensure date doesn't include time.
     * @return A list of all pictures for the day.
     */
    public ArrayList<PictureEntry> getPicturesForDate(long dateIn)
    {
        // Create empty list
        ArrayList<PictureEntry> list = new ArrayList<PictureEntry>();
        // Connect to the database to read data
        SQLiteDatabase db = this.getReadableDatabase();
        // Generate SQL SELECT statement
        String selectQuery = "SELECT * FROM " + TABLE_NAME_PICTURES + " WHERE " + COL_DIET_DATE + " = " + dateIn;

        // Execute select statement
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) { // If data (records) available
            int idDate = cursor.getColumnIndex(COL_DIET_DATE);
            int idPicture = cursor.getColumnIndex(COL_PICTURES_IMAGE);
            do {
                //Str,str,long,double,double,bitmap
                list.add(new PictureEntry(
                        cursor.getLong(idDate),
                        imageBytesToBitmap(cursor.getBlob(idPicture))
                ));
            } while (cursor.moveToNext()); // repeat until there are no more records
        }
        db.close();
        return list;
    }

    //--------------------Info accessor methods---------------------------------------------------//

    /**Adds an info object to the database.
     *
     * @param name The users name
     * @param height The users height
     * @param age The users age
     * @param weight The users weight
     * @param gender The users gender
     * @param activityLevel The users activity level
     * @param date The date on which this got pushed to the db
     * @return The id of the entry
     */
    public long addInfo(String name,float height, int age, float weight, String gender , int activityLevel, long date)
    {
        // Open database connection (for write)
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_INFO_NAME , name);
        values.put(COL_INFO_HEIGHT, height);
        values.put(COL_INFO_AGE, age);
        values.put(COL_INFO_WEIGHT, weight);
        values.put(COL_INFO_GENDER, gender);
        values.put(COL_INFO_ACTIVITY_LEVEL, activityLevel);
        values.put(COL_DIET_DATE, date);

        // Add record to database and get id of new record (must long integer).
        long id = db.insert(TABLE_NAME_INFO, null, values);
        db.close(); // Closing database connection
        return id; // Return id for new record
    }

    /**Adds an info object to the database, from an infoentry object.
     *
     * @param info The infoentry object to add.
     * @return The id of the database record.
     */
    public long addInfo(InfoEntry info)
    {
        // Open database connection (for write)
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_INFO_NAME , info.getName());
        values.put(COL_INFO_AGE, info.getAge());
        values.put(COL_INFO_HEIGHT, info.getHeight());
        values.put(COL_INFO_WEIGHT, info.getWeight());
        values.put(COL_INFO_ACTIVITY_LEVEL, info.getActivityLevel());
        values.put(COL_INFO_GENDER, info.getGender());
        values.put(COL_DIET_DATE, info.getDate());

        // Add record to database and get id of new record (must long integer).
        long id = db.insert(TABLE_NAME_INFO, null, values);
        db.close(); // Closing database connection
        return id; // Return id for new record
    }

    /**Get the info entry for that day.
     *
     * @param date The date to get info for.
     * @return The infoentry object.
     */
    public InfoEntry getInfoEntry(long date)
    {
        // Connect to the database to read data
        SQLiteDatabase db = this.getReadableDatabase();
        // Generate SQL SELECT statement
        String selectQuery = "SELECT * FROM " + TABLE_NAME_INFO + " WHERE " + COL_DIET_DATE + " = " + date;

        // Execute select statement
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) { // If data (records) available
            int idName = cursor.getColumnIndex(COL_INFO_NAME);
            int idAge = cursor.getColumnIndex(COL_INFO_AGE);
            int idHeight = cursor.getColumnIndex(COL_INFO_HEIGHT);
            int idWeight = cursor.getColumnIndex(COL_INFO_WEIGHT);
            int idGender = cursor.getColumnIndex(COL_INFO_GENDER);
            int idActivity = cursor.getColumnIndex(COL_INFO_ACTIVITY_LEVEL);
            int idDate = cursor.getColumnIndex(COL_DIET_DATE);
            InfoEntry returnValue = new InfoEntry(
                    cursor.getInt(idAge),
                    cursor.getFloat(idHeight),
                    cursor.getFloat(idWeight),
                    cursor.getString(idName),
                    cursor.getString(idGender),
                    cursor.getInt(idActivity),
                    cursor.getLong(idDate)
            );
            db.close();
            return returnValue;
        }
        return null;
    }

    public long addRoutine(String day, int exerciseId)
    {
        // Open database connection (for write)
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DAY , day);
        values.put(COL_EXERCISE_ID, exerciseId);

        // Add record to database and get id of new record (must long integer).
        long id = db.insert(TABLE_NAME_ROUTINE, null, values);
        db.close(); // Closing database connection
        return id; // Return id for new record
    }

    public long addRoutine(RoutineEntry routineEntry)
    {
        // Open database connection (for write)
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_INFO_NAME , routineEntry.getDay());
        values.put(COL_INFO_AGE, routineEntry.getExerciseId());

        // Add record to database and get id of new record (must long integer).
        long id = db.insert(TABLE_NAME_ROUTINE, null, values);
        db.close(); // Closing database connection
        return id; // Return id for new record
    }

    public RoutineEntry getRoutineEntry()
    {

        // Connect to the database to read data
        SQLiteDatabase db = this.getReadableDatabase();

        // Generate SQL SELECT statement
        String selectQuery = "SELECT * FROM " + TABLE_NAME_ROUTINE;

        // Execute select statement
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) { // If data (records) available
            int idDay = cursor.getColumnIndex(COL_DAY);
            int idExerciseId = cursor.getColumnIndex(COL_EXERCISE_ID);
            db.close();
            return new RoutineEntry(
                    cursor.getString(idDay),
                    cursor.getInt(idExerciseId)
            );


        }
        return null;
    }



}