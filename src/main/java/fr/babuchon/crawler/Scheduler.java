package fr.babuchon.crawler;

import fr.babuchon.crawler.model.Program;

import java.util.*;

/**
 * This class is a singleton it contain all the programs
 */
public class Scheduler {

    /**
     * The instance
     */
    private static Scheduler instance = null;

    /**
     * The list of programs
     */
    private ArrayList<Program> programs;

    /**
     * private constructor
     */
    private Scheduler () {
        programs = new ArrayList<>();
    }

    /**
     * Get the instance
     * @return the instance
     */
    public static Scheduler getInstance() {
        if(Objects.nonNull(instance)) return instance;
        else
            instance = new Scheduler();
        return getInstance();
    }

    /**
     * Fusion programs with their name
     * @param programs : The list of program to fusion
     */
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

    /**
     * Add all the icons
     * @param mainProgram : The program to get the icons
     * @param program : The program to give its icons
     */
    private void addIcons(Program mainProgram, Program program) {
        mainProgram.getIcons().putAll(program.getIcons());
    }

    /**
     * Get all the programs
     * @return : Return all the programs
     */
    public ArrayList<Program> getPrograms() {
        return programs;
    }

    /**
     * Print all the programs
     */
    public void printPrograms() {
        for(Program p : programs)
            System.out.println(p);
    }
}
