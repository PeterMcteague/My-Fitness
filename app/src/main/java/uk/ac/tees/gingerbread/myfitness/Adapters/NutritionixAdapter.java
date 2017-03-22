package uk.ac.tees.gingerbread.myfitness.Adapters;

/**
 * Created by 07mct on 22/03/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import uk.ac.tees.gingerbread.myfitness.Models.NutritionixModel;
import uk.ac.tees.gingerbread.myfitness.R;

import java.util.ArrayList;

public class NutritionixAdapter extends ArrayAdapter<NutritionixModel>{

    private static class ViewHolder {
        TextView id;
        TextView name;
    }

    public NutritionixAdapter(Context context, ArrayList<NutritionixModel> entries) {
        super(context, R.layout.item_nutritionix, entries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NutritionixModel nutritionixEntry = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_nutritionix, parent, false);

            viewHolder.id = (TextView) convertView.findViewById(R.id.value_nutritionix_id);
            viewHolder.name = (TextView) convertView.findViewById(R.id.value_nutritionix_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.id.setText(nutritionixEntry.getId());
        viewHolder.name.setText(nutritionixEntry.getName());

        return convertView;
    }
}
