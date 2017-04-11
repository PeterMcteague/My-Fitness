package uk.ac.tees.gingerbread.myfitness.Fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import uk.ac.tees.gingerbread.myfitness.Models.ExerciseEntry;
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
    private ListView listView;
    private FloatingActionButton addExerciseButton;

    public RoutineFragment() {
        // Required empty public constructor
    }

    public void updateTitleBar(long date)
    {
        c = Calendar.getInstance();
        c.setTimeInMillis(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        getActivity().setTitle("Routine for " + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR));
    }

    public void updateList(RoutineEntry routine)
    {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);
        for (ExerciseEntry e : routine.getExercises())
        {
            //Add list entry item and bind
            arrayAdapter.add(e.getName());
        }
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_routine, container, false);
        setHasOptionsMenu(true);

        listView = (ListView) view.findViewById(R.id.routine_layout_list);
        addExerciseButton = (FloatingActionButton) view.findViewById(R.id.routine_add_to_routine_button);

        //Set calendar up
        c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        timeInMillis = c.getTimeInMillis();
        todayTimeInMillis = c.getTimeInMillis();

        updateTitleBar(timeInMillis);

        dh = new DatabaseHandler(getActivity());
        //Get (or prompt to create) routine entry.
        if (dh.getRoutineEntry(todayTimeInMillis) == null)
        {
            //If a previous entry with same day of week exists , offer to copy. Otherwise ask to create blank one.
            if (dh.getLatestRoutineForDay(c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())) != null)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder
                        .setTitle("Create routine for day")
                        .setMessage("Could not find routine for day but one was found for the same day last week.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Copy last weeks", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                //Get last entry update date and add to db
                                routine = dh.getLatestRoutineForDay(c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
                                routine.setDate(todayTimeInMillis);
                                dh.addRoutineEntry(routine);
                            }
                        })
                        .setNegativeButton("Create blank routine", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Do nothing, only add when you add an exercise.
                            }
                        })
                        .show();
            }
        }
        else
        {
            routine = dh.getRoutineEntry(todayTimeInMillis);
        }

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Populate list
        if (routine != null)
        {
            updateList(routine);
        }


        //Bind add button
        addExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (routine != null)
                {
                    //Show a list of exercises to add with cancel button, containing all exercises not added to routineentry already
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                    builderSingle.setIcon(android.R.drawable.ic_menu_add);
                    builderSingle.setTitle("Add an exercise");

                    final List<ExerciseEntry> exercises = dh.getExcercisesNotInRoutine(routine);
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);
                    for (ExerciseEntry entry : exercises)
                    {
                        arrayAdapter.add(entry.getName());
                    }

                    builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builderSingle.setAdapter(arrayAdapter , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.d("Selected",exercises.get(which).getName());
                            dh.addExcerciseToRoutine(routine,exercises.get(which).getId());
                            updateList(routine);
                        }
                    });
                    builderSingle.show();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder
                            .setTitle("Create routine for day")
                            .setMessage("Could not find routine for selected day.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Create one", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which) {
                                    //Get last entry update date and add to db
                                    routine = new RoutineEntry(timeInMillis,new ArrayList<ExerciseEntry>());
                                    dh.addRoutineEntry(routine);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                }

            }
        });

        getActivity().setTitle("Routine");
    }
}
