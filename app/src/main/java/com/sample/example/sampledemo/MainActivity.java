package com.sample.example.sampledemo;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener listener;
    private ArrayList<SampleData> arrayList;
    private ArrayList<String> objectKeys;
    private ValueEventListener eventListener;
    private DataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rcyl_view);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("USER");
        arrayList = new ArrayList<>();
        objectKeys = new ArrayList<>();

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Progress bar can be stopped here.
                //Toast.makeText(MainActivity.this, "Item have been loaded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(eventListener);

        FloatingActionButton fb = findViewById(R.id.add_button);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpDialog();


            }
        });


        adapter = new DataAdapter(this, arrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        getFirebaseMessage();

    }


    private void attachDatabaseListener() {

        if(listener == null)
        {
            listener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    SampleData data = dataSnapshot.getValue(SampleData.class);
                    String key = dataSnapshot.getKey();
                    arrayList.add(data);
                    objectKeys.add(key);

                    adapter.setList(arrayList);
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        objectKeys.remove(0);
                        arrayList.remove(0);

                    adapter.setList(arrayList);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            databaseReference.addChildEventListener(listener);
        }
    }

    private void detachDatabaseListener()
    {
        if(listener!=null)
        {
            databaseReference.removeEventListener(listener);
            arrayList.clear();
            adapter.setList(arrayList);
            adapter.notifyDataSetChanged();
            listener = null;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        attachDatabaseListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseListener();
    }

    private void popUpDialog()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.input_dialog);
        final EditText nameEd = dialog.findViewById(R.id.name_ed);
        final EditText ageEd = dialog.findViewById(R.id.age_ed);
        dialog.show();
        dialog.findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SampleData a = new SampleData(nameEd.getText().toString(), ageEd.getText().toString());
                databaseReference.push().setValue(a);
                dialog.cancel();
            }
        });

    }

    public void getFirebaseMessage() {

        FirebaseMessaging.getInstance().subscribeToTopic("USER")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String result = "success";
                        if(!task.isSuccessful())
                            result = "failed";

                        Log.e("MainActivity", result);
                    }
                });
    }
}
