package dev.majed.checkdo;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static dev.majed.checkdo.CreateAList.DeleteTask;
import static dev.majed.checkdo.R.id.txt;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private ArrayList<SingleEntry> taskList;
    Context ctx;
    int a=0;

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView Title;
        ImageView bin;
        View mView;

        public MyViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            this. Title=(TextView) mView.findViewById(txt);
            this.bin=(ImageView)mView.findViewById(R.id.img);
            ctx=mView.getContext();
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
        if(a==9999){
            holder.bin.setVisibility(View.INVISIBLE);
        }
        holder.bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteTask(a,taskList.get(position).getTaskId());
            }
        });
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