package com.example.peterphchen.firebasepractice;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReadDataActivity extends AppCompatActivity {
    private static final String TAG = "ReadDataActivity";
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mRef;
    private FirebaseUser user;
    private String userID = null;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();
        userID = user.getUid();
        listView = findViewById(R.id.listView);
        FirebaseReadDdata();
    }

    private void FirebaseReadDdata(){
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot){
            UserInformation uInfo = new UserInformation();
            uInfo = dataSnapshot.child("user").child(userID).getValue(UserInformation.class);
            Log.d(TAG, "showData: Name: " + uInfo.getName());
            Log.d(TAG, "showData: emailaddress: " +uInfo.getEmailAddress());
            ArrayList<String> Infolist = new ArrayList<>();
            Infolist.add(uInfo.getName());
            Infolist.add(uInfo.getEmailAddress());
            Infolist.add(uInfo.getPhonenumber().toString());
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,Infolist);
            listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.optionRead:
                Toast.makeText(this, "Please Read data", Toast.LENGTH_SHORT).show();
                break;
            case R.id.optopnWrite:
                Intent intent = new Intent(this, MajorActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }
}
