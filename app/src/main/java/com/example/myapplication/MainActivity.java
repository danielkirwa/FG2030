package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.concurrent.Executor;

import kotlin.jvm.internal.Ref;

public class MainActivity extends AppCompatActivity {
Button openapp;
ImageView finger_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openapp = findViewById(R.id.btn_open_app);
        finger_button = findViewById(R.id.use_fingerprint);



        // code for Biometric
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(MainActivity.this, "Error" + errString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // success authentication
                Intent opendashboardintent = new Intent(MainActivity.this, Dashboard.class);
                startActivity(opendashboardintent);

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(MainActivity.this, "Error trying to authenticate", Toast.LENGTH_LONG).show();
            }
        });
        // use finger print
        finger_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BiometricPrompt.PromptInfo.Builder promptInfo = dialogMetric();
                promptInfo.setNegativeButtonText("Cancel");

                //activate callback if it succeed
                biometricPrompt.authenticate(promptInfo.build());
            }

            private BiometricPrompt.PromptInfo.Builder dialogMetric() {
                //Show prompt dialog
                return new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Biometric login")
                        .setSubtitle("Log in using your biometric credential");
            }
        });
        // use pin code
        openapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent opendashboardintent = new Intent(MainActivity.this, Dashboard.class);
                startActivity(opendashboardintent);
            }
        });





    // call check biometric
        checkBiometricSupported();
    }

    private void checkBiometricSupported() {
      String info = "";
        BiometricManager manager = BiometricManager.from(this);
        switch (manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.BIOMETRIC_WEAK)){
            case BiometricManager.BIOMETRIC_SUCCESS:
                info = "You can use Fingerprint to log in";
                enableBioButton(true);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                info = "Finger print not found";
                enableBioButton(false);
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                info = "Finger print not fount";
                enableBioButton(false);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                info = "Finger print not set";
                enableBioButton(false);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.BIOMETRIC_WEAK));
        }
        Toast.makeText(this, info, Toast.LENGTH_LONG).show();

    }
    void enableBioButton(boolean enable){
       finger_button.setEnabled(enable);
    }
}