package fr.babuchon.crawler.controller;

import fr.babuchon.crawler.model.tv.Program;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerTest {

    private Scheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = Scheduler.getInstance();
    }

    @Test
    void getInstance() {
        assertNotNull(scheduler);
        assertEquals(scheduler, Scheduler.getInstance());
    }

    @Test
    void fusionPrograms() {
        assertNotNull(scheduler);

        assertEquals(0, scheduler.getPrograms().size());

        ArrayList<Program> programs = new ArrayList<>();

        Program p1 = new Program("p1");
        p1.addIcon("url1", "site1");
        p1.addIcon("url2", "site2");
        p1.addIcon("url3", "site3");

        Program p2 = new Program("p1");
        p2.addIcon("url4", "site1");
        p2.addIcon("url5", "site2");
        p2.addIcon("url1", "site1");

        programs.add(p1);
        programs.add(p2);

        scheduler.fusionPrograms(programs);

        assertEquals(1, scheduler.getPrograms().size());
        assertEquals(5, scheduler.getPrograms().get(0).getIcons().size());

        Program p3 = new Program("p2");
        p3.addIcon("url1", "site1");
        p3.addIcon("url2", "site2");
        p3.addIcon("url3", "site3");

        programs.clear();
        programs.add(p3);

        scheduler.fusionPrograms(programs);
        assertEquals(2, scheduler.getPrograms().size());
        assertEquals(5, scheduler.getPrograms().get(0).getIcons().size());
        assertEquals(3, scheduler.getPrograms().get(1).getIcons().size());


    }
}