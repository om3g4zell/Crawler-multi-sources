package fr.babuchon.crawler.model.site;

import fr.babuchon.crawler.model.Program;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeFigaro extends AbstractSite {

    private static final Pattern p = Pattern.compile("http://tvmag\\.lefigaro\\.fr/programme-tv/programme/.*");
    private static final Logger LOGGER = LoggerFactory.getLogger(LeFigaro.class);
    private static final Pattern IMAGE_PATTERN = Pattern.compile(
            "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)");
    private static final Pattern TRICK_IMAGE_PATTERN = Pattern.compile(
            ".*/([0-9]*x_crop)/.*");

    public LeFigaro() {
        super();

        this.name = "Le Figaro";
        this.url = "http://tvmag.lefigaro.fr/";

        allowUrl.add(Pattern.compile("http://tvmag\\.lefigaro\\.fr/.*"));

    }

    @Override
    public ArrayList<Program> getPrograms(Document page) {
        ArrayList<Program> programs = new ArrayList<>();

        if(p.matcher(page.location()).matches()) {
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

    private String trickImageUrl(String imageUrl) {
        Matcher m = TRICK_IMAGE_PATTERN.matcher(imageUrl);
        if(m.matches()) {
            String result = imageUrl.replace(m.group(1), "5000x");
            LOGGER.debug(result);
            if (result == null) {
                return imageUrl;
            } else {
                return result;
            }
        }
        else
            return imageUrl;
    }
}
