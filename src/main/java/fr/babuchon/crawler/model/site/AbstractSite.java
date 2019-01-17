package fr.babuchon.crawler.model.site;

import fr.babuchon.crawler.model.Program;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * This class is an abstract class wich represent a Site to crawl
 * @author Louis Babuchon
 */
public abstract class AbstractSite {

    /**
     * The name of the site
     */
    protected String name;

    /**
     * The starting url
     */
    protected String url;

    /**
     * Allowed url to follow pattern
     */
    protected ArrayList<Pattern> allowUrl;

    /**
     * Banned url to follow pattern
     */
    protected ArrayList<Pattern> deniedUrl;


    /**
     * Constructor, initialize variables
     */
    public AbstractSite() {

        name = "Insert a name";
        url = "Insert an url";

        allowUrl = new ArrayList<>();
        deniedUrl = new ArrayList<>();
    }

    /**
     * Parse the page and return all the programs
     * @param page : the page to parse
     * @return The list of programs found
     */
    public abstract ArrayList<Program> getPrograms(Document page);

    /**
     * Parse the page and return all the links
     * @param page : The page to parse
     * @return The list of external links
     */
    public ArrayList<String> getUrls(Document page) {
        ArrayList<String> urls = new ArrayList<>();

        Elements links = page.select("a[href]");

        for(Element link : links) {
            String url = link.attr("abs:href");
            if(!urls.contains(url))
                urls.add(url);
        }

        return urls;
    }

    /**
     * Check if the url isValid with the allowed and denied pattern
     * @param url : the url to verify
     * @return boolean : True if valid, False else
     */
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

    /**
     * Return the name of the site
     * @return String : the name of the site
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return the starting url of the site
     * @return String : the starting url of the site
     */
    public String getUrl() {
        return this.url;
    }
}
