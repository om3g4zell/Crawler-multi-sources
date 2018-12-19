package fr.babuchon.crawler.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class HTTPImageGetter implements Runnable {
	public final int BUFFER_SIZE = 512;
	private String imageURL;
	private String filePath;
	private String name;
	
	public HTTPImageGetter(String url, String filePath, String name) {
		this.imageURL = url;
		this.filePath = filePath;
		name = name.replace("/", "-");
		name = name.replace("*", "-");
		name = name.replace("?", "-");
		name = name.replace(":", "-");
		name = name.replace(".", "");
		
		this.name = name;
	}
	
	public void run() {
		String filename = "";
		String imagePath = "";
		try {
		String dir = "";
		dir = getDomainName(imageURL);
		if(dir != "") {
			File file = new File(filePath + "/" + dir);
			if(!file.exists()) {
				file.mkdirs();
			}
		}
		File nameDir = new File(filePath + "/" + dir + "/" + name);
		if(!nameDir.exists())
			nameDir.mkdirs();
		int shittyChar = imageURL.indexOf('?');
		
		if(shittyChar == -1 ) {
			filename = imageURL.substring(imageURL.lastIndexOf("/") + 1);
		}
		else {
			filename = imageURL.substring(imageURL.lastIndexOf("/") + 1, shittyChar);
		}
		imagePath = filePath + "/" + dir + "/" + name + "/" + filename;
		File file = new File(imagePath);
		if(file.exists())
			return;
		Response resultImageResponse = Jsoup.connect(imageURL)
		                                        .ignoreContentType(true).execute();

		// output here
		FileOutputStream out = new FileOutputStream(file);
		out.write(resultImageResponse.bodyAsBytes());  
		out.close();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("Saved : " + imagePath);
	}
	
	public static String getDomainName(String url) throws URISyntaxException {
	    URI uri = new URI(url);
	    String domain = uri.getHost();
	    return domain.startsWith("www.") ? domain.substring(4) : domain;
	}
}
