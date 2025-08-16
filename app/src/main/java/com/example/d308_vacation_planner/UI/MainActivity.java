package com.example.d308_vacation_planner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d308_vacation_planner.R;
import com.example.d308_vacation_planner.UI.database.LinkUsers;

public class MainActivity extends AppCompatActivity {

    public static int numAlert;
    EditText editEmailAddress;
    EditText editPassword;
    Button registerLog;
    Button loginButton;


    // Database helper
    LinkUsers linkUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Link UI elements
        editEmailAddress = findViewById(R.id.editEmailAddress);
        editPassword = findViewById(R.id.editPassword);
        registerLog = findViewById(R.id.register_button);
        loginButton = findViewById(R.id.loginButton);


        // Initialize db helper
        linkUsers = new LinkUsers(this);

        // Navigate to Register activity
        registerLog.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, Register.class);
            startActivity(i);
        });

        // Login button functionality
        loginButton.setOnClickListener(v -> {
            String email = editEmailAddress.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            // 1️Check for empty fields
            if (email.isEmpty() && password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Email and Password are required", Toast.LENGTH_SHORT).show();
                return;
            } else if (email.isEmpty()) {
                Toast.makeText(MainActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                return;
            } else if (password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2️Validate credentials with db
            boolean isValid = linkUsers.validateUser(email, password);

            if (isValid) {
                // Go to VacationList
                Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, VacationList.class);
                startActivity(intent);
                finish(); // close login activity
            } else {
                // Show error
                Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}