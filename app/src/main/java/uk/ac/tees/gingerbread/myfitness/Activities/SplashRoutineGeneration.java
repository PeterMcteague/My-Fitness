package uk.ac.tees.gingerbread.myfitness.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import uk.ac.tees.gingerbread.myfitness.R;

public class SplashRoutineGeneration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_routine_generation);


        // Get context and button objects
        final Context context = this;
        Button routine_auto = (Button) findViewById(R.id.splash_routine_generation_button_auto_generate);
        Button routine_custom = (Button) findViewById(R.id.splash_routine_generation_button_customize);

        routine_auto.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Put routine and/or diet plan in table
                        Intent intent = new Intent(context, Home.class);
                        startActivity(intent);
                    }
                }

        );
        routine_custom.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Put routine and/or diet plan in table
                        Intent intent = new Intent(context, Home.class);
                        startActivity(intent);
                    }
                }

        );
    }
}
