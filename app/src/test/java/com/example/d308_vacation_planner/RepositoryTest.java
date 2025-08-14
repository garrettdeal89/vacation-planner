package com.example.d308_vacation_planner;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.d308_vacation_planner.UI.database.Repository;
import com.example.d308_vacation_planner.UI.entities.Vacation;

import java.util.List;


public class RepositoryTest {

    Repository repository = new Repository(new android.app.Application());

    @Test
    public void testInsertVacationExists() throws InterruptedException {
        // Create a test vacation
        Vacation vacation = new Vacation(100, "Test Vacation", "Test Hotel", "08/15/2025", "08/20/2025");
        repository.insert(vacation);

        // Retrieve all vacations
        List<Vacation> vacations = repository.getmAllVacations();
        boolean exists = vacations.stream().anyMatch(v -> v.getVacationID() == 100);

        // Assertion: the vacation should exist
        assertTrue("Inserted vacation should exist in repository", exists);
    }
}