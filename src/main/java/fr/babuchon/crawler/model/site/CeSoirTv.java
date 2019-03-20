package fr.babuchon.crawler.model.site;

import fr.babuchon.crawler.model.tv.Program;
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
 * This class extends {@link AbstractSite} and represent the Site "CeSoirTv"
 * @author Louis Babuchon
 */
public class CeSoirTv extends AbstractSite{

    /**
     * The trick url pattern
     */
    private static final Pattern TRICK_IMAGE_PATTERN = Pattern.compile("^.*(http\\.3A\\.2F\\.2F.*\\.2Ejpg).*");

    /**
     * The programs url's page pattern
     */
    private static final Pattern PROGRAM_PATTERN = Pattern.compile("https://www\\.cesoirtv\\.com/programme/.*");

    /**
     * The logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CeSoirTv.class);

    /**
     * Constructor
     */
    public CeSoirTv() {
        super();

        this.name = "CeSoirTv";
        this.url = "https://www.cesoirtv.com";

        allowUrl.add(Pattern.compile("https://www\\.cesoirtv\\.com/.*"));
    }

    @Override
    public ArrayList<Program> getPrograms(Document page) {
        ArrayList<Program> programs = new ArrayList<>();
        if(PROGRAM_PATTERN.matcher(page.location()).matches()) {
            Element titleElement = page.select(".Titre.d-ib").first();
            Element imageElement = page.select("meta[property=\"og:image\"]").first();

            String title = null;
            String imageUrl = null;

            if(titleElement != null && imageElement != null) {
                title = titleElement.text();
                imageUrl = imageElement.attr("abs:content");

            }

            if(imageUrl != null && title != null) {
                Program p = new Program(title);
                //p.addIcon(trickImageUrl(imageUrl), name);
                p.addIcon(imageUrl, name);
                programs.add(p);
            }
        }
        return programs;
    }

    /**
     * Take the image url and apply tricks to return the link of the original image from the server
     * @param imageUrl : The url to trick
     * @return String : The tricked url
     */
    protected String trickImageUrl(String imageUrl) {

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
