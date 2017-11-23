package dev.majed.checkdo;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.wunderlist.slidinglayer.SlidingLayer;

import java.util.ArrayList;

import static dev.majed.checkdo.MainData.allArrayList;
import static dev.majed.checkdo.MainData.notifyDataChanged;
import static dev.majed.checkdo.MainData.save;


public class AllListUI extends AppCompatActivity {
    String Title;
    static int index;
    TextView listTitle;
    RecyclerView listView;
    EditText editText;
    static ExpandableListView expandableListView;
    FloatingActionButton fab;
    SlidingLayer slidingLayer;
    static TaskAdapter tadp;
    static ExListAdapter exListAdapter;
    Button button;
    boolean isTimeSorted = false;
    public static Context ctx;
    ArrayList<SingleEntry> AllList;
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_delete_this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        ctx=this;
        button=(Button)findViewById(R.id.button);
        Title="All Lists";
        index=getIntent().getIntExtra("index",-1);

        expandableListView= (ExpandableListView)findViewById(R.id.expList);
        listTitle=(TextView)findViewById(R.id.list_title);
        listTitle.setText(Title);
        listView=(RecyclerView) findViewById(R.id.list);
        fab=(FloatingActionButton)findViewById(R.id.floatingActionButton);
        slidingLayer=(SlidingLayer)findViewById(R.id.slidingLayer);
        editText=(EditText)findViewById(R.id.editText);
         AllList=new ArrayList();

        for(int j=0;j< allArrayList.size();j++){
        for(int i=0;i< allArrayList.get(j).getItemList().size();i++){
                        AllList.add(allArrayList.get(j).getItemList().get(i));
        }}

        tadp = new TaskAdapter(AllList,9999);
        listView.setAdapter(tadp);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        listView.setLayoutManager(linearLayoutManager);

        Log.e("allList",String.valueOf(AllList));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingLayer.openLayer(true);
            }
        });
        fab.setVisibility(View.INVISIBLE);
        exListAdapter= new ExListAdapter(AllList,this,9999);
        expandableListView.setAdapter(exListAdapter);

        expandableListView.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
        //listView.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllListUI.this, NotesActivity.class);
                intent.putExtra("keyTo","notFirstOpen");
                startActivity(intent);

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.choice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_choice) {
            if(isTimeSorted==true){

                //exListAdapter.notifyDataSetChanged();
                expandableListView.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                isTimeSorted=false;
                item.setTitle("Sort by : Time");
            }else{

                expandableListView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
                isTimeSorted=true;
                item.setTitle("Sort by : List");

            }
            // moveToUserProfile();
            return true;
        }
        if(id==R.id.clear){
            //removeAllChecked(Indx,checked);
           /* for(int i=0;i<checked.size();i++){
                deleteTask(Indx,checked.get(i));}
            checked=new ArrayList<>();*/
            save();
            notifyDataChanged();
            tadp.notifyDataSetChanged();
            exListAdapter.notifyDataSetChanged();
           // exListAdapter = new ExListAdapter( AllList,this,9999);
            expandableListView.setAdapter(exListAdapter);
            if(index==9999){
                updateAllList();
                exListAdapter= new ExListAdapter(AllList,this,9999);
                expandableListView.setAdapter(exListAdapter);
            }
           /* exListAdapter.notifyDataSetChanged();
            tadp.notifyDataSetChanged();
            exListAdapter = new ExListAdapter( allArrayList.get(index).getItemList(),getApplicationContext(),index);
            expandableListView.setAdapter(exListAdapter);
            tadp = new TaskAdapter( allArrayList.get(index).getItemList(),index);
            recyclerView.setAdapter(tadp);*/
        }
        return super.onOptionsItemSelected(item);
    }



public void updateAllList(){
    for(int j=0;j< allArrayList.size();j++){
        for(int i=0;i< allArrayList.get(j).getItemList().size();i++){
            AllList.add(allArrayList.get(j).getItemList().get(i));
        }}

}

}
