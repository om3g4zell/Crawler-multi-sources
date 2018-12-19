package fr.babuchon.crawler;

import fr.babuchon.crawler.model.Program;
import fr.babuchon.crawler.model.site.Tele7;
import fr.babuchon.crawler.model.xmltv.XMLTVGetter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
	
    public static void main( String[] args ) {

        Scheduler scheduler = Scheduler.getInstance();
        ExecutorService service = Executors.newFixedThreadPool(2);

        List<Future<ArrayList<Program>>> futures = new ArrayList<>();


        futures.add(service.submit(new GuideCrawler(new Tele7())));
        //futures.add(service.submit(new XMLTVGetter()));

        for(Future<ArrayList<Program>> f : futures) {
            try {
                scheduler.fusionPrograms(f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Size : " + scheduler.getPrograms().size());
        try {
            FileWriter fw = new FileWriter(new File("res/test.txt"));
            for(Program p : scheduler.getPrograms()) {
                fw.write(p.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //scheduler.printPrograms();
        service.shutdown();

        // TODO Ajouter le telechargement / sauvegarde des images de manière mulithreadé
        // TODO Ajouter un fichier de configuration
        // TODO Ajouter de nombreux site
    }
}
