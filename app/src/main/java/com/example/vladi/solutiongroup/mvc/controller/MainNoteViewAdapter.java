package com.example.vladi.solutiongroup.mvc.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vladi.solutiongroup.R;
import com.example.vladi.solutiongroup.mvc.model.MainNoteItem;

import java.util.List;

public class MainNoteViewAdapter extends BaseAdapter {

    private static final String STATE_NORMAL = "normal";
    private static final String STATE_DELETE = "delete";

    private List<MainNoteItem> mDataProvider;
    private LayoutInflater mLayoutInflater;
    private String currentState = STATE_NORMAL;

    public MainNoteViewAdapter(Context context, List<MainNoteItem> dataProvider) {
        mDataProvider = dataProvider;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mDataProvider.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataProvider.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) view = mLayoutInflater.inflate(R.layout.main_note_item, parent, false);

        MainNoteItem item = (MainNoteItem) getItem(position);

        ImageView foreground = (ImageView) view.findViewById(R.id.mainListItemDeletionForeground);
        ImageView foregroundIcon = (ImageView) view.findViewById(R.id.mainListItemDeletionForegroundIcon);

        if (currentState.equals(STATE_NORMAL)) {
            foreground.setVisibility(View.GONE);
            foregroundIcon.setVisibility(View.GONE);
        } else if (currentState.equals(STATE_DELETE)) {
            foreground.setVisibility(View.VISIBLE);
            foregroundIcon.setVisibility(View.VISIBLE);
        }

        ((TextView) view.findViewById(R.id.mainListItemTitle)).setText(item.getName());


        return view;
    }

    public void setViewState(String state) {
        currentState = state;
        notifyDataSetChanged();
    }

}
