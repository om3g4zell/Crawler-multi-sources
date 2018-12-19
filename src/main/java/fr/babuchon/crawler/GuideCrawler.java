package fr.babuchon.crawler;

import fr.babuchon.crawler.model.site.AbstractSite;
import fr.babuchon.crawler.model.Program;
import java.util.*;
import java.util.concurrent.*;

public class GuideCrawler implements Callable<ArrayList<Program>>{

    public double timeout;

    private Set<String> visited;
    private ArrayList<Program> programs;
    private Queue<String> queue;

    private AbstractSite site;

    public GuideCrawler(AbstractSite site, double timeout) {

        this.site = site;
        this.timeout = timeout;

        visited = ConcurrentHashMap.newKeySet();
        programs = new ArrayList<>();
        queue = new ConcurrentLinkedQueue<>();

        queue.add(site.getUrl());
    }

    public GuideCrawler(AbstractSite site) {
        this(site, 200000);
    }

    @Override
    public ArrayList<Program> call() throws Exception {

        int nbThread = 10;

        ExecutorService service = Executors.newFixedThreadPool(nbThread);

        List<Future<ArrayList<Program>>> futures = new ArrayList<>();

        for(int i = 0; i < nbThread; i++) {
            Future<ArrayList<Program>> future = service.submit(new CrawlerRunner(queue, visited, site, timeout));
            futures.add(future);
        }
        for(Future<ArrayList<Program>> f : futures) {
            programs.addAll(f.get());
        }
        service.shutdown();
        System.out.println("Ended : " + Thread.currentThread().getName());
        return programs;
    }
}
