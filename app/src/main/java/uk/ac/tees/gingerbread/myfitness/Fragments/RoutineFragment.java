package uk.ac.tees.gingerbread.myfitness.Fragments;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import uk.ac.tees.gingerbread.myfitness.Adapters.RoutineAdapter;
import uk.ac.tees.gingerbread.myfitness.Models.ExerciseEntry;
import uk.ac.tees.gingerbread.myfitness.Models.RoutineAdapterModel;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_calendar) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    final Long previousDate = timeInMillis;
                    c.set(year, monthOfYear, dayOfMonth);
                    timeInMillis = c.getTimeInMillis();
                    if (dh.getRoutineEntry(timeInMillis) == null)
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
                                            updateTitleBar(timeInMillis);
                                            updateList(routine);
                                            c.setTimeInMillis(timeInMillis);
                                            Toast.makeText(getContext(),"Routine created",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton("Create blank routine", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dh.addRoutineEntry(new RoutineEntry(timeInMillis,new ArrayList<ExerciseEntry>()));
                                            routine = dh.getRoutineEntry(timeInMillis);
                                            updateTitleBar(timeInMillis);
                                            updateList(routine);
                                            c.setTimeInMillis(timeInMillis);
                                            Toast.makeText(getContext(),"Routine created",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            timeInMillis=previousDate;
                                            c.setTimeInMillis(previousDate);
                                        }
                                    })
                                    .show();
                        }
                        else
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder
                                    .setTitle("Create routine for day")
                                    .setMessage("Could not find routine for day.")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton("Create blank routine", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dh.addRoutineEntry(new RoutineEntry(timeInMillis,new ArrayList<ExerciseEntry>()));
                                            routine = dh.getRoutineEntry(timeInMillis);
                                            updateTitleBar(timeInMillis);
                                            updateList(routine);
                                            c.setTimeInMillis(timeInMillis);
                                            Toast.makeText(getContext(),"Routine created",Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            timeInMillis=previousDate;
                                            c.setTimeInMillis(previousDate);
                                        }
                                    })
                                    .show();
                        }
                    }
                    else
                    {
                        routine = dh.getRoutineEntry(timeInMillis);
                        updateTitleBar(timeInMillis);
                        updateList(routine);
                        c.setTimeInMillis(timeInMillis);
                    }
                }
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
            return true;
        }

        return false;
    }

    public void updateTitleBar(long date)
    {
        c = Calendar.getInstance();
        c.setTimeInMillis(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        getActivity().setTitle("Routine for " + c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR));
    }


    public void updateList(final RoutineEntry routine)
    {
        if (!routine.getExercises().isEmpty())
        {
            ArrayList<RoutineAdapterModel> entries = new ArrayList<>();
            for (ExerciseEntry e : routine.getExercises())
            {
                entries.add(new RoutineAdapterModel(
                        routine,
                        e.getId(),
                        e.getName(),
                        e.getDescription()
                ));
            }

            //Set active in entries , from what is in routine
            int count = 0;
            List<Boolean> status = routine.getExerciseStatus();
            while (count < status.size())
            {
                entries.get(count).setActive(status.get(count));
                count++;
            }

            RoutineAdapter adapter = new RoutineAdapter(getContext(),entries);

            listView.setAdapter(adapter);
            listView.setItemsCanFocus(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final ExerciseEntry exercise = routine.getExercises().get(position);
                    //Show ok/delete button dialog with description on it
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder
                            .setTitle(exercise.getName())
                            .setMessage(exercise.getDescription())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which) {}
                            })
                            .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dh.removeExcerciseFromRoutine(routine,exercise);
                                    routine.removeExercise(exercise);
                                    updateList(routine);
                                }
                            })
                            .show();
                }
            });
            adapter.notifyDataSetChanged();
        }
        else
        {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);
            arrayAdapter.add("No exercises in routine");
            listView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            listView.setOnItemClickListener(null);
        }
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
                                dh.addRoutineEntry(new RoutineEntry(timeInMillis,new ArrayList<ExerciseEntry>()));
                                routine = dh.getRoutineEntry(timeInMillis);
                            }
                        })
                        .show();
            }
            else
            {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);
                arrayAdapter.add("Routine not found");
                listView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
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

                    final List<ExerciseEntry> exercises = dh.getExercisesNotInRoutine(routine);

                    if (exercises.isEmpty())
                    {
                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);
                        arrayAdapter.add("All exercises in routine");
                    }
                    else
                    {
                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item);
                        for (ExerciseEntry entry : exercises)
                        {
                            arrayAdapter.add(entry.getName());
                        }
                        builderSingle.setAdapter(arrayAdapter , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Exercise selected
                                dh.addExcerciseToRoutine(routine,exercises.get(which).getId());
                                routine.addExercise(exercises.get(which));
                                updateList(routine);
                            }
                        });
                    }

                    builderSingle.setPositiveButton("Add new", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, int which) {
                            final AlertDialog alert = new AlertDialog.Builder(getContext()).create();
                            alert.setIcon(android.R.drawable.ic_menu_add);
                            alert.setTitle("Create new exercise");

                            LinearLayout layout = new LinearLayout(getContext());
                            layout.setOrientation(LinearLayout.VERTICAL);

                            final EditText titleBox = new EditText(getContext());
                            titleBox.setHint("Title");
                            layout.addView(titleBox);

                            final EditText descriptionBox = new EditText(getContext());
                            descriptionBox.setHint("Description");
                            layout.addView(descriptionBox);

                            final Button addButton = new Button(getContext());
                            addButton.setText("Add");
                            addButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (titleBox.getText().length() == 0)
                                    {
                                        Toast.makeText(getContext(),"Please enter a title",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        List<ExerciseEntry> list = dh.getAllExercises();
                                        boolean alreadyExists = false;
                                        for (ExerciseEntry item : list)
                                        {
                                            if (item.getName().equals(titleBox.getText().toString()))
                                            {
                                                alreadyExists = true;
                                            }
                                        }

                                        if (alreadyExists)
                                        {
                                            Toast.makeText(getContext(),"A exercise with that name already exists",Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            dh.addExerciseEntry(titleBox.getText().toString(),descriptionBox.getText().toString());
                                            addExerciseButton.performClick();
                                        }
                                    }
                                }
                            });
                            layout.addView(addButton);

                            final Button cancelButton = new Button(getContext());
                            cancelButton.setText("Cancel");
                            cancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alert.dismiss();
                                }
                            });
                            layout.addView(cancelButton);

                            alert.setView(layout);
                            alert.show();
                        }
                    });

                    builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
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
                                    updateList(routine);
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

        updateTitleBar(timeInMillis);
    }
}
