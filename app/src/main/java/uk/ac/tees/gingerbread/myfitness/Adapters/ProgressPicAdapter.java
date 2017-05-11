package uk.ac.tees.gingerbread.myfitness.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import uk.ac.tees.gingerbread.myfitness.Models.PictureEntry;
import uk.ac.tees.gingerbread.myfitness.R;

/**
 * Created by Peter on 05/04/2017.
 */

public class ProgressPicAdapter extends ArrayAdapter<PictureEntry>
{
    private static class ViewHolder {
        ImageView picture;
    }

    public ProgressPicAdapter(Context context, ArrayList<PictureEntry> entries) {
        super(context, R.layout.item_picture, entries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PictureEntry pictureEntry = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_picture, parent, false);

            viewHolder.picture = (ImageView) convertView.findViewById(R.id.item_imageview);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (pictureEntry != null) {
            viewHolder.picture.setImageBitmap(pictureEntry.getPicture());
        }

        return convertView;
    }
}
