package uk.ac.tees.gingerbread.myfitness.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import uk.ac.tees.gingerbread.myfitness.Models.InfoEntry;
import uk.ac.tees.gingerbread.myfitness.Models.RoutineEntry;
import uk.ac.tees.gingerbread.myfitness.R;
import uk.ac.tees.gingerbread.myfitness.Services.DatabaseHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoutineFragment extends Fragment {

    private long timeInMillis;
    private long todayTimeInMillis;
    private Calendar c;
    private RoutineEntry routine;
    private DatabaseHandler dh;
    private FloatingActionButton addExerciseButton;

    public RoutineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_routine, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Routine");
    }
}
