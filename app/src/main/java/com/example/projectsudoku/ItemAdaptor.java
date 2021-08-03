package com.example.projectsudoku;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



public class ItemAdaptor extends ArrayAdapter<String> {
    public ItemAdaptor(@NonNull Context context, int resource, int textViewResourceId, @NonNull String[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    private View populateView(View view, int position){
        String sudoku = this.getItem(position);
        TextView text = view.findViewById(R.id.sudokuType);
        text.setText(sudoku);
        return view;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view =  super.getView(position, convertView, parent);
        return populateView(view, position);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view =  super.getDropDownView(position, convertView, parent);
        populateView(view, position);
        view.setBackgroundColor(position%2==0? Color.WHITE : Color.LTGRAY);
        return view;
    }
}
