package uk.ac.tees.gingerbread.myfitness.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.Fragment;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import uk.ac.tees.gingerbread.myfitness.Models.DietEntry;

public class DietScreenMain extends Fragment {

    private long timeInMillis;
    private long todayTimeInMillis;
    private Calendar c;
    private DietEntry diet;
    private DatabaseHandler dh;

    private Button dateButton;
    private EditText caloriesEntry;
    private EditText caloriesGoalEntry;
    private EditText proteinEntry;
    private EditText proteinGoalEntry;
    private EditText foodEntry;
    private ListView foodList;

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_calendar) {
            Log.d("Calendar Button","Diet");
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
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
            datePickerDialog.getDatePicker().setMaxDate(todayTimeInMillis);
            datePickerDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    //Include any getting of views in here and assign to variables.
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_diet_screen_main, container,false);

        setHasOptionsMenu(true);

        caloriesEntry = (EditText) view.findViewById(R.id.diet_calories_entry);
        caloriesGoalEntry = (EditText) view.findViewById(R.id.diet_calories_goal_entry);
        proteinEntry = (EditText) view.findViewById(R.id.diet_protein_entry);
        proteinGoalEntry = (EditText) view.findViewById(R.id.diet_protein_goal_entry);
        foodEntry = (EditText) view.findViewById(R.id.diet_food_entry);
        foodList = (ListView) view.findViewById(R.id.diet_food_list);

        //Set calendar up
        c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        timeInMillis = c.getTimeInMillis();
        todayTimeInMillis = c.getTimeInMillis();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {

        dh = new DatabaseHandler(getActivity());

        //Create record for diet in db if there isn't one for today
        diet = dh.getDietEntryToday();
        if (diet == null)
        {
            dh.addDietEntryToday();
            diet = dh.getDietEntryToday();
        }

        updateTextFields(diet);

        caloriesEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equalsIgnoreCase(""))
                {
                    if (diet.getCalories() != (int) Double.parseDouble(s.toString()))
                    {
                        diet.setCalories((int) Double.parseDouble(s.toString()));
                        dh.updateDietEntry(diet,timeInMillis);
                        Toast.makeText(getActivity().getApplicationContext(),"Calories updated.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        caloriesGoalEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equalsIgnoreCase("")) {
                    if (diet.getCaloriesGoal() != (int) Double.parseDouble(s.toString())) {
                        diet.setCaloriesGoal((int) Double.parseDouble(s.toString()));
                        dh.updateDietEntry(diet, timeInMillis);
                        Toast.makeText(getActivity().getApplicationContext(), "Calories goal updated.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        proteinEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equalsIgnoreCase(""))
                {
                    if (diet.getProtein() != (int) Double.parseDouble(s.toString())) {
                        diet.setProtein((int) Double.parseDouble(s.toString()));
                        dh.updateDietEntry(diet, timeInMillis);
                        Toast.makeText(getActivity().getApplicationContext(), "Protein updated.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        proteinGoalEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equalsIgnoreCase("")) {
                    if (diet.getProteinGoal() != (int) Double.parseDouble(s.toString())) {
                        diet.setProteinGoal((int) Double.parseDouble(s.toString()));
                        dh.updateDietEntry(diet, timeInMillis);
                        Toast.makeText(getActivity().getApplicationContext(), "Protein updated.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        foodEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s)
            {
                if (foodList != null)
                {
                    if (foodList.getAdapter() != null)
                    {
                        ((NutritionixAdapter) foodList.getAdapter()).clear();
                        ((NutritionixAdapter) foodList.getAdapter()).notifyDataSetChanged();
                    }
                }
                if (s.toString().length() > 2 && !s.toString().matches("")){
                    getEntries(getActivity(),s.toString());
                }
            }
        });

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Diet Info");
    }

    private void getEntries(final Activity activity, String searchQuery) {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("Accept", "application/json"));

        foodList = (ListView) getView().findViewById(R.id.diet_food_list);

        NutritionixRestClient.get(activity, searchQuery + "?fields=item_name%2Cnf_calories%2Cnf_protein&appId=f7a6647d&appKey=973127408431e443f91406c6aa837715", headers.toArray(new Header[headers.size()]),
                null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        Log.d("RESPONSE",response + "");
                        try {
                            Log.d("LOGFIELDS",((JSONObject) response.get(1)).get("fields") + "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
                        nutritionixAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        ArrayList<NutritionixModel> nutritionixArray = new ArrayList<NutritionixModel>();
                        NutritionixAdapter nutritionixAdapter = new NutritionixAdapter(activity,nutritionixArray);

                        foodList = (ListView) getView().findViewById(R.id.diet_food_list);
                        foodList.clearChoices();

                        Log.d("SUCCESS", response + "");
                        try {
                            JSONArray array = response.getJSONArray("hits");
                            onSuccess(statusCode, headers, array);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**Updates text fields on the screen with info from a diet object
     *
     * @param diet The diet entry to use.
     */
    protected void updateTextFields(DietEntry diet)
    {
        EditText currentCaloriesView = (EditText) getView().findViewById(R.id.diet_calories_entry);
        EditText goalCaloriesView = (EditText) getView().findViewById(R.id.diet_calories_goal_entry);

        EditText currentProteinView = (EditText) getView().findViewById(R.id.diet_protein_entry);
        EditText goalProteinView = (EditText) getView().findViewById(R.id.diet_protein_goal_entry);

        currentCaloriesView.setText(Integer.toString(diet.getCalories()));
        goalCaloriesView.setText(Integer.toString(diet.getCaloriesGoal()));

        currentProteinView.setText(Float.toString(diet.getProtein()));
        goalProteinView.setText(Float.toString(diet.getProteinGoal()));

        if (timeInMillis != todayTimeInMillis)
        {
            caloriesEntry.setInputType(InputType.TYPE_NULL);
            caloriesGoalEntry.setInputType(InputType.TYPE_NULL);
            proteinEntry.setInputType(InputType.TYPE_NULL);
            proteinGoalEntry.setInputType(InputType.TYPE_NULL);
        }
        else
        {
            caloriesEntry.setInputType(InputType.TYPE_CLASS_NUMBER);
            caloriesGoalEntry.setInputType(InputType.TYPE_CLASS_NUMBER);
            proteinEntry.setInputType(InputType.TYPE_CLASS_NUMBER);
            proteinGoalEntry.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }
}