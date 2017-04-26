package uk.ac.tees.gingerbread.myfitness.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import uk.ac.tees.gingerbread.myfitness.Models.DietEntry;
import uk.ac.tees.gingerbread.myfitness.Models.InfoEntry;
import uk.ac.tees.gingerbread.myfitness.R;
import uk.ac.tees.gingerbread.myfitness.Services.DatabaseHandler;


public class SummaryFragment extends Fragment {

    private long timeInMillis;
    private long todayTimeInMillis;
    private Calendar c;

    private DatabaseHandler dh;

    private GraphView weightGraph;
    private GraphView dietGraph;
    private TextView weightText;
    private TextView dietText;

    public SummaryFragment()
    {
        //Required constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        setHasOptionsMenu(true);

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Setting up vars
        dh = new DatabaseHandler(getContext());
        weightGraph = (GraphView) view.findViewById(R.id.weight_graph);
        dietGraph = (GraphView) view.findViewById(R.id.diet_graph);
        weightText = (TextView) view.findViewById(R.id.weight_summary_text);
        dietText = (TextView) view.findViewById(R.id.diet_summary_text);

        updateViews(timeInMillis);
        getActivity().setTitle("Summary");
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
            Log.d("Date clicked","");

            //This looks dated but I couldn't find a way to do this with the material date picker.
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    c.set(year, monthOfYear, dayOfMonth);
                    timeInMillis = c.getTimeInMillis();
                    updateViews(timeInMillis);
                }
            }, c.get(java.util.Calendar.YEAR), c.get(java.util.Calendar.MONTH), c.get(java.util.Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(todayTimeInMillis);
            datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("day","id","android")).setVisibility(View.GONE);
            datePickerDialog.show();
            return true;
        }

        return false;
    }

    private void updateViews(long date)
    {
        //Get first and last day of month
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        long lastDayOfMonth = c.getTimeInMillis();
        c.set(Calendar.DATE, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        long firstDayOfMonth = c.getTimeInMillis();

        //Updating the weight graph
        //Clear
        weightGraph.removeAllSeries();
        //Set X bounds
        weightGraph.getViewport().setXAxisBoundsManual(true);
        weightGraph.getViewport().setMinX(0);
        weightGraph.getViewport().setMaxX(c.getActualMaximum(Calendar.DAY_OF_MONTH) + 2);
        //Set Y bounds
        weightGraph.getViewport().setYAxisBoundsManual(true);
        weightGraph.getViewport().setMinY(0);
        weightGraph.getViewport().setMaxY(500);
        //Set axis labels
        GridLabelRenderer gridLabel = weightGraph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Day");
        gridLabel.setVerticalAxisTitle("Weight /KG");

        //Add points based on database entries
        ArrayList<InfoEntry> entries = dh.getInfoForMonth(firstDayOfMonth,lastDayOfMonth);
        if (!entries.isEmpty())
        {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
            for (InfoEntry e : entries)
            {
                int day;
                c.setTimeInMillis(e.getDate());
                day = c.get(Calendar.DAY_OF_MONTH);
                series.appendData(new DataPoint(day,e.getWeight()),true,31);
            }
            series.setColor(Color.RED);
            weightGraph.addSeries(series);
            weightGraph.getViewport().setMaxY(entries.get(0).getWeight() + 20);
        }


        //Updating the diet graph
        //Clear
        dietGraph.removeAllSeries();
        //Set X bounds
        dietGraph.getViewport().setXAxisBoundsManual(true);
        dietGraph.getViewport().setMinX(0);
        dietGraph.getViewport().setMaxX(c.getActualMaximum(Calendar.DAY_OF_MONTH) + 2);
        dietGraph.getViewport().setYAxisBoundsManual(true);
        dietGraph.getViewport().setMinY(0);
        dietGraph.getViewport().setMaxY(3000);
        //Set axis labels
        GridLabelRenderer gridLabel2 = dietGraph.getGridLabelRenderer();
        gridLabel2.setHorizontalAxisTitle("Day");
        gridLabel2.setVerticalAxisTitle("Calories /KCal");

        //Add points based on database entries
        ArrayList<DietEntry> entries2 = dh.getAllDietEntriesForMonth(firstDayOfMonth,lastDayOfMonth);
        if (!entries2.isEmpty())
        {
            //Calories series
            LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>();
            series2.setColor(Color.RED);
            for (DietEntry e : entries2)
            {
                int day;
                c.setTimeInMillis(e.getDate());
                day = c.get(Calendar.DAY_OF_MONTH);
                series2.appendData(new DataPoint(day,e.getCalories()),true,31);
            }
            series2.setTitle("Calories");
            dietGraph.addSeries(series2);

            //Calories goal series
            LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>();
            for (DietEntry e : entries2)
            {
                int day;
                c.setTimeInMillis(e.getDate());
                day = c.get(Calendar.DAY_OF_MONTH);
                series3.appendData(new DataPoint(day,e.getCaloriesGoal()),true,31);
            }
            series3.setColor(Color.GRAY);
            series3.setTitle("Calories Goal");
            dietGraph.addSeries(series3);

            dietGraph.getLegendRenderer().setVisible(true);
            dietGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
            dietGraph.getViewport().setMaxY(entries2.get(0).getCaloriesGoal() + 100);
        }

        //Updating the text of the text views to say "X Summary for MONTH YEAR".
        weightText.setText("Weight summary for " + c.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.getDefault()) + " " + c.get(Calendar.YEAR));
        dietText.setText("Diet summary for " + c.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.getDefault()) + " " + c.get(Calendar.YEAR));

        //reset
        c.setTimeInMillis(date);
    }
}
