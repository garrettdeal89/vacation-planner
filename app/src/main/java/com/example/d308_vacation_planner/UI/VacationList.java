package com.example.d308_vacation_planner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308_vacation_planner.R;
import com.example.d308_vacation_planner.UI.database.Repository;
import com.example.d308_vacation_planner.UI.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VacationList extends AppCompatActivity {

    private Repository repository;
    private VacationAdapter vacationAdapter;
    private List<Vacation> allVacations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);

        setTitle("My Vacation List");

        // Debug/view lookups
        Log.d("DEBUG", "searchView: " + findViewById(R.id.search_view));
        Log.d("DEBUG", "recyclerView: " + findViewById(R.id.recyclerview));

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        SearchView searchView = findViewById(R.id.search_view);

        repository = new Repository(getApplication());

        // prevent null list
        List<Vacation> data = repository.getmAllVacations();
        if (data != null) {
            allVacations.clear();
            allVacations.addAll(data);
        }

        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);

        // SearchView filtering
        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    filterVacations(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    filterVacations(newText);
                    return true;
                }
            });
        } else {
            Log.e("ERROR", "SearchView is NULL - check layout reference!");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Filtering helper
    private void filterVacations(String text) {
        List<Vacation> filteredList = new ArrayList<>();
        for (Vacation vacation : allVacations) {
            if (vacation.getVacationTitle() != null &&
                    vacation.getVacationTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(vacation);
            }
        }
        vacationAdapter.setVacations(filteredList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Vacation> data = repository.getmAllVacations();
        if (data != null) {
            allVacations.clear();
            allVacations.addAll(data);
            vacationAdapter.setVacations(allVacations);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.myVacations) {
            Intent intent = new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
































