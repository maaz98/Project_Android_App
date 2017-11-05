package dev.majed.checkdo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.wunderlist.slidinglayer.SlidingLayer;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static dev.majed.checkdo.MainData.adapter;
import static dev.majed.checkdo.MainData.allArrayList;
import static dev.majed.checkdo.MainData.fetch;
import static dev.majed.checkdo.MainData.removeAllChecked;
import static dev.majed.checkdo.MainData.save;


public class CreateAList extends AppCompatActivity  {

    String Title;
    static int index;
    TextView listTitle;
    RecyclerView recyclerView;
    ListView listView;
    static ExpandableListView expandableListView;
    FloatingActionButton fab;
    RecyclerView checkedListRecyclerView;
    EditText editText;
    SlidingLayer slidingLayer;

    Button button;

   public static TaskAdapter tadp;
   public static ExListAdapter exListAdapter;
    boolean isTimeSorted = true;
    public static Context ctx;
    int Indx;

    Button okButton;

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fetch();

        setContentView(R.layout.activity_delete_this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        ctx=this;


        Title=getIntent().getStringExtra("title");
        index=getIntent().getIntExtra("index",-1);
        button=(Button)findViewById(R.id.button);
        Indx=index;
        expandableListView= (ExpandableListView)findViewById(R.id.expList);

        listTitle=(TextView)findViewById(R.id.list_title);
        listTitle.setText(Title);

        recyclerView=(RecyclerView) findViewById(R.id.list);
        fab=(FloatingActionButton)findViewById(R.id.floatingActionButton);
        slidingLayer=(SlidingLayer)findViewById(R.id.slidingLayer);
        editText=(EditText)findViewById(R.id.editText);
        okButton = (Button)findViewById(R.id.okButton);

       okButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

                if(editText.getText().toString().compareTo("")!=0) {

                    Calendar calendar=Calendar.getInstance();
                    Long TaskTime=calendar.getTime().getTime();

                    String taskName = editText.getText().toString();
                    Long taskId=TaskTime;
                    SingleEntry singleEntry=new SingleEntry(taskName, TaskTime,taskId);
                    int pos= allArrayList.get(index).getItemList().size();
                    allArrayList.get(index).getItemList().add(singleEntry);
                    save();
                    //int pos =  getAllArrayList().get(index).getItemList().indexOf(singleEntry);

                    adapter.notifyDataSetChanged();
                    tadp.notifyDataSetChanged();
                    exListAdapter.notifyDataSetChanged();

                    slidingLayer.closeLayer(true);
                    exListAdapter.notifyDataSetChanged();
                    exListAdapter = new ExListAdapter( allArrayList.get(index).getItemList(),getApplicationContext(),index);
                    expandableListView.setAdapter(exListAdapter);
                    // the user is done typing.
                   /* exListAdapter= new ExListAdapter(superList.arrayList.get(index).itemList,getApplicationContext(),index);
                    expandableListView.setAdapter(exListAdapter);*/

                 /*   Date d = new Date(taskId);
                    SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                    String dateText = df2.format(d);
                    superList.saveChildEventTaskDay(index,taskId,dateText);*/
                    callToSetTime (taskId,index,pos);
                }
            }
        });


        SampleListAdapter adapter = new SampleListAdapter(this, SampleTasks, imageId);
        listView=(ListView)findViewById(R.id.SampleTaskList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editText.setText(SampleTasks[position]);
             }
        });
        listView.setAdapter(adapter);

        tadp = new TaskAdapter( allArrayList.get(index).getItemList(),index);
        recyclerView.setAdapter(tadp);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingLayer.openLayer(true);
            }
        });

        exListAdapter = new ExListAdapter( allArrayList.get(index).getItemList(),this,index);
        expandableListView.setAdapter(exListAdapter);

        expandableListView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);

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

  private void callToSetTime(Long id, final int index,  final int pos) {
      final String dateStr ="";

      View coordinatorLayout = findViewById(R.id.coordinateLayout);
      Snackbar snackbar = Snackbar
              .make(coordinatorLayout, "New Task Created", Snackbar.LENGTH_LONG)
              .setAction("Set Time", new View.OnClickListener() {
                  @RequiresApi(api = Build.VERSION_CODES.N)
                  @Override
                  public void onClick(View view) {

                      DatePickerDialog dialog = null;
                      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                          dialog = new DatePickerDialog(view.getContext(), AlertDialog.THEME_HOLO_DARK);
                      }

                      TimePickerDialog dialog2 = null;
                      TimePickerDialog.OnTimeSetListener listener= new TimePickerDialog.OnTimeSetListener() {
                          @Override
                          public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                             Long dateLong = allArrayList.get(Indx).getItemList().get(pos).getTaskTime();
                              Long timeLong = Long.valueOf((hourOfDay*60+ minute)*60*1000);
                              allArrayList.get(Indx).getItemList().get(pos).setTaskTime(dateLong+timeLong);
                              save();

                          }
                      };

                      final java.util.Date currentTime = Calendar.getInstance().getTime();
                      dialog2=new TimePickerDialog(view.getContext(), AlertDialog.THEME_HOLO_DARK, listener, currentTime.getHours() , currentTime.getMinutes(), false);

                      dialog.show();
                      final TimePickerDialog finalDialog = dialog2;
                      dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                          @Override
                          public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                              Calendar calendar= Calendar.getInstance();
                              calendar.set(year,month,dayOfMonth,0,0,0);
                              //Date date = new Date(year,month,dayOfMonth);
                              SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                              String dateText = df2.format(calendar.getTime());
                              Log.e("data",String.valueOf(Indx+"####"+pos));

                              allArrayList.get(Indx).getItemList().get(pos).setTaskDay(dateText);
                              allArrayList.get(Indx).getItemList().get(pos).setTaskTime(calendar.getTime().getTime());
                              save();
                              save();
                              //int pos =  getAllArrayList().get(index).getItemList().indexOf(singleEntry);

                              adapter.notifyDataSetChanged();
                              tadp.notifyDataSetChanged();
                              exListAdapter.notifyDataSetChanged();
                              exListAdapter = new ExListAdapter( allArrayList.get(index).getItemList(),getApplicationContext(),index);
                              expandableListView.setAdapter(exListAdapter);
                              finalDialog.show();
                          }
                      });
                  }
              });
      snackbar.show();


  }



    /*void setTime(Long id){
        callToSetTime();
        Log.e("thisIsTime1", time1);
        *//*for(int i=0;i<superList.arrayList.get(index).getItemList().size();i++){
            if(superList.arrayList.get(index).getItemList().get(i).getTaskId().equals(id)){
                superList.arrayList.get(index).getItemList().get(i).setTaskDay(time1);
            }

        }*//*
        Log.e("timeSetted",String.valueOf(time1));
    }*/
   // @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)

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
                recyclerView.setVisibility(View.VISIBLE);
                isTimeSorted=false;
                item.setTitle("Sort by : Time");

            }else{
                item.setTitle("Sort: ");
                expandableListView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                isTimeSorted=true;
                item.setTitle("Sort by : List");

            }

             return true;
        }
        if(id==R.id.clear){
            removeAllChecked(Indx);
            exListAdapter.notifyDataSetChanged();
            tadp.notifyDataSetChanged();
            exListAdapter = new ExListAdapter( allArrayList.get(index).getItemList(),getApplicationContext(),index);
            expandableListView.setAdapter(exListAdapter);
            tadp = new TaskAdapter( allArrayList.get(index).getItemList(),index);
            recyclerView.setAdapter(tadp);
        }
        return super.onOptionsItemSelected(item);
    }

   public static void DeleteTask(int index, int pos){
       allArrayList.get(index).getItemList().remove(pos);
        adapter.notifyDataSetChanged();
        exListAdapter.notifyDataSetChanged();
        tadp.notifyDataSetChanged();
        save();
       /*exListAdapter= new ExListAdapter(superList.arrayList.get(index).itemList,ctx,index);
       expandableListView.setAdapter(exListAdapter);*/
   }
   public static void setTaskChecked(int index,int pos,boolean isChecked){
       allArrayList.get(index).getItemList().get(pos).setDone(isChecked);
       adapter.notifyDataSetChanged();
       exListAdapter.notifyDataSetChanged();
       tadp.notifyDataSetChanged();
       save();

     /*  exListAdapter= new ExListAdapter(superList.arrayList.get(index).itemList,ctx,index);
       expandableListView.setAdapter(exListAdapter);*/
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
