package fr.babuchon.crawler.model.xmltv;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import fr.babuchon.crawler.model.tv.Channel;
import fr.babuchon.crawler.model.tv.Program;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Represent A TVGuide from the XMLTV file
 * @author Louis Babuchon
 */
public class Guide {

	/**
	 * All the channels
	 */
	public Set<Channel> channels;

	/**
	 * All the programs
	 */
	public Set<Program> programs;

	/**
	 * Constructor
	 */
	public Guide() {
		channels = new HashSet<>();
		programs = new HashSet<>();
	}

	/**
	 * Add a channel to the channels from the xmltv nodes
	 * @param channelsNode : The channels xmltv nodes
	 */
	public void AddChannel(NodeList channelsNode) {
		for(int i = 0 ; i < channelsNode.getLength(); i++) {
			Node n = channelsNode.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n;
				String id = e.getAttribute("id");
				String name = e.getElementsByTagName("display-name").item(0).getTextContent();
				String icon = "";
				if(e.getElementsByTagName("icon").item(0).getNodeType() == Node.ELEMENT_NODE)
					icon = ((Element)e.getElementsByTagName("icon").item(0)).getAttribute("src");
				channels.add(new Channel(id, name, icon));
			}
		}
	}

	/**
	 * Return a String with the size of the channels and the list of channels
	 * @return String : the size of the channels and the list of channels
	 */
	public String channelsString() {
		return "Size : " + channels.size() + "\n" + channels.toString();
	}

	/**
	 * Return a String with the size of the programs and the list of programs
	 * @return String : the size of the channels and the list of programs
	 */
	public String programString() {
		return "Size : " + programs.size() + "\n" + programs.toString();
	}

	/**
	 * Add programs to the programs from the xmltv
	 * @param programsNode : the programs nodes
	 */
	public void addPrograms(NodeList programsNode) {
		for(int i = 0 ; i < programsNode.getLength(); i++) {
			Node n = programsNode.item(i);
			if(n.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) n;
				//noinspection SpellCheckingInspection
				LocalDateTime start = LocalDateTime.parse(e.getAttribute("start"), DateTimeFormatter.ofPattern("yyyyMMddHHmmss[ Z]"));
				LocalDateTime end = LocalDateTime.parse(e.getAttribute("stop"), DateTimeFormatter.ofPattern("yyyyMMddHHmmss[ Z]"));
				Channel c = getChannel(e.getAttribute("channel"));
				String title = e.getElementsByTagName("title").item(0).getTextContent();
				String icon = "";
				NodeList iconNode = e.getElementsByTagName("icon");
				if(iconNode.getLength() > 0) {
					if(iconNode.item(0).getNodeType() == Node.ELEMENT_NODE)
						icon = ((Element)iconNode.item(0)).getAttribute("src");
				}
				String episode = "No Ep";
				NodeList ep = e.getElementsByTagName("episode-num");
				if(ep.getLength() > 0) {
					if(ep.item(0).getNodeType() == Node.ELEMENT_NODE)
						episode = ((Element)ep.item(0)).getTextContent();
					
					
					
				}
				if(icon.equals(""))
					continue;

				Program p = new Program(start, end, c, title, episode);
				p.addIcon(icon, "Telerama");
				programs.add(p);
			}
		}
		
	}

	/**
	 * Return the programs
	 * @return The programs
	 */
	public Set<Program> getPrograms() {
		return this.programs;
	}

	/**
	 * Get a channel from its channel id
	 * @param id : The channel id
	 * @return Channel : The associated channel
	 * If no associated channel return an empty channel
	 */
	public Channel getChannel(String id) {
		for(Channel c : channels) {
			if(c.getId().equals(id))
				return c;
		}
		return new Channel("", "", "");
	}
}
