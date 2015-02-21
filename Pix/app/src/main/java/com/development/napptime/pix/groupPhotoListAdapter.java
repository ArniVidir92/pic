package com.development.napptime.pix;


import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Napptime on 2/10/15.
 */

public class groupPhotoListAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] titles;
    private final String[] comments;
    private final Bitmap[] bitmap;
    private int width;
    private int height;
    public groupPhotoListAdapter(Activity context,
                                 String[] titles, String[] comments, Bitmap[] bitmap) {
        super(context, R.layout.photo_row, titles);
        this.context = context;
        this.titles = titles;
        this.comments = comments;
        this.bitmap = bitmap;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.photo_row, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.photoListTitle);
        TextView txtComment= (TextView) rowView.findViewById(R.id.photoListSubTitle);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.photoListPhoto);
        txtTitle.setText(titles[position]);
        txtComment.setText(comments[position]);
        //Bitmap resized = Bitmap.createScaledBitmap(bitmap[position], width, height, true);
        imageView.setImageBitmap(bitmap[position]);
        return rowView;
    }
}
