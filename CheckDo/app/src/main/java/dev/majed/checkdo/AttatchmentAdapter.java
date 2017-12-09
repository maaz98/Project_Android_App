package dev.majed.checkdo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;


public class AttatchmentAdapter extends
        RecyclerView.Adapter<AttatchmentAdapter.MyViewHolder> {

    Context ctx;
    ArrayList<SingleAttatchment> list = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        ImageView imageView;
        ImageView bin;
        View mView;
        Context ctx;

         public MyViewHolder(final View itemView) {
            super(itemView);
            mView = itemView;
            this.imageView=(ImageView)mView.findViewById(R.id.img);
            this.bin=(ImageView)mView.findViewById(R.id.bin);
        }

    }

     public AttatchmentAdapter(ArrayList<SingleAttatchment> mData,Context ctx) {

        this.list=new ArrayList<>();
        this.list.clear();
        this.list = mData;
        this.ctx=ctx;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        switch(list.get(position).getType()) {
            case 1:
            case 2:
                if(list.get(position).getSource()!=null)
                Picasso.with(ctx).load(new File(list.get(position).getSource())).into(holder.imageView);
                else{
                    Log.e("error","null");
                }
                break;
            case 3: Picasso.with(ctx).load(R.drawable.video_cam).into(holder.imageView);
                break;
            case 4: Picasso.with(ctx).load(R.drawable.ic_attach_file_black_24dp).into(holder.imageView);
                break;
            case 5:
            case 6: Picasso.with(ctx).load(R.drawable.audio_file).into(holder.imageView);
                break;
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(list.get(position).getType()==1){
                    Intent myIntent = new Intent(Intent.ACTION_VIEW);
                    String source = list.get(position).getSource();
                    File file = new File(source);
                    Log.e("Uri",source+"");
                  //String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
                    myIntent.setDataAndType(Uri.fromFile(file),"image/*");
                    ctx.startActivity(myIntent);
                }
                else if(list.get(position).getType()==6||list.get(position).getType()==5){
                    Intent myIntent = new Intent(Intent.ACTION_VIEW);
                    String source = list.get(position).getSource();
                    File file = new File(source);
                    Log.e("Uri",source+"");
                    //  String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
                    myIntent.setDataAndType(Uri.fromFile(file),"audio/*");
                    ctx.startActivity(myIntent);
                }
              else {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW);
                    String source = list.get(position).getSource();
                    File file = new File(source);
                    Log.e("Uri",source+"");
                    //  String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
                    String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
                    String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                    Log.e("mimeType", mimetype + " ");
                    Log.e("extension", extension + " ");
                    if(mimetype!=null)
                    {  myIntent.setDataAndType(Uri.fromFile(file),mimetype);}
                    else{
                        myIntent.setDataAndType(Uri.fromFile(file),"*/*");
                    }
                    ctx.startActivity(myIntent);


                   /* Intent myIntent = new Intent(Intent.ACTION_VIEW);
                    String source = list.get(position).getSource();
                    File file = new File(source);
                    Log.e("Uri", source + "");
                    String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
                    String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                    myIntent.setDataAndType(Uri.fromFile(file), mimetype);
                    ctx.startActivity(myIntent);
*/
                  /*  Log.e("mimeType", mimetype + " ");
                    Log.e("extension", extension + " ");*/


                }
            }
        });
        holder.bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddAttatchment.remove(position);
            }
        });

       /* holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW);
                    File file = new File(list.get(position).getSource());
                    String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
                    String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                    myIntent.setDataAndType(Uri.fromFile(file),mimetype);
                    ctx.startActivity(myIntent);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });*/
/*
        if(list.get(position).getType()==1){
            Picasso.with(ctx).load(list.get(position).getUri()).into(holder.imageView);
        }
        else if(list.get(position).getType()==2||list.get(position).getType()==3) {
            Picasso.with(ctx).load(R.drawable.ic_attach_file_black_24dp).into(holder.imageView);

          }*/
    }




    @Override
    public int getItemCount() {
         return list.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attatchment_item_layout,parent, false);
        return new MyViewHolder(v);
    }
}