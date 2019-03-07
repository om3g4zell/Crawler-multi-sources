package fr.babuchon.crawler.model.site;

import fr.babuchon.crawler.model.tv.Program;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * This class extends {@link AbstractSite} and represent the Site "linternaute"
 * @author Louis Babuchon
 */
public class Linternaute extends AbstractSite{

    /**
     * The logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Linternaute.class);

    /**
     * Constructor
     */
    public Linternaute() {
        super();

        this.name = "linternaute";
        this.url = "https://www.linternaute.com/television/";

        allowUrl.add(Pattern.compile("https://www\\.linternaute\\.com/television/.*"));
    }

    @Override
    public ArrayList<Program> getPrograms(Document page) {
        ArrayList<Program> programs = new ArrayList<>();

        Element titleElement = page.select(".bu_tvprogram_episode_title").first();
        Element imageElement = page.select("meta[property=\"og:image\"]").first();

        String title = null;
        String imageUrl = null;

        if(titleElement != null && imageElement != null) {
            title = titleElement.text();
            imageUrl = imageElement.attr("abs:content");
        }

        if(imageUrl != null && title != null) {
            Program p = new Program(title);
            p.addIcon(imageUrl, name);
            programs.add(p);
        }

        return programs;
    }

}
