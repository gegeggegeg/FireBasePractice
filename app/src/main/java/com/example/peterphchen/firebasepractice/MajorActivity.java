package com.example.peterphchen.firebasepractice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MajorActivity extends AppCompatActivity {

    private static final String TAG = "MajorActivity";

    private FirebaseDatabase mFirebasedatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    private EditText phoneNumber;
    private EditText nameInput;
    private EditText emailAddress;
    private Button submitBtn;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.optopnWrite:
                Toast.makeText(this, "Please Write data", Toast.LENGTH_SHORT).show();
                break;
            case R.id.optionRead:
                Intent intent = new Intent(MajorActivity.this, ReadDataActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.optionUpload:
                Intent intent2 =new Intent(MajorActivity.this, UploadImageActivity.class);
                startActivity(intent2);
                finish();
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major);
        emailAddress = findViewById(R.id.emailInput);
        submitBtn = findViewById(R.id.submit);
        mAuth = FirebaseAuth.getInstance();
        phoneNumber =findViewById(R.id.phoneInput);
        nameInput = findViewById(R.id.nameInput);
        mFirebasedatabase = FirebaseDatabase.getInstance();
        mRef = mFirebasedatabase.getReference();

        // Read from the database
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object value = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Log.d(TAG, "onClick: Attempt to change value of database");
                    FirebaseUser user = mAuth.getCurrentUser();
                    String userID = user.getUid();
                    Long phonenumber = Long.valueOf(phoneNumber.getText().toString());
                    if(phoneNumber.getText() != null)
                        mRef.child("user").child(userID).child("phonenumber").setValue(phonenumber);
                    if(emailAddress.getText() != null)
                        mRef.child("user").child(userID).child("emailAddress").setValue(emailAddress.getText().toString());
                    if(nameInput.getText() != null)
                        mRef.child("user").child(userID).child("name").setValue(nameInput.getText().toString());
                    emailAddress.setText("");
                    phoneNumber.setText("");
                    nameInput.setText("");
            }
        });
    }

}
