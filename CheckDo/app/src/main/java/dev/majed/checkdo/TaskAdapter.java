package dev.majed.checkdo;


import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static dev.majed.checkdo.CreateAList.tadp;
import static dev.majed.checkdo.MainData.allArrayList;
import static dev.majed.checkdo.MainData.deleteTask;
import static dev.majed.checkdo.MainData.notifyDataChanged;
import static dev.majed.checkdo.MainData.save;
import static dev.majed.checkdo.R.id.txt;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private ArrayList<SingleEntry> taskList;
    Context ctx;
    int a=0;
    AddToIntrested addToIntrested;


    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView Title;
        ImageView bin;
        View mView;
        CheckBox checkBox;
        TextView timeOfTask;
        ShineButton shineBtn;

        public MyViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            this. Title=(TextView) mView.findViewById(txt);
            this.bin=(ImageView)mView.findViewById(R.id.img);
            this.checkBox=(CheckBox) mView.findViewById(R.id.checkBtn);
            this.timeOfTask=(TextView)mView.findViewById(R.id.time);
            this.shineBtn = (ShineButton)mView.findViewById(R.id.shineButton);
            ctx=mView.getContext();
            addToIntrested=new AddToIntrested(ctx);

        }
    }

    public TaskAdapter( ArrayList<SingleEntry> taskList,int index) {
        this.taskList=new ArrayList<>();
        this.taskList = taskList;
        a=index;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

         holder.Title.setText(taskList.get(position).getTaskName());
        if(taskList.get(position).getDone()){
            holder.checkBox.setChecked(true);
            /// holder.bin.setVisibility(View.VISIBLE);
            holder.mView.setAlpha((float)0.5);
        }
        else{
            holder.checkBox.setChecked(false);
          //   holder.bin.setVisibility(View.INVISIBLE);
            holder.mView.setAlpha(1);
        }
        Date date = new Date(taskList.get(position).getTaskTime());
        DateFormat formatter = new SimpleDateFormat("dd/MM/YY  hh:mm aaa");
        String dateFormatted = formatter.format(date);
        holder.timeOfTask.setText(dateFormatted);

        if(a==9999){
            holder.bin.setVisibility(View.INVISIBLE);
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
        holder.bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask(a,taskList.get(position).getTaskId());
                notifyDataChanged();
                tadp.notifyDataSetChanged();
                save();
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicked(a,position);
            }
        });
        final long TaskId = taskList.get(position).getTaskId();

        if(addToIntrested.isEventAlreadyPresent(String.valueOf(TaskId))){
            Log.e("RefreshFor",String.valueOf(TaskId));
            holder.shineBtn.setChecked(true);

        }
        holder.shineBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(addToIntrested.isEventAlreadyPresent(String.valueOf(TaskId))){
                    addToIntrested.remove(String.valueOf(TaskId));
                }
                else{
                    Log.e("AddedFromsmall",String.valueOf(TaskId));
                    //String d=(eventList.get(position).getEventDate());
                    Date date = new Date(taskList.get(position).getTaskTime());
                    addToIntrested.add(String.valueOf(TaskId),date,taskList.get(position).getTaskName());
                }
            }
        });
    }

    private void clicked(int parentPos, int position) {
     if(allArrayList.get(parentPos).getItemList().get(position).getDone()){
         allArrayList.get(parentPos).getItemList().get(position).setDone(false);
     }
     else{
         allArrayList.get(parentPos).getItemList().get(position).setDone(true);
     }
     save();
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sample_list_item2,parent, false);
        return new MyViewHolder(v);
    }
}