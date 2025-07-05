package com.lamdah.medicinereminder;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        new Thread(() -> {
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Intent intent;
                if (currentUser == null) {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                } else {
                    intent = new Intent(MainActivity.this, HomeActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }).start();
    }
}
