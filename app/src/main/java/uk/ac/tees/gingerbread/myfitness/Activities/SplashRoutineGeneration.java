package uk.ac.tees.gingerbread.myfitness.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import uk.ac.tees.gingerbread.myfitness.R;
import uk.ac.tees.gingerbread.myfitness.Services.DatabaseHandler;

public class SplashRoutineGeneration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_routine_generation);


        // Get context and button objects
        final Context context = this;
        Button routine_yes = (Button) findViewById(R.id.splash_routine_generation_button_auto_generate);
        Button routine_no = (Button) findViewById(R.id.splash_routine_generation_button_customize);

        routine_yes.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SplashGoal.class);
                        //Create the database..
                        DatabaseHandler dh = new DatabaseHandler(context);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    }
                }

        );
        routine_no.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Set user has finished setup, here
                        SharedPreferences settings = getSharedPreferences("MyFitnessPrefs",0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("setupComplete",true);
                        editor.apply();
                        //Finished
                        Intent intent = new Intent(context, MenuScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                }

        );
    }
}
