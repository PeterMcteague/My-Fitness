package uk.ac.tees.gingerbread.myfitness.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;

import uk.ac.tees.gingerbread.myfitness.DatabaseHandling.DatabaseHandler;
import uk.ac.tees.gingerbread.myfitness.R;
import uk.ac.tees.gingerbread.myfitness.Classes.DietEntry;

public class DietScreenMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_screen_main);

//        //Calendar
//        final Calendar c = Calendar.getInstance();
//        c.set(Calendar.HOUR_OF_DAY, 0);
//        c.set(Calendar.MINUTE, 0);
//        c.set(Calendar.SECOND, 0);
//        c.set(Calendar.MILLISECOND, 0);
//
//        //Create record for diet in db if there isn't one for today
//        final Context context = this;
//        DatabaseHandler dh = new DatabaseHandler(context);
//        if (selectedDate == c.getTimeInMillis())
//        {
//            if (dh.getDietEntryToday() == null)
//            {
//                dh.addDietEntryToday();
//            }
//        }
//        //Set activity attributes
//        DietEntry diet = dh.getDietEntryToday();
//        diet.getCalories();
//        diet.getCaloriesGoal();
//        diet.getProtein();
//        diet.getProteinGoal();

    }
}