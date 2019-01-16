package fr.babuchon.crawler;

import fr.babuchon.crawler.model.Program;
import fr.babuchon.crawler.model.site.AbstractSite;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * This class is a runner who crawl programs from a given site
 * @author Louis Babuchon
 */
public class CrawlerRunner implements Callable<ArrayList<Program>> {

    /**
     * The logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerRunner.class);

    /**
     * The program's list
     */
    private ArrayList<Program> programs;

    /**
     * The toVisit queue
     */
    private Queue<String> queue;

    /**
     * The visited urls
     */
    private Set<String> visited;

    /**
     * The site to crawl
     */
    private AbstractSite site;

    /**
     * The timeout in seconds
     */
    private double timeout;

    /**
     * Constructor
     * @param queue : The concurent url to crawl
     * @param visited : the visited urls
     * @param site : The site to crawl
     * @param timeout : The timeout in ms
     */
    public CrawlerRunner(Queue<String> queue, Set<String> visited, AbstractSite site, double timeout) {
        programs = new ArrayList<>();

        this.queue = queue;
        this.visited = visited;
        this.site = site;
        this.timeout = timeout;
    }

    @Override
    public ArrayList<Program> call() throws Exception {
        //programs.add(new Program(LocalDateTime.now(), LocalDateTime.now(), new Channel("", "tf1", ""), "p", ""));
        double start = System.currentTimeMillis();
        double end = start;
        double faultCounter = 0;
        boolean stop = true;
        while(stop) {

            String url = queue.poll();
            if(url != null) {
                runPage(url);
                //LOGGER.debug("Crawled : {} T : {} Site : {}", url, Thread.currentThread().getName(), site.getUrl());
                faultCounter = 0;
            }
            else {
                Thread.sleep(1500);
                //System.out.println("No link : " + Thread.currentThread().getName());
                faultCounter++;
            }

            end = System.currentTimeMillis();

            if(end - start >= timeout) {
                stop = false;
            }
            if(faultCounter >= 6)
                stop = false;
        }
        LOGGER.info("Ended : {}", Thread.currentThread().getName());
        return this.programs;
    }

    /**
     * Extract programs and external links of the page
     * @param url : the url to crawl
     */
    public void runPage(String url) {
        try {
            ArrayList<String> urls;
            ArrayList<Program> programs;

            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com").get();
            visited.add(url);

            urls = site.getUrls(doc);

            programs = site.getPrograms(doc);

            for(String link : urls) {
                if(!visited.contains(link)) {
                    if(site.isValidUrl(link)) {
                        queue.add(link);
                    }
                    else {
                        visited.add(link);
                    }
                }
            }

            this.programs.addAll(programs);

        } catch (IOException e) {
            if(e instanceof UnsupportedMimeTypeException) {
                visited.add(url);
            }
            else if(e instanceof HttpStatusException) {
                HttpStatusException httpE = (HttpStatusException)e;
                switch (httpE.getStatusCode()) {
                    case 404 :
                        visited.add(url);
                        break;
                    case 410:
                        visited.add(url);
                        LOGGER.error("Error {} : " + url, httpE.getStatusCode(), e);
                        break;
                    case 503:
                        System.out.println("ddos ?" + url);
                        break;
                    default:
                        LOGGER.error("Error : {}", httpE.getStatusCode(), e);
                        break;
                }
            }
            else {
                e.printStackTrace();
            }
        }
    }
}
