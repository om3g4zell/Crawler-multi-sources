package fr.babuchon.crawler;

import fr.babuchon.crawler.model.Program;

import java.util.*;

public class Scheduler {

    private static Scheduler instance = null;

    private ArrayList<Program> programs;

    private Scheduler () {
        programs = new ArrayList<>();
    }

    public static Scheduler getInstance() {
        if(Objects.nonNull(instance)) return instance;
        else
            instance = new Scheduler();
        return getInstance();
    }

    public void fusionPrograms(ArrayList<Program> programs) {
        for(int i = 0; i < programs.size(); i++) {
            Program program = programs.get(i);
            boolean exist = false;
            for(Program realProgram : this.programs)
            if (realProgram.equals(program)) {
                addIcons(realProgram, program);
                exist = true;
            }
            if(!exist) {
                this.programs.add(program);
            }
        }
    }

    private void addIcons(Program mainProgram, Program program) {
        mainProgram.getIcons().putAll(program.getIcons());
    }

    public ArrayList<Program> getPrograms() {
        return programs;
    }

    public void printPrograms() {
        for(Program p : programs)
            System.out.println(p);
    }
}
