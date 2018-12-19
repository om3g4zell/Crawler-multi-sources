package fr.babuchon.crawler.model;

public class Channel {
	private String id;
	private String name;
	private String icon;
	
	public Channel(String id, String name, String icon) {
		this.id = id;
		this.name = name;
		this.icon = icon;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

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
