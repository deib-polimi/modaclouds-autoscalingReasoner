package util;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ConfigurationFileException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.OptimizationExecution;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.Path;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigManager {

	private static HashMap<String, String> configs = new HashMap<String, String>();


	
	public void inizializeFileSystem(List<OptimizationExecution> executions) throws ProjectFileSystemException{

		File file = null;
		
		
		this.clearFileSystem();
		
		for(OptimizationExecution ex: executions){
			file = new File("executions/execution_"+ex.toString()+"/IaaS_1");
			boolean success=file.mkdirs();
			
			if(!success)
				throw new ProjectFileSystemException("Error initializing the project file system: the following folder cannot be created: "
						+ "executions/execution_"+ex.toString()+"/IaaS_1");

		}
		
	}
	
	private void clearFileSystem(){
		File file = new File("executions/");
		
		if(file.exists() & file.isDirectory()){
			try {
				FileUtils.deleteDirectory(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void loadConfiguration() throws ConfigurationFileException {
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		DocumentBuilder b;
		Document d;
		

		try {
			b = f.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new ConfigurationFileException(e.getMessage());
		}
		
		try {
			d = b.parse(findConfigFile());
		} catch (SAXException e) {
			throw new ConfigurationFileException(e.getMessage());
		} catch (IOException e) {
			throw new ConfigurationFileException(e.getMessage());
		}
		
		Element root = d.getDocumentElement();
		
		if (!root.getNodeName().equals("properties")) {
			throw new ConfigurationFileException(
					"Error in the root element!!!IT MUST BE <properties>");
		}
		
		NodeList children = root.getChildNodes();
		
		if (children.getLength() == 0) {
			throw new ConfigurationFileException(
					"THERE IS NO PROPERTY");
		}
		
		for (int i = 0; i < children.getLength(); i++) {
			Node n = children.item(i);

							if (n.getNodeName().equals("property")) {
								
								NamedNodeMap map=n.getAttributes();
								
								String key=map.getNamedItem("name").getNodeValue();
								String value=map.getNamedItem("value").getNodeValue();
								
								configs.put(key,value);
							}		
		}
		
		this.printConfig();
	}

	
	private InputStream findConfigFile() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream toReturn=loader.getResourceAsStream("config.xml");
		
		if(toReturn==null){
			System.out.println("input stream null");
		}
		
		return toReturn;
	}
	
	private void printConfig(){
		
		for(String key: configs.keySet()){
			System.out.println(key+"="+configs.get(key));
		}
	}
	
	public static String getConfig(String name){
		return configs.get(name);
	}
	
	public static HashMap<String, String> getConfigs(){
		return configs;
	}
}
