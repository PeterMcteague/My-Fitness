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
        dh.addInfo(
                intent.getStringExtra("name"),
                intent.getFloatExtra("height",0),
                intent.getIntExtra("age",0),
                intent.getFloatExtra("weight",0),
                intent.getStringExtra("gender"),
                intent.getIntExtra("activityLevel",0),
                goal,
                c.getTimeInMillis());
        //Add diet and routine
        dh.addDietEntryToday();
        if (goal == "Muscle")
        {
            dh.addRoutine("Monday",dh.getExerciseIDFromName("Bench Press"));
            dh.addRoutine("Monday",dh.getExerciseIDFromName("Dumbbell Bench Press"));
            dh.addRoutine("Monday",dh.getExerciseIDFromName("Pushups"));
            dh.addRoutine("Monday",dh.getExerciseIDFromName("Dumbbell Flyes"));
            dh.addRoutine("Monday",dh.getExerciseIDFromName("3/4 Sit-Up"));
            dh.addRoutine("Monday",dh.getExerciseIDFromName("Alternate Heel Touchers"));
            dh.addRoutine("Wednesday",dh.getExerciseIDFromName("Deadlift"));
            dh.addRoutine("Wednesday",dh.getExerciseIDFromName("Seated Cable Rows"));
            dh.addRoutine("Wednesday",dh.getExerciseIDFromName("T-Bar Row with Handle"));
            dh.addRoutine("Wednesday",dh.getExerciseIDFromName("Shotgun Row"));
            dh.addRoutine("Wednesday",dh.getExerciseIDFromName("Close-Grip Front Lat Pulldown"));
            dh.addRoutine("Friday",dh.getExerciseIDFromName("Triceps Dips"));
            dh.addRoutine("Friday",dh.getExerciseIDFromName("Decline EZ Bar Triceps Extension"));
            dh.addRoutine("Friday",dh.getExerciseIDFromName("Reverse Grip Triceps Pushdown"));
            dh.addRoutine("Friday",dh.getExerciseIDFromName("Barbell Curl"));
            dh.addRoutine("Friday",dh.getExerciseIDFromName("Hammer Curls"));
            dh.addRoutine("Friday",dh.getExerciseIDFromName("Dumbbell Bicep Curl"));
            dh.addRoutine("Saturday",dh.getExerciseIDFromName("Side Laterals to Front Raise"));
            dh.addRoutine("Saturday",dh.getExerciseIDFromName("One-Arm Side Laterals"));
            dh.addRoutine("Saturday",dh.getExerciseIDFromName("Front Dumbbell Raise"));
            dh.addRoutine("Saturday",dh.getExerciseIDFromName("Barbell Full Squat"));
            dh.addRoutine("Saturday",dh.getExerciseIDFromName("Bodyweight Lunge"));
            dh.addRoutine("Saturday",dh.getExerciseIDFromName("Seated Calf Raise"));

        }
        else if (goal == "WeightLoss")
        {
            dh.addRoutine("Monday",dh.getExerciseIDFromName("Bicycling"));
            dh.addRoutine("Thursday",dh.getExerciseIDFromName("Treadmill"));
            dh.addRoutine("Saturday",dh.getExerciseIDFromName("Bicycling"));
        }
        else
        {
            dh.addRoutine("Saturday",dh.getExerciseIDFromName("Bicycling"));
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
