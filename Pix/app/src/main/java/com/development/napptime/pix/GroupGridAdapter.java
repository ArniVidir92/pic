package com.development.napptime.pix;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Napptime on 3/4/15.
 *
 * Klasi þessi sér um uppstillingu hópa og að búa til 'cards' fyrir hópa og raða þeim upp á
 * mismunandi skjástærðir eftir sem bestu getu.
 */

public class GroupGridAdapter extends BaseAdapter{
    private Context mContext;
    private final String[] groupName;
    private final Bitmap[] images;
    public GroupGridAdapter(Context c, String[] groupNames, Bitmap[] Imageid) {
        mContext = c;
        this.images = Imageid;
        this.groupName = groupNames;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return groupName.length;
    }
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);

            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            imageView.setPadding(8, 8, 8, 8);


            textView.setText(groupName[position]);
            imageView.setImageBitmap(images[position]);
        } else {
            grid = (View) convertView;
        }
        return grid;
    }
}