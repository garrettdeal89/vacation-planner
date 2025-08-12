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

    LinkUsers linkUsers; // Database helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        editNameReg = findViewById(R.id.editNameReg);
        editEmailAddressReg = findViewById(R.id.editEmailAddressReg);
        editPasswordReg = findViewById(R.id.editPasswordReg);
        editConfirmPasswordReg = findViewById(R.id.editConfirmPasswordReg);
        registerButtonReg = findViewById(R.id.registerButtonReg);
        returnButtonReg = findViewById(R.id.returnButtonReg);

        linkUsers = new LinkUsers(this);

        // Return to login
        returnButtonReg.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, MainActivity.class));
            finish();
        });

        // Register button logic
        registerButtonReg.setOnClickListener(v -> {
            String strName = editNameReg.getText().toString().trim();
            String strEmail = editEmailAddressReg.getText().toString().trim();
            String strPassword = editPasswordReg.getText().toString().trim();
            String strConfirmPassword = editConfirmPasswordReg.getText().toString().trim();

            // Validation checks
            if (strName.isEmpty() || strEmail.isEmpty() || strPassword.isEmpty() || strConfirmPassword.isEmpty()) {
                Toast.makeText(Register.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                Toast.makeText(Register.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!strPassword.equals(strConfirmPassword)) {
                Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (linkUsers.checkUserExists(strEmail)) {
                Toast.makeText(Register.this, "Email already registered", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert into database
            Users newUser = new Users(strName, strEmail, strPassword);
            linkUsers.addUser(newUser);

            Toast.makeText(Register.this, "Registration successful!", Toast.LENGTH_SHORT).show();

            // Redirect to login
            startActivity(new Intent(Register.this, MainActivity.class));
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}