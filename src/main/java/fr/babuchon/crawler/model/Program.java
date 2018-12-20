package fr.babuchon.crawler.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class Program {
	
	private LocalDateTime start;
	private LocalDateTime end;
	private Channel channel;
	private String title;
	private String episode;
	
	// url, site
	private HashMap<String, String> icons;
	
	public Program(LocalDateTime start, LocalDateTime end, Channel channel, String title, String episode) {
		icons = new HashMap<>();
		this.start = start;
		this.end = end;
		this.channel = channel;
		this.title = title;
		this.episode = episode;
	}

	public Program(String title) {
		this(LocalDateTime.now(), LocalDateTime.now(), new Channel("Test", "Test", "Test"), title, "0");
	}
	
	public Program(LocalDateTime start, LocalDateTime end, Channel channel, String title, String episode, String icon) {
		this(start, end, channel, title, episode);
		addIcon(icon, "Telerama");
		
	}
	
	public void addIcon(String url, String site) {
		icons.put(url, site);
	}
	
	public LocalDateTime getStart() {
		return start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public Channel getChannel() {
		return channel;
	}

	public String getTitle() {
		return title;
	}

	public String getEpisode() {
		return episode;
	}

	public HashMap<String, String> getIcons() {
		return icons;
	}
	
	@Override
	public String toString() {
		String str =  "\nDebut : " + toStringDate(start)
		+ "\nEnd : " + toStringDate(end)
		+ "\nTitre : " + title + "\nEpisode : " + episode + "\n" + "Channel : \n" + channel
		+ "Icons : \n{\n";
		Iterator<Entry<String, String>> it = icons.entrySet().iterator();
	    while (it.hasNext()) {
	    	Entry<String, String> pair = (Entry<String, String>)it.next();
	        str += "\ticon : " + pair.getKey() + " from " + pair.getValue() + "\n";
	    }
	    str += "\n}\n";
	    return str;
	}
	
	/*@Override
	public boolean equals(Object o) {
		if (o instanceof Program) {
			Program c = (Program)o;
			if(toStringDate(start).equals(toStringDate(c.start)) && toStringDate(end).equals(toStringDate(c.end)) && title.equals(c.title))
				return true;
		}
		return false;
	}*/

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

	private String toStringDate(LocalDateTime date) {
		return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:ss")).toString();
	}
}
