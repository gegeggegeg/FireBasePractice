package com.example.peterphchen.firebasepractice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private String mail = null;
    private String password = null;
    private TextView mailText;
    private TextView pwText;
    private Button createBtn;
    private Button SignInBtn;
    private Button SignOutBtn;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser = mAuth.getCurrentUser();
        updateUI(currentuser);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mailText = findViewById(R.id.email);
        pwText = findViewById(R.id.password);
        mail = mailText.getText().toString();
        password = pwText.getText().toString();
        SignInBtn =findViewById(R.id.SignIn);
        createBtn = findViewById(R.id.create);
        SignOutBtn = findViewById(R.id.SignOut);
    }
    private void startSignIn(String email, String password){
        if(!validateForm())
            return;
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: siginInwithEmail: success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                    Intent intent = new Intent(MainActivity.this,MajorActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void createNewAccount(String mail, String password){
        if(!validateForm())
            return;
        mAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (!user.isEmailVerified()){
                        sendverifyemail();
                        mailverifyNotification();
                    }
                    updateUI(user);
                }else{
                    Log.d(TAG, "onComplete: failure "+task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateUI(FirebaseUser user){
        if(user != null){
            Log.d(TAG, "updateUI: Sign In with account: "+user.getUid());
            mailText.setText(user.getEmail());
            SignInBtn.setEnabled(true);
            SignOutBtn.setEnabled(true);
        }
        else {
            Log.d(TAG, "updateUI: User is not found");
            //SignInBtn.setEnabled(false);
            SignOutBtn.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.SignIn:
                startSignIn(mailText.getText().toString(),pwText.getText().toString());
                break;
            case  R.id.create:
                createNewAccount(mailText.getText().toString(),pwText.getText().toString());
                break;
            case R.id.SignOut:
                mAuth.signOut();
                updateUI(null);
                break;
        }
    }

    private boolean validateForm(){
        boolean valid = true;
        String email =mailText.getText().toString();
        if(TextUtils.isEmpty(email)){
            mailText.setError("Required");
            valid = false;
        }
        String password = pwText.getText().toString();
        if(TextUtils.isEmpty(password)){
            pwText.setError("Required");
            valid = false;
        }
        return valid;
    }
    private void sendverifyemail(){
        final FirebaseUser user = mAuth.getCurrentUser();
        final String email = user.getEmail();
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: email send successfully");
                    Toast.makeText(MainActivity.this, "Verification email has sent to "+ email, Toast.LENGTH_SHORT).show();
                }else{
                    Log.e(TAG, "onComplete: Failed to send verifiaction email "+task.getException());
                    Toast.makeText(MainActivity.this, "Unable to send enmail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void mailverifyNotification(){
        String channelId = getString(R.string.gcm_defaultSenderId);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Please Verify your email!")
                .setContentText("Your new account has been created, please verify your email to finish final step")
                .setSound(defaultSoundUri)
                .setAutoCancel(true);
        NotificationManager mgr = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mgr.notify(0,builder.build());
    }
}
