package dev.majed.checkdo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adapter extends
        RecyclerView.Adapter<Adapter.MyViewHolder> {

    private ArrayList<MyList> mData;
    Context ctx;



    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView Title;
        TextView number;
        ImageView imageView;
        View mView;

        public MyViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            this. Title=(TextView) mView.findViewById(R.id.title_id);
            this. number=(TextView) mView.findViewById(R.id.number_id);
            this.imageView=(ImageView)mView.findViewById(R.id.bin);
            ctx=mView.getContext();
        }

    }

    public Adapter( ArrayList<MyList> mData) {
        this.mData=new ArrayList<>();
        this.mData = mData;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


    if (position == 0) {
        holder.Title.setText("All");
        holder.imageView.setVisibility(View.INVISIBLE);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, AllListUI.class);
                intent.putExtra("title", "ALL");
                intent.putExtra("index", position);
                ctx.startActivity(intent);
            }
        });

        int noOfItems=0;
        Log.e("09876543",String.valueOf(mData.size()));
        for(int i=0;i<mData.size();i++){
           noOfItems=noOfItems+ mData.get(i).getNumberOfItems();
        }
        holder.number.setText(String.valueOf(noOfItems) + " Items");
    } else {

        holder.Title.setText(mData.get(position-1).getItemName());
        holder.number.setText(String.valueOf(mData.get(position-1).getNumberOfItems()) + " Items");
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, CreateAList.class);
                intent.putExtra("title", mData.get(position-1).getItemName());
                intent.putExtra("index", position-1);
                ctx.startActivity(intent);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotesActivity.superList.deleteList(position-1);
                NotesActivity.adapter.notifyDataSetChanged();
            }
        });
    }
}



    @Override
    public int getItemCount() {

        return mData.size()+1;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item,parent, false);
        return new MyViewHolder(v);
    }
}