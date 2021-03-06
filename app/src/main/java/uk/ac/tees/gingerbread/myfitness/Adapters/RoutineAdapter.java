package uk.ac.tees.gingerbread.myfitness.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.gingerbread.myfitness.Models.RoutineAdapterModel;
import uk.ac.tees.gingerbread.myfitness.R;
import uk.ac.tees.gingerbread.myfitness.Services.DatabaseHandler;

/**
 * Created by Peter on 14/04/2017.
 */

public class RoutineAdapter extends ArrayAdapter<RoutineAdapterModel> {

    private static class ViewHolder {
        TextView name;
        CheckBox box;
    }

    public RoutineAdapter(Context context, ArrayList<RoutineAdapterModel> entries) {
        super(context, R.layout.item_exercise_routine, entries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final RoutineAdapterModel entry = getItem(position);
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_exercise_routine, parent, false);

            viewHolder.name = (TextView) convertView.findViewById(R.id.exercise_name_view);
            viewHolder.box = (CheckBox) convertView.findViewById(R.id.exercise_checkbox);
            viewHolder.box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //Set exercise in routine to disabled
                    List<Boolean> statuses = entry.getRoutine().getExerciseStatus();
                    int index = entry.getRoutine().getExerciseIndex(viewHolder.name.getText().toString());
                    statuses.set(index , isChecked);
                    entry.getRoutine().setExerciseStatus(statuses);
                    //Update in database
                    DatabaseHandler dh = new DatabaseHandler(getContext());
                    dh.updateRoutineExercises(entry.getRoutine());
                    if (isChecked)
                    {
                        viewHolder.name.setPaintFlags(viewHolder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        viewHolder.name.setTextColor(Color.parseColor("#E0E0E0"));
                    }
                    else
                    {
                        viewHolder.name.setPaintFlags(0);
                        viewHolder.name.setTextColor(Color.GRAY);
                        viewHolder.name.setTextAppearance(R.style.TextAppearance_AppCompat_Medium);
                    }
             }
            });

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(entry.getName());
        viewHolder.box.setChecked(entry.isActive());

        return convertView;
    }
}
