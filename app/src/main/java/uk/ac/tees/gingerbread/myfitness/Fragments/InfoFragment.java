package uk.ac.tees.gingerbread.myfitness.Fragments;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.ac.tees.gingerbread.myfitness.Models.InfoEntry;
import uk.ac.tees.gingerbread.myfitness.R;
import uk.ac.tees.gingerbread.myfitness.Services.DatabaseHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    private long timeInMillis;
    private long todayTimeInMillis;
    private Calendar c;

    private InfoEntry info;
    private DatabaseHandler dh;

    private FloatingActionButton dateButton;
    private EditText weightField;
    private EditText heightField;
    private Spinner activitySpinner;
    private Spinner goalSpinner;

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_calendar) {
            Log.d("Calendar Button","Info");
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    c.set(year, monthOfYear, dayOfMonth);
                    timeInMillis = c.getTimeInMillis();

                    if (dh.getInfoEntry(timeInMillis) == null)
                    {
                        Toast.makeText(getContext(),"Could not find info for today.",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        info = dh.getInfoEntry(timeInMillis);
                        updateFields(info);
                    }

                }
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(todayTimeInMillis);
            datePickerDialog.show();
            return true;
        }

        return false;
    }

    public InfoFragment() {
        // Required empty public constructor
    }

    public void updateFields(InfoEntry info)
    {
        List<String> goalList = new ArrayList<>();
        goalList.add("Not Set");
        goalList.add("Build Muscle");
        goalList.add("Lose weight");
        goalList.add("Stay healthy");

        weightField.setText(String.valueOf(info.getWeight()));
        heightField.setText(String.valueOf(info.getHeight()));
        activitySpinner.setSelection(info.getActivityLevel() - 1);

        goalSpinner.setSelection(goalList.indexOf(info.getGoal()));

        if (timeInMillis != todayTimeInMillis)
        {
            weightField.setInputType(InputType.TYPE_NULL);
            heightField.setInputType(InputType.TYPE_NULL);
            activitySpinner.setClickable(true);
            goalSpinner.setClickable(true);
        }
        else
        {
            weightField.setInputType(InputType.TYPE_CLASS_NUMBER);
            heightField.setInputType(InputType.TYPE_CLASS_NUMBER);
            activitySpinner.setClickable(false);
            goalSpinner.setClickable(false);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        setHasOptionsMenu(true);

        weightField = (EditText) view.findViewById(R.id.editText_weight);
        heightField = (EditText) view.findViewById(R.id.editText_Height);

        activitySpinner = (Spinner) view.findViewById(R.id.spinner_activity);
        List<String> activityList = new ArrayList<>();
        activityList.add("Sedentary");
        activityList.add("Lightly active");
        activityList.add("Active");
        activityList.add("Very active");
        ArrayAdapter<String> dataAdapterActivity = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, activityList);
        dataAdapterActivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(dataAdapterActivity);

        goalSpinner = (Spinner) view.findViewById(R.id.spinner_goal);
        List<String> goalList = new ArrayList<>();
        goalList.add("Not Set");
        goalList.add("Build Muscle");
        goalList.add("Lose weight");
        goalList.add("Stay healthy");
        ArrayAdapter<String> goalAdapterActivity = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, goalList);
        goalAdapterActivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalSpinner.setAdapter(goalAdapterActivity);

        //Set calendar up
        c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        timeInMillis = c.getTimeInMillis();
        todayTimeInMillis = c.getTimeInMillis();

        dh = new DatabaseHandler(getActivity());
        //Create record for diet in db if there isn't one for today
        info = dh.getInfoEntry(todayTimeInMillis);
        if (info == null)
        {
            //Copy last one with todays date
            InfoEntry info = dh.getLatestInfo();
            if (info != null)
            {
                info.setDate(todayTimeInMillis);
                dh.addInfo(info);
            }
            else
            {
                info = new InfoEntry(0,0,0,todayTimeInMillis,"Not set");
                dh.addInfo(info);
            }
        }
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateFields(info);

        weightField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equalsIgnoreCase(""))
                {
                    if (info.getWeight() != Float.parseFloat(s.toString()))
                    {
                        info.setWeight(Float.parseFloat(s.toString()));
                        dh.updateInfoEntry(info);
                        Toast.makeText(getActivity().getApplicationContext(),"Weight updated.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        heightField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equalsIgnoreCase(""))
                {
                    if (info.getHeight() != Float.parseFloat(s.toString()))
                    {
                        info.setHeight(Float.parseFloat(s.toString()));
                        dh.updateInfoEntry(info);
                        Toast.makeText(getActivity().getApplicationContext(),"Height updated.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position + 1 != info.getActivityLevel())
                {
                    info.setActivityLevel(position + 1);
                    dh.updateInfoEntry(info);
                    Toast.makeText(getActivity().getApplicationContext(),"Activity level updated.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        goalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<String> goalList = new ArrayList<>();
                goalList.add("Not Set");
                goalList.add("Build Muscle");
                goalList.add("Lose weight");
                goalList.add("Stay healthy");

                if (!(info.getGoal().equals(goalList.get(position))))
                {
                    info.setGoal(goalList.get(position));
                    dh.updateInfoEntry(info);
                    Toast.makeText(getActivity().getApplicationContext(),"Goal updated.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        getActivity().setTitle("Personal Info");
    }
}
