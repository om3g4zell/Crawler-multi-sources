package fr.babuchon.crawler;

import fr.babuchon.crawler.controller.Scheduler;
import fr.babuchon.crawler.model.site.*;
import fr.babuchon.crawler.model.tv.Program;
import fr.babuchon.crawler.runners.GuideCrawler;
import fr.babuchon.crawler.runners.XMLTVGetter;
import fr.babuchon.crawler.utils.HTTPImageGetter;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

/**
 * Main class
 * @author Louis Babuchon
 */
public class Main {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    /**
     * The main method
     * @param args : the main args
     */
    public static void main( String[] args ) {

        File resFile = new File("/res");
        if(!resFile.exists()) {
            if (!resFile.mkdir()) {
                LOGGER.error("Le dossier res n'a pas pu être crée !");
                return;
            }
        }
        JSONObject configJson;
        JSONObject xmltvObject;

        // Sites
        JSONObject tele7Object;
        JSONObject teleLoisirObject;
        JSONObject ceSoirTvObject;
        JSONObject linternauteObject;
        JSONObject figaroObject;

        // Runners
        JSONObject crawlersObject;
        JSONObject downloaderObject;

        try {
            String configPath = "";
            LOGGER.debug(Arrays.toString(args));
            if(args.length >= 1) {
                configPath = args[0];
            }
            else {
                LOGGER.error("Vous devez fournir un fichier de configuration");
                return;
            }
            File file = new File(configPath);
            LOGGER.debug(file.getPath());
            String content = FileUtils.readFileToString(file, "utf-8");
            configJson = new JSONObject(content);
            crawlersObject = configJson.getJSONObject("crawlers");
            xmltvObject = crawlersObject.getJSONObject("xmltv");
            tele7Object = crawlersObject.getJSONObject("tele7");
            teleLoisirObject = crawlersObject.getJSONObject("tele_loisir");
            figaroObject = crawlersObject.getJSONObject("figaro");
            linternauteObject = crawlersObject.getJSONObject("linternaute");
            ceSoirTvObject = crawlersObject.getJSONObject("ceSoirTv");
            downloaderObject = configJson.getJSONObject("downloader");

        } catch (IOException e) {
            LOGGER.error("Error in config file loading", e);
            return;
        }

        Scheduler scheduler = Scheduler.getInstance();
        Iterator<String> keys = crawlersObject.keys();
        int nbCrawler = 0;
        while(keys.hasNext()) {
            String key = keys.next();
            if(crawlersObject.get(key) instanceof JSONObject) {
                if(((JSONObject) crawlersObject.get(key)).getBoolean("active"))
                    nbCrawler++;
            }
        }
        if(nbCrawler == 0)
            return;
        ExecutorService service = Executors.newFixedThreadPool(nbCrawler);

        List<Future<ArrayList<Program>>> futures = new ArrayList<>();

        // On crée les crawler associées
        if(tele7Object.getBoolean("active"))
            futures.add(service.submit(new GuideCrawler(new Tele7(), tele7Object.getDouble("timeout"), tele7Object.getInt("nb_thread"))));
        if(xmltvObject.getBoolean("active"))
            futures.add(service.submit(new XMLTVGetter(xmltvObject.getString("directory"), xmltvObject.getString("url"), xmltvObject.getString("xml_url"))));
        if(teleLoisirObject.getBoolean("active"))
            futures.add(service.submit(new GuideCrawler(new Teleloisir(), teleLoisirObject.getDouble("timeout"), teleLoisirObject.getInt("nb_thread"))));
        if(figaroObject.getBoolean("active"))
            futures.add(service.submit(new GuideCrawler(new LeFigaro(), figaroObject.getDouble("timeout"), figaroObject.getInt("nb_thread"))));
        if(ceSoirTvObject.getBoolean("active"))
            futures.add(service.submit(new GuideCrawler(new CeSoirTv(), ceSoirTvObject.getDouble("timeout"), ceSoirTvObject.getInt("nb_thread"))));
        if(linternauteObject.getBoolean("active"))
            futures.add(service.submit(new GuideCrawler(new Linternaute(), linternauteObject.getDouble("timeout"), linternauteObject.getInt("nb_thread"))));


        for(Future<ArrayList<Program>> f : futures) {
            try {
                scheduler.fusionPrograms(f.get());
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error("Error : ", e);
            }
        }
        LOGGER.info("Number of programs {}", scheduler.getPrograms().size());

        service.shutdown();

        // Download images
        if(!downloaderObject.getBoolean("active"))
            return;

        int counter = 0;
        List<Future<Integer>> imageFutures = new ArrayList<>();
        ConcurrentLinkedQueue<Program> queue = new ConcurrentLinkedQueue<>(scheduler.getPrograms());
        int nbThread = downloaderObject.getInt("nb_thread");
        service = Executors.newFixedThreadPool(nbThread);
        for(int i = 0; i  < nbThread; i++) {
            imageFutures.add(service.submit(new HTTPImageGetter(queue, downloaderObject.getDouble("timeout"), downloaderObject.getString("image_path"))));
        }
        try {
            for (Future<Integer> f : imageFutures) {
                counter += f.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Error : ", e);
        }
        service.shutdown();
        LOGGER.info("Downloaded : {}", counter);
    }
}
