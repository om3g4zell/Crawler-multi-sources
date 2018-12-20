package fr.babuchon.crawler.model.xmltv;

import fr.babuchon.crawler.model.Program;
import fr.babuchon.crawler.utils.XmltvParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLTVGetter implements Callable<ArrayList<Program>>{
    private String directory;
    private String url;
    private String xmlUrl;
    private final static Pattern pattern = Pattern.compile("((\\d{2}/\\d{2}/\\d{4})|((lundi|mardi|mercredi|jeudi|vendredi|samedi|dimanche) \\d{1,2} (janvier|fevrier|mars|avril|mai|juin|juillet|aout|septembre|octobre|novembre|decembre) \\d{4}))");

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
            //System.out.println(guide.programString());
        }
        /*System.out.println(guide.programString());
        for(Program  p : guide.getPrograms()) {
            for(Map.Entry<String, String> i : p.getIcons().entrySet()) {
                if(!i.getKey().equals("")) {
                    HTTPImageGetter getter = new HTTPImageGetter(i.getKey(), SAVE_REPO, p.getTitle());
                    getter.run();
                }
            }
        }*/
        return new ArrayList<>(guide.programs);
    }

    private List<File> getXmltvs(String dir) {
        File directory = new File(dir);
        ArrayList<File> files = new ArrayList<>();
        if(directory.isDirectory() && directory.exists()) {
            for(String f : directory.list()) {
                System.out.println(f);
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

    private void checkUpdate()  throws IOException {
        Document doc = Jsoup.connect(url).get();
        byte[] xml = Jsoup.connect(xmlUrl).maxBodySize(0).execute().bodyAsBytes();
        String updateDate = getDate(doc);
        saveXML(directory + updateDate + ".xml", xml);
    }

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

    private static void saveXML(String path, byte[] content) {
        File file = new File(path);
        try {
            if(!file.exists() && !file.isDirectory()) {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(content);
                fos.close();
                System.out.println("A new XML has been saved ! " + file.getAbsolutePath());
            } else {
                System.out.println("Xml not saved file exists already !");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
