package uk.ac.tees.gingerbread.myfitness.Fragments;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

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

    private boolean lastThirtyDays; //If true do last 30 days, otherwise do month behavior. Swap on datechange.

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

        lastThirtyDays = true;

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

            //This looks dated but I couldn't find a way to do this with the material date picker.
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    c.set(year, monthOfYear, dayOfMonth);
                    timeInMillis = c.getTimeInMillis();
                    lastThirtyDays = false;
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
        long lastDayOfMonth;
        long firstDayOfMonth;

        if (lastThirtyDays)
        {
            //Between today and 30 days ago
            lastDayOfMonth = todayTimeInMillis;
            c.setTimeInMillis(todayTimeInMillis);
            c.add(Calendar.DAY_OF_MONTH, -30);
            firstDayOfMonth = c.getTimeInMillis();
        }
        else
        {
            //Get first and last day of month
            c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            lastDayOfMonth = c.getTimeInMillis();
            c.set(Calendar.DATE, c.getActualMinimum(Calendar.DAY_OF_MONTH));
            firstDayOfMonth = c.getTimeInMillis();
        }


        //Updating the weight graph
        //Clear
        weightGraph.removeAllSeries();
        //Set X bounds
        if (lastThirtyDays)
        {
            weightGraph.getViewport().setXAxisBoundsManual(true);
            weightGraph.getViewport().setMinX(0);
            weightGraph.getViewport().setMaxX(30);
        }
        else
        {
            weightGraph.getViewport().setXAxisBoundsManual(true);
            weightGraph.getViewport().setMinX(0);
            weightGraph.getViewport().setMaxX(c.getActualMaximum(Calendar.DAY_OF_MONTH) + 2);
        }
        //Set Y bounds
        weightGraph.getViewport().setYAxisBoundsManual(true);
        weightGraph.getViewport().setMinY(0);
        weightGraph.getViewport().setMaxY(500);
        //Set axis labels
        GridLabelRenderer gridLabel = weightGraph.getGridLabelRenderer();
        if (lastThirtyDays)
        {
            gridLabel.setHorizontalAxisTitle("Days since 30 days ago");
        }
        else
        {
            gridLabel.setHorizontalAxisTitle("Day of month");
        }
        gridLabel.setVerticalAxisTitle("Weight /KG");

        //Add points based on database entries
        ArrayList<InfoEntry> entries = dh.getInfoForMonth(firstDayOfMonth,lastDayOfMonth);
        if (!entries.isEmpty())
        {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
            for (InfoEntry e : entries)
            {
                series.appendData(new DataPoint((e.getDate() - firstDayOfMonth) / (1000*60*60*24),e.getWeight()),true,31);
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
        ArrayList<DietEntry> entries2 = dh.getDietEntriesBetween(firstDayOfMonth,lastDayOfMonth);
        if (!entries2.isEmpty())
        {
            //Calories series
            LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>();
            series2.setColor(Color.RED);
            for (DietEntry e : entries2)
            {
                series2.appendData(new DataPoint((e.getDate() - firstDayOfMonth) / (1000*60*60*24),e.getCalories()),true,31);
            }
            series2.setTitle("Calories");
            dietGraph.addSeries(series2);

            //Calories goal series
            LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>();
            for (DietEntry e : entries2)
            {
                series3.appendData(new DataPoint((e.getDate() - firstDayOfMonth) / (1000*60*60*24),e.getCaloriesGoal()),true,31);
            }
            series3.setColor(Color.GRAY);
            series3.setTitle("Calories Goal");
            dietGraph.addSeries(series3);

            dietGraph.getLegendRenderer().setVisible(true);
            dietGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
            dietGraph.getViewport().setMaxY(entries2.get(0).getCaloriesGoal() + 500);
        }

        if (lastThirtyDays)
        {
            weightText.setText(getString(R.string.weight_summary_30));
            dietText.setText(getString(R.string.diet_summary_30));
        }
        else
        {
            //Updating the text of the text views to say "X Summary for MONTH YEAR".
            weightText.setText("Weight summary for " + c.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.getDefault()) + " " + c.get(Calendar.YEAR));
            dietText.setText("Diet summary for " + c.getDisplayName(Calendar.MONTH,Calendar.LONG, Locale.getDefault()) + " " + c.get(Calendar.YEAR));
        }

        //reset
        c.setTimeInMillis(date);
    }
}
