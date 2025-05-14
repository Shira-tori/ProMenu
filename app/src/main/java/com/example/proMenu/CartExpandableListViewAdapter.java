package com.example.proMenu;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CartExpandableListViewAdapter extends BaseExpandableListAdapter {

    ArrayList<String> group;
    HashMap<String, ArrayList<String>> children;
    public CartExpandableListViewAdapter(ArrayList<String> stores, HashMap<String, ArrayList<String>> items){
        this.group = stores;
        this.children = items;
    }
    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return children.get(group.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return children.get(group.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View expandableListGroupItemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        TextView groupText = expandableListGroupItemView.findViewById(android.R.id.text1);
        groupText.setText(group.get(groupPosition));
        return expandableListGroupItemView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View expandableListChildItemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_selectable_list_item, parent, false);
        TextView childText = expandableListChildItemView.findViewById(android.R.id.text1);
        childText.setText(children.get(group.get(groupPosition)).get(childPosition));
        return expandableListChildItemView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
