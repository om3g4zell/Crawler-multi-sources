package fr.babuchon.crawler.utils;

import java.io.IOException;
import java.security.InvalidParameterException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmltvParser {
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private org.w3c.dom.Document xml;
	
	public XmltvParser(String path) {
		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			xml = builder.parse(path);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public XmltvParser() {
		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void changeXML(String path) {
		try {
			xml = builder.parse(path);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public NodeList getChannels() throws InvalidParameterException {
		if(xml == null)
			throw new InvalidParameterException("Error you need to open the xml !");
		// Extract Channels infos
		NodeList channelsNode = xml.getElementsByTagName("channel");
		return channelsNode;
	}
	
	public NodeList getPrograms() throws InvalidParameterException{
		if(xml == null)
			throw new InvalidParameterException("Error you need to open the xml !");
		// Extract Channels infos
		NodeList channelsNode = xml.getElementsByTagName("programme");
		return channelsNode;
	}
}
