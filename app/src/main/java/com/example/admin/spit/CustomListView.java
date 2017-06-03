package com.example.admin.spit;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 17/01/2017.
 */

public class CustomListView extends ArrayAdapter<Topics>{

    private final Activity context;
    private final ArrayList<Topics> values;


    public CustomListView(Activity context,ArrayList<Topics> values)
    {
        super(context,R.layout.list_view_items,values);
        this.context=context;
        this.values=values;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getLayoutInflater();
        View rowView=layoutInflater.inflate(R.layout.list_view_items,null);
        TextView titleView=(TextView)rowView.findViewById(R.id.list_view_title);
        TextView descriptionView=(TextView)rowView.findViewById(R.id.list_view_description);
        TextView dateView=(TextView)rowView.findViewById(R.id.listViewDate);
        titleView.setText(values.get(position).title);
        descriptionView.setText(values.get(position).description);
        return rowView;
    }
}
