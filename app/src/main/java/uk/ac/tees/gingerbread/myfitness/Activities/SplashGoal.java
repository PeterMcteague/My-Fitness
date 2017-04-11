package uk.ac.tees.gingerbread.myfitness.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Calendar;

import uk.ac.tees.gingerbread.myfitness.Models.ExerciseEntry;
import uk.ac.tees.gingerbread.myfitness.Models.InfoEntry;
import uk.ac.tees.gingerbread.myfitness.Models.RoutineEntry;
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

        Log.d("Monday",monday + "");
        Log.d("Wednesday",wednesday + "");
        Log.d("Thursday",thursday + "");
        Log.d("Friday",friday + "");
        Log.d("Saturday",saturday + "");

        //Create routines and add excercises
        if (goal == "Muscle")
        {
            dh.addRoutineEntry(new RoutineEntry(monday,new ArrayList<ExerciseEntry>()));
            dh.addRoutineEntry(new RoutineEntry(wednesday,new ArrayList<ExerciseEntry>()));
            dh.addRoutineEntry(new RoutineEntry(friday,new ArrayList<ExerciseEntry>()));
            dh.addRoutineEntry(new RoutineEntry(saturday,new ArrayList<ExerciseEntry>()));

            dh.addExcerciseToRoutine(dh.getRoutineEntry(monday),dh.getExerciseIDFromName("Bench Press"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(monday),dh.getExerciseIDFromName("Dumbbell Bench Press"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(monday),dh.getExerciseIDFromName("Pushups"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(monday),dh.getExerciseIDFromName("Dumbbell Flyes"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(monday),dh.getExerciseIDFromName("3/4 Sit-Up"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(monday),dh.getExerciseIDFromName("Alternate Heel Touchers"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(wednesday),dh.getExerciseIDFromName("Deadlift"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(wednesday),dh.getExerciseIDFromName("Seated Cable Rows"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(wednesday),dh.getExerciseIDFromName("T-Bar Row with Handle"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(wednesday),dh.getExerciseIDFromName("Shotgun Row"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(wednesday),dh.getExerciseIDFromName("Close-Grip Front Lat Pulldown"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(friday),dh.getExerciseIDFromName("Triceps Dips"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(friday),dh.getExerciseIDFromName("Decline EZ Bar Triceps Extension"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(friday),dh.getExerciseIDFromName("Reverse Grip Triceps Pushdown"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(friday),dh.getExerciseIDFromName("Barbell Curl"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(friday),dh.getExerciseIDFromName("Hammer Curls"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(friday),dh.getExerciseIDFromName("Dumbbell Bicep Curl"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(friday),dh.getExerciseIDFromName("Side Laterals to Front Raise"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(saturday),dh.getExerciseIDFromName("One-Arm Side Laterals"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(saturday),dh.getExerciseIDFromName("Front Dumbbell Raise"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(saturday),dh.getExerciseIDFromName("Barbell Full Squat"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(saturday),dh.getExerciseIDFromName("Bodyweight Lunge"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(saturday),dh.getExerciseIDFromName("Seated Calf Raise"));
        }
        else if (goal == "WeightLoss")
        {
            dh.addRoutineEntry(new RoutineEntry(monday,new ArrayList<ExerciseEntry>()));
            dh.addRoutineEntry(new RoutineEntry(thursday,new ArrayList<ExerciseEntry>()));
            dh.addRoutineEntry(new RoutineEntry(saturday,new ArrayList<ExerciseEntry>()));

            dh.addExcerciseToRoutine(dh.getRoutineEntry(monday),dh.getExerciseIDFromName("Bicycling"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(thursday),dh.getExerciseIDFromName("Treadmill"));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(saturday),dh.getExerciseIDFromName("Bicycling"));
        }
        else
        {
            dh.addRoutineEntry(new RoutineEntry(saturday,new ArrayList<ExerciseEntry>()));
            dh.addExcerciseToRoutine(dh.getRoutineEntry(saturday),dh.getExerciseIDFromName("Bicycling"));
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
