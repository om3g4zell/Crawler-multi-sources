package fr.babuchon.crawler.model.site;

import fr.babuchon.crawler.model.Program;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Tele7 extends AbstractSite{


    public Tele7() {
        super();
        this.name = "Tele 7 jour";
        this.url = "https://www.programme-television.org/";

        allowUrl.add(Pattern.compile("https://www\\.programme-television\\.org/.*"));
    }

    @Override
    public ArrayList<Program> getPrograms(Document page) {
        // TODO
        ArrayList<Program> programs= new ArrayList<>();
        Element typeElement = page.select("meta[property=\"og:type\"]").first();
        Element imageElement = page.select("meta[property=\"og:image\"]").first();
        Element titleElement = page.select("meta[property=\"og:title\"]").first();

        String imageUrl = null;
        String title = null;

        if(typeElement != null && typeElement.attr("content").equals("tv_show")) {
            imageUrl = imageElement.attr("content");
            title = titleElement.attr("content");
        }

        if(imageUrl != null && title != null) {
            Program p = new Program(title);
            p.addIcon(imageUrl, name);
            programs.add(p);
        }
        return programs;
    }

    @Override
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

    @Override
    public boolean isValidUrl(String url) {
        for(Pattern p : denieUrl) {
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

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }
}
