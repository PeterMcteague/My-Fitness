package uk.ac.tees.gingerbread.myfitness.Services;

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
import java.util.Calendar;

import uk.ac.tees.gingerbread.myfitness.Classes.DietEntry;
import uk.ac.tees.gingerbread.myfitness.Classes.ExerciseEntry;
import uk.ac.tees.gingerbread.myfitness.Classes.PictureEntry;
import uk.ac.tees.gingerbread.myfitness.Classes.InfoEntry;
import uk.ac.tees.gingerbread.myfitness.Classes.RoutineEntry;

/**A class for interacting with the sqlite database tables.*/
public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Name
    private static final String DATABASE_NAME = "Fitness.db";
    // Table names
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
    private static final String COL_INFO_AGE = "age" ;
    private static final String COL_INFO_WEIGHT = "weight";
    private static final String COL_INFO_GENDER = "gender";
    private static final String COL_INFO_ACTIVITY_LEVEL = "activity_level";
    private static final String COL_INFO_GOAL = "goal";

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
                + "("  + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_DIET_DATE + " DATE, "
                + COL_DIET_CAL + " INTEGER, "
                + COL_DIET_CAL_GOAL + " INTEGER, "
                + COL_DIET_PROTEIN + " REAL, "
                + COL_DIET_PROTEIN_GOAL + " REAL" + ")";

        String CREATE_TABLE_EXERCISES = "CREATE TABLE " +
                TABLE_NAME_EXERCISES
                + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL UNIQUE, "
                + COL_DESCRIPTION + " TEXT" + ")";

        String CREATE_TABLE_INFO = "CREATE TABLE " + TABLE_NAME_INFO +
                "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_INFO_NAME +  " TEXT, "
                + COL_DIET_DATE + " DATE NOT NULL UNIQUE, "
                + COL_INFO_HEIGHT + " REAL, "
                + COL_INFO_AGE + " INTEGER, "
                + COL_INFO_WEIGHT + " REAL, "
                + COL_INFO_GENDER + " TEXT, "
                + COL_INFO_ACTIVITY_LEVEL + " INTEGER CHECK (" + COL_INFO_ACTIVITY_LEVEL + "<=4 AND " + COL_INFO_ACTIVITY_LEVEL + "> 0),"
                + COL_INFO_GOAL + " STRING)";

        String CREATE_TABLE_ROUTINE = "CREATE TABLE " + TABLE_NAME_ROUTINE
                + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_DAY + " TEXT, "
                + COL_EXERCISE_ID + " INTEGER," +
                " FOREIGN KEY(" + COL_EXERCISE_ID + ") REFERENCES " + TABLE_NAME_EXERCISES + "(" + COL_EXERCISE_ID + "))";

        String CREATE_TABLE_PICTURE = "CREATE TABLE " + TABLE_NAME_PICTURES
                + "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DIET_DATE + " DATE, "
                + COL_PICTURES_IMAGE + " BLOB" + ")";

        //Execute/run the create SQL statement
        db.execSQL(CREATE_TABLE_DIET);
        db.execSQL(CREATE_TABLE_EXERCISES);
        db.execSQL(CREATE_TABLE_INFO);
        db.execSQL(CREATE_TABLE_ROUTINE);
        db.execSQL(CREATE_TABLE_PICTURE);

        Log.d("Database", "Database Created.");

        //Inserting default data into the exercise table
        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Bench press' , 'Lie back on a flat bench. Using a medium width grip (a grip that creates a 90-degree angle in the middle of the movement between the forearms and the upper arms), lift the bar from the rack and hold it straight over you with your arms locked. This will be your starting position.\n" +
                "    From the starting position, breathe in and begin coming down slowly until the bar touches your middle chest.\n" +
                "    After a brief pause, push the bar back to the starting position as you breathe out. Focus on pushing the bar using your chest muscles. Lock your arms and squeeze your chest in the contracted position at the top of the motion, hold for a second and then start coming down slowly again. Tip: Ideally, lowering the weight should take about twice as long as raising it.\n" +
                "    Repeat the movement for the prescribed amount of repetitions.\n" +
                "    When you are done, place the bar back in the rack.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Dumbbell Bench Press' , 'Lie down on a flat bench with a dumbbell in each hand resting on top of your thighs. The palms of your hands will be facing each other.\n" +
                "    Then, using your thighs to help raise the dumbbells up, lift the dumbbells one at a time so that you can hold them in front of you at shoulder width.\n" +
                "    Once at shoulder width, rotate your wrists forward so that the palms of your hands are facing away from you. The dumbbells should be just to the sides of your chest, with your upper arm and forearm creating a 90 degree angle. Be sure to maintain full control of the dumbbells at all times. This will be your starting position.\n" +
                "    Then, as you breathe out, use your chest to push the dumbbells up. Lock your arms at the top of the lift and squeeze your chest, hold for a second and then begin coming down slowly. Tip: Ideally, lowering the weight should take about twice as long as raising it.\n" +
                "    Repeat the movement for the prescribed amount of repetitions of your training program.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Pushups','Lie on the floor face down and place your hands about 36 inches apart while holding your torso up at arms length.\n" +
                "    Next, lower yourself downward until your chest almost touches the floor as you inhale.\n" +
                "    Now breathe out and press your upper body back up to the starting position while squeezing your chest.\n" +
                "    After a brief pause at the top contracted position, you can begin to lower yourself downward again for as many repetitions as needed.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Dumbbell Flyes','Lie down on a flat bench with a dumbbell on each hand resting on top of your thighs. The palms of your hand will be facing each other.\n" +
                "    Then using your thighs to help raise the dumbbells, lift the dumbbells one at a time so you can hold them in front of you at shoulder width with the palms of your hands facing each other. Raise the dumbbells up like you''re pressing them, but stop and hold just before you lock out. This will be your starting position.\n" +
                "    With a slight bend on your elbows in order to prevent stress at the biceps tendon, lower your arms out at both sides in a wide arc until you feel a stretch on your chest. Breathe in as you perform this portion of the movement. Tip: Keep in mind that throughout the movement, the arms should remain stationary; the movement should only occur at the shoulder joint.\n" +
                "    Return your arms back to the starting position as you squeeze your chest muscles and breathe out. Tip: Make sure to use the same arc of motion used to lower the weights.\n" +
                "    Hold for a second at the contracted position and repeat the movement for the prescribed amount of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Single-Arm Linear Jammer','Position a bar into a landmine or securely anchor it in a corner. Load the bar to an appropriate weight.\n" +
                "    Raise the bar from the floor, taking it to your shoulders with one or both hands. Adopt a wide stance. This will be your starting position.\n" +
                "    Perform the movement by extending the elbow, pressing the weight up. Move explosively, extending the hips and knees fully to produce maximal force.\n" +
                "    Return to the starting position.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Side Laterals to Front Raise','In a standing position, hold a pair of dumbbells at your side. This will be your starting position.\n" +
                "    Keeping your elbows slightly bent, raise the weights directly in front of you to shoulder height, avoiding any swinging or cheating.\n" +
                "    At the top of the exercise move the weights out in front of you, keeping your arms extended.\n" +
                "    Lower the weights with a controlled motion.\n" +
                "    On the next repetition, raise the weights in front of you to shoulder height before moving the weights laterally to your sides.\n" +
                "    Lower the weights to the starting position.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('One-Arm Side Laterals','Pick a dumbbell and place it in one of your hands. Your non lifting hand should be used to grab something steady such as an incline bench press. Lean towards your lifting arm and away from the hand that is gripping the incline bench as this will allow you to keep your balance.\n" +
                "    Stand with a straight torso and have the dumbbell by your side at armâ€™s length with the palm of the hand facing you. This will be your starting position.\n" +
                "    While maintaining the torso stationary (no swinging), lift the dumbbell to your side with a slight bend on the elbow and your hand slightly tilted forward as if pouring water in a glass. Continue to go up until you arm is parallel to the floor. Exhale as you execute this movement and pause for a second at the top.\n" +
                "    Lower the dumbbell back down slowly to the starting position as you inhale.\n" +
                "    Repeat for the recommended amount of repetitions.\n" +
                "    Switch arms and repeat the exercise.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Seated Dumbbell Press','Grab a couple of dumbbells and sit on a military press bench or a utility bench that has a back support on it as you place the dumbbells upright on top of your thighs.\n" +
                "    Clean the dumbbells up one at a time by using your thighs to bring the dumbbells up to shoulder height at each side.\n" +
                "    Rotate the wrists so that the palms of your hands are facing forward. This is your starting position.\n" +
                "    As you exhale, push the dumbbells up until they touch at the top.\n" +
                "    After a second pause, slowly come down back to the starting position as you inhale.\n" +
                "    Repeat for the recommended amount of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Front Dumbbell Raise','Pick a couple of dumbbells and stand with a straight torso and the dumbbells on front of your thighs at arms length with the palms of the hand facing your thighs. This will be your starting position.\n" +
                "    While maintaining the torso stationary (no swinging), lift the left dumbbell to the front with a slight bend on the elbow and the palms of the hands always facing down. Continue to go up until you arm is slightly above parallel to the floor. Exhale as you execute this portion of the movement and pause for a second at the top. Inhale after the second pause.\n" +
                "    Now lower the dumbbell back down slowly to the starting position as you simultaneously lift the right dumbbell.\n" +
                "    Continue alternating in this fashion until all of the recommended amount of repetitions have been performed for each arm.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Incline Hammer Curls','Seat yourself on an incline bench with a dumbbell in each hand. You should pressed firmly against he back with your feet together. Allow the dumbbells to hang straight down at your side, holding them with a neutral grip. This will be your starting position.\n" +
                "    Initiate the movement by flexing at the elbow, attempting to keep the upper arm stationary.\n" +
                "    Continue to the top of the movement and pause, then slowly return to the start position.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Wide-Grip Standing Barbell Curl','Stand up with your torso upright while holding a barbell at the wide outer handle. The palm of your hands should be facing forward. The elbows should be close to the torso. This will be your starting position.\n" +
                "    While holding the upper arms stationary, curl the weights forward while contracting the biceps as you breathe out. Tip: Only the forearms should move.\n" +
                "    Continue the movement until your biceps are fully contracted and the bar is at shoulder level. Hold the contracted position for a second and squeeze the biceps hard.\n" +
                "    Slowly begin to bring the bar back to starting position as your breathe in.\n" +
                "    Repeat for the recommended amount of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Hammer Curls','Stand up with your torso upright and a dumbbell on each hand being held at arms length. The elbows should be close to the torso.\n" +
                "    The palms of the hands should be facing your torso. This will be your starting position.\n" +
                "    Now, while holding your upper arm stationary, exhale and curl the weight forward while contracting the biceps. Continue to raise the weight until the biceps are fully contracted and the dumbbell is at shoulder level. Hold the contracted position for a brief moment as you squeeze the biceps. Tip: Focus on keeping the elbow stationary and only moving your forearm.\n" +
                "    After the brief pause, inhale and slowly begin the lower the dumbbells back down to the starting position.\n" +
                "    Repeat for the recommended amount of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Concentration Curls','Sit down on a flat bench with one dumbbell in front of you between your legs. Your legs should be spread with your knees bent and feet on the floor.\n" +
                "    Use your right arm to pick the dumbbell up. Place the back of your right upper arm on the top of your inner right thigh. Rotate the palm of your hand until it is facing forward away from your thigh. Tip: Your arm should be extended and the dumbbell should be above the floor. This will be your starting position.\n" +
                "    While holding the upper arm stationary, curl the weights forward while contracting the biceps as you breathe out. Only the forearms should move. Continue the movement until your biceps are fully contracted and the dumbbells are at shoulder level. Tip: At the top of the movement make sure that the little finger of your arm is higher than your thumb. This guarantees a good contraction. Hold the contracted position for a second as you squeeze the biceps.\n" +
                "    Slowly begin to bring the dumbbells back to starting position as your breathe in. Caution: Avoid swinging motions at any time.\n" +
                "    Repeat for the recommended amount of repetitions. Then repeat the movement with the left arm.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Barbell Curl','Stand up with your torso upright while holding a barbell at a shoulder-width grip. The palm of your hands should be facing forward and the elbows should be close to the torso. This will be your starting position.\n" +
                "    While holding the upper arms stationary, curl the weights forward while contracting the biceps as you breathe out. Tip: Only the forearms should move.\n" +
                "    Continue the movement until your biceps are fully contracted and the bar is at shoulder level. Hold the contracted position for a second and squeeze the biceps hard.\n" +
                "    Slowly begin to bring the bar back to starting position as your breathe in.\n" +
                "    Repeat for the recommended amount of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Dumbbell Bicep Curl','Stand up straight with a dumbbell in each hand at arm''s length. Keep your elbows close to your torso and rotate the palms of your hands until they are facing forward. This will be your starting position.\n" +
                "    Now, keeping the upper arms stationary, exhale and curl the weights while contracting your biceps. Continue to raise the weights until your biceps are fully contracted and the dumbbells are at shoulder level. Hold the contracted position for a brief pause as you squeeze your biceps.\n" +
                "    Then, inhale and slowly begin to lower the dumbbells back to the starting position.\n" +
                "    Repeat for the recommended amount of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('3/4 Sit-Up','Lie down on the floor and secure your feet. Your legs should be bent at the knees.\n" +
                "    Place your hands behind or to the side of your head. You will begin with your back on the ground. This will be your starting position.\n" +
                "    Flex your hips and spine to raise your torso toward your knees.\n" +
                "    At the top of the contraction your torso should be perpendicular to the ground. Reverse the motion, going only Â¾ of the way down.\n" +
                "    Repeat for the recommended amount of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Air Bike','Lie flat on the floor with your lower back pressed to the ground. For this exercise, you will need to put your hands beside your head. Be careful however to not strain with the neck as you perform it. Now lift your shoulders into the crunch position.\n" +
                "    Bring knees up to where they are perpendicular to the floor, with your lower legs parallel to the floor. This will be your starting position.\n" +
                "    Now simultaneously, slowly go through a cycle pedal motion kicking forward with the right leg and bringing in the knee of the left leg. Bring your right elbow close to your left knee by crunching to the side, as you breathe out.\n" +
                "    Go back to the initial position as you breathe in.\n" +
                "    Crunch to the opposite side as you cycle your legs and bring closer your left elbow to your right knee and exhale.\n" +
                "    Continue alternating in this manner until all of the recommended repetitions for each side have been completed.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Alternate Heel Touchers','Lie on the floor with the knees bent and the feet on the floor around 18-24 inches apart. Your arms should be extended by your side. This will be your starting position.\n" +
                "    Crunch over your torso forward and up about 3-4 inches to the right side and touch your right heel as you hold the contraction for a second. Exhale while performing this movement.\n" +
                "    Now go back slowly to the starting position as you inhale.\n" +
                "    Now crunch over your torso forward and up around 3-4 inches to the left side and touch your left heel as you hold the contraction for a second. Exhale while performing this movement and then go back to the starting position as you inhale. Now that both heels have been touched, that is considered 1 repetition.\n" +
                "    Continue alternating sides in this manner until all prescribed repetitions are done.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Barbell Side Bend','Stand up straight while holding a barbell placed on the back of your shoulders (slightly below the neck). Your feet should be shoulder width apart. This will be your starting position.\n" +
                "    While keeping your back straight and your head up, bend only at the waist to the right as far as possible. Breathe in as you bend to the side. Then hold for a second and come back up to the starting position as you exhale. Tip: Keep the rest of the body stationary.\n" +
                "    Now repeat the movement but bending to the left instead. Hold for a second and come back to the starting position.\n" +
                "    Repeat for the recommended amount of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Bottoms Up','Begin by lying on your back on the ground. Your legs should be straight and your arms at your side. This will be your starting position.\n" +
                "    To perform the movement, tuck the knees toward your chest by flexing the hips and knees. Following this, extend your legs directly above you so that they are perpendicular to the ground. Rotate and elevate your pelvis to raise your glutes from the floor.\n" +
                "    After a brief pause, return to the starting position. ')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Barbell Full Squat','This exercise is best performed inside a squat rack for safety purposes. To begin, first set the bar on a rack just above shoulder level. Once the correct height is chosen and the bar is loaded, step under the bar and place the back of your shoulders (slightly below the neck) across it.\n" +
                "    Hold on to the bar using both arms at each side and lift it off the rack by first pushing with your legs and at the same time straightening your torso.\n" +
                "    Step away from the rack and position your legs using a shoulder-width medium stance with the toes slightly pointed out. Keep your head up at all times and maintain a straight back. This will be your starting position.\n" +
                "    Begin to slowly lower the bar by bending the knees and sitting back with your hips as you maintain a straight posture with the head up. Continue down until your hamstrings are on your calves. Inhale as you perform this portion of the movement.\n" +
                "    Begin to raise the bar as you exhale by pushing the floor with the heel or middle of your foot as you straighten the legs and extend the hips to go back to the starting position.\n" +
                "    Repeat for the recommended amount of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Rope Jumping','Hold an end of the rope in each hand. Position the rope behind you on the ground. Raise your arms up and turn the rope over your head bringing it down in front of you. When it reaches the ground, jump over it. Find a good turning pace that can be maintained. Different speeds and techniques can be used to introduce variation.\n" +
                "    Rope jumping is exciting, challenges your coordination, and requires a lot of energy. A 150 lb person will burn about 350 calories jumping rope for 30 minutes, compared to over 450 calories running. ')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Barbell Walking Lunge','Begin standing with your feet shoulder width apart and a barbell across your upper back.\n" +
                "    Step forward with one leg, flexing the knees to drop your hips. Descend until your rear knee nearly touches the ground. Your posture should remain upright, and your front knee should stay above the front foot.\n" +
                "    Drive through the heel of your lead foot and extend both knees to raise yourself back up.\n" +
                "    Step forward with your rear foot, repeating the lunge on the opposite leg.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Bodyweight Lunge','Stand erect with your feet hip-width apart, chest out, and shoulders back, maintaining the natural curvature of your spine. Your knees should be unlocked and your hand on your hips. This is your starting position.\n" +
                "    Take a moderate-length step forward with one foot, descending to a point in which your rear knee approaches the floor without touching, maintaining your body''s upright posture. Your front knee should bend about 90 degrees, but for knee health it should not be forward of the vertical plane that extends straight up from your toes. If so, take a slightly longer step.\n" +
                "    From the bottom position, push back up from your forward foot, bringing it back beside the other.\n" +
                "    Repeat on the opposite side for the required number of repetitions. ')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Bicycling','To begin, seat yourself on the bike and adjust the seat to your height.\n" +
                "    Wear protective gear to avoid possible injury. Ensure that you are courteous to vehicles and pedestrians, and that you obey the rules of the road. Cycling offers convenience, cardiovascular benefits, and has less impact than other activities. A 150 lb person will burn about 280 calories cycling at a moderate rate for 30 minutes, compared to 450 calories or more running.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Standing Calf Raises','Adjust the padded lever of the calf raise machine to fit your height.\n" +
                "    Place your shoulders under the pads provided and position your toes facing forward (or using any of the two other positions described at the beginning of the chapter). The balls of your feet should be secured on top of the calf block with the heels extending off it. Push the lever up by extending your hips and knees until your torso is standing erect. The knees should be kept with a slight bend; never locked. Toes should be facing forward, outwards or inwards as described at the beginning of the chapter. This will be your starting position.\n" +
                "    Raise your heels as you breathe out by extending your ankles as high as possible and flexing your calf. Ensure that the knee is kept stationary at all times. There should be no bending at any time. Hold the contracted position by a second before you start to go back down.\n" +
                "    Go back slowly to the starting position as you breathe in by lowering your heels as you bend the ankles until calves are stretched.\n" +
                "    Repeat for the recommended amount of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Seated Calf Raise','Sit on the machine and place your toes on the lower portion of the platform provided with the heels extending off. Choose the toe positioning of your choice (forward, in, or out) as per the beginning of this chapter.\n" +
                "    Place your lower thighs under the lever pad, which will need to be adjusted according to the height of your thighs. Now place your hands on top of the lever pad in order to prevent it from slipping forward.\n" +
                "    Lift the lever slightly by pushing your heels up and release the safety bar. This will be your starting position.\n" +
                "    Slowly lower your heels by bending at the ankles until the calves are fully stretched. Inhale as you perform this movement.\n" +
                "    Raise the heels by extending the ankles as high as possible as you contract the calves and breathe out. Hold the top contraction for a second.\n" +
                "    Repeat for the recommended amount of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Triceps Dips','To get into the starting position, hold your body at arm''s length with your arms nearly locked above the bars.\n" +
                "    Now, inhale and slowly lower yourself downward. Your torso should remain upright and your elbows should stay close to your body. This helps to better focus on tricep involvement. Lower yourself until there is a 90 degree angle formed between the upper arm and forearm.\n" +
                "    Then, exhale and push your torso back up using your triceps to bring your body back to the starting position.\n" +
                "    Repeat the movement for the prescribed amount of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Decline EZ Bar Triceps Extension','Secure your legs at the end of the decline bench and slowly lay down on the bench.\n" +
                "    Using a close grip (a grip that is slightly less than shoulder width), lift the EZ bar from the rack and hold it straight over you with your arms locked and elbows in. The arms should be perpendicular to the floor. This will be your starting position. Tip: In order to protect your rotator cuff, it is best if you have a spotter help you lift the barbell off the rack.\n" +
                "    As you breathe in and you keep the upper arms stationary, bring the bar down slowly by moving your forearms in a semicircular motion towards you until you feel the bar slightly touch your forehead. Breathe in as you perform this portion of the movement.\n" +
                "    Lift the bar back to the starting position by contracting the triceps and exhaling.\n" +
                "    Repeat until the recommended amount of repetitions is performed.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Close-Grip Barbell Bench Press','Lie back on a flat bench. Using a close grip (around shoulder width), lift the bar from the rack and hold it straight over you with your arms locked. This will be your starting position.\n" +
                "    As you breathe in, come down slowly until you feel the bar on your middle chest. Tip: Make sure that - as opposed to a regular bench press - you keep the elbows close to the torso at all times in order to maximize triceps involvement.\n" +
                "    After a second pause, bring the bar back to the starting position as you breathe out and push the bar using your triceps muscles. Lock your arms in the contracted position, hold for a second and then start coming down slowly again. Tip: It should take at least twice as long to go down than to come up.\n" +
                "    Repeat the movement for the prescribed amount of repetitions.\n" +
                "    When you are done, place the bar back in the rack.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Triceps Pushdown - V-Bar Attachment','Attach a V-Bar to a high pulley and grab with an overhand grip (palms facing down) at shoulder width.\n" +
                "    Standing upright with the torso straight and a very small inclination forward, bring the upper arms close to your body and perpendicular to the floor. The forearms should be pointing up towards the pulley as they hold the bar. The thumbs should be higher than the small finger. This is your starting position.\n" +
                "    Using the triceps, bring the bar down until it touches the front of your thighs and the arms are fully extended perpendicular to the floor. The upper arms should always remain stationary next to your torso and only the forearms should move. Exhale as you perform this movement.\n" +
                "    After a second hold at the contracted position, bring the V-Bar slowly up to the starting point. Breathe in as you perform this step.\n" +
                "    Repeat for the recommended amount of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Reverse Grip Triceps Pushdown','Start by setting a bar attachment (straight or e-z) on a high pulley machine.\n" +
                "    Facing the bar attachment, grab it with the palms facing up (supinated grip) at shoulder width. Lower the bar by using your lats until your arms are fully extended by your sides. Tip: Elbows should be in by your sides and your feet should be shoulder width apart from each other. This is the starting position.\n" +
                "    Slowly elevate the bar attachment up as you inhale so it is aligned with your chest. Only the forearms should move and the elbows/upper arms should be stationary by your side at all times.\n" +
                "    Then begin to lower the cable bar back down to the original staring position while exhaling and contracting the triceps hard.\n" +
                "    Repeat for the recommended amount of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Standing Dumbbell Triceps Extension','To begin, stand up with a dumbbell held by both hands. Your feet should be about shoulder width apart from each other. Slowly use both hands to grab the dumbbell and lift it over your head until both arms are fully extended.\n" +
                "    The resistance should be resting in the palms of your hands with your thumbs around it. The palm of the hands should be facing up towards the ceiling. This will be your starting position.\n" +
                "    Keeping your upper arms close to your head with elbows in and perpendicular to the floor, lower the resistance in a semicircular motion behind your head until your forearms touch your biceps. Tip: The upper arms should remain stationary and only the forearms should move. Breathe in as you perform this step.\n" +
                "    Go back to the starting position by using the triceps to raise the dumbbell. Breathe out as you perform this step.\n" +
                "    Repeat for the recommended amount of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('EZ-Bar Skullcrusher','Using a close grip, lift the EZ bar and hold it with your elbows in as you lie on the bench. Your arms should be perpendicular to the floor. This will be your starting position.\n" +
                "    Keeping the upper arms stationary, lower the bar by allowing the elbows to flex. Inhale as you perform this portion of the movement. Pause once the bar is directly above the forehead.\n" +
                "    Lift the bar back to the starting position by extending the elbow and exhaling.\n" +
                "    Repeat.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Pullups',' Grab the pull-up bar with the palms facing forward using the prescribed grip. Note on grips: For a wide grip, your hands need to be spaced out at a distance wider than your shoulder width. For a medium grip, your hands need to be spaced out at a distance equal to your shoulder width and for a close grip at a distance smaller than your shoulder width.\n" +
                "As you have both arms extended in front of you holding the bar at the chosen grip width, bring your torso back around 30 degrees or so while creating a curvature on your lower back and sticking your chest out. This is your starting position.\n" +
                "Pull your torso up until the bar touches your upper chest by drawing the shoulders and the upper arms down and back. Exhale as you perform this portion of the movement. Tip: Concentrate on squeezing the back muscles once you reach the full contracted position. The upper torso should remain stationary as it moves through space and only the arms should move. The forearms should do no other work other than hold the bar.\n" +
                "After a second on the contracted position, start to inhale and slowly lower your torso back to the starting position when your arms are fully extended and the lats are fully stretched.\n" +
                "Repeat this motion for the prescribed amount of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('V-Bar Pulldown','Sit down on a pull-down machine with a V-Bar attached to the top pulley.\n" +
                "    Adjust the knee pad of the machine to fit your height. These pads will prevent your body from being raised by the resistance attached to the bar.\n" +
                "    Grab the V-bar with the palms facing each other (a neutral grip). Stick your chest out and lean yourself back slightly (around 30-degrees) in order to better engage the lats. This will be your starting position.\n" +
                "    Using your lats, pull the bar down as you squeeze your shoulder blades. Continue until your chest nearly touches the V-bar. Exhale as you execute this motion. Tip: Keep the torso stationary throughout the movement.\n" +
                "    After a second hold on the contracted position, slowly bring the bar back to the starting position as you breathe in.\n" +
                "    Repeat for the prescribed number of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Shotgun Row','Attach a single handle to a low cable.\n" +
                "After selecting the correct weight, stand a couple feet back with a wide-split stance. Your arm should be extended and your shoulder forward. This will be your starting position.\n" +
                "Perform the movement by retracting the shoulder and flexing the elbow. As you pull, supinate the wrist, turning the palm upward as you go.\n" +
                "After a brief pause, return to the starting position.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Close-Grip Front Lat Pulldown','Sit down on a pull-down machine with a wide bar attached to the top pulley. Make sure that you adjust the knee pad of the machine to fit your height. These pads will prevent your body from being raised by the resistance attached to the bar.\n" +
                "    Grab the bar with the palms facing forward using the prescribed grip. Note on grips: For a wide grip, your hands need to be spaced out at a distance wider than your shoulder width. For a medium grip, your hands need to be spaced out at a distance equal to your shoulder width and for a close grip at a distance smaller than your shoulder width.\n" +
                "    As you have both arms extended in front of you - while holding the bar at the chosen grip width - bring your torso back around 30 degrees or so while creating a curvature on your lower back and sticking your chest out. This is your starting position.\n" +
                "    As you breathe out, bring the bar down until it touches your upper chest by drawing the shoulders and the upper arms down and back. Tip: Concentrate on squeezing the back muscles once you reach the full contracted position. The upper torso should remain stationary (only the arms should move). The forearms should do no other work except for holding the bar; therefore do not try to pull the bar down using the forearms.\n" +
                "    After a second in the contracted position, while squeezing your shoulder blades together, slowly raise the bar back to the starting position when your arms are fully extended and the lats are fully stretched. Inhale during this portion of the movement.\n" +
                "    6. Repeat this motion for the prescribed amount of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('T-Bar Row with Handle','Position a bar into a landmine or in a corner to keep it from moving. Load an appropriate weight onto your end.\n" +
                "    Stand over the bar, and position a Double D row handle around the bar next to the collar. Using your hips and legs, rise to a standing position.\n" +
                "    Assume a wide stance with your hips back and your chest up. Your arms should be extended. This will be your starting position.\n" +
                "    Pull the weight to your upper abdomen by retracting the shoulder blades and flexing the elbows. Do not jerk the weight or cheat during the movement.\n" +
                "    After a brief pause, return to the starting position.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('One-Arm Dumbbell Row','Choose a flat bench and place a dumbbell on each side of it.\n" +
                "    Place the right leg on top of the end of the bench, bend your torso forward from the waist until your upper body is parallel to the floor, and place your right hand on the other end of the bench for support.\n" +
                "    Use the left hand to pick up the dumbbell on the floor and hold the weight while keeping your lower back straight. The palm of the hand should be facing your torso. This will be your starting position.\n" +
                "    Pull the resistance straight up to the side of your chest, keeping your upper arm close to your side and keeping the torso stationary. Breathe out as you perform this step. Tip: Concentrate on squeezing the back muscles once you reach the full contracted position. Also, make sure that the force is performed with the back muscles and not the arms. Finally, the upper torso should remain stationary and only the arms should move. The forearms should do no other work except for holding the dumbbell; therefore do not try to pull the dumbbell up using the forearms.\n" +
                "    Lower the resistance straight down to the starting position. Breathe in as you perform this step.\n" +
                "    Repeat the movement for the specified amount of repetitions.\n" +
                "    Switch sides and repeat again with the other arm.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Seated Cable Rows','For this exercise you will need access to a low pulley row machine with a V-bar. Note: The V-bar will enable you to have a neutral grip where the palms of your hands face each other. To get into the starting position, first sit down on the machine and place your feet on the front platform or crossbar provided making sure that your knees are slightly bent and not locked.\n" +
                "    Lean over as you keep the natural alignment of your back and grab the V-bar handles.\n" +
                "    With your arms extended pull back until your torso is at a 90-degree angle from your legs. Your back should be slightly arched and your chest should be sticking out. You should be feeling a nice stretch on your lats as you hold the bar in front of you. This is the starting position of the exercise.\n" +
                "    Keeping the torso stationary, pull the handles back towards your torso while keeping the arms close to it until you touch the abdominals. Breathe out as you perform that movement. At that point you should be squeezing your back muscles hard. Hold that contraction for a second and slowly go back to the original position while breathing in.\n" +
                "    Repeat for the recommended amount of repetitions.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Deadlift','Approach the bar so that it is centered over your feet. Your feet should be about hip-width apart. Bend at the hip to grip the bar at shoulder-width allowing your shoulder blades to protract. Typically, you would use an alternating grip.\n" +
                "    With your feet and your grip set, take a big breath and then lower your hips and flex the knees until your shins contact the bar. Look forward with your head. Keep your chest up and your back arched, and begin driving through the heels to move the weight upward.\n" +
                "    After the bar passes the knees aggressively pull the bar back, pulling your shoulder blades together as you drive your hips forward into the bar.\n" +
                "    Lower the bar by bending at the hips and guiding it to the floor.')");

        db.execSQL("INSERT INTO " + TABLE_NAME_EXERCISES + " (" + COL_NAME  + "," + COL_DESCRIPTION + ") VALUES ('Treadmill','Run on a treadmill at a sensible speed for an amount of time you find appropriate.')");

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

    public long addDietEntryToday()
    {
        //Calendar setup
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        //Get info
        InfoEntry todaysInfo = getInfoEntry(c.getTimeInMillis());
        //Form calorie and protein goal based on info
        if (todaysInfo != null)
        {
            if (todaysInfo.getGoal() == "Muscle")
            {
                if (todaysInfo.getGender() == "Male")
                {
                    if (todaysInfo.getActivityLevel() == 0)
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.2 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() + 5) + 200),
                                0,
                                120);
                    }
                    else if (todaysInfo.getActivityLevel() == 1)
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.37 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() + 5) + 200),
                                0,
                                120);

                    }
                    else if (todaysInfo.getActivityLevel() == 2)
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.5 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() + 5) + 200),
                                0,
                                120);
                    }
                    else
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.7 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() + 5) + 200),
                                0,
                                120);
                    }

                }
                else
                {
                    if (todaysInfo.getActivityLevel() == 0)
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.2 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() - 161) + 200),
                                0,
                                120);
                    }
                    else if (todaysInfo.getActivityLevel() == 1)
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.37 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() - 161) + 200),
                                0,
                                120);

                    }
                    else if (todaysInfo.getActivityLevel() == 2)
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.5 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() - 161) + 200),
                                0,
                                120);
                    }
                    else
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.7 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() - 161) + 200),
                                0,
                                120);
                    }
                }
            }
            else if (todaysInfo.getGoal() == "WeightLoss")
            {
                if (todaysInfo.getGender() == "Male")
                {
                    if (todaysInfo.getActivityLevel() == 0)
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.2 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() + 5) - 500),
                                0,
                                0);
                    }
                    else if (todaysInfo.getActivityLevel() == 1)
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.37 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() + 5) - 500),
                                0,
                                0);

                    }
                    else if (todaysInfo.getActivityLevel() == 2)
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.5 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() + 5) - 500),
                                0,
                                0);
                    }
                    else
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.7 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() + 5) - 500),
                                0,
                                0);
                    }
                }
                else {
                    if (todaysInfo.getActivityLevel() == 0) {
                        return addDietEntryToday(
                                0,
                                (int) (1.2 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() - 161) - 500),
                                0,
                                0);
                    } else if (todaysInfo.getActivityLevel() == 1) {
                        return addDietEntryToday(
                                0,
                                (int) (1.37 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() - 161) - 500),
                                0,
                                0);

                    } else if (todaysInfo.getActivityLevel() == 2) {
                        return addDietEntryToday(
                                0,
                                (int) (1.5 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() - 161) - 500),
                                0,
                                0);
                    } else {
                        return addDietEntryToday(
                                0,
                                (int) (1.7 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() - 161) - 500),
                                0,
                                0);
                    }
                }
            }
            else if (todaysInfo.getGoal() == "Fitness")
            {
                if (todaysInfo.getGender() == "Male")
                {
                    if (todaysInfo.getActivityLevel() == 0)
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.2 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() + 5)),
                                0,
                                0);
                    }
                    else if (todaysInfo.getActivityLevel() == 1)
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.37 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() + 5)),
                                0,
                                0);

                    }
                    else if (todaysInfo.getActivityLevel() == 2)
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.5 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() + 5)),
                                0,
                                0);
                    }
                    else
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.7 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() + 5)),
                                0,
                                0);
                    }
                }
                else
                {
                    if (todaysInfo.getActivityLevel() == 0)
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.2 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() - 161)),
                                0,
                                0);
                    }
                    else if (todaysInfo.getActivityLevel() == 1)
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.37 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() - 161)),
                                0,
                                0);

                    }
                    else if (todaysInfo.getActivityLevel() == 2)
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.5 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() - 161)),
                                0,
                                0);
                    }
                    else
                    {
                        return addDietEntryToday(
                                0,
                                (int) (1.7 * Math.round(10 * todaysInfo.getWeight() + 6.25 * (todaysInfo.getHeight() * 100) - 5 * todaysInfo.getAge() - 161)),
                                0,
                                0);
                    }
                }
            }
            else
            {
                return addDietEntryToday(
                        0,
                        0,
                        0,
                        0);
            }
        }
        return addDietEntryToday(
                0,
                0,
                0,
                0);
    }

    public long addDietEntryToday(int calories, int caloriesGoal, float protein, float proteinGoal)
    {
        //Setting up date getting
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        // Open database connection (for write)
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DIET_DATE , c.getTimeInMillis());
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

    public DietEntry getDietEntryToday()
    {
        //Setting up date getting
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        // Connect to the database to read data
        SQLiteDatabase db = this.getReadableDatabase();
        // Generate SQL SELECT statement
        String selectQuery = "SELECT * FROM " + TABLE_NAME_DIET + " WHERE " + COL_DIET_DATE + " = " + c.getTimeInMillis();

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

    public void updateDietEntry(DietEntry dietIn , Long day)
    {
        // Connect to the database to read data
        SQLiteDatabase db = this.getReadableDatabase();
        // Generate SQL UPDATE statement to update cal, calgoal, protein, proteingoal
        String selectQuery = "UPDATE " + TABLE_NAME_DIET
                + " SET " + COL_DIET_CAL + " = " + dietIn.getCalories()
                + ", SET " + COL_DIET_CAL_GOAL + " = " + dietIn.getCaloriesGoal()
                + ", SET " + COL_DIET_PROTEIN + " = " + dietIn.getProtein()
                + ", SET " + COL_DIET_PROTEIN_GOAL + " = " + dietIn.getProteinGoal()
                + " WHERE " + COL_DIET_DATE + " = " + day;
        db.execSQL(selectQuery);
        db.close();
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

    /**Gets an exercise id from its name
     *
     * @param name The name of the exercise to get for
     * @return The id of the exercise
     */
    public int getExerciseIDFromName(String name)
    {
        // Connect to the database to read data
        SQLiteDatabase db = this.getReadableDatabase();
        // Generate SQL SELECT statement
        String selectQuery = "SELECT * FROM " + TABLE_NAME_EXERCISES + " WHERE " + COL_NAME + " = " + name;

        // Execute select statement
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) { // If data (records) available
            int idId = cursor.getColumnIndex(COL_ID);
            return cursor.getInt(idId);
        }
        return 0;
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
     * @param goal The goal for the user
     * @return The id of the entry
     */
    public long addInfo(String name,float height, int age, float weight, String gender , int activityLevel, String goal, long date)
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
        values.put(COL_INFO_GOAL, goal);
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
            int idGoal = cursor.getColumnIndex(COL_INFO_GOAL);
            InfoEntry returnValue = new InfoEntry(
                    cursor.getInt(idAge),
                    cursor.getFloat(idHeight),
                    cursor.getFloat(idWeight),
                    cursor.getString(idName),
                    cursor.getString(idGender),
                    cursor.getInt(idActivity),
                    cursor.getLong(idDate),
                    cursor.getString(idGoal)
            );
            db.close();
            return returnValue;
        }
        return null;
    }

    /**Adds a routine from day and exerciseID
     *
     * @param day The day for the exercise to be done on as part of a routine
     * @param exerciseId The id of the exercise
     * @return The id of the routine entry
     */
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

    /**A method to add a routine using a routine entry object.
     *
     * @param routineEntry The routine entry object to add.
     * @return The id of the added routine entry.
     */
    public long addRoutine(RoutineEntry routineEntry)
    {
        // Open database connection (for write)
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DAY , routineEntry.getDay());
        values.put(COL_EXERCISE_ID, routineEntry.getExerciseId());

        // Add record to database and get id of new record (must long integer).
        long id = db.insert(TABLE_NAME_ROUTINE, null, values);
        db.close(); // Closing database connection
        return id; // Return id for new record
    }

    /**Gets a list of exercise entries from a routine day.
     *
     * @param day The day to get for
     * @return The exercise entries for routine for that day.
     */
    public ArrayList<ExerciseEntry> getRoutineEntry(String day)
    {
        // Connect to the database to read data
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> list = new ArrayList<>();

        // Generate SQL SELECT statement
        String selectQuery = "SELECT * FROM " + TABLE_NAME_ROUTINE + " WHERE " + COL_DAY + " = " + day;

        // Execute select statement
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) { // If data (records) available
            int idExerciseID = cursor.getColumnIndex(COL_EXERCISE_ID);
            do {
                //Str,str,long,double,double,bitmap
                list.add(cursor.getInt(idExerciseID));
            } while (cursor.moveToNext()); // repeat until there are no more records
        }

        ArrayList<ExerciseEntry> toReturn = new ArrayList<>();

        for(int i=0; i<list.size(); i++)
        {
            //Now turn into exercise entries
            String selectQuery2 = "SELECT * FROM " + TABLE_NAME_EXERCISES + " WHERE " + COL_EXERCISE_ID + " = " + list.get(i);
            // Execute select statement
            cursor = db.rawQuery(selectQuery2, null);
            if (cursor.moveToFirst())
            {
                int idName = cursor.getColumnIndex(COL_NAME);
                int idDescription = cursor.getColumnIndex(COL_DESCRIPTION);

                toReturn.add(new ExerciseEntry(
                        cursor.getString(idName),
                        cursor.getString(idDescription)
                ));
            }
        }
        return toReturn;
    }



}