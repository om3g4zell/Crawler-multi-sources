package fr.babuchon.crawler.model;

/**
 * This class represent the model for a Channel
 * @author Louis Babuchon
 */
public class Channel {

	/**
	 * The id of the channel
	 */
	private String id;

	/**
	 * The name of the channel
	 */
	private String name;

	/**
	 * The icon of the channel
	 */
	private String icon;

	/**
	 * Constructor
	 * @param id : The id of the channel
	 * @param name : The name of the channel
	 * @param icon : The icon of the channel
	 */
	public Channel(String id, String name, String icon) {
		this.id = id;
		this.name = name;
		this.icon = icon;
	}

	/**
	 * Return the id of the channel
	 * @return String : The channel's id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the channel's id
	 * @param id : the channel's id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Return the name of the channel
	 * @return String : The name of the channel
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the channel's name
	 * @param name : The channel's name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return the channel's icon
	 * @return String : The channel's icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Set the channel's icon
	 * @param icon : The channel's icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	@Override
	public String toString() {
		return "{\n\tId : " + id + "\n\t" + "Name : " + name + "\n\t" + "Icon : " + icon + "\n}\n";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Channel) {
			Channel c = (Channel)o;
			if(this.id.equals(c.id) && this.name.equals(c.name) && this.icon.equals(c.icon))
				return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode() + name.hashCode() + icon.hashCode();
	}
	
	
}
