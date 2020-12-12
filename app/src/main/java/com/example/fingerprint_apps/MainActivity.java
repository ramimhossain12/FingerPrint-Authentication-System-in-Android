package com.example.fingerprint_apps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView msg_txt  = findViewById(R.id.txt_msg);
        Button login_btn = findViewById(R.id.login_btn);



        //.....check if our user can use the fingerprint sensor or not .........

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate())
        {
            // switch some constant to check different possibilty

            case  BiometricManager.BIOMETRIC_SUCCESS:
                msg_txt.setText("You can use the fingerprint to login");
                msg_txt.setText(Color.parseColor("#ffff"));
                break;

            case  BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:// this is mean that the device don't have a fingerprint sensor
                  msg_txt.setText("The device don't have a fingerprint sensor");
                  break;
            case  BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msg_txt.setText("The biometric sensors is currently unavilabe");
                login_btn.setVisibility(View.GONE);
                break;
            case  BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msg_txt.setText("Your device don't have any fingerprint saved, please check your security setting");
                login_btn.setVisibility(View.GONE);
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);

        BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override// this method is called when there is an error while the authentication
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override //this method is called when the authentication is success
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),"Login Success!",Toast.LENGTH_SHORT).show();
            }

            @Override//this method is called if we have failed the authentication
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setDescription("User your fingerprint to login to your app")
                .setNegativeButtonText("Cancel")
                .build();

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);

            }
        });
    }
}