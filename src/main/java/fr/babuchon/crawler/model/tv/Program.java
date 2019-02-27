package fr.babuchon.crawler.model.tv;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * This class represent the model of a Program
 * @author Louis Babuchon
 */
public class Program {

	/**
	 * The start time of the program
	 */
	private LocalDateTime start;

	/**
	 * The end time of the program
	 */
	private LocalDateTime end;

	/**
	 * The program's channel
	 */
	private Channel channel;

	/**
	 * The program's title
	 */
	private String title;

	/**
	 * The program's episode
	 */
	private String episode;

	/**
	 * The list of images of the programm
	 * key is the image's url and the value the site
	 */
	private HashMap<String, String> icons;

	/**
	 * Constructor
	 * @param start : The start time of the program
	 * @param end : The end time of the program
	 * @param channel : The program's channel
	 * @param title : The program's title
	 * @param episode : The program's episode
	 */
	public Program(LocalDateTime start, LocalDateTime end, Channel channel, String title, String episode) {
		icons = new HashMap<>();
		this.start = start;
		this.end = end;
		this.channel = channel;
		this.title = title;
		this.episode = episode;
	}

	/**
	 * Constructor
	 * @param title : The program's title
	 */
	public Program(String title) {
		this(LocalDateTime.now(), LocalDateTime.now(), new Channel("Test", "Test", "Test"), title, "0");
	}

	/**
	 * Constructor
	 * @param start : The start time of the program
	 * @param end : The end time of the program
	 * @param channel : The program's channel
	 * @param title : The program's title
	 * @param episode : The program's episode
	 * @param icon : The list of images of the programm
	 * key is the image's url and the value the site
	 */
	public Program(LocalDateTime start, LocalDateTime end, Channel channel, String title, String episode, String icon) {
		this(start, end, channel, title, episode);
		addIcon(icon, "default");
		
	}

	/**
	 * Add an icon to the program
	 * @param url : The icon's url
	 * @param site : the site
	 */
	public void addIcon(String url, String site) {
		icons.put(url, site);
	}

	/**
	 * Return the program's start
	 * @return : The program's start
	 */
	public LocalDateTime getStart() {
		return start;
	}

	/**
	 * Return the program's end
	 * @return : The program's end
	 */
	public LocalDateTime getEnd() {
		return end;
	}

	/**
	 * Return the program's channel
	 * @return Channel : The channel
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * Return the program's title
	 * @return String : the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Return the program's episode
	 * @return : The program's episode
	 */
	public String getEpisode() {
		return episode;
	}

	/**
	 * Return the program's icons
	 * @return : The program's icons
	 */
	public HashMap<String, String> getIcons() {
		return icons;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder("\nDebut : " + toStringDate(start)
                + "\nEnd : " + toStringDate(end)
                + "\nTitre : " + title + "\nEpisode : " + episode + "\n" + "Channel : \n" + channel
                + "Icons : \n{\n");
		Iterator<Entry<String, String>> it = icons.entrySet().iterator();
	    while (it.hasNext()) {
	    	Entry<String, String> pair = it.next();
	        str.append("\ticon : ").append(pair.getKey()).append(" from ").append(pair.getValue()).append("\n");
	    }
	    str.append("\n}\n");
	    return str.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Program) {
			Program c = (Program)o;
			if(title.equals(c.title))
				return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return toStringDate(start).hashCode() + toStringDate(end).hashCode() + title.hashCode();
	}

	/**
	 * Return the formatted date
	 * @param date : The date to format
	 * @return String : The formatted date
	 */
	private String toStringDate(LocalDateTime date) {
		return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:ss"));
	}
}
