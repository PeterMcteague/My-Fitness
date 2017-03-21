package uk.ac.tees.gingerbread.myfitness.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.ac.tees.gingerbread.myfitness.DatabaseHandling.DatabaseHandler;
import uk.ac.tees.gingerbread.myfitness.R;
import uk.ac.tees.gingerbread.myfitness.Classes.DietEntry;

public class DietScreenMain extends AppCompatActivity {

    private long timeInMillis;
    private long todayTimeInMillis;
    private final DatabaseHandler dh = new DatabaseHandler(this);
    private DietEntry diet;

    /**Updates text fields on the screen with info from a diet object
     *
     * @param diet The diet entry to use.
     */
    protected void updateTextFields(DietEntry diet)
    {
        //Update text fields
        //Set calories and calories goal
        if (diet.getCaloriesGoal() != 0)
        {
            TextView currentCaloriesView = (TextView) findViewById(R.id.calories_current);
            TextView goalCaloriesView = (TextView) findViewById(R.id.calories_goal);
            currentCaloriesView.setText(Integer.toString(diet.getCalories()));
            goalCaloriesView.setText(Integer.toString(diet.getCaloriesGoal()));
        }
        else
        {
            TextView currentCaloriesView = (TextView) findViewById(R.id.calories_current);
            TextView goalCaloriesView = (TextView) findViewById(R.id.calories_goal);
            currentCaloriesView.setVisibility(View.GONE);
            goalCaloriesView.setVisibility(View.GONE);
        }

        //Same for protein
        if (diet.getProteinGoal() != 0)
        {
            TextView currentProteinView = (TextView) findViewById(R.id.protein_current);
            TextView goalProteinView = (TextView) findViewById(R.id.protein_goal);
            currentProteinView.setText(Float.toString(diet.getProtein()));
            goalProteinView.setText(Float.toString(diet.getProteinGoal()));
        }
        else
        {
            TextView currentProteinView = (TextView) findViewById(R.id.protein_current);
            TextView goalProteinView = (TextView) findViewById(R.id.protein_goal);
            currentProteinView.setVisibility(View.GONE);
            goalProteinView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_screen_main);

        Button dateButton = (Button)findViewById(R.id.btn_date);
        //Set variables
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        timeInMillis = c.getTimeInMillis();
        todayTimeInMillis = c.getTimeInMillis();

        //Button should change calendar and set time in millis
        dateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getApplicationContext(), new DatePickerDialog.OnDateSetListener()
                        {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                                {
                                    //On date set read diet entry from db for selected day and set timeinmillis
                                    c.set(year, monthOfYear, dayOfMonth);
                                    diet = dh.getDietEntry(c.getTimeInMillis());
                                    timeInMillis = c.getTimeInMillis();

                                    updateTextFields(diet);
                                }
                                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                }});

        //Create record for diet in db if there isn't one for today
        if (dh.getDietEntryToday() == null)
        {
            dh.addDietEntryToday();
        }

        //Set activity attributes
        diet = dh.getDietEntryToday();
        updateTextFields(diet);

        //Add updater choices to spinner
        final Spinner updateChoice = (Spinner) findViewById(R.id.update_choice_spinner);
        List<String> choiceList = new ArrayList<>();
        choiceList.add("Calories");
        choiceList.add("Protein");
        choiceList.add("Food");
        ArrayAdapter<String> dataAdapterActivity = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, choiceList);
        dataAdapterActivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        updateChoice.setAdapter(dataAdapterActivity);

        //Bind update button
        Button updateButton = (Button) findViewById(R.id.update_button);
        final EditText textField = (EditText) findViewById(R.id.value_entry_field);

        updateButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //Find value of spinner
                        String selection = updateChoice.getSelectedItem().toString();
                        if (selection == "Calories")
                        {
                            //Get current calories, add value in box and update entry and refresh.
                            diet.setCalories(diet.getCalories()+ Integer.parseInt(textField.getText().toString()));
                            dh.updateDietEntry(diet , timeInMillis);
                            updateTextFields(diet);
                        }
                        else if (selection == "Protein")
                        {
                            //Get current protein, add value in box and update entry and refresh.
                            diet.setProtein(diet.getProtein() + Long.parseLong(textField.getText().toString()));
                            dh.updateDietEntry(diet , timeInMillis);
                            updateTextFields(diet);
                        }
                        else if (selection == "Food")
                        {
                            //Ask nurtitionix for calories and protein KEY 973127408431e443f91406c6aa837715 DOC https://developer.nutritionix.com/v1_1/quick-start/simple-food-search
                            diet.setProtein(diet.getProtein() + PROTEINFROMNUT);
                            diet.setCalories(diet.getCalories()+ CALORIESFROMNUT);
                            dh.updateDietEntry(diet , timeInMillis);
                            updateTextFields(diet);
                        }
                    }
                }
        );
    }
}