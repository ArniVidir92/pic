package com.development.napptime.pix;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Napptime on 2/24/15.
 *
 * Þessi klasi er ekki notaður í augnablikinu, var hann notaður í gamla group viewinu. Hann gæti
 * haft smá notagildi og viljum við ganga úr skugga um að svo verði ekki áður en við eyðum honum.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.samplepic6, R.drawable.samplepic6,
            R.drawable.samplepic6, R.drawable.samplepic6,
            R.drawable.samplepic6, R.drawable.samplepic6,
            R.drawable.samplepic6, R.drawable.samplepic6,
            R.drawable.samplepic6, R.drawable.samplepic6,
            R.drawable.samplepic6, R.drawable.samplepic6,
            R.drawable.samplepic6, R.drawable.samplepic6,
            R.drawable.samplepic6, R.drawable.samplepic6,
            R.drawable.samplepic6, R.drawable.samplepic6,
            R.drawable.samplepic6, R.drawable.samplepic6,
            R.drawable.samplepic6, R.drawable.samplepic6
    };
}