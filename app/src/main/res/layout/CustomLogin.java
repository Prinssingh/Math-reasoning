package Testing.Firebase;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import Testing.Firebase.Login.Login;

public class CustomLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_login_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, Login.newInstance())
                    .commitNow();
        }
    }
}