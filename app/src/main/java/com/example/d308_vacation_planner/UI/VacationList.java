package com.example.d308_vacation_planner.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.net.Uri;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308_vacation_planner.R;
import com.example.d308_vacation_planner.UI.database.Repository;
import com.example.d308_vacation_planner.UI.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        // menu option to generate report
        if (item.getItemId() == R.id.generate_report) {
            // Show confirmation dialog before generating report
            new AlertDialog.Builder(this)
                    .setTitle("Generate Report?")
                    .setMessage("Do you want to generate and share your current Planned Vacations Report? (reports generate as .csv files)" )
                    .setPositiveButton("Yes", (dialog, which) -> {
                        generateAndShareReport();  // Generate and share report if confirmed
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss(); // Do nothing if canceled
                    })
                    .show();
            return true;
        }

        /* if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        } */

        return super.onOptionsItemSelected(item);
    }

    // Generate and share report
    private void generateAndShareReport() {
        if (allVacations.isEmpty()) {
            Toast.makeText(this, "No vacations to include in the report.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder csvBuilder = new StringBuilder();

        // Title row
        csvBuilder.append("Planned Vacations Report\n");
        csvBuilder.append("\n");  // Blank line for spacing

        // Column headers
        csvBuilder.append("Destination,Start Date,End Date,Number of Days,Date/Time Generated\n");

        SimpleDateFormat sdfDate = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
        String currentDateTime = sdfDateTime.format(new Date());

        for (Vacation vac : allVacations) {
            String destination = vac.getVacationTitle() != null ? vac.getVacationTitle() : "N/A";
            String startDate = vac.getStartDate() != null ? vac.getStartDate() : "N/A";
            String endDate = vac.getEndDate() != null ? vac.getEndDate() : "N/A";

            int numberOfDays = 0;
            try {
                Date start = sdfDate.parse(startDate);
                Date end = sdfDate.parse(endDate);
                if (start != null && end != null) {
                    long diff = end.getTime() - start.getTime();
                    numberOfDays = (int) (diff / (1000 * 60 * 60 * 24)) + 1; // inclusive
                }
            } catch (Exception e) {
                numberOfDays = 0;
            }

            csvBuilder.append(String.format("\"%s\",\"%s\",\"%s\",%d,\"%s\"\n",
                    destination.replace("\"", "\"\""),
                    startDate,
                    endDate,
                    numberOfDays,
                    currentDateTime));
        }

        try {
            // Save CSV file in cache directory
            File cacheDir = new File(getCacheDir(), "shared_csv");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }

            File csvFile = new File(cacheDir, "Planned_Vacations_Report.csv");

            try (FileOutputStream fos = new FileOutputStream(csvFile)) {
                fos.write(csvBuilder.toString().getBytes(StandardCharsets.UTF_8));
            }

            // Get URI from FileProvider
            Uri contentUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".fileprovider",
                    csvFile);

            // Share Report
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/csv");
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivity(Intent.createChooser(shareIntent, "Share Planned Vacations Report"));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to generate report: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    }

































