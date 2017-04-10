package uk.ac.tees.gingerbread.myfitness.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;

import java.util.Calendar;

import uk.ac.tees.gingerbread.myfitness.Models.InfoEntry;
import uk.ac.tees.gingerbread.myfitness.Services.DatabaseHandler;
import uk.ac.tees.gingerbread.myfitness.R;

//Caloric intake taken from http://www.calculator.net/calorie-calculator.html
public class SplashGoal extends AppCompatActivity {

    private void setSetupComplete()
    {
        //Set user has finished setup, here
        SharedPreferences settings = getSharedPreferences("MyFitnessPrefs",0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("setupComplete",true);
        editor.commit();
    }

    //Activity: Sedentary * 1.2, Lightly active 1.37, Moderately 1.5, Very active 1.7
    private void addDbFromIntent(Context context, Intent intent, String goal)
    {
        //Setting up date getting
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        //Adding db entry for info
        DatabaseHandler dh = new DatabaseHandler(context);
        InfoEntry info = dh.getInfoEntry(c.getTimeInMillis());
        info.setGoal(goal);
        dh.updateInfoEntry(info);
        //Add diet and routine
        dh.addDietEntryToday();

        //Get long for Monday, Wednesday, Thursday, Friday, Saturday
        long currentDate = c.getTimeInMillis();

        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            c.add(Calendar.DATE, 1);
        }
        long monday = c.getTimeInMillis();

        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.WEDNESDAY) {
            c.add(Calendar.DATE, 1);
        }
        long wednesday = c.getTimeInMillis();

        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.THURSDAY) {
            c.add(Calendar.DATE, 1);
        }
        long thursday = c.getTimeInMillis();

        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY) {
            c.add(Calendar.DATE, 1);
        }
        long friday = c.getTimeInMillis();

        while (c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            c.add(Calendar.DATE, 1);
        }
        long saturday = c.getTimeInMillis();

        if (goal == "Muscle")
        {
            dh.addRoutineExercise(monday,dh.getExerciseIDFromName("Bench Press"));
            dh.addRoutineExercise(monday,dh.getExerciseIDFromName("Dumbbell Bench Press"));
            dh.addRoutineExercise(monday,dh.getExerciseIDFromName("Pushups"));
            dh.addRoutineExercise(monday,dh.getExerciseIDFromName("Dumbbell Flyes"));
            dh.addRoutineExercise(monday,dh.getExerciseIDFromName("3/4 Sit-Up"));
            dh.addRoutineExercise(monday,dh.getExerciseIDFromName("Alternate Heel Touchers"));
            dh.addRoutineExercise(wednesday,dh.getExerciseIDFromName("Deadlift"));
            dh.addRoutineExercise(wednesday,dh.getExerciseIDFromName("Seated Cable Rows"));
            dh.addRoutineExercise(wednesday,dh.getExerciseIDFromName("T-Bar Row with Handle"));
            dh.addRoutineExercise(wednesday,dh.getExerciseIDFromName("Shotgun Row"));
            dh.addRoutineExercise(wednesday,dh.getExerciseIDFromName("Close-Grip Front Lat Pulldown"));
            dh.addRoutineExercise(friday,dh.getExerciseIDFromName("Triceps Dips"));
            dh.addRoutineExercise(friday,dh.getExerciseIDFromName("Decline EZ Bar Triceps Extension"));
            dh.addRoutineExercise(friday,dh.getExerciseIDFromName("Reverse Grip Triceps Pushdown"));
            dh.addRoutineExercise(friday,dh.getExerciseIDFromName("Barbell Curl"));
            dh.addRoutineExercise(friday,dh.getExerciseIDFromName("Hammer Curls"));
            dh.addRoutineExercise(friday,dh.getExerciseIDFromName("Dumbbell Bicep Curl"));
            dh.addRoutineExercise(saturday,dh.getExerciseIDFromName("Side Laterals to Front Raise"));
            dh.addRoutineExercise(saturday,dh.getExerciseIDFromName("One-Arm Side Laterals"));
            dh.addRoutineExercise(saturday,dh.getExerciseIDFromName("Front Dumbbell Raise"));
            dh.addRoutineExercise(saturday,dh.getExerciseIDFromName("Barbell Full Squat"));
            dh.addRoutineExercise(saturday,dh.getExerciseIDFromName("Bodyweight Lunge"));
            dh.addRoutineExercise(saturday,dh.getExerciseIDFromName("Seated Calf Raise"));
        }
        else if (goal == "WeightLoss")
        {
            dh.addRoutineExercise(monday,dh.getExerciseIDFromName("Bicycling"));
            dh.addRoutineExercise(thursday,dh.getExerciseIDFromName("Treadmill"));
            dh.addRoutineExercise(saturday,dh.getExerciseIDFromName("Bicycling"));
        }
        else
        {
            dh.addRoutineExercise(saturday,dh.getExerciseIDFromName("Bicycling"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_goal);

        // Get context and button objects
        final Context context = this;
        ImageButton buttonMuscle = (ImageButton) findViewById(R.id.splash_button_muscle);
        ImageButton buttonFat = (ImageButton) findViewById(R.id.splash_button_fat);
        ImageButton buttonHealth = (ImageButton) findViewById(R.id.splash_button_health);

        final Intent intent = getIntent();

        buttonMuscle.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Put routine and/or diet plan in table
                        DatabaseHandler dh = new DatabaseHandler(context);
                        addDbFromIntent(context,getIntent(),"Muscle");
                        setSetupComplete();
                        Intent intent = new Intent(context, MenuScreen.class);
                        startActivity(intent);
                    }
                }

        );
        buttonFat.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Put routine and/or diet plan in table
                        addDbFromIntent(context,getIntent(),"WeightLoss");
                        setSetupComplete();
                        Intent intent = new Intent(context, MenuScreen.class);
                        startActivity(intent);
                    }
                }

        );
        buttonHealth.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Put routine and/or diet plan in table
                        addDbFromIntent(context,getIntent(),"Health");
                        setSetupComplete();
                        Intent intent = new Intent(context, MenuScreen.class);
                        startActivity(intent);
                    }
                }

        );
    }
}
