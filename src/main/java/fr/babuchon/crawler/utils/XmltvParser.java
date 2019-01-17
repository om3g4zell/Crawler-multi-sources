package fr.babuchon.crawler.utils;

import java.io.IOException;
import java.security.InvalidParameterException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class is an utility class for parsing an XMLTV
 * @author Louis Babuchon
 */
public class XmltvParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(XmltvParser.class);

	/**
	 * The document builder factory
	 */
	private DocumentBuilderFactory factory;

	/**
	 * The document builder
	 */
	private DocumentBuilder builder;

	/**
	 * The xml document
	 */
	private org.w3c.dom.Document xml;

	/**
	 * Constructor
	 * @param path : The xml's path
	 */
	public XmltvParser(String path) {
		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			xml = builder.parse(path);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			LOGGER.error("Error : ", e);
		}
	}

	/**
	 * Constructor
	 */
	public XmltvParser() {
		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			LOGGER.error("Error : ", e);
		}
	}

	/**
	 * Change the current xml
	 * @param path : The path of the new xml
	 */
	public void changeXML(String path) {
		try {
			xml = builder.parse(path);
		} catch (SAXException | IOException e) {
			LOGGER.error("Error : ", e);
		}
	}

	/**
	 * Return the nodeList of the channels
	 * @return The list of channel
	 * @throws InvalidParameterException : If the xlm is null
	 */
	public NodeList getChannels() throws InvalidParameterException {
		if(xml == null)
			throw new InvalidParameterException("Error you need to open the xml !");
		// Extract Channels infos
		return xml.getElementsByTagName("channel");
	}

	/**
	 * Return the nodeList of the programs
	 * @return The list of programs
	 * @throws InvalidParameterException : if the xml is null
	 */
	public NodeList getPrograms() throws InvalidParameterException{
		if(xml == null)
			throw new InvalidParameterException("Error you need to open the xml !");
		// Extract Channels infos
		return xml.getElementsByTagName("programme");
	}
}
