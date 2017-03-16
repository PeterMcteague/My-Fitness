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

import uk.ac.tees.gingerbread.myfitness.DatabaseHandling.DatabaseHandler;
import uk.ac.tees.gingerbread.myfitness.R;

public class SplashGoal extends AppCompatActivity {

    private void setSetupComplete()
    {
        //Set user has finished setup, here
        SharedPreferences settings = getSharedPreferences("MyFitnessPrefs",0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("setupComplete",true);
        editor.commit();
    }

    private void addDbFromIntent(Context context, Intent intent)
    {
        //Setting up date getting
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        //Adding db entry
        DatabaseHandler dh = new DatabaseHandler(context);
        dh.addInfo(
                intent.getStringExtra("name"),
                intent.getFloatExtra("height",0),
                intent.getIntExtra("age",0),
                intent.getFloatExtra("weight",0),
                intent.getStringExtra("gender"),
                intent.getIntExtra("activityLevel",0),
                c.getTimeInMillis());
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

        buttonMuscle.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Put routine and/or diet plan in table
                        //addDbFromIntent(context,getIntent());
                        setSetupComplete();
                        Intent intent = new Intent(context, Home.class);
                        startActivity(intent);
                    }
                }

        );
        buttonFat.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Put routine and/or diet plan in table
                        //addDbFromIntent(context,getIntent());
                        setSetupComplete();
                        Intent intent = new Intent(context, Home.class);
                        startActivity(intent);
                    }
                }

        );
        buttonHealth.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Put routine and/or diet plan in table
                        //addDbFromIntent(context,getIntent());
                        setSetupComplete();
                        Intent intent = new Intent(context, Home.class);
                        startActivity(intent);
                    }
                }

        );
    }
}
