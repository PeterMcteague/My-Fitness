package uk.ac.tees.gingerbread.myfitness.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.gingerbread.myfitness.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    private FloatingActionButton dateButton;
    private EditText weightField;
    private EditText heightField;
    private Spinner activitySpinner;
    private Spinner goalSpinner;

    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
                //Get resources
        dateButton = (FloatingActionButton) view.findViewById(R.id.date_picker);
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
        goalList.add("Build Muscle");
        goalList.add("Lose weight");
        goalList.add("Stay healthy");
        ArrayAdapter<String> goalAdapterActivity = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, goalList);
        goalAdapterActivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalSpinner.setAdapter(goalAdapterActivity);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Personal Info");
    }
}
