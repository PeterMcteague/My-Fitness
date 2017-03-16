package uk.ac.tees.gingerbread.myfitness.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by 07mct on 14/03/2017.
 */

public class Start extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context context = this;

        //Get whether the setup has been completed before.
        SharedPreferences settings = getSharedPreferences("MyFitnessPrefs",0);
        //Just set this to false if you want to go to setup.
        boolean setupComplete = false;
        //boolean setupComplete = settings.getBoolean("setupComplete",false);

        if(setupComplete)//if running for first time
        {
            Intent intent = new Intent(context, Home.class);
            startActivity(intent);
            finish();
        }
        else
        {

            Intent intent = new Intent(context, SplashIntroduction.class);
            startActivity(intent);  // Launch next activity
            finish();
        }
    }
}
