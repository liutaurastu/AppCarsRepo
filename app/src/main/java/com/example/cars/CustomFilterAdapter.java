package com.example.cars;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CustomFilterAdapter extends BaseAdapter {

    int count;
    Context mContext;
    LayoutInflater inflater;
    List<FilterArrayListClass> carsList = null;
    ArrayList<FilterArrayListClass> arrayList;
    double[] longit;
    double[] latit;

    public CustomFilterAdapter(Context context, int count, List<FilterArrayListClass> carsList, double[] longit, double[] latit) {

        this.count = count;
        this.mContext = context;
        this.carsList = carsList;
        this.longit = longit;
        this.latit = latit;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<FilterArrayListClass>();
        this.arrayList.addAll(carsList);
    }

    static class ViewHolder{
        ImageView photo;
        TextView title;
        TextView numberPlate;
        TextView batteryPercentage;
    }

    @Override
    public int getCount() {
        return carsList.size();
    }

    @Override
    public FilterArrayListClass getItem(int position) {
        return carsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //get views
        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.content_filter, null);

            viewHolder.photo = convertView.findViewById((R.id.imageView));
            viewHolder.title = convertView.findViewById(R.id.textViewTitle);
            viewHolder.numberPlate = convertView.findViewById(R.id.textViewNumberPlate);
            viewHolder.batteryPercentage = convertView.findViewById(R.id.textViewBatteryPercentage);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //set texts and load image
        Picasso.get().load(carsList.get(position).getUrls()).into(viewHolder.photo);
        viewHolder.title.setText("Car Title: " + carsList.get(position).getTitles());
        viewHolder.numberPlate.setText("Number Plate: " + carsList.get(position).getNumberPlates());
        viewHolder.batteryPercentage.setText("Battery Percentage: " + carsList.get(position).getBattPercentages());

        return convertView;
    }
    //filter by number plate adapter function
    public void filterByNumberPlate(String charText) {

        //use text from edit text(charText) to filter the list
        charText = charText.toLowerCase(Locale.getDefault());
        carsList.clear();
        if (charText.length() == 0) {
            carsList.addAll(arrayList);
        }
        else{
            for (FilterArrayListClass car : arrayList){
                if (car.getNumberPlates().toLowerCase(Locale.getDefault()).contains(charText)){
                    carsList.add(car);
                }
            }
        }
        notifyDataSetChanged();
    }
    //filter by battery percentage adapter function
    public void filterByBatteryPercentage(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        carsList.clear();
        if (charText.length() == 0) {
            carsList.addAll(arrayList);
        }
        else{
            for (FilterArrayListClass car : arrayList){
                if (String.valueOf(car.getBattPercentages()).toLowerCase(Locale.getDefault()).contains(charText)){
                    carsList.add(car);
                }
            }
        }
        notifyDataSetChanged();
    }
    //sort by distance adapter function
    public void sortByDistance(double lat, double lon)
    {
        carsList.clear();
        FilterArrayListClass temp;
        for (int x=0; x<arrayList.size(); x++){
            for (int i=0; i < arrayList.size()-x-i; i++)
            {
                if (arrayList.get(i).getDistance(lat, lon) > arrayList.get(i+1).getDistance(lat, lon))
                {
                    temp = arrayList.get(i);
                    arrayList.set(i, arrayList.get(i+1));
                    arrayList.set(i+1, temp);
                }
            }
        }
        carsList.addAll(arrayList);
    }
}
