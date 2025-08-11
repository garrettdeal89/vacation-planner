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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d308_vacation_planner.R;
import com.example.d308_vacation_planner.UI.database.Repository;
import com.example.d308_vacation_planner.UI.entities.Excursion;
import com.example.d308_vacation_planner.UI.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {

    //initialize variables
    int excursionID;
    int vacaID;
    int excurID;
    String excursionTitle;
    //String excursionDate;
    EditText editExcursionTitle;
    TextView editExcursionDate;
    String excursionDateStr;
    Repository repository;
    //Date Picker
    DatePickerDialog.OnDateSetListener myDate;
    final Calendar myCalendarStart = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);

        // Change toolbar title
        setTitle("My Excursion Details");

        //initialize repo and views
        repository = new Repository(getApplication());

        editExcursionTitle = findViewById(R.id.excursiontitle);
        editExcursionDate = findViewById(R.id.excursiondate);

        //set format
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        //retrieve data passed via intent
        excursionID = getIntent().getIntExtra("id", -1);
        excursionTitle = getIntent().getStringExtra("title");
        excursionDateStr = getIntent().getStringExtra("date");
        vacaID = getIntent().getIntExtra("vacaID", -1);

        // Set excursion title
        editExcursionTitle.setText(excursionTitle);

        // Set excursion date if available, otherwise show placeholder
        if (excursionDateStr != null && !excursionDateStr.isEmpty()) {
            editExcursionDate.setText(excursionDateStr);
        } else {
            editExcursionDate.setText("MM/dd/yyyy"); // Default value
            editExcursionDate.setTextColor(Color.GRAY);
        }

        editExcursionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //generate date
                Date date;
                String info = editExcursionDate.getText().toString();
                if (info.equals("MM/dd/yyyy")) info = ("mm/dd/yyyy");
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(ExcursionDetails.this, myDate, myCalendarStart.get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH), myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        myDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, month);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }
        };
    }

    //date picker update label method
    public void updateLabelStart() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editExcursionDate.setText(sdf.format(myCalendarStart.getTime()));
        editExcursionDate.setTextColor(Color.BLACK);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_excursiondetails, menu);
        return true;
    }

    //open menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.excursionsave) {
            Excursion excursion;
            Vacation associatedVacation = null;
            String excurTitle = editExcursionTitle.getText().toString().trim();
            String excurDate = editExcursionDate.getText().toString().trim();

            // Check if any required field is empty
            if (excurTitle.isEmpty() || excurDate.equals("MM/dd/yyyy")) {
                Toast.makeText(this, "Excursion title and date required!", Toast.LENGTH_LONG).show();
                return false; // Prevent saving if fields are empty
            }

            // Retrieve the associated vacation
            for (Vacation vacation : repository.getmAllVacations()) {
                if (vacation.getVacationID() == vacaID) {
                    associatedVacation = vacation;
                    break;
                }
            }

            if (associatedVacation == null) {
                Toast.makeText(ExcursionDetails.this, "Error: Associated vacation not found.", Toast.LENGTH_LONG).show();
                return true;
            }

            // Parse excursion date
            String excursionDateStr = editExcursionDate.getText().toString();
            String myFormat = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date excursionDate;

            try {
                excursionDate = sdf.parse(excursionDateStr);
            } catch (ParseException e) {
                Toast.makeText(ExcursionDetails.this, "Invalid excursion date format.", Toast.LENGTH_LONG).show();
                return true;
            }

            // Parse vacation start and end dates
            Date vacationStartDate, vacationEndDate;

            try {
                vacationStartDate = sdf.parse(associatedVacation.getStartDate());
                vacationEndDate = sdf.parse(associatedVacation.getEndDate());
            } catch (ParseException e) {
                //Toast.makeText(ExcursionDetails.this, "Error parsing vacation dates.", Toast.LENGTH_LONG).show();
                return true;
            }

            // Validate that the excursion date falls within the vacation period
            assert excursionDate != null;
            if (excursionDate.before(vacationStartDate) || excursionDate.after(vacationEndDate)) {
                Toast.makeText(ExcursionDetails.this, "Excursion date must be within vacation start and end dates!", Toast.LENGTH_LONG).show();
                return true;
            }

            // Save or update the excursion
            if (excursionID == -1) {
                if (repository.getmAllExcursions().size() == 0)
                    excursionID = 1;
                else
                    excursionID = repository.getmAllExcursions().get(repository.getmAllExcursions().size() - 1).getExcursionID() + 1;
                excursion = new Excursion(excursionID, editExcursionTitle.getText().toString(), excursionDateStr, vacaID);
                repository.insert(excursion);
            } else {
                excursion = new Excursion(excursionID, editExcursionTitle.getText().toString(), excursionDateStr, vacaID);
                repository.update(excursion);
            }

            Toast.makeText(ExcursionDetails.this, excurTitle + " excursion saved!", Toast.LENGTH_LONG).show();
            finish();
            return true;
        }

        if (item.getItemId() == R.id.setexcursionalert) {

            String excursionDateFromScreen = editExcursionDate.getText().toString();
            String excursionTitleFromScreen = editExcursionTitle.getText().toString();

            // Verify inputs are not empty
            if (excursionTitleFromScreen.isEmpty() || excursionDateFromScreen.isEmpty()) {
                Toast.makeText(ExcursionDetails.this, "Please enter excursion title and date!", Toast.LENGTH_LONG).show();
                return true;
            }

            String myFormat = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myExcursionDate = null;

            try {
                myExcursionDate = sdf.parse(excursionDateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Long excurTrigger = myExcursionDate.getTime();
            Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
            intent.putExtra("key", "Your " + excursionTitleFromScreen + " excursion starts today!");
            PendingIntent excurSender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager excurAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            excurAlarmManager.set(AlarmManager.RTC_WAKEUP, excurTrigger, excurSender);

            Toast.makeText(ExcursionDetails.this, excursionTitleFromScreen + " Alert Set!", Toast.LENGTH_LONG).show();
            return true;
        }

        if (item.getItemId() == R.id.excursiondelete) {
            String excurTitle = editExcursionTitle.getText().toString().trim();

            // Find the current excursion to delete
            Excursion currentExcursion = null;
            for (Excursion excursion : repository.getmAllExcursions()) {
                if (excursion.getExcursionID() == excursionID) {
                    currentExcursion = excursion;
                    break;
                }
            }
            if (currentExcursion != null) {
                repository.delete(currentExcursion);
                Toast.makeText(ExcursionDetails.this, excurTitle + " excursion deleted!", Toast.LENGTH_LONG).show();
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}







