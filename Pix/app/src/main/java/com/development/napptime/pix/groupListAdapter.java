package com.development.napptime.pix;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Napptime on 2/10/15.
 *
 * Klasi sem sér um að búa til listview adapter sem er fær um að vinna með listview eins og notað er
 * í discover tabinu.
 */

public class groupListAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] titles;
    private final String[] comments;
    private final Integer[] imageId;
    public groupListAdapter(Activity context,
                            String[] titles, String[] comments, Integer[] imageId) {
        super(context, R.layout.photo_row, titles);
        this.context = context;
        this.titles = titles;
        this.comments = comments;
        this.imageId = imageId;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.group_row, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.groupListTitle);
        TextView txtComment= (TextView) rowView.findViewById(R.id.groupListSubTitle);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.groupListPhoto);
        txtTitle.setText(titles[position]);
        txtComment.setText(comments[position]);
        //imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
