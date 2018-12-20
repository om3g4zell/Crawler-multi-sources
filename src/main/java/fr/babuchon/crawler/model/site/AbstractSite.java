package fr.babuchon.crawler.model.site;

import fr.babuchon.crawler.model.Program;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Pattern;

public abstract class AbstractSite {

    protected String name;

    protected String url;

    protected ArrayList<Pattern> allowUrl;
    protected ArrayList<Pattern> deniedUrl;


    public AbstractSite() {

        name = "Insert a name";
        url = "Insert an url";

        allowUrl = new ArrayList<>();
        deniedUrl = new ArrayList<>();
    }

    public abstract ArrayList<Program> getPrograms(Document page);

    public ArrayList<String> getUrls(Document page) {
        ArrayList<String> urls = new ArrayList<String>();

        Elements links = page.select("a[href]");

        for(Element link : links) {
            String url = link.attr("abs:href");
            if(!urls.contains(url))
                urls.add(url);
        }

        return urls;
    }

    public boolean isValidUrl(String url) {
        for(Pattern p : deniedUrl) {
            if(p.matcher(url).matches()) {
                return false;
            }
        }
        for(Pattern p : allowUrl) {
            if(p.matcher(url).matches()) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }
}
