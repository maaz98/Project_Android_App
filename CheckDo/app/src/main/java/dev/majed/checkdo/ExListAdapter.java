package dev.majed.checkdo;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static dev.majed.checkdo.CreateAList.checked;
import static dev.majed.checkdo.CreateAList.exListAdapter;
import static dev.majed.checkdo.CreateAList.expandableListView;
import static dev.majed.checkdo.CreateAList.tadp;
import static dev.majed.checkdo.MainData.allArrayList;
import static dev.majed.checkdo.MainData.deleteTask;
import static dev.majed.checkdo.MainData.notifyDataChanged;
import static dev.majed.checkdo.MainData.save;
//Adapter Class for displaying tasks when sorted by time.

public class ExListAdapter extends BaseExpandableListAdapter {

    private ArrayList<SingleEntry> taskList;
    private ArrayList<ArrayList<SingleEntry>> sortedList;
    private ArrayList<SingleEntry> past;
    private ArrayList<SingleEntry> today;
    private ArrayList<SingleEntry> tomorrow;
    private ArrayList<SingleEntry> upcoming;



    String dateTextToday;
    String dateTextTomorrow;

    private int[] NumArr;
    Context context;
    private int index=0;
    final AddToIntrested addToIntrested;

     ExListAdapter(ArrayList<SingleEntry> taskList, Context context,int index){
       this.taskList=new ArrayList<>();
       this.taskList=taskList;
       this.context=context;
       this.index=index;

         addToIntrested=new AddToIntrested(context);

         Date date = new Date();
         SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
         dateTextToday = df2.format(date);

         sortedList=new ArrayList<>();

         past=new ArrayList<>();
         today=new ArrayList<>();
         tomorrow=new ArrayList<>();
         upcoming=new ArrayList<>();

         sortedList.add(past);
         sortedList.add(today);
         sortedList.add(tomorrow);
         sortedList.add(upcoming);

         Calendar c = Calendar.getInstance();

      long timeToday = getStartOfDay().getTime();
         Log.e("Date###today",String.valueOf(getStartOfDay().getTime()));
      long timeTomorrow = timeToday + 24*60*60*1000;
         Log.e("Date###tomm",String.valueOf(timeTomorrow));

         long DayAfterTomm = timeToday + 2*24*60*60*1000;
         Log.e("Date###dayAfterTomm",String.valueOf(DayAfterTomm));


       for(int i=0;i<taskList.size();i++){

           String taskName = taskList.get(i).getTaskName();
           Long time = taskList.get(i).getTaskTime();
           Long id= taskList.get(i).getTaskId();
           SingleEntry task= new SingleEntry(taskName,time,id);
           Log.e("time",String.valueOf(time));

           if(time<=timeToday){
               sortedList.get(0).add(task);
           }
           else if(time>timeToday&&time<timeTomorrow){
               sortedList.get(1).add(task);
           }
           else if(time>=timeTomorrow&&time<DayAfterTomm){
               sortedList.get(2).add(task);
           }
           else if(time>=DayAfterTomm){
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
        notifyDataSetChanged();
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

        LayoutInflater infalInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.sample_list_heading, null);
        TextView textView =(TextView)convertView.findViewById(R.id.txt);
        String name="";
        if(groupPosition==0){name= "Past";}
        else if (groupPosition==1){name= "Today";}
        else if(groupPosition==2){name= "Tomorrow";}
        else if(groupPosition==3){name= "Upcoming";}
        textView.setText(name);
        textView.setTextSize(23);
        textView.setPadding(10,5,5,5);
        TextView numberOfTasks =(TextView)convertView.findViewById(R.id.num);
        int number = sortedList.get(groupPosition).size();
        numberOfTasks.setText(number+" Tasks");

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.sample_list_item2, null);
        }
        TextView sequence = (TextView) convertView.findViewById(R.id.txt);
        sequence.setText(sortedList.get(groupPosition).get(childPosition).getTaskName());

        final TextView timeOfTasks = (TextView)convertView.findViewById(R.id.time);
        final ImageView bin = (ImageView)convertView.findViewById(R.id.img);
        final ImageView attatchment = (ImageView)convertView.findViewById(R.id.attatch);

        final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBtn);
        final ShineButton shineButton = (ShineButton) convertView.findViewById(R.id.shineButton);
        if(sortedList.get(groupPosition).get(childPosition).getDone()){
            //convertView.setAlpha((float) 0.5);
            checkBox.setChecked(true);
         }

        else{
           // convertView.setAlpha((float) 1);
             checkBox.setChecked(false);
         }


        Date date = new Date(sortedList.get(groupPosition).get(childPosition).getTaskTime());
        DateFormat formatter = new SimpleDateFormat("dd/MM/YY  hh:mm");
        String dateFormatted = formatter.format(date);
        timeOfTasks.setText(dateFormatted);

         checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 long id =  sortedList.get(groupPosition).get(childPosition).getTaskId();
                clicked(id);
               }
        });

        bin.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Long id= getChildId(groupPosition,childPosition);
             deleteTask(index,id);
             save();
             notifyDataChanged();
             tadp.notifyDataSetChanged();
             exListAdapter.notifyDataSetChanged();
             exListAdapter = new ExListAdapter( allArrayList.get(index).getItemList(),context,index);
             expandableListView.setAdapter(exListAdapter);
         }
     });
        attatchment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Long id= getChildId(groupPosition,childPosition);
                Intent intent = new Intent(context,AddAttatchment.class);
                intent.putExtra("id",id);
                context.startActivity(intent);*/
            }
        });
        final long TaskId = sortedList.get(groupPosition).get(childPosition).getTaskId();
        if(addToIntrested.isEventAlreadyPresent(String.valueOf(TaskId))){
            Log.e("RefreshFor",String.valueOf(TaskId));
            shineButton.setChecked(true);

        }
        shineButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(addToIntrested.isEventAlreadyPresent(String.valueOf(TaskId))){
                    addToIntrested.remove(String.valueOf(TaskId));
                }
                else{
                    Log.e("AddedFromsmall",String.valueOf(TaskId));
                    //String d=(eventList.get(position).getEventDate());
                    Date date = new Date(sortedList.get(groupPosition).get(childPosition).getTaskTime());
                    addToIntrested.add(String.valueOf(TaskId),date,sortedList.get(groupPosition).get(childPosition).getTaskName());
                }
            }
        });

        return convertView;
    }

    private void clicked(long id) {

        checked.add(id);
       /* for(int j=0;j<allArrayList.get(index).getItemList().size();j++)
        { if(allArrayList.get(index).getItemList().get(j).getTaskId().equals(id)){
            if(allArrayList.get(index).getItemList().get(j).getDone()){
                allArrayList.get(index).getItemList().get(j).setDone(false);
            }
            else{
                allArrayList.get(index).getItemList().get(j).setDone(true);
            }
        }
    }
        save();
        notifyDataChanged();
        tadp.notifyDataSetChanged();
        exListAdapter.notifyDataSetChanged();*/
       /* exListAdapter = new ExListAdapter( allArrayList.get(index).getItemList(),context,index);
        expandableListView.setAdapter(exListAdapter);*/
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
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

    private Date getStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTime();
    }

}
