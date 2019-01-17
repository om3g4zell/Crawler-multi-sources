package fr.babuchon.crawler;

import fr.babuchon.crawler.model.site.AbstractSite;
import fr.babuchon.crawler.model.Program;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * This class represent the main task of the crawler for the given site, it creates sub-crawlers
 * @author Louis Babuchon
 */
public class GuideCrawler implements Callable<ArrayList<Program>>{

    /**
     * The logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GuideCrawler.class);

    /**
     * The timeout in ms
     */
    private double timeout;

    /**
     * The visited urls
     */
    private Set<String> visited;

    /**
     * The list of programs
     */
    private ArrayList<Program> programs;

    /**
     * The urls queue
     */
    private Queue<String> queue;

    /**
     * The number of crawler to launch
     */
    private int nbThread;

    /**
     * The site to crawl
     */
    private AbstractSite site;

    /**
     * Constructor
     * @param site : The site to crawl
     * @param timeout : The timeout in ms
     * @param nbThread : The number of crawler
     */
    public GuideCrawler(AbstractSite site, double timeout, int nbThread) {

        this.site = site;
        this.timeout = timeout;
        this.nbThread = nbThread;

        visited = ConcurrentHashMap.newKeySet();
        programs = new ArrayList<>();
        queue = new ConcurrentLinkedQueue<>();

        queue.add(site.getUrl());
    }

    /**
     * Constructor with default values
     * @param site : The site to crawl
     */
    public GuideCrawler(AbstractSite site) {
        this(site, 200000, 10);
    }

    @Override
    public ArrayList<Program> call() throws Exception {

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
        LOGGER.info("Ended : {}", Thread.currentThread().getName());
        return programs;
    }
}

