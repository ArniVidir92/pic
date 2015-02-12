package com.development.napptime.pix;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Napptime on 2/10/15.
 */


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
public class groupPhotoList extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] titles;
    private final String[] comments;
    private final Integer[] imageId;
    public groupPhotoList(Activity context,
                          String[] titles, String[] comments, Integer[] imageId) {
        super(context, R.layout.list_single, titles);
        this.context = context;
        this.titles = titles;
        this.comments = comments;
        this.imageId = imageId;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView1);
        TextView txtComment= (TextView) rowView.findViewById(R.id.textView2);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1);
        txtTitle.setText(titles[position]);
        txtComment.setText(comments[position]);
        //imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
