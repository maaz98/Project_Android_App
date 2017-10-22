package dev.majed.checkdo;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import static dev.majed.checkdo.CreateAList.DeleteTask;
//Adapter Class for displaying tasks when sorted by time.

public class ExListAdapter extends BaseExpandableListAdapter {

    private ArrayList<SingleEntry> taskList;
    private ArrayList<ArrayList<SingleEntry>> sortedList;
    private ArrayList<SingleEntry> past;
    private ArrayList<SingleEntry> today;
    private ArrayList<SingleEntry> tomorrow;
    private ArrayList<SingleEntry> someday;

    private int[] NumArr;
    Context context;
    private int index=0;
     ExListAdapter(ArrayList<SingleEntry> taskList, Context context,int index){
       this.taskList=new ArrayList<>();
       this.taskList=taskList;
       this.context=context;
       this.index=index;

       Calendar date = Calendar.getInstance();
// reset hour, minutes, seconds and millis
       date.set(Calendar.HOUR_OF_DAY, 0);
       date.set(Calendar.MINUTE, 0);
       date.set(Calendar.SECOND, 0);
       date.set(Calendar.MILLISECOND, 0);

         sortedList=new ArrayList<>();
         past=new ArrayList<>();
         today=new ArrayList<>();
         tomorrow=new ArrayList<>();
         someday=new ArrayList<>();
         sortedList.add(past);
         sortedList.add(today);
         sortedList.add(tomorrow);
         sortedList.add(someday);

// sorting the tasks by time.
       date.add(Calendar.DAY_OF_MONTH, 1);
       long timeToday = date.getTimeInMillis();
       date.add(Calendar.DAY_OF_MONTH, 1);
       long timeTomorrow  = date.getTimeInMillis();
       date.add(Calendar.DAY_OF_MONTH, -2);
       long yesterday  = date.getTimeInMillis();
       for(int i=0;i<taskList.size();i++){

           String taskName = taskList.get(i).getTaskName();
           Long time = taskList.get(i).getTaskTime();
           Long id= taskList.get(i).getTaskId();
           SingleEntry task= new SingleEntry(taskName,time,id);
           Log.e("time",String.valueOf(time));

           if(time<=yesterday){
               sortedList.get(0).add(task);
           }
           else if(time>yesterday&&time<timeToday){
               sortedList.get(1).add(task);
           }
           else if(time>=timeToday&&time<timeTomorrow){
               sortedList.get(2).add(task);
           }
           else if(time>=timeTomorrow){
               sortedList.get(3).add(task);
           }
        }
       NumArr = new int[4];
       NumArr[0]=sortedList.get(0).size();
       NumArr[1]=sortedList.get(1).size();
       NumArr[2]=sortedList.get(2).size();
       NumArr[3]=sortedList.get(3).size();
   }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return 4;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return NumArr[groupPosition];
    }

    @Override
    public ArrayList getGroup(int groupPosition) {
        return sortedList.get(groupPosition);}

    @Override
    public String getChild(int groupPosition, int childPosition) {
       return  sortedList.get(groupPosition).get(childPosition).getTaskName();}


    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return sortedList.get(groupPosition).get(childPosition).getTaskId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView textView = new TextView(context);
        String name="";
        if(groupPosition==0){name= "Past";}
        else if (groupPosition==1){name= "Today";}
        else if(groupPosition==2){name= "Tomorrow";}
        else if(groupPosition==3){name= "Someday";}
        textView.setText(name);
        textView.setTextSize(23);
        textView.setPadding(10,5,5,5);
        return textView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        String name="";
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.sample_list_item2, null);
        }
        TextView sequence = (TextView) convertView.findViewById(R.id.txt);
        if(groupPosition==0){name= past.get(childPosition).getTaskName();}
        else if (groupPosition==1){name= today.get(childPosition).getTaskName();}
        else if(groupPosition==2){name= tomorrow.get(childPosition).getTaskName();}
        else if(groupPosition==3){name= someday.get(childPosition).getTaskName();}
        sequence.setText(name);

        ImageView bin = (ImageView)convertView.findViewById(R.id.img);
         if(index==9999){
          bin.setVisibility(View.INVISIBLE);
        }
        bin.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Long id= getChildId(groupPosition,childPosition);
             DeleteTask(index,id);
         }
     });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}
