package dev.majed.checkdo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class NotesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener {

    public static String email;
    private String name;

    TextView tv_name;
    TextView tv_email;
    private TextView mTextMessage;
    FragmentTransaction transaction;
    Fragment fragment;

     @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        mTextMessage = (TextView) findViewById(R.id.message);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        tv_name = (TextView) header.findViewById(R.id.name);
        tv_email = (TextView) header.findViewById(R.id.email);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);


        SharedPreferences preferences = getSharedPreferences("CHECKDO", MODE_PRIVATE);

        name = preferences.getString("loggedUser", "");
        email = preferences.getString("loggedEmail", "");
        tv_email.setText(email);
        tv_name.setText(name);

        fragment= new MainFragment();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContent, fragment);
        transaction.commit();
        if(getIntent().getStringExtra("keyTo").compareTo("firstOpen")==0){
            Intent intent = new Intent(this,AllListUI.class);
            startActivity(intent);
        }
 /*    ArrayList<SingleEntry> thisList=  makeCalenderlist();*/
      /*   Log.e("SortedData",thisList.toString());*/



      /*   MainData mainData = new MainData(this);
         SingleEntry singleEntry = new SingleEntry("listElement 1",(long)123,(long)123);
         ArrayList<SingleEntry> array =new ArrayList<>();
         array.add(singleEntry);
         MyList myList = new MyList(array,"MyTestListNew");
         allArrayList.add(myList);
         mainData.save();

     Log.e("size",String.valueOf(allArrayList.size()));*/

     }

   /* private ArrayList<SingleEntry> makeCalenderlist() {
         ArrayList<SingleEntry> list =  new ArrayList<>();
         for(int i=0;i<superList.arrayList.size();i++){
             for(int j=0;j<superList.arrayList.get(i).getItemList().size();j++){
                 list.add(superList.arrayList.get(i).getItemList().get(j));
             }
         }
        Collections.sort(list, new Comparator<SingleEntry>() {
            @Override
            public int compare(SingleEntry singleEntry, SingleEntry t1) {
                return singleEntry.getTaskTime().compareTo(t1.getTaskTime());
            }
        });
        return list;
    }*/

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            moveToUserProfile();
            return true;
        }
        else if (id == R.id.action_plan) {
            moveToPlanMyDay();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void moveToPlanMyDay() {
        Intent intent = new Intent(this, PlanMyDayActivity.class);  // intent to user Profile

        startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void moveToUserProfile() {
        Intent intent = new Intent(this, UserProfile.class);  // intent to user Profile
        intent.putExtra("email", email);
        intent.putExtra("name", name);
        startActivity(intent);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_home:

                     fragment= new MainFragment();
                     transaction = getSupportFragmentManager().beginTransaction();
                     transaction.replace(R.id.flContent, fragment);
                     transaction.commit();
                     return true;

                case R.id.navigation_dashboard:

                      fragment= new CalenderFragment();
                      transaction = getSupportFragmentManager().beginTransaction();
                      transaction.replace(R.id.flContent, fragment);
                      transaction.commit();
                     return true;

                case R.id.navigation_notifications:
                  //  mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };
}
