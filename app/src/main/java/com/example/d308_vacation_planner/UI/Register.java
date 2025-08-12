package com.example.d308_vacation_planner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d308_vacation_planner.R;

public class Register extends AppCompatActivity {

    EditText editNameReg;
    EditText editEmailAddressReg;
    EditText editPasswordReg;
    EditText editConfirmPasswordReg;
    Button registerButtonReg;
    Button returnButtonReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        editNameReg = findViewById(R.id.editEmailAddressReg);
        editEmailAddressReg = findViewById(R.id.editEmailAddressReg);
        editPasswordReg = findViewById(R.id.editPasswordReg);
        editConfirmPasswordReg = findViewById(R.id.editConfirmPasswordReg);
        registerButtonReg = findViewById(R.id.registerButtonReg);
        returnButtonReg = findViewById(R.id.returnButtonReg);

        //return to login
        returnButtonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}