package dev.majed.checkdo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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

public class AllListUI extends AppCompatActivity {

    String Title;
    static int index;
    TextView listTitle;
    RecyclerView listView;
    EditText editText;
    static ExpandableListView expandableListView;
    FloatingActionButton fab;
    SlidingLayer slidingLayer;
    private CoordinatorLayout coordinatorLayout;


    static TaskAdapter tadp;
    static ExListAdapter exListAdapter;
    Button button;
    boolean isTimeSorted = false;
    public static Context ctx;
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
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinateLayout);
        ArrayList<SingleEntry> AllList=new ArrayList();

        for(int i=0;i<NotesActivity.superList.arrayList.size();i++){
            for (int j=0;j<NotesActivity.superList.arrayList.get(i).getItemList().size();j++)
            {            AllList.add(NotesActivity.superList.arrayList.get(i).getItemList().get(j));
            }
        }


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
        return super.onOptionsItemSelected(item);
    }





}
