package fr.babuchon.crawler.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import fr.babuchon.crawler.model.Program;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements {@link Callable} it represent a task who download images from a pool
 * @author Louis Babuchon
 */
public class HTTPImageGetter implements Callable<Integer> {

	/**
	 * The logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HTTPImageGetter.class);

	/**
	 * The programs Queue
	 */
	private Queue<Program> queue;

	/**
	 * The saving path
	 */
	private String filepath;

	/**
	 * The timeout in ms
	 */
	private double timeout;

	/**
	 * The max faultCounter
	 */
	private int counter;

	/**
	 * Constructor
	 * @param queue : The programs Queue
	 * @param timeout : The timeout
	 * @param filepath : The saving path
	 */
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

		LOGGER.info("Ended {}", Thread.currentThread().getName());
		return counter;
	}

	/**
	 * Download the given image, reformat it name and save it in the appropriate folder
	 * @param imageURL : The image's url
	 * @param name : The name of the program
	 * @param source : The source of the image
	 */
	private void downloadImage(String imageURL, String name, String source) {

		String filename = source + "_";
		try {
			File nameDir = new File(filepath, sanitizeFileName(name));
			nameDir.mkdirs();

			int shittyChar = imageURL.indexOf('?');

			if(shittyChar == -1 ) {
				filename += imageURL.substring(imageURL.lastIndexOf("/") + 1);
			}
			else {
				filename += imageURL.substring(imageURL.lastIndexOf("/") + 1, shittyChar);
			}
			File file = new File(nameDir, sanitizeFileName(filename));
			if(file.exists())
				return;
			Response resultImageResponse = Jsoup.connect(imageURL).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
					.referrer("http://www.google.com")
					.ignoreContentType(true).execute();

			// output here
			FileOutputStream out = new FileOutputStream(file);
			out.write(resultImageResponse.bodyAsBytes());
			out.close();
			LOGGER.info("Saved : {} Thread : {}", file, Thread.currentThread().getName());

		}catch(Exception e) {
			LOGGER.error("Error : ", e);
			return;
		}

		counter++;
	}

	/**
	 * Remove forbidden characters inside a filename.
	 *
	 * @param name The filename to sanitize.
	 *
	 * @return The filename without forbidden characters.
	 */
	public static String sanitizeFileName(String name){
		return name.chars().mapToObj(i -> (char) i).filter(c -> Character.isLetterOrDigit(c) || c == '-' || c == '_' || c == ' ').map(String::valueOf).collect(Collectors.joining()).trim();
	}
}
