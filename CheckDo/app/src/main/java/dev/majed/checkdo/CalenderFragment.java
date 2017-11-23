package dev.majed.checkdo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.nlmartian.silkcal.DatePickerController;
import me.nlmartian.silkcal.DayPickerView;
import me.nlmartian.silkcal.SimpleMonthAdapter;

import static dev.majed.checkdo.MainData.callendarDatesList;
import static dev.majed.checkdo.MainData.callendarList;
import static dev.majed.checkdo.MainData.fetchCalenderList;


public class CalenderFragment extends Fragment implements DatePickerController {
DayPickerView calendarView;
Button button;
boolean open=false;
ExpandableListView expandableListView;
RecyclerView recyclerView;
String DateStr = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_calender, container, false);

        button = (Button) rootView.findViewById(R.id.button);
        calendarView = (DayPickerView) rootView.findViewById(R.id.calendar_view);
        calendarView.setController(this);
        calendarView.setVisibility(View.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (open) {
                    calendarView.setVisibility(View.INVISIBLE);
                    open = false;
                } else {
                    calendarView.setVisibility(View.VISIBLE);
                    open = true;
                }
            }
        });
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        DateStr = df2.format(calendar.getTime());

        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Select a list in which you want to add the task.", Toast.LENGTH_LONG).show();
                Fragment newFragment = new MainFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.flContent, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        expandableListView = (ExpandableListView) rootView.findViewById(R.id.list);
        //ArrayList<CalenderListClass> mainList;

      /*  ArrayList<SingleEntry> AllList=new ArrayList();


        for(int j=0;j<allArrayList.size();j++){
            for(int i=0;i<allArrayList.get(j).getItemList().size();i++){
                AllList.add(allArrayList.get(j).getItemList().get(i));
            }}
        Collections.sort(AllList);*/

      /*  ArrayList<String> datesArrayLists = new ArrayList<>();
        ArrayList<ArrayList<SingleEntry>> arrayLists = new ArrayList<>();
*/
       /* for (int i = 0; i < AllList.size(); i++) {
            String dateText =AllList.get(i).getTaskDay();
             Log.e("dateTaskFetchedHere",AllList.get(i).getTaskDay() +"$$$$$");
             int position;

                datesArrayLists.add(dateText);
               // Log.e("Calledfor", dateText);
            for (int k = 0; k < AllList.size(); k++) {
                Log.e("qwr",AllList.get(k).getTaskDay()+"$$$$$"+k);

            }
            for (int k = 0; k < datesArrayLists.size(); k++) {
                Log.e("qwr",datesArrayLists.get(k)+"$$$$$");

            }

            position = datesArrayLists.indexOf(dateText);
            if (arrayLists.size() < position) {
                arrayLists.get(position).add(AllList.get(i));
            } else {
                ArrayList<SingleEntry> newarray = new ArrayList<>();
                newarray.add(AllList.get(i));
                arrayLists.add(position, newarray);
            }
        }
        for(int i=0;i<datesArrayLists.size();i++) {

            e("sortedDatesList", datesArrayLists.get(i)+"123456789");
        }
        for(int i=0;i<arrayLists.size();i++) {
            for(int j=0;j<arrayLists.get(i).size();j++) {

                e("sortedArrayList",arrayLists.get(i).get(j).getTaskName() );
            }
        }
*/

       // CalenderList calenderList = new CalenderList(arrayLists,datesArrayLists,getContext());
        fetchCalenderList();
        CalenderList adapter = new CalenderList(getContext(),callendarList,callendarDatesList);
        expandableListView.setAdapter(adapter);

        for(int i=0;i<callendarDatesList.size();i++)
        { expandableListView.expandGroup(i);}

        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.choice, menu);
    }
    @Override
    public int getMaxYear() {
        return 2;
    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day,0,0,0);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        DateStr = df2.format(calendar.getTime());

    }

    @Override
    public void onDateRangeSelected(SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays) {

    }
}
