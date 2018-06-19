package com.example.peterphchen.firebasepractice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class UploadImageActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "UploadImageActivity";

    private ImageView imageView;
    private ViewPager pager;
    private FirebaseAuth mAuth;
    private StorageReference mReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        int permissionCheck = this.checkSelfPermission("Manifest.permission.READ_EXTERNAL_STORAGE");
        permissionCheck += this.checkSelfPermission("Manifest.permission.WRITE_EXTERNAL_STORAGE");
        if (permissionCheck != 0) {
            this.requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1001); //Any number
        }
        this.pager = findViewById(R.id.pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter(getSupportFragmentManager(), ImageFilePath());
        pager.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.uploadBtn:
                    uploadPicture();
                    break;
        }
    }

    private ArrayList<String> ImageFilePath(){
        File directory =new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath()+"/Camera/");
        File[] files = directory.listFiles();
        ArrayList<String> filePaths = new ArrayList<String>();
        for(File fil: files)
            filePaths.add(fil.getPath());
        return filePaths;
    }

    private void uploadPicture(){
        String userID = mAuth.getCurrentUser().getUid();
        String filepath = ImageFilePath().get(pager.getCurrentItem());
        Uri uri = Uri.parse("file://"+filepath);
        String filename = LocalDateTime.now().toString();
        Log.d(TAG, "uploadPicture: "+filename);
        StorageReference mstorageReference = mReference.child("image/"+userID+"/"+filename+".bmp");
        mstorageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UploadImageActivity.this, "upload success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadImageActivity.this, "Fail to upload image", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: "+e.getMessage());
            }
        });
    }
}
