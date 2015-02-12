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


public class CustomList extends BaseAdapter {

    String items[];
    Integer pictures[];
    LayoutInflater mInflater;

    public CustomList(Context context, String[] items, Integer[] imageId) {
        mInflater = LayoutInflater.from(context);
        this.items = items;
        this.pictures = imageId;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView ==null)
        {
            convertView = mInflater.inflate(R.layout.list_single,parent,false);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.textView1);
            holder.iv = (ImageView) convertView.findViewById(R.id.imageView1);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(items[position]);
        holder.iv.setImageResource(pictures[position]);
        holder = null;
        // use holder.iv to set whatever image you want according to the position
        return convertView;
    }

    static class ViewHolder
    {
        ImageView iv;
        TextView tv;
    }
}