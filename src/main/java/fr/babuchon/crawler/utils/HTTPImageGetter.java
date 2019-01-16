package fr.babuchon.crawler.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;

import fr.babuchon.crawler.model.Program;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTTPImageGetter implements Callable<Integer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(HTTPImageGetter.class);

	private Queue<Program> queue;
	private String filepath;
	private double timeout;
	private int counter;

	public HTTPImageGetter(Queue<Program> queue, double timeout, String filepath) {
		this.queue = queue;
		if(timeout == 0)
			this.timeout = Double.MAX_VALUE;
		else
			this.timeout = timeout;
		this.filepath = filepath;
		this.counter = 0;
	}
	
	public Integer call() throws Exception{
		double start = System.currentTimeMillis();
		double end;
		double faultCounter = 0;
		boolean stop = true;
		while(stop) {

			Program p = queue.poll();
			if(p != null) {

				for(Map.Entry<String, String> i : p.getIcons().entrySet()) {
					LOGGER.debug(i.getKey());
					downloadImage(i.getKey(), p.getTitle(), i.getValue());
				}

				//System.out.println("Crawled : " + url + "T : " + Thread.currentThread().getName());
				faultCounter = 0;
			}
			else {
				Thread.sleep(500);
				//System.out.println("No link : " + Thread.currentThread().getName());
				faultCounter++;
			}

			end = System.currentTimeMillis();

			if(end - start >= timeout) {
				stop = false;
			}
			if(faultCounter >= 3)
				stop = false;
		}

		System.out.println("Ended :" + Thread.currentThread().getName());
		return counter;
	}

	private void downloadImage(String imageURL, String name, String source) {

		name = name.replace("/", "-");
		name = name.replace("*", "-");
		name = name.replace("?", "-");
		name = name.replace(":", "-");
		name = name.replace(".", "_");

		String filename = source + "_";
		String imagePath = "";
		try {
			File file = new File(filepath);
			if(!file.exists()) {
				file.mkdirs();
			}

			File nameDir = new File(filepath + "/" + name);
			if(!nameDir.exists())
				nameDir.mkdirs();
			int shittyChar = imageURL.indexOf('?');

			if(shittyChar == -1 ) {
				filename += imageURL.substring(imageURL.lastIndexOf("/") + 1);
			}
			else {
				filename += imageURL.substring(imageURL.lastIndexOf("/") + 1, shittyChar);
			}
			imagePath = filepath + "/" + name + "/" + filename;
			file = new File(imagePath);
			if(file.exists())
				return;
			Response resultImageResponse = Jsoup.connect(imageURL).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
					.referrer("http://www.google.com")
					.ignoreContentType(true).execute();

			// output here
			FileOutputStream out = new FileOutputStream(file);
			out.write(resultImageResponse.bodyAsBytes());
			out.close();

		}catch(Exception e) {
			e.printStackTrace();
			return;
		}

		System.out.println("Saved : " + imagePath + " Thread : " + Thread.currentThread().getName());
		counter++;
	}
}
