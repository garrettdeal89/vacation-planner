package com.example.d308_vacation_planner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308_vacation_planner.R;
import com.example.d308_vacation_planner.UI.database.Repository;
import com.example.d308_vacation_planner.UI.entities.Excursion;
import com.example.d308_vacation_planner.UI.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VacationList extends AppCompatActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);

        // Change toolbar title
        setTitle("My Vacation List");

        FloatingActionButton fab=findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        //get repository
        repository = new Repository(getApplication());
        //get list of vacations
        List<Vacation> allVacations = repository.getmAllVacations();
        //vacation adapter
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        // layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //put vacations on recycler view
        vacationAdapter.setVacations(allVacations);

        //System.out.println(getIntent().getStringExtra("test"));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    // inflating the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    //resuming method
    @Override
    protected void onResume(){

        super.onResume();
        List<Vacation> allVacations = repository.getmAllVacations();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }


    //menu actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.myVacations) {

            repository = new Repository(getApplication());

            // Populate Database with Test data
            /*
            Vacation vacation = new Vacation(0,"Italy", "Bonavilla", "12/1/2025", "12/14/2025");
            repository.insert(vacation);

            vacation = new Vacation(0,"Japan", "Okami", "12/15/2025", "12/28/2025");
            repository.insert(vacation);

            Excursion excursion = new Excursion(0,"Kayaking","12/20/205", 0);
            repository.insert(excursion); */

            // Add navigation to VacationDetails
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
            return true;
        }

        // Close the current activity
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        // Keep the default behavior for other options
        return super.onOptionsItemSelected(item);
    }

    /*
    //live search filtering
    EditText searchBar = findViewById(R.id.search_bar);

    searchBar.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not needed for our search
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String query = s.toString();

            // Observe LiveData from repository
            repository.searchVacations(query).observe(VacationList.this, vacations -> {
                // Update the RecyclerView adapter with new results
                adapter.setVacations(vacations);
            });
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Not needed for our search
        }
    });
    */

}
































