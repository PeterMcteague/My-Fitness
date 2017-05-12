package uk.ac.tees.gingerbread.myfitness.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import uk.ac.tees.gingerbread.myfitness.Services.DatabaseHandler;

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
        boolean setupComplete = settings.getBoolean("setupComplete",false);

        if(setupComplete)
        {
            Intent intent = new Intent(context, MenuScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        else
        {
            //Delete database if exists
            DatabaseHandler dh = new DatabaseHandler(context);
            dh.deleteDB(context);

            Intent intent = new Intent(context, SplashIntroduction.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);  // Launch next activity
            finish();
        }
    }
}
