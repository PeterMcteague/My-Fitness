package uk.ac.tees.gingerbread.myfitness.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.gingerbread.myfitness.R;

public class SplashPersonal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_personal);

        //Populating spinner
        Spinner genderSpinner = (Spinner) findViewById(R.id.splash_personal_spinner);
        List<String> list = new ArrayList<>();
        list.add("Male");
        list.add("Female");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(dataAdapter);

        // Get context and button objects
        final Context context = this;
        Button button = (Button) findViewById(R.id.splash_personal_button);

        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SplashGoal.class);
                        startActivity(intent);
                    }
                }

        );
    }
}
