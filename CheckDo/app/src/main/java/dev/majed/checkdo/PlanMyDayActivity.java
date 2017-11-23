package dev.majed.checkdo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.wunderlist.slidinglayer.SlidingLayer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static dev.majed.checkdo.MainData.ChangeTimeById;
import static dev.majed.checkdo.MainData.callendarList;
import static dev.majed.checkdo.MainData.deleteTaskById;
import static dev.majed.checkdo.MainData.fetchCalenderList;
import static dev.majed.checkdo.MainData.save;


public class PlanMyDayActivity extends AppCompatActivity {

    private SlidingLayer eventLayer;
    LinearLayout event1;
    HorizontalScrollView event2;
    LinearLayout event3;
    long TaskId;
    String TaskName = "testTask";
    String TaskTime;
     AddToIntrested addToIntrested;
    LinearLayout event_1_1;
    LinearLayout event_1_2;
    LinearLayout event_1_3;
    LinearLayout event_1_4;
    LinearLayout event_2_1;
    LinearLayout event_2_2;
    LinearLayout event_2_3;
    LinearLayout event_2_4;
    LinearLayout event_2_5;
    LinearLayout event_2_6;
    LinearLayout event_2_7;
    LinearLayout event_3_1;
    LinearLayout event_3_2;
    LinearLayout event_3_3;
    LinearLayout event_3_4;

    ArrayList<SingleEntry> list = new ArrayList<>();

    TextView num_task;
    TextView task_title;
    TextView task_time;
    TextView more_num_tasks;

