package dev.majed.checkdo;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.wunderlist.slidinglayer.SlidingLayer;

import java.util.Calendar;

import static dev.majed.checkdo.NotesActivity.superList;


public class CreateAList extends AppCompatActivity  {
    String Title;
    static int index;
    TextView listTitle;
    RecyclerView listView;
    ListView list2;
    EditText editText;
    static ExpandableListView expandableListView;
    FloatingActionButton fab;
    SlidingLayer slidingLayer;
    Button button;
    Calendar c;
    private CoordinatorLayout coordinatorLayout;

    static TaskAdapter tadp;
    static ExListAdapter exListAdapter;
    boolean isTimeSorted = true;
    public static Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_delete_this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        ctx=this;


        Title=getIntent().getStringExtra("title");
        index=getIntent().getIntExtra("index",-1);
        button=(Button)findViewById(R.id.button);

        expandableListView= (ExpandableListView)findViewById(R.id.expList);

        listTitle=(TextView)findViewById(R.id.list_title);
        listTitle.setText(Title);

        listView=(RecyclerView) findViewById(R.id.list);
        fab=(FloatingActionButton)findViewById(R.id.floatingActionButton);
        slidingLayer=(SlidingLayer)findViewById(R.id.slidingLayer);
        editText=(EditText)findViewById(R.id.editText);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinateLayout);


        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(editText.getText().toString().compareTo("")!=0) {

                    Calendar calendar=Calendar.getInstance();
                    Long TaskTime=calendar.getTime().getTime();
                    String taskName = editText.getText().toString();
                    Long taskId=TaskTime;
                    SingleEntry singleEntry=new SingleEntry(taskName, TaskTime,taskId);
                    superList.AddChildListElement(index,singleEntry);
                    NotesActivity.adapter.notifyDataSetChanged();
                    tadp.notifyDataSetChanged();
                    exListAdapter.notifyDataSetChanged();
                    slidingLayer.closeLayer(true);
                    exListAdapter.notifyDataSetChanged();
                    // the user is done typing.
                    exListAdapter= new ExListAdapter(superList.arrayList.get(index).itemList,getApplicationContext(),index);
                    expandableListView.setAdapter(exListAdapter);
                    //setTime(taskId);
                }
            }
        });


        SampleListAdapter adapter = new SampleListAdapter(this, SampleTasks, imageId);
        list2=(ListView)findViewById(R.id.SampleTaskList);
        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editText.setText(SampleTasks[position]);
             }
        });
        list2.setAdapter(adapter);

         tadp = new TaskAdapter(superList.arrayList.get(index).itemList,index);
        listView.setAdapter(tadp);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        listView.setLayoutManager(linearLayoutManager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingLayer.openLayer(true);
            }
        });

        exListAdapter= new ExListAdapter(superList.arrayList.get(index).itemList,this,index);
        expandableListView.setAdapter(exListAdapter);

        expandableListView.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAList.this, NotesActivity.class);
                intent.putExtra("keyTo","notFirstOpen");
                startActivity(intent);

            }
        });
    }
  //AddSetTimeMethodHere

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)

   //AddSetTimeMethodHere.


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
                item.setTitle("Sort: ");
                expandableListView.setVisibility(View.VISIBLE);
                listView.setVisibility(View.INVISIBLE);
                isTimeSorted=true;
                item.setTitle("Sort by : List");

            }
             return true;
        }
        return super.onOptionsItemSelected(item);
    }

   public static void DeleteTask(int index, Long id){
        superList.RemoveChildListElement(index,id);
        NotesActivity.adapter.notifyDataSetChanged();
        exListAdapter.notifyDataSetChanged();
        tadp.notifyDataSetChanged();
       exListAdapter= new ExListAdapter(superList.arrayList.get(index).itemList,ctx,index);
       expandableListView.setAdapter(exListAdapter);
   }
    String[] SampleTasks = {
            "Call",
            "read",
            "Email",
            "Buy",
            "Meet / Schedule",
            "Pay",
            "Remind"
    } ;
    Integer[] imageId = {
            R.drawable.call,
            R.drawable.read,
            R.drawable.email,
            R.drawable.buy,
            R.drawable.schedule,
            R.drawable.pay,
            R.drawable.remind
    };

}
