package uk.ac.tees.gingerbread.myfitness.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import uk.ac.tees.gingerbread.myfitness.R;

public class SplashGoal extends AppCompatActivity {

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
                        Intent intent = new Intent(context, SplashRoutineGeneration.class);
                        startActivity(intent);
                    }
                }

        );
        buttonFat.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Put routine and/or diet plan in table
                        Intent intent = new Intent(context, SplashRoutineGeneration.class);
                        startActivity(intent);
                    }
                }

        );
        buttonHealth.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Put routine and/or diet plan in table
                        Intent intent = new Intent(context, SplashRoutineGeneration.class);
                        startActivity(intent);
                    }
                }

        );
    }
}
