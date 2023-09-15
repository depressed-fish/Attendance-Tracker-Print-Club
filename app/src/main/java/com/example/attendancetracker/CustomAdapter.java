package com.example.attendancetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;

    ArrayList<String> listID;
    ArrayList<String> listName;
    ArrayList<String> listCGPA;

    public CustomAdapter(Context context, ArrayList<String> listID, ArrayList<String> listName, ArrayList<String> listCGPA) {
        this.context = context;
        this.listID = listID;
        this.listName = listName;
        this.listCGPA = listCGPA;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listID.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.custom_list_data, null);
        TextView tvID = view.findViewById(R.id.tv_lv_ID);
        TextView tvName = view.findViewById(R.id.tv_lv_name);
        TextView tvCGPA = view.findViewById(R.id.tv_lv_cgpa);

        tvID.setText(listID.get(i));
        tvName.setText(listName.get(i));
        tvCGPA.setText(listCGPA.get(i));

        return view;
    }
}
