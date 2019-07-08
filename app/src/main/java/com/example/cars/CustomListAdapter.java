package com.example.cars;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;


public class CustomListAdapter extends ArrayAdapter<Car> {

    Context context;
    String[] titles;
    String[] urls;

    public CustomListAdapter(Context context, String[] titles, String[] urls) {
        super(context, R.layout.content_list);
        this.titles = titles;
        this.urls = urls;
        this.context = context;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //set views
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.content_list, parent, false);
            viewHolder.photo = convertView.findViewById((R.id.imageView));
            viewHolder.title = convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //set texts and load image
        Picasso.get().load(urls[position]).into(viewHolder.photo);
        viewHolder.title.setText(titles[position]);

        return convertView;
    }

    static class ViewHolder{
        ImageView photo;
        TextView title;
    }
}
