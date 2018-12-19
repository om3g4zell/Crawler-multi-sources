package fr.babuchon.crawler.model.site;

import fr.babuchon.crawler.model.Program;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.regex.Pattern;

public abstract class AbstractSite {

    protected String name;

    protected String url;

    protected ArrayList<Pattern> allowUrl;
    protected ArrayList<Pattern> denieUrl;


    public AbstractSite() {
        name = "Insert a name";
        url = "Insert an url";

        allowUrl = new ArrayList<>();
        denieUrl = new ArrayList<>();
    }

    public abstract ArrayList<Program> getPrograms(Document page);

    public abstract ArrayList<String> getUrls(Document page);

    public abstract boolean isValidUrl(String url);

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }
}