    public static int index = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_plan_my_day);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        index = 0;

        TaskId = new Date().getTime();

        event1 = findViewById(R.id.event1);
        event2 = findViewById(R.id.event2);
        event3 = findViewById(R.id.event3);

        event1.setVisibility(View.VISIBLE);
        event2.setVisibility(View.GONE);
        event3.setVisibility(View.GONE);

        addToIntrested = new AddToIntrested(this);

        num_task=findViewById(R.id.num_tasks);
        task_time = findViewById(R.id.task_time);
        task_title = findViewById(R.id.task_title);
        more_num_tasks=findViewById(R.id.more_num_tasks);


        long startingTime = getStartOfDay().getTime();
       // long endingTime = startingTime + 24*60*60*1000;
        fetchCalenderList();
       for(int i=0;i<callendarList.size();i++){
           for(int j=0;j<callendarList.get(i).size();j++){
               if((callendarList.get(i).get(j).getTaskTime()-startingTime) <= 24*60*60*1000 &&(callendarList.get(i).get(j).getTaskTime()-startingTime)>=0){
                   list.add(callendarList.get(i).get(j));
               }
           }
       }
        num_task.setText(list.size()+" Tasks Today");

       for(int h=0;h<list.size();h++){
           Log.e("name",list.get(h).getTaskName());
           Log.e("time",String.valueOf(new Date(list.get(h).getTaskTime()).toLocaleString()));

       }

        next();

        event_1_1 = (LinearLayout)findViewById(R.id.event_1_1);
        event_1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event1.setVisibility(View.GONE);
                event2.setVisibility(View.VISIBLE);
            }
        });

         event_1_2 = (LinearLayout)findViewById(R.id.event_1_2);
        event_1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event1.setVisibility(View.GONE);
                event3.setVisibility(View.VISIBLE);
            }
        });

         event_1_3 = (LinearLayout)findViewById(R.id.event_1_3);
        event_1_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChecked();
                next();
            }
        });

         event_1_4 = (LinearLayout)findViewById(R.id.event_1_4);
        event_1_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               deleteEvent();
                next();
            }
        });

         event_2_1 = (LinearLayout)findViewById(R.id.event_2_1);
        event_2_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addToIntrested.isEventAlreadyPresent(String.valueOf(TaskId))){
                    addToIntrested.remove(String.valueOf(TaskId));
                }
                next();
            }
        });

         event_2_2 = (LinearLayout)findViewById(R.id.event_2_2);
        event_2_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(addToIntrested.isEventAlreadyPresent(String.valueOf(TaskId))){
                    addToIntrested.remove(String.valueOf(TaskId));
                }
                Date date = new Date(getStartOfDay().getTime() + 9*60*60*1000);
                addToIntrested.add(String.valueOf(TaskId),date,TaskName);
                next();
                Toast.makeText(PlanMyDayActivity.this, "Will remind you at 9 AM", Toast.LENGTH_SHORT).show();
            }
        });

         event_2_3 = (LinearLayout)findViewById(R.id.event_2_3);
        event_2_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addToIntrested.isEventAlreadyPresent(String.valueOf(TaskId))){
                    addToIntrested.remove(String.valueOf(TaskId));
                }
                Date date = new Date(getStartOfDay().getTime() + 12*60*60*1000);
                addToIntrested.add(String.valueOf(TaskId),date,TaskName);
                next();
                Toast.makeText(PlanMyDayActivity.this, "Will remind you at 12 noon", Toast.LENGTH_SHORT).show();
            }
        });

         event_2_4 = (LinearLayout)findViewById(R.id.event_2_4);
        event_2_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addToIntrested.isEventAlreadyPresent(String.valueOf(TaskId))){
                    addToIntrested.remove(String.valueOf(TaskId));
                }
                Date date = new Date(getStartOfDay().getTime() + 16*60*60*1000);
                addToIntrested.add(String.valueOf(TaskId),date,TaskName);
                next();
                Toast.makeText(PlanMyDayActivity.this, "Will remind you at 4 PM", Toast.LENGTH_SHORT).show();
            }
        });

         event_2_5 = (LinearLayout)findViewById(R.id.event_2_5);
        event_2_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addToIntrested.isEventAlreadyPresent(String.valueOf(TaskId))){
                    addToIntrested.remove(String.valueOf(TaskId));
                }
                Date date = new Date(getStartOfDay().getTime() + 19*60*60*1000);
                addToIntrested.add(String.valueOf(TaskId),date,TaskName);
                next();
                Toast.makeText(PlanMyDayActivity.this, "Will remind you at 7 PM", Toast.LENGTH_SHORT).show();
            }
        });

        event_2_6 = (LinearLayout)findViewById(R.id.event_2_6);
        event_2_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addToIntrested.isEventAlreadyPresent(String.valueOf(TaskId))){
                    addToIntrested.remove(String.valueOf(TaskId));
                }
                Date date = new Date(getStartOfDay().getTime() + 22*60*60*1000);
                addToIntrested.add(String.valueOf(TaskId),date,TaskName);
                next();
                Toast.makeText(PlanMyDayActivity.this, "Will remind you at 10 PM", Toast.LENGTH_SHORT).show();
            }
        });

        event_2_7 = (LinearLayout)findViewById(R.id.event_2_7);
        event_2_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addToIntrested.isEventAlreadyPresent(String.valueOf(TaskId))){
                    addToIntrested.remove(String.valueOf(TaskId));
                }


                TimePickerDialog.OnTimeSetListener listener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        long add=((hourOfDay*60)+minute)*60*1000;
                        Date date = new Date(getStartOfDay().getTime() + add);
                        addToIntrested.add(String.valueOf(TaskId),date,TaskName);

                        String time=date.toLocaleString();
                        Toast.makeText(PlanMyDayActivity.this, "Will remind you at " +time , Toast.LENGTH_SHORT).show();
                        save();
                        next();

                    }
                };
                final java.util.Date currentTime = Calendar.getInstance().getTime();
                TimePickerDialog dialog =new TimePickerDialog(view.getContext(), AlertDialog.THEME_HOLO_DARK, listener, currentTime.getHours() , currentTime.getMinutes(), false);
                dialog.show();
            }
        });

        event_3_1 = (LinearLayout)findViewById(R.id.event_3_1);
        event_3_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               advanceTime(24*60*60*1000);
                Toast.makeText(PlanMyDayActivity.this, "Task shifted to tomorrow", Toast.LENGTH_SHORT).show();

            }
        });

        event_3_2 = (LinearLayout)findViewById(R.id.event_3_2);
        event_3_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advanceTime(2*24*60*60*1000);
                Toast.makeText(PlanMyDayActivity.this, "Task shifted to day after tomorrow", Toast.LENGTH_SHORT).show();


            }
        });
        event_3_3 = (LinearLayout)findViewById(R.id.event_3_3);
        event_3_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                advanceTime(7*24*60*60*1000);
                Toast.makeText(PlanMyDayActivity.this, "Task shifted to next week", Toast.LENGTH_SHORT).show();

            }
        });
        event_3_4 = (LinearLayout)findViewById(R.id.event_3_4);
        event_3_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    setDate();
            }
        });


         check_time();


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setDate() {
      DatePickerDialog dialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_DARK);
        dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar= Calendar.getInstance();
                calendar.set(year,month,dayOfMonth,0,0,0);
               advanceTime(calendar.getTime().getTime()-getStartOfDay().getTime());
                Toast.makeText(PlanMyDayActivity.this, "Task shifted to future", Toast.LENGTH_SHORT).show();

            }
        });
        dialog.show();
    }

    private void advanceTime(long plusTime) {
        ChangeTimeById(TaskId,list.get(index-1).getTaskTime()+plusTime);
        save();
        next();

    }

    private void check_time() {

        long timeStart = getStartOfDay().getTime();
        long timeNow = new Date().getTime();
        long time_9 = timeStart + 9*60*60*1000;
        long time_12 = timeStart + 12*60*60*1000;
        long time_4 = timeStart + 16*60*60*1000;
        long time_7 = timeStart + 19*60*60*1000;
        long time_10 = timeStart + 22*60*60*1000;

        if(timeNow>time_10){
            event_2_2.setVisibility(View.GONE);
            event_2_3.setVisibility(View.GONE);
            event_2_4.setVisibility(View.GONE);
            event_2_5.setVisibility(View.GONE);
            event_2_6.setVisibility(View.GONE);
        }
        else if(timeNow>time_7){
            event_2_2.setVisibility(View.GONE);
            event_2_3.setVisibility(View.GONE);
            event_2_4.setVisibility(View.GONE);
            event_2_5.setVisibility(View.GONE);
        }
        else if(timeNow>time_4){
            event_2_2.setVisibility(View.GONE);
            event_2_3.setVisibility(View.GONE);
            event_2_4.setVisibility(View.GONE);
        }
        else if(timeNow>time_12){
            event_2_2.setVisibility(View.GONE);
            event_2_3.setVisibility(View.GONE);
        }
        else if(timeNow>time_9){
            event_2_2.setVisibility(View.GONE);
        }
    }

    private void deleteEvent() {
        deleteTaskById(TaskId);
        save();
        Toast.makeText(this, "Task Deleted", Toast.LENGTH_SHORT).show();

    }

    private void setChecked() {
    }

    private void next() {
if(list.size()-index>0){
        event1.setVisibility(View.VISIBLE);
        event2.setVisibility(View.GONE);
        event3.setVisibility(View.GONE);

        TaskName = list.get(index).getTaskName();
        TaskId = list.get(index).getTaskId();
       // TaskTime = list.get(index).getTaskDay();
         DateFormat formatter = new SimpleDateFormat("hh:mm aaa");
         String dateFormatted = formatter.format(new Date(list.get(index).getTaskTime()));
        task_time.setText(dateFormatted);
        task_title.setText(TaskName);
        more_num_tasks.setText("+ "+ (list.size()-index-1)  +" More");

        index++;
    }
    else{
    task_time.setText("");
    task_title.setText("No more tasks for today!");
    more_num_tasks.setText("");

    event1.setVisibility(View.GONE);
    event2.setVisibility(View.GONE);
    event3.setVisibility(View.GONE);

}
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


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
