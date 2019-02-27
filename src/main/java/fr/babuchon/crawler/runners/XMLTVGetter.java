package fr.babuchon.crawler.runners;

import fr.babuchon.crawler.model.tv.Program;
import fr.babuchon.crawler.model.xmltv.Guide;
import fr.babuchon.crawler.utils.XmltvParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class implements {@link Callable} This class download the lastest XMLTV and parse it
 * @author Louis Babuchon
 */
public class XMLTVGetter implements Callable<ArrayList<Program>>{

    /**
     * The logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(XMLTVGetter.class);

    /**
     * The xmltv saving directory
     */
    private String directory;

    /**
     * The url of the site
     */
    private String url;

    /**
     * The url of the xmltv
     */
    private String xmlUrl;

    /**
     * The date pattern
     */
    private final static Pattern pattern = Pattern.compile("((\\d{2}/\\d{2}/\\d{4})|((lundi|mardi|mercredi|jeudi|vendredi|samedi|dimanche) \\d{1,2} (janvier|fevrier|mars|avril|mai|juin|juillet|aout|septembre|octobre|novembre|decembre) \\d{4}))");

    /**
     * Constructor
     * @param directory : the saving directory
     * @param url : The url of the site
     * @param xmlUrl : The xmltv url
     */
    public XMLTVGetter(String directory, String url, String xmlUrl) {
        this.directory = directory;
        this.url = url;
        this.xmlUrl = xmlUrl;
    }

    @Override
    public ArrayList<Program> call() throws Exception {
        Guide guide = new Guide();
        XmltvParser parser = new XmltvParser();
        checkUpdate();
        List<File> xmls = getXmltvs(directory);


        for(File f: xmls) {
            parser.changeXML(directory + "/" + f.getPath());
            NodeList channelsNode = parser.getChannels();
            NodeList programsNode = parser.getPrograms();

            guide.AddChannel(channelsNode);
            guide.addPrograms(programsNode);
        }

        return new ArrayList<>(guide.programs);
    }

    /**
     * Return all the xmltv of the given directory
     * @param dir : The directory where are the xmltvs
     * @return All the xmltv's path
     */
    private List<File> getXmltvs(String dir) {
        File directory = new File(dir);
        ArrayList<File> files = new ArrayList<>();
        if(directory.isDirectory() && directory.exists()) {
            for(String f : Objects.requireNonNull(directory.list())) {
                int index = f.indexOf('.');
                String extension = "";
                if(index > 0) {
                    extension = f.substring(index + 1);
                }
                if(extension.equals("xml"))
                    files.add(new File(f));
            }
        }
        return files;
    }

    /**
     * Check if there is a new xmltv on the site
     * @throws IOException : If there are problems in the connection
     */
    private void checkUpdate()  throws IOException {
        Document doc = Jsoup.connect(url).get();
        byte[] xml = Jsoup.connect(xmlUrl).maxBodySize(0).execute().bodyAsBytes();
        String updateDate = getDate(doc);
        saveXML(directory + updateDate + ".xml", xml);
    }

    /**
     * Parse the date of the XMLTV from the site
     * @param doc : The page to parse
     * @return String : the date
     */
    private static String getDate(Document doc) {

        // We get the copyright content
        Elements copyright = doc.getElementsByClass("copyright");
        String copyStr = copyright.text();

        // We extract the update date
		/*int begin = copyStr.indexOf(":") + 2;
		int end = copyStr.indexOf(",");

		if(begin == -1 || end == -1) System.out.println("fuck");*/
        Matcher m = pattern.matcher(copyStr);
        String date;
        if(m.matches()) {
            date = m.group(0);
            date = date.replace(" ", "_");
        }
        else {
            date = new SimpleDateFormat("yyyy_MM_dd").format(new Date());
        }

        return date;
    }

    /**
     * Save the XMLTV in the given path
     * @param path : The path to save
     * @param content : the xmltv
     */
    private static void saveXML(String path, byte[] content) {
        File file = new File(path);
        try {
            if(!file.exists() && !file.isDirectory()) {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(content);
                fos.close();
                LOGGER.info("A new XML has been saved ! {}", file.getAbsolutePath());
            } else {
                LOGGER.info("Xml not saved file exists already !");
            }
        } catch (IOException e) {
            LOGGER.error("Error : ", e);
        }
    }

}
