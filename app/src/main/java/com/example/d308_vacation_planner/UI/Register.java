package com.example.d308_vacation_planner.UI;

import android.content.Intent;
import android.os.Bundle;
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

public class Register extends AppCompatActivity {

    EditText editNameReg;
    EditText editEmailAddressReg;
    EditText editPasswordReg;
    EditText editConfirmPasswordReg;
    Button registerButtonReg;
    Button returnButtonReg;

    // Database helper object
    LinkUsers linkUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Connect UI
        editNameReg = findViewById(R.id.editNameReg);
        editEmailAddressReg = findViewById(R.id.editEmailAddressReg);
        editPasswordReg = findViewById(R.id.editPasswordReg);
        editConfirmPasswordReg = findViewById(R.id.editConfirmPasswordReg);
        registerButtonReg = findViewById(R.id.registerButtonReg);
        returnButtonReg = findViewById(R.id.returnButtonReg);

        // Initialize db helper
        linkUsers = new LinkUsers(this);

        // go back to login screen
        returnButtonReg.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, MainActivity.class));
            finish();
        });

        // Register button click
        registerButtonReg.setOnClickListener(v -> {
            // Step 1: Get user input
            String strName = editNameReg.getText().toString().trim();
            String strEmail = editEmailAddressReg.getText().toString().trim();
            String strPassword = editPasswordReg.getText().toString().trim();
            String strConfirmPassword = editConfirmPasswordReg.getText().toString().trim();

            // Validate empty fields
            if (strName.isEmpty() || strEmail.isEmpty() || strPassword.isEmpty() || strConfirmPassword.isEmpty()) {
                Toast.makeText(Register.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate email format
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                Toast.makeText(Register.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate password rules
            if (!isPasswordValid(strPassword)) {
                Toast.makeText(Register.this,
                        "Password must be 6-15 characters and include at least 1 letter, 1 number, and 1 special character",
                        Toast.LENGTH_LONG).show();
                return;
            }

            // Check if passwords match
            if (!strPassword.equals(strConfirmPassword)) {
                Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if email already exists in DB
            if (linkUsers.checkUserExists(strEmail)) {
                Toast.makeText(Register.this, "Email already registered", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert new user into database
            Users newUser = new Users(strName, strEmail, strPassword);
            linkUsers.addUser(newUser);

            // Show success toast message
            Toast.makeText(Register.this, "Registration successful!", Toast.LENGTH_SHORT).show();

            // Redirect to login screen
            startActivity(new Intent(Register.this, MainActivity.class));
            finish();
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    // method to validate
    private boolean isPasswordValid(String password) {
        String pattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{6,15}$";
        return password.matches(pattern);
    }
}