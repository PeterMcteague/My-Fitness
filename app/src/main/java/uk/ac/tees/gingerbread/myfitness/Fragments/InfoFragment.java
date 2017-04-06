package uk.ac.tees.gingerbread.myfitness.Fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.ac.tees.gingerbread.myfitness.Adapters.ProgressPicAdapter;
import uk.ac.tees.gingerbread.myfitness.Models.InfoEntry;
import uk.ac.tees.gingerbread.myfitness.Models.PictureEntry;
import uk.ac.tees.gingerbread.myfitness.R;
import uk.ac.tees.gingerbread.myfitness.Services.DatabaseHandler;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    private long timeInMillis;
    private long todayTimeInMillis;
    private Calendar c;

    private InfoEntry info;
    private DatabaseHandler dh;

    private EditText weightField;
    private EditText heightField;
    private Spinner activitySpinner;
    private Spinner goalSpinner;
    private FloatingActionButton addPictureButton;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                        DatabaseHandler dh = new DatabaseHandler(getContext());
                        dh.addPictureEntry(timeInMillis,bitmap);
                        populateImageList();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                        DatabaseHandler dh = new DatabaseHandler(getContext());
                        dh.addPictureEntry(timeInMillis,bitmap);
                        populateImageList();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public void updateTitleBar(long date)
    {
        c = Calendar.getInstance();
        c.setTimeInMillis(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        getActivity().setTitle("Personal Info " + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR));
    }

    public void populateImageList()
    {
        ListView pictureList = (ListView) getView().findViewById(R.id.info_picture_list);
        DatabaseHandler dh = new DatabaseHandler(getContext());

        ArrayList<PictureEntry> pictures = dh.getPicturesForDate(timeInMillis);
        ProgressPicAdapter adapter = new ProgressPicAdapter(getActivity(),pictures);
        for (int i = 0; i < pictures.size(); i++) {
            //add to adapter object
            adapter.add(pictures.get(i));
        }

        pictureList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
            Log.d("Calendar Button","Info");
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    final Long previousDate = timeInMillis;
                    c.set(year, monthOfYear, dayOfMonth);
                    timeInMillis = c.getTimeInMillis();

                    if (dh.getInfoEntry(timeInMillis) == null)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder
                                .setTitle("Create info")
                                .setMessage("Could not find info for selected day. Would you like to create some?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Add for selected date
                                        info = dh.getInfoEntry(todayTimeInMillis);
                                        info.setDate(timeInMillis);
                                        dh.addInfo(info);
                                        //Update text fields
                                        updateFields(info);
                                        updateTitleBar(timeInMillis);
                                        Toast.makeText(getContext(),"Info created",Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        c.setTimeInMillis(previousDate);
                                    }
                                })
                                .show();
                    }
                    else
                    {
                        info = dh.getInfoEntry(timeInMillis);
                        updateFields(info);
                        populateImageList();
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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        setHasOptionsMenu(true);

        weightField = (EditText) view.findViewById(R.id.editText_weight);
        heightField = (EditText) view.findViewById(R.id.editText_Height);

        addPictureButton = (FloatingActionButton) view.findViewById(R.id.add_picture_button);

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

        updateTitleBar(timeInMillis);

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
        populateImageList();

        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Ask whether to pick from camera, gallery or cancel
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add picture");
                builder.setIcon(R.drawable.ic_menu_gallery);
                builder.setMessage("Where would you like to add a picture from?");
                builder.setPositiveButton("Camera",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                //Start camera intent and get bitmap and save to db and refresh
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 0);//zero can be replaced with any action code
                            }
                        });

                builder.setNeutralButton("Cancel",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.cancel();
                            }
                        });

                builder.setNegativeButton("Gallery",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                //Start gallery intent and get bitmap and save to db and refresh
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
                            }
                        });
                builder.show();
            }
        });

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
