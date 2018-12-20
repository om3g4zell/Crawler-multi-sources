package fr.babuchon.crawler;

import fr.babuchon.crawler.model.Program;
import fr.babuchon.crawler.model.site.Tele7;
import fr.babuchon.crawler.model.xmltv.XMLTVGetter;
import fr.babuchon.crawler.utils.HTTPImageGetter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public final static String IMAGE_SAVE_PATH = "D:\\Images\\PRD\\multi-sources";

    public static void main( String[] args ) {

        Scheduler scheduler = Scheduler.getInstance();
        ExecutorService service = Executors.newFixedThreadPool(2);

        List<Future<ArrayList<Program>>> futures = new ArrayList<>();


        futures.add(service.submit(new GuideCrawler(new Tele7(), 200000)));
        futures.add(service.submit(new XMLTVGetter()));

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
        /*try {
            FileWriter fw = new FileWriter(new File("res/test.txt"));
            for(Program p : scheduler.getPrograms()) {
                fw.write(p.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        //scheduler.printPrograms();
        service.shutdown();

        // Download images
        int counter = 0;
        List<Future<Integer>> imageFutures = new ArrayList<>();
        ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue<>(scheduler.getPrograms());
        int nbThread = 10;
        service = Executors.newFixedThreadPool(nbThread);
        for(int i = 0; i  < nbThread; i++) {
            imageFutures.add(service.submit(new HTTPImageGetter(queue, 0, IMAGE_SAVE_PATH)));
        }
        try {
            for (Future<Integer> f : imageFutures) {
                counter += f.get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        service.shutdown();
        System.out.println("Downloaded : " + counter);

        // TODO Ajouter le telechargement / sauvegarde des images de manière mulithreadé
        // TODO Ajouter un fichier de configuration
        // TODO Ajouter de nombreux site
    }
}
