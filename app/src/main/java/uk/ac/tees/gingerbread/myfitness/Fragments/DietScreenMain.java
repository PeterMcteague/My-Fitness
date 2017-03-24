package uk.ac.tees.gingerbread.myfitness.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;
import uk.ac.tees.gingerbread.myfitness.Adapters.NutritionixAdapter;
import uk.ac.tees.gingerbread.myfitness.Clients.NutritionixRestClient;
import uk.ac.tees.gingerbread.myfitness.Services.DatabaseHandler;
import uk.ac.tees.gingerbread.myfitness.Models.NutritionixModel;
import uk.ac.tees.gingerbread.myfitness.R;
import uk.ac.tees.gingerbread.myfitness.Classes.DietEntry;

public class DietScreenMain extends Fragment {

    private long timeInMillis;
    private long todayTimeInMillis;
    private DietEntry diet;
    private ListView foodList;
    private Activity activity;
    private DatabaseHandler dh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myFrag = inflater.inflate(R.layout.fragment_diet, container,false);
        return myFrag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Diet Info");
    }

    private void getEntries(final Activity activity, String searchQuery) {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("Accept", "application/json"));

        NutritionixRestClient.get(activity, searchQuery + "?fields=item_name%2Cnf_calories%2Cnf_protein&appId=f7a6647d&appKey=973127408431e443f91406c6aa837715", headers.toArray(new Header[headers.size()]),
                null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        ArrayList<NutritionixModel> nutritionixArray = new ArrayList<NutritionixModel>();
                        NutritionixAdapter nutritionixAdapter = new NutritionixAdapter(activity,nutritionixArray);

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                nutritionixAdapter.add(new NutritionixModel(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        //Use nutritionix to populate list view with buttons
                        foodList = (ListView) getActivity().findViewById(R.id.diet_food_list);
                        foodList.setAdapter(nutritionixAdapter);
                        foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position,
                                                    long id) {
                                final NutritionixModel selected = (NutritionixModel) foodList.getItemAtPosition(position);

                                new AlertDialog.Builder(activity)
                                        .setTitle("Add " + selected.getName() + " to diet?")
                                        .setMessage("Do you want to add " + selected.getName() + " to your goals?")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                diet.setCalories(diet.getCalories() + selected.getCalories());
                                                diet.setProtein(diet.getProtein() + selected.getProtein());
                                                dh.updateDietEntry(diet,timeInMillis);
                                                updateTextFields(diet);
                                                Toast.makeText(activity, "Added calories and/or protein to total.", Toast.LENGTH_LONG).show();
                                            }})
                                        .setNegativeButton(android.R.string.no, null).show();
                            }
                        });
                    }});}

    /**Updates text fields on the screen with info from a diet object
     *
     * @param diet The diet entry to use.
     */
    protected void updateTextFields(DietEntry diet)
    {
        EditText currentCaloriesView = (EditText) getActivity().findViewById(R.id.diet_calories_entry);
        TextView goalCaloriesView = (TextView) getActivity().findViewById(R.id.diet_calories_goal_text);
        TextView caloriesTextView = (TextView) getActivity().findViewById(R.id.diet_calories_text);

        EditText currentProteinView = (EditText) getActivity().findViewById(R.id.diet_protein_entry);
        TextView goalProteinView = (TextView) getActivity().findViewById(R.id.diet_protein_goal_text);
        TextView proteinTextView = (TextView) getActivity().findViewById(R.id.diet_protein_text);


        if (diet.getCaloriesGoal() != 0)
        {
            currentCaloriesView.setText(Integer.toString(diet.getCalories()));
            goalCaloriesView.setText(Integer.toString(diet.getCaloriesGoal()));
        }
        else
        {
            currentCaloriesView.setVisibility(View.GONE);
            goalCaloriesView.setVisibility(View.GONE);
            caloriesTextView.setVisibility(View.GONE);
        }

        //Same for protein
        if (diet.getProteinGoal() != 0)
        {
            currentProteinView.setText(Float.toString(diet.getProtein()));
            goalProteinView.setText(Float.toString(diet.getProteinGoal()));
        }
        else
        {
            currentProteinView.setVisibility(View.GONE);
            goalProteinView.setVisibility(View.GONE);
            proteinTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setContentView(R.layout.activity_diet_screen_main);

        activity = getActivity();
        dh = new DatabaseHandler(activity);

        //DATE BUTTON
        Button dateButton = (Button)getActivity().findViewById(R.id.diet_date_button);
        //Set variables
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        timeInMillis = c.getTimeInMillis();
        todayTimeInMillis = c.getTimeInMillis();

        dateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity().getApplicationContext(), new DatePickerDialog.OnDateSetListener()
                        {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                                {
                                    //On date set read diet entry from db for selected day and set timeinmillis
                                    c.set(year, monthOfYear, dayOfMonth);
                                    timeInMillis = c.getTimeInMillis();
                                    diet = dh.getDietEntry(timeInMillis);
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

        //Bind EditText's
        EditText currentCaloriesView = (EditText) getActivity().findViewById(R.id.diet_calories_entry);
        EditText currentProteinView = (EditText) getActivity().findViewById(R.id.diet_protein_entry);
        EditText foodEntryView = (EditText) getActivity().findViewById(R.id.diet_food_entry);

        currentCaloriesView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                diet.setCalories(Integer.parseInt(s.toString()));
                dh.updateDietEntry(diet,timeInMillis);
                Toast.makeText(getActivity().getApplicationContext(),"Calories updated.",Toast.LENGTH_SHORT);
            }
        });

        currentProteinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                diet.setProtein(Integer.parseInt(s.toString()));
                dh.updateDietEntry(diet,timeInMillis);
                Toast.makeText(getActivity().getApplicationContext(),"Protein updated.",Toast.LENGTH_SHORT);
            }
        });

        foodEntryView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                getEntries(activity,s.toString());
            }
        });
    }
}