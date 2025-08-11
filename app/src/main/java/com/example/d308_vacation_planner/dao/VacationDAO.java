package com.example.d308_vacation_planner.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d308_vacation_planner.UI.entities.Excursion;
import com.example.d308_vacation_planner.UI.entities.Vacation;

import java.util.List;

@Dao
public interface VacationDAO {

    //insert db
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Vacation vacation);

    //update db
    @Update
    void update(Vacation vacation);

    //delete db
    @Delete
    void delete(Vacation vacation);

    //query db
    @Query("SELECT * FROM VACATIONS ORDER BY vacationID ASC")
    List<Vacation> getAllVacations();

    //search method
    @Query("SELECT * FROM vacations WHERE vacationTitle LIKE :searchQuery ORDER BY vacationID ASC")
    LiveData<List<Vacation>> searchVacations(String searchQuery);
}
