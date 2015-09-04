package it.polimi.modaclouds.recedingHorizonScaling4Cloud.util;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl.AdaptationInitializer;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ConfigurationFileException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigManager {
	
	private static final Logger journal = Logger
			.getLogger(ConfigManager.class.getName());

	public static String OWN_IP;
	public static String LISTENING_PORT;
	public static String PATH_TO_DESIGN_TIME_MODEL;
	public static String SSH_USER_NAME;
	public static String SSH_HOST;
	public static String SSH_PASSWORD;
	public static String OPTIMIZATION_LAUNCHER;
	public static String OPTIMIZATION_INPUT_FOLDER;
	public static String OPTIMIZATION_OUTPUT_FILE;
	public static String CLOUDML_WEBSOCKET_IP;
	public static String CLOUDML_WEBSOCKET_PORT;
	public static String MONITORING_PLATFORM_IP;
	public static String MONITORING_PLATFORM_PORT;
	public static String DEFAULT_DEMAND;
	
	private static boolean isSetFromArguments=false;

	

	public static void inizializeFileSystem() throws ProjectFileSystemException{

		File file = null;
		
		
		clearFileSystem();
		
		for(Container c: ModelManager.getModel().getContainer()){
			file = new File("executions/execution_"+c.getId()+"/IaaS_1");
			boolean success=file.mkdirs();
			
			if(!success)
				throw new ProjectFileSystemException("Error initializing the project file system: the following folder cannot be created: "
						+ "executions/execution_"+c.getId()+"/IaaS_1");


		}
		
	}
	
	private static void clearFileSystem(){
		File file = new File("executions/");
		
		if(file.exists() & file.isDirectory()){
			try {
				FileUtils.deleteDirectory(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void loadConfiguration() throws ConfigurationFileException {
		
		
		
		if(!isSetFromArguments)	{
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
									setProperty(key, value);
								}		
			}
			
			
		}
		
	}

	
	private static InputStream findConfigFile() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream toReturn=loader.getResourceAsStream("config.xml");
		
		if(toReturn==null){
			System.out.println("input stream null");
		}
		
		return toReturn;
	}
	
	private static void setProperty(String key, String value){
		switch (key) {
		case "OWN_IP":
			OWN_IP=value;
			break;
			
		case "LISTENING_PORT":
			LISTENING_PORT=value;
			break;
			
		case "PATH_TO_DESIGN_TIME_MODEL":
			PATH_TO_DESIGN_TIME_MODEL=value;
			break;

		case "SSH_USER_NAME":
			SSH_USER_NAME=value;
			break;

		case "SSH_HOST":
			SSH_HOST=value;
			break;

		case "SSH_PASSWORD":
			SSH_PASSWORD=value;
			break;

		case "OPTIMIZATION_LAUNCHER":
			OPTIMIZATION_LAUNCHER=value;
			break;

		case "OPTIMIZATION_INPUT_FOLDER":
			OPTIMIZATION_INPUT_FOLDER=value;
			break;

		case "OPTIMIZATION_OUTPUT_FILE":
			OPTIMIZATION_OUTPUT_FILE=value;
			break;
			
		case "CLOUDML_WEBSOCKET_IP":
			CLOUDML_WEBSOCKET_IP=value;
			break;

		case "CLOUDML_WEBSOCKET_PORT":
			CLOUDML_WEBSOCKET_PORT=value;
			break;

		case "MONITORING_PLATFORM_IP":
			MONITORING_PLATFORM_IP=value;
			break;

		case "MONITORING_PLATFORM_PORT":
			MONITORING_PLATFORM_PORT=value;
			break;

		case "DEFAULT_DEMAND":
			DEFAULT_DEMAND=value;
			break;

		default:
			break;
		}
	}
	
	public static void setFromArguments(String ownIp, String t4cIp, String cloudMLIp, String listeningPort, String t4cPort, String cloudMLPort,
											String pathToDesignAdapatationModel, String sshUser, String sshPass, String sshHost, 
											String pathToOptInputFolder, String pathToOptLauncher, String pathToOptOutputFile, String defaultDemand){
		
		OWN_IP=ownIp;
		CLOUDML_WEBSOCKET_IP=cloudMLIp;
		CLOUDML_WEBSOCKET_PORT=cloudMLPort;
		MONITORING_PLATFORM_IP=t4cIp;
		MONITORING_PLATFORM_PORT=t4cPort;
		LISTENING_PORT=listeningPort;
		PATH_TO_DESIGN_TIME_MODEL=pathToDesignAdapatationModel;
		SSH_HOST=sshHost;
		SSH_USER_NAME=sshUser;
		SSH_PASSWORD=sshPass;
		OPTIMIZATION_INPUT_FOLDER=pathToOptInputFolder;
		OPTIMIZATION_LAUNCHER=pathToOptLauncher;
		OPTIMIZATION_OUTPUT_FILE=pathToOptOutputFile;
		DEFAULT_DEMAND=defaultDemand;
		
		isSetFromArguments=true;
		
	}
	
	public static void printConfig(){
		
		journal.log(Level.INFO,"ownIp="+OWN_IP);
		journal.log(Level.INFO,"t4cIp)"+MONITORING_PLATFORM_IP);
		journal.log(Level.INFO,"cloudMLIp="+CLOUDML_WEBSOCKET_IP);
		journal.log(Level.INFO,"listeningPort="+LISTENING_PORT);
		journal.log(Level.INFO,"t4cPort="+MONITORING_PLATFORM_PORT);
		journal.log(Level.INFO,"cloudMLPort="+CLOUDML_WEBSOCKET_PORT);
		journal.log(Level.INFO,"pathToDesignAdapatationModel="+PATH_TO_DESIGN_TIME_MODEL);
		journal.log(Level.INFO,"sshUser="+SSH_USER_NAME);
		journal.log(Level.INFO,"sshPass="+SSH_PASSWORD);
		journal.log(Level.INFO,"sshHost="+SSH_HOST);
		journal.log(Level.INFO,"pathToOptInputFolder="+OPTIMIZATION_INPUT_FOLDER);
		journal.log(Level.INFO,"pathToOptLauncher="+OPTIMIZATION_LAUNCHER);
		journal.log(Level.INFO,"pathToOptOutputFile="+OPTIMIZATION_OUTPUT_FILE);
		journal.log(Level.INFO,"defaultDemand="+DEFAULT_DEMAND);


	}
	
}
