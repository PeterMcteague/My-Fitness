package uk.ac.tees.gingerbread.myfitness.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.gingerbread.myfitness.R;

public class SplashPersonal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_personal);

        //Populating gender spinner
        Spinner genderSpinner = (Spinner) findViewById(R.id.intro_personal_info_gender_spinner);
        List<String> genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");
        ArrayAdapter<String> dataAdapterGender = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, genderList);
        dataAdapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(dataAdapterGender);

        //Populating activity level spinner
        Spinner activitySpinner = (Spinner) findViewById(R.id.intro_personal_info_activity_spinner);
        List<String> activityList = new ArrayList<>();
        activityList.add("Sedentary");
        activityList.add("Lightly active");
        activityList.add("Active");
        activityList.add("Very active");
        ArrayAdapter<String> dataAdapterActivity = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, activityList);
        dataAdapterActivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(dataAdapterActivity);

        // Get context and button objects
        final Context context = this;
        Button button = (Button) findViewById(R.id.intro_personal_info_okay);

        // Get references to edittext fields
        final EditText nameEntry = (EditText) findViewById(R.id.intro_personal_info_name_entry);
        final EditText ageEntry = (EditText) findViewById(R.id.intro_personal_info_age_entry);
        final EditText heightEntry = (EditText) findViewById(R.id.intro_personal_info_height_entry);
        final EditText weightEntry = (EditText) findViewById(R.id.intro_personal_info_weight_entry);

        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Check for if all fields are full
                        if (nameEntry.getText().toString().equals("") || ageEntry.getText().toString().equals("") || heightEntry.getText().toString().equals("") || weightEntry.getText().toString().equals(""))
                        {
                            Toast.makeText(context, "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //Add info to db after checks.
                            Intent intent = new Intent(context, SplashGoal.class);
                            startActivity(intent);
                        }
                    }
                }

        );
    }
}
