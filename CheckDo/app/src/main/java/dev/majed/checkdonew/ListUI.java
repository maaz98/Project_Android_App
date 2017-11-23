package dev.majed.checkdonew;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import dev.majed.checkdo.R;
import dev.majed.checkdo.SingleEntry;

import static dev.majed.checkdo.LoginActivity.ctxx;

public class ListUI extends AppCompatActivity {
long id;
    ArrayList<SingleEntry> TaskList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        long id  = getIntent().getLongExtra("ListID",0);

        TaskList= new ArrayList<>();
        Hawk.init(ctxx).build();
        TaskList = Hawk.get(String.valueOf(id),new ArrayList<SingleEntry>());
        Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();



    }

}
