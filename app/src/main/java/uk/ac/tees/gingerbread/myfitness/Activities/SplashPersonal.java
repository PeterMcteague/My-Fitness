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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.ac.tees.gingerbread.myfitness.R;
import uk.ac.tees.gingerbread.myfitness.Services.DatabaseHandler;

public class SplashPersonal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_personal);

        //Populating gender spinner
        final Spinner genderSpinner = (Spinner) findViewById(R.id.intro_personal_info_gender_spinner);
        List<String> genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");
        ArrayAdapter<String> dataAdapterGender = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, genderList);
        dataAdapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(dataAdapterGender);

        //Populating activity level spinner
        final Spinner activitySpinner = (Spinner) findViewById(R.id.intro_personal_info_activity_spinner);
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
        final EditText birthDateEntry = (EditText) findViewById(R.id.intro_personal_info_birth_date_entry);
        final EditText heightEntry = (EditText) findViewById(R.id.intro_personal_info_height_entry);
        final EditText weightEntry = (EditText) findViewById(R.id.intro_personal_info_weight_entry);

        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY");
                        try {
                            //Check for if all fields are full
                            if (nameEntry.getText().toString().equals("") || birthDateEntry.getText().toString().equals("") || heightEntry.getText().toString().equals("") || weightEntry.getText().toString().equals(""))
                            {
                                Toast.makeText(context, "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Date birthDate = df.parse(birthDateEntry.getText().toString());
                                Calendar cal = Calendar.getInstance();
                                cal.set(Calendar.HOUR_OF_DAY, 0);
                                cal.set(Calendar.MINUTE, 0);
                                cal.set(Calendar.SECOND, 0);
                                cal.set(Calendar.MILLISECOND, 0);
                                long todaysDate = cal.getTimeInMillis();
                                cal.setTime(birthDate);

                                DatabaseHandler dh = new DatabaseHandler(context);
                                dh.addInfo(
                                        Float.valueOf(heightEntry.getText().toString()),
                                        Float.valueOf(weightEntry.getText().toString()),
                                        activitySpinner.getSelectedItemPosition() + 1,
                                        "Not Set",
                                        todaysDate);
                                dh.addPersistentInfo(nameEntry.getText().toString(),cal.getTimeInMillis(),genderSpinner.getSelectedItem().toString());

                                //Add info to intent to be passed along to goal screen

                                Intent intent = new Intent(context, SplashRoutineGeneration.class);

                                startActivity(intent);
                            }
                        } catch (ParseException e) {
                            Toast.makeText(context, "Please enter your birthdate correctly.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

        );
    }
}
