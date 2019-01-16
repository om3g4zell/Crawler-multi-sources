package fr.babuchon.crawler.model.site;

import fr.babuchon.crawler.model.Program;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class extends {@link AbstractSite} and represent the Site "Tele Loisir"
 * @author Louis Babuchon
 */
public class Teleloisir extends AbstractSite{

    /**
     * The trick url pattern
     */
    private static final Pattern TRICK_IMAGE_PATTERN = Pattern.compile("^.*(http\\.3A\\.2F\\.2F.*\\.2Ejpg).*");

    /**
     * The logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Teleloisir.class);

    /**
     * Constructor
     */
    public Teleloisir() {
        super();

        this.name = "Tele loisir";
        this.url = "https://www.programme-tv.net/";
        //this.url = "https://www.programme-tv.net/cinema/2499963-love-et-autres-drogues/";
        allowUrl.add(Pattern.compile("https://www\\.programme-tv\\.net/.*"));


    }

    @Override
    public ArrayList<Program> getPrograms(Document page) {
        ArrayList<Program> programs = new ArrayList<>();

        Element titleElement = page.select("#corps h1.ws-nw").first();
        Element typeElement = page.select("meta[property=\"og:type\"]").first();
        Element imageElement = page.select("meta[property=\"og:image\"]").first();

        String  imageUrl = null;
        String title = null;
        String type = null;
        if(typeElement != null && titleElement != null && imageElement != null) {
            type = typeElement.attr("content");
            if(type.contains("video")) {
                imageUrl = imageElement.attr("abs:content");
                title = titleElement.text();
                LOGGER.debug("type : {}", type);
            }
        }

        if(imageUrl != null && title != null) {
            Program p = new Program(title);
            p.addIcon(trickImageUrl(imageUrl), name);
            programs.add(p);
        }

        return programs;
    }

    /**
     * Take the image url and apply tricks to return the link of the original image from the server
     * @param imageUrl : The url to trick
     * @return String : The tricked url
     */
    private String trickImageUrl(String imageUrl) {

        try {
            Matcher m = TRICK_IMAGE_PATTERN.matcher(imageUrl);
            if(m.matches()) {
                String result = m.group(1).replace(".", "%");
                return URLDecoder.decode( result, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return imageUrl;
    }
}
