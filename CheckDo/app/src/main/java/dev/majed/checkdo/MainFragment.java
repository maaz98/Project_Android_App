package dev.majed.checkdo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import static dev.majed.checkdo.MainData.allArrayList;
import static dev.majed.checkdo.MainData.save;


public class MainFragment extends Fragment {
    EditText input;
    public static AlertDialog ad;
    public static Adapter addp;
    public static RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

      recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);

        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ad.show();
            }
        });

        addp = new Adapter(allArrayList);
        recyclerView.setAdapter(addp);
        LinearLayoutManager staggeredGridLayoutManager = new LinearLayoutManager(getContext());
       // staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add new list");
        input = new EditText(getContext());
        input.setHint("Title");
        builder.setView(input);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                if (text.trim().length() <= 0) {
                    Toast.makeText(getContext(), "invalid name", Toast.LENGTH_SHORT).show();
                }
                else {
                    ArrayList<SingleEntry> stringList = new ArrayList<>();
                    MyList myList = new MyList(stringList, text);
                    allArrayList.add(myList);
                    save();
                    addp.notifyDataSetChanged();
                    input.setText("");
                }

                addp.notifyDataSetChanged();
                addp = new Adapter(allArrayList);
                recyclerView.setAdapter(addp);

            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        ad = builder.create();

         return rootView;

    }

}
