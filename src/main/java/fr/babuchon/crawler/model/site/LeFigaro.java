package fr.babuchon.crawler.model.site;

import fr.babuchon.crawler.model.tv.Program;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class extends {@link AbstractSite} and represent the Site "LeFigaro"
 * @author Louis Babuchon
 */
public class LeFigaro extends AbstractSite {


    /**
     * The programs url's page pattern
     */
    private static final Pattern PROGRAM_PATTERN = Pattern.compile("http://tvmag\\.lefigaro\\.fr/programme-tv/programme/.*");

    /**
     * The logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LeFigaro.class);

    /**
     * The image extension pattern
     */
    private static final Pattern IMAGE_PATTERN = Pattern.compile(
            "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)");

    /**
     * The trick url pattern
     */
    private static final Pattern TRICK_IMAGE_PATTERN = Pattern.compile(
            ".*/([0-9]*x_crop)/.*");

    /**
     * Constructor
     */
    public LeFigaro() {
        super();

        this.name = "Le Figaro";
        this.url = "http://tvmag.lefigaro.fr/";

        allowUrl.add(Pattern.compile("http://tvmag\\.lefigaro\\.fr/.*"));

    }


    @Override
    public ArrayList<Program> getPrograms(Document page) {
        ArrayList<Program> programs = new ArrayList<>();

        if(PROGRAM_PATTERN.matcher(page.location()).matches()) {
            Element titleElement = page.select(".tvm-program__title").first();
            Elements imageElements = page.select(".fig-hidden-links a");
            String title = null;
            if(titleElement != null) {
                title = titleElement.text();
                Program p = new Program(title);
                for(Element e : imageElements) {
                    String imageUrl = e.attr("abs:href");
                    if(IMAGE_PATTERN.matcher(imageUrl).matches()) {
                        p.addIcon(trickImageUrl(imageUrl), name);
                    }
                }
                programs.add(p);
                LOGGER.debug(p.toString());
            }

        }
        return programs;
    }

    /**
     * Take the image url and apply tricks to return the link of the original image from the server
     * @param imageUrl : The url to trick
     * @return String : The tricked url
     */
    private String trickImageUrl(String imageUrl) {
        Matcher m = TRICK_IMAGE_PATTERN.matcher(imageUrl);
        if(m.matches()) {
            String result = imageUrl.replace(m.group(1), "5000x");
            LOGGER.debug(result);
            return result;
        }
        else
            return imageUrl;
    }
}
