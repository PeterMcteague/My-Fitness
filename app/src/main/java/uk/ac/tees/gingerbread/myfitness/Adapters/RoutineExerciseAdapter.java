package uk.ac.tees.gingerbread.myfitness.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.gingerbread.myfitness.Models.ExerciseEntry;
import uk.ac.tees.gingerbread.myfitness.Models.PictureEntry;
import uk.ac.tees.gingerbread.myfitness.Models.RoutineEntry;
import uk.ac.tees.gingerbread.myfitness.R;

/**
 * Created by Peter on 05/04/2017.
 */

public class RoutineExerciseAdapter extends ArrayAdapter<ExerciseEntry>
{
    private static class ViewHolder {
        TextView textView;
    }

    public RoutineExerciseAdapter(Context context, List<ExerciseEntry> entries) {
        super(context, R.layout.item_exercise_routine, entries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ExerciseEntry exerciseEntry = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_exercise_routine, parent, false);

            viewHolder.textView = (TextView) convertView.findViewById(R.id.exercise_name_view);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (exerciseEntry != null) {
            viewHolder.textView.setText(exerciseEntry.getName());
            viewHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle(exerciseEntry.getName());
                    alertDialog.setMessage(exerciseEntry.getDescription());
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });
        }

        return convertView;
    }
}
