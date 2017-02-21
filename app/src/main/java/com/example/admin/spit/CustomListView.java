package com.example.admin.spit;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 17/01/2017.
 */

public class CustomListView extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<String> values_titles;


    public CustomListView(Activity context,ArrayList<String> values_titles)
    {
        super(context,R.layout.list_view_items,values_titles);
        this.context=context;
        this.values_titles=values_titles;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getLayoutInflater();

        View rowView=layoutInflater.inflate(R.layout.list_view_items,null);
        TextView titleView=(TextView)rowView.findViewById(R.id.list_view_title);
        TextView descriptionView=(TextView)rowView.findViewById(R.id.list_view_description);
        titleView.setText(values_titles.get(position));

        return rowView;
    }
}
