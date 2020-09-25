package com.Math.MathReasoning;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.Math.MathReasoning.Login.Register;


public class CustomLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_login_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Register.newInstance())
                    .commitNow();
        }
    }
}