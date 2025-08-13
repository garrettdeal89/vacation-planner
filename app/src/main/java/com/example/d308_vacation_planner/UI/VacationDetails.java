package com.example.d308_vacation_planner.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {

    String title;
    String hotel;
    int vacationID;
    String startDateStr;
    String endDateStr;
    EditText editTitle;
    EditText editHotel;
    TextView editStartDate;
    TextView editEndDate;
    Vacation currentVacation;
    int numVacations;
    Repository repository;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);

        // Change toolbar title
        setTitle("My Vacation Details");
        //
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton fab=findViewById(R.id.floatingActionButton2);

        //find text views
        editTitle = findViewById(R.id.titletext);
        editHotel = findViewById(R.id.hoteltext);
        editStartDate = findViewById(R.id.startdatetext);
        editEndDate = findViewById(R.id.enddatetext);

        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        // getters
        vacationID = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        hotel = getIntent().getStringExtra("hotel");
        startDateStr = getIntent().getStringExtra("startDate");
        endDateStr = getIntent().getStringExtra("endDate");

        //setters
        editTitle.setText(title);
        editHotel.setText(hotel);

        // Set the saved start date or default value
        if (startDateStr != null && !startDateStr.isEmpty()) {
            editStartDate.setText(startDateStr); // Display saved date
        } else {
            editStartDate.setText("MM/dd/yyyy"); // Default value
            editStartDate.setTextColor(Color.GRAY);
        }

        //set the saved end date or default value
        if (endDateStr != null && !endDateStr.isEmpty()) {
            editEndDate.setText(endDateStr);
        } else {
            editEndDate.setText("MM/dd/yyyy");
            editEndDate.setTextColor(Color.GRAY);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacaID", vacationID); //pass vacationID
                startActivity(intent);
            }
        });

        //start date picker
        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //generate date
                Date date;
                String info = editStartDate.getText().toString();
                if (info.equals("MM/dd/yyyy")) info = "mm/dd/yyyy";
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, startDate, myCalendarStart.get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH), myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, month);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }
        };

        //end date picker
        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //generate date
                Date date;
                String info = editEndDate.getText().toString();
                if (info.equals("MM/dd/yyyy")) info = "mm/dd/yyyy";
                try {
                    myCalendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, endDate, myCalendarEnd.get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH), myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, month);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEnd();
            }
        };

        //set data in recycler view
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getmAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);
    }

    //start date update label
    public void updateLabelStart() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editStartDate.setText(sdf.format(myCalendarStart.getTime()));
        editStartDate.setTextColor(Color.BLACK);
    }

    //end date update label
    public void updateLabelEnd() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editEndDate.setText(sdf.format(myCalendarEnd.getTime()));
        editEndDate.setTextColor(Color.BLACK);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // validate start date is before end date
        if (item.getItemId() == R.id.vacationsave) {

            String title = editTitle.getText().toString().trim();
            String hotel = editHotel.getText().toString().trim();
            String startDateStr = editStartDate.getText().toString().trim();
            String endDateStr = editEndDate.getText().toString().trim();

            // no empty field validation
            if (title.isEmpty() || startDateStr.equals("MM/dd/yyyy") || endDateStr.equals("MM/dd/yyyy")) {
                Toast.makeText(this, "Vacation title and dates required!", Toast.LENGTH_LONG).show();
                return false; // Prevent saving if fields are empty
            }

            // Date validation
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            try {
                Date startDate = sdf.parse(startDateStr);
                Date endDate = sdf.parse(endDateStr);


                if (startDate != null && endDate != null && !endDate.after(startDate)) {
                    // Show Toast message if the input is invalid
                    Toast.makeText(this, "End date must be after the start date.", Toast.LENGTH_LONG).show();
                    return false; // Do not proceed with saving
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "Invalid date format. Please use MM/dd/yyyy.", Toast.LENGTH_LONG).show();
                return false; // Do not proceed with saving
            }

            //save vacation to database option
            Vacation vacation;
            if (vacationID == -1) {
                if (repository.getmAllVacations().size() == 0) vacationID = 1;
                else vacationID = repository.getmAllVacations().get(repository.getmAllVacations().size() - 1).getVacationID() + 1;
                vacation = new Vacation(vacationID, editTitle.getText().toString(), editHotel.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                repository.insert(vacation);
            } else {
                vacation = new Vacation(vacationID, editTitle.getText().toString(), editHotel.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                repository.update(vacation);
            }
            Toast.makeText(VacationDetails.this, title + " Vacation  Saved!", Toast.LENGTH_LONG).show();
            finish();
            this.finish();
            return true;
        }

        // set vacation alert
        if (item.getItemId() == R.id.setvacationalert) {

            String startDateFromScreen = editStartDate.getText().toString().trim();
            String endDateFromScreen = editEndDate.getText().toString().trim();
            String vacationTitleFromScreen = editTitle.getText().toString().trim();

            // Verify inputs are not empty
            if (startDateFromScreen.isEmpty() || endDateFromScreen.isEmpty() || vacationTitleFromScreen.isEmpty()) {
                Toast.makeText(VacationDetails.this, "Please enter vacation title and dates!", Toast.LENGTH_LONG).show();
                return true;
            }

            String myFormat = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            Date myStartDate = null;
            Date myEndDate = null;

            try {
                myStartDate = sdf.parse(startDateFromScreen);
                myEndDate = sdf.parse(endDateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // Get the current date
            Date currentDate = new Date();
            String currentDateStr = sdf.format(currentDate);

            if (myStartDate != null) {
                // Check if the current date matches the start date
                if (currentDateStr.equals(startDateFromScreen)) {
                    Long trigger = myStartDate.getTime();
                    Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                    intent.putExtra("key", "Your " + vacationTitleFromScreen + " vacation starts today!");
                    PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                }
            }

            if (myEndDate != null) {
                // Check if the current date matches the end date
                if (currentDateStr.equals(endDateFromScreen)) {
                    Long endTrigger = myEndDate.getTime();
                    Intent endIntent = new Intent(VacationDetails.this, MyReceiver.class);
                    endIntent.putExtra("key", "Your " + vacationTitleFromScreen + " vacation ends today!");
                    PendingIntent endSender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, endIntent, PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager endAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    endAlarmManager.set(AlarmManager.RTC_WAKEUP, endTrigger, endSender);
                }
            }

            // If both dates match, send both notifications
            if (startDateFromScreen.equals(endDateFromScreen) && currentDateStr.equals(startDateFromScreen)) {
                Long trigger = myStartDate.getTime();
                Intent startIntent = new Intent(VacationDetails.this, MyReceiver.class);
                startIntent.putExtra("key", "Your " + vacationTitleFromScreen + " vacation starts today!");
                PendingIntent startSender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, startIntent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, startSender);

                Intent endIntent = new Intent(VacationDetails.this, MyReceiver.class);
                endIntent.putExtra("key", "Your " + vacationTitleFromScreen + " vacation ends today!");
                PendingIntent endSender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, endIntent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, endSender);
            }
            Toast.makeText(VacationDetails.this, vacationTitleFromScreen + " Alert Set!", Toast.LENGTH_LONG).show();
            return true;
        }

        //share vacation details
        if (item.getItemId() == R.id.sharevacationdetails) {

            String startDateFromScreen = editStartDate.getText().toString().trim();
            String endDateFromScreen = editEndDate.getText().toString().trim();
            String vacationTitleFromScreen = editTitle.getText().toString().trim();

            // Verify inputs are not empty
            if (startDateFromScreen.isEmpty() || endDateFromScreen.isEmpty() || vacationTitleFromScreen.isEmpty()) {
                Toast.makeText(VacationDetails.this, "Vacation title and dates required to share!", Toast.LENGTH_LONG).show();
                return true;
            }

            Intent sentIntent = new Intent();
            sentIntent.setAction(Intent.ACTION_SEND);

            //get vacation details for sentIntent
            sentIntent.putExtra(Intent.EXTRA_TEXT, "Vacation: " + editTitle.getText().toString() + "\n"
                    + "Hotel: " + editHotel.getText().toString() + "\n"
                    + "Start Date: " + editStartDate.getText().toString() + "\n"
                    + "End Date: " + editEndDate.getText().toString());
            sentIntent.putExtra(Intent.EXTRA_TITLE, editTitle.getText().toString() + " Shared Vacation Details");
            sentIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sentIntent, null);
            startActivity(shareIntent);
            return true;
        }

        //delete from database menu option
        if (item.getItemId() == R.id.vacationdelete) {
            //delete vacation from database
            for (Vacation vaca : repository.getmAllVacations()) {
                if (vaca.getVacationID() == vacationID) currentVacation = vaca;
            }

            numVacations = 0;
            for (Excursion excursion : repository.getmAllExcursions()) {
                if (excursion.getVacationID() == vacationID) ++numVacations;
            }
            // validate a vacation cannot be deleted if it has an associated excursion
            if (numVacations == 0) {
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getVacationTitle() + " vacation deleted!", Toast.LENGTH_LONG).show();
                VacationDetails.this.finish();
            } else {
                // display pop-up message if vacation cannot be deleted.
                Toast.makeText(VacationDetails.this, "Cannot delete a vacation with excursions!", Toast.LENGTH_LONG).show();
            }
            return true;
        }

        //log out and return to login activity
        if (item.getItemId() == R.id.log_out_vList) {
            Intent intent = new Intent(VacationDetails.this, MainActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //resuming method
    @Override
    protected void onResume(){

        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //filter excursions based on vacation ID:
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getmAllExcursions()) {

            if (e.getVacationID() == vacationID) {
                filteredExcursions.add(e);
            }
        }
        //update adapter
        excursionAdapter.setExcursions(filteredExcursions);
    }

}




































