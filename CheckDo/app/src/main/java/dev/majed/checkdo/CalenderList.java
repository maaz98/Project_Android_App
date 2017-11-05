package dev.majed.checkdo;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
//This is Adapter class for calendar list.
public class CalenderList extends BaseExpandableListAdapter {

    Context context;
    ArrayList<ArrayList<SingleEntry>> thisList;
    ArrayList<String> datesList;
    CalenderList(Context context,ArrayList<ArrayList<SingleEntry>> list,ArrayList<String> datesList){
        this.context=context;
        this.thisList = new ArrayList<>();
        this.thisList = list;
        this.datesList = new ArrayList<>();
        this.datesList = datesList;
    }

    @Override
    public int getGroupCount() {
        return datesList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return thisList.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i*10000+i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.calender_heading, null);
        }
        TextView textView = view.findViewById(R.id.headingText);
        textView.setText(datesList.get(i));
        TextView textView2 = view.findViewById(R.id.num);
        textView2.setText(thisList.get(i).size()+" tasks");
        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

         if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.sample_list_heading, null);
        }
        TextView textView = convertView.findViewById(R.id.txt);
        textView.setText(thisList.get(groupPosition).get(childPosition).getTaskName());
        TextView textView2 = convertView.findViewById(R.id.num);
        textView2.setText(thisList.get(groupPosition).get(childPosition).getTaskDay());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
