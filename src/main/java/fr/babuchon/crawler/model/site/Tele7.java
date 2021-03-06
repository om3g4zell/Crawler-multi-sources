package fr.babuchon.crawler.model.site;

import fr.babuchon.crawler.model.tv.Program;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * This class extends {@link AbstractSite} and represent the Site "Tele7"
 * @author Louis Babuchon
 */
public class Tele7 extends AbstractSite{

    /**
     * Constructor
     */
    public Tele7() {
        super();
        this.name = "Tele7";
        this.url = "https://www.programme-television.org/";

        allowUrl.add(Pattern.compile("https://www\\.programme-television\\.org/.*"));
    }

    @Override
    @SuppressWarnings("Duplicated")
    public ArrayList<Program> getPrograms(Document page) {
        ArrayList<Program> programs= new ArrayList<>();
        Element typeElement = page.select("meta[property=\"og:type\"]").first();
        Element imageElement = page.select("meta[property=\"og:image\"]").first();
        Element titleElement = page.select("meta[property=\"og:title\"]").first();

        String imageUrl = null;
        String title = null;

        if(typeElement != null && typeElement.attr("content").equals("tv_show")) {
            imageUrl = imageElement.attr("abs:content");
            title = titleElement.attr("content");
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
    String trickImageUrl(String imageUrl) {
        int start = imageUrl.lastIndexOf("/r");
        int end = imageUrl.indexOf("/img");
        if(start != -1 && end != -1) {
            String temp = "" + imageUrl;
            imageUrl = imageUrl.substring(0, start) + temp.substring(end, temp.length());
        }
        return imageUrl;
    }
}
