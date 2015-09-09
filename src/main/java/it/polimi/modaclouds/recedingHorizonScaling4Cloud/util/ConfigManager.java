package it.polimi.modaclouds.recedingHorizonScaling4Cloud.util;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ConfigurationFileException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigManager {
	
	private static final Logger journal = LoggerFactory
			.getLogger(ConfigManager.class);

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
	public static String OBJECT_STORE_IP;
	public static String OBJECT_STORE_PORT;
	public static String OBJECT_STORE_MODEL_PATH;
	
	private static boolean isAlreadySet=false;

	public static final String DEFAULTS_WORKING_DIRECTORY_PREFIX = "autoscalingReasoner";
	public static Path LOCAL_TEMPORARY_FOLDER = null;
	
	public static String DEFAULTS_WORKING_DIRECTORY = "/tmp/modaclouds";
	public static String RUN_WORKING_DIRECTORY = DEFAULTS_WORKING_DIRECTORY;
	
	
	
	public static void initFolders() {
		setWorkingSubDirectory();
		createNewLocalTmp();
	}

	public static void setWorkingSubDirectory() {
		if (isRunningLocally())
			RUN_WORKING_DIRECTORY = LOCAL_TEMPORARY_FOLDER.toString();
		else
			RUN_WORKING_DIRECTORY = DEFAULTS_WORKING_DIRECTORY + "/" + DEFAULTS_WORKING_DIRECTORY_PREFIX + "/" + getDate();
	}
	
	public static Path createNewLocalTmp() {
		try {
			LOCAL_TEMPORARY_FOLDER = Files.createTempDirectory(DEFAULTS_WORKING_DIRECTORY_PREFIX);
		} catch (Exception e) {
			journal.error("Error while creating the temporary folder. Reverting to /tmp/" + DEFAULTS_WORKING_DIRECTORY_PREFIX + ".", e);
			LOCAL_TEMPORARY_FOLDER = Paths.get("/tmp", DEFAULTS_WORKING_DIRECTORY_PREFIX);
		}
		return LOCAL_TEMPORARY_FOLDER;
	}
	
	public static String getDate() {
		Calendar c = Calendar.getInstance();
		
		DecimalFormat f = new DecimalFormat("00");
		
		return String.format("%d%s%s-%s%s%s",
				c.get(Calendar.YEAR),
				f.format(c.get(Calendar.MONTH) + 1),
				f.format(c.get(Calendar.DAY_OF_MONTH)),
				f.format(c.get(Calendar.HOUR_OF_DAY)),
				f.format(c.get(Calendar.MINUTE)),
				f.format(c.get(Calendar.SECOND))
				);
	}
	
	public static final String RUN_FILE = "AMPL.run"; //sets where temp AMPL file AMPL.run will be saved
	public static final String RUN_MODEL_STANDARD = "model.mod";
	public static final String RUN_MODEL_STARTING_SOLUTION = "modelstartingsolution.mod";
	public static final String RUN_DATA = "data.dat"; //sets where temp AMPL file data.dat will be saved
	public static String RUN_SOLVER = "/usr/optimization/cplex-studio/cplex/bin/x86-64_linux/cplexamp";
	public static String RUN_AMPL_FOLDER = "/usr/optimization/ampl";
	public static final String RUN_LOG = "solution.log"; //"log.tmp";//sets where temp AMPL file log.tmp will be saved
	public static final String RUN_RES = "solution.sol"; //"shortrez.out";//sets where temp AMPL file shortrez.out will be saved
	public static final String DEFAULTS_BASH = "bashAMPL.run";

	public static final String RUN_FILE_CMPL = "CMPL.run";
	public static final String RUN_MODEL_STANDARD_CMPL = "model.cmpl";
	public static final String RUN_MODEL_STARTING_SOLUTION_CMPL = "modelstartingsolution.cmpl";
	public static String RUN_SOLVER_CMPL = "cbc"; // glpk, cbc, scip, gurobi, cplex
	public static final String RUN_DATA_CMPL = "data.cdat";
	public static String RUN_CMPL_FOLDER = "/usr/share/Cmpl";
	public static final String RUN_LOG_CMPL = "solution.log";
	public static final String RUN_RES_CMPL = "solution.sol";
	public static final String DEFAULTS_BASH_CMPL = "bashCMPL.run";
	public static int CMPL_THREADS = 4;
	
	public static int getCMPLThreads() {
		int threads = CMPL_THREADS;
		if (isRunningLocally()) {
			threads = Runtime.getRuntime().availableProcessors() - 1;
			if (threads <= 0)
				threads = 1;
		}
		return threads;
	}

	public static Solver MATH_SOLVER = Solver.CMPL;
	
	public static enum Solver {
		AMPL("AMPL"), CMPL("CMPL");

		private String name;

		private Solver(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static Solver getById(int id) {
			Solver[] values = Solver.values();
			if (id < 0)
				id = 0;
			else if (id >= values.length)
				id = values.length - 1;
			return values[id];
		}

		public static int size() {
			return Solver.values().length;
		}

		public static Solver getByName(String name) {
			Solver[] values = Solver.values();
			for (Solver s : values)
				if (s.name.equals(name))
					return s;
			return values[0];
		}

	}
	
	public static void setFromFile() throws ConfigurationFileException {

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
	
	public static final String PROPERTY_NAME_OWN_IP = "MODACLOUDS_AR_OWN_IP";
	public static final String PROPERTY_NAME_LISTENING_PORT = "MODACLOUDS_AR_LISTENER_PORT";
	public static final String PROPERTY_NAME_PATH_TO_DESIGN_TIME_MODEL = "MODACLOUDS_AR_PATH_TO_MODEL";
	public static final String PROPERTY_NAME_SSH_USER_NAME = "MODACLOUDS_AR_SSH_USER";
	public static final String PROPERTY_NAME_SSH_HOST = "MODACLOUDS_AR_SSH_HOST";
	public static final String PROPERTY_NAME_SSH_PASSWORD = "MODACLOUDS_AR_SSH_PASS";
	public static final String PROPERTY_NAME_OPTIMIZATION_LAUNCHER = "MODACLOUDS_AR_OPT_LAUNCHER";
	public static final String PROPERTY_NAME_OPTIMIZATION_INPUT_FOLDER = "MODACLOUDS_AR_OPT_IN_FOLDER";
	public static final String PROPERTY_NAME_OPTIMIZATION_OUTPUT_FILE = "MODACLOUDS_AR_OPT_OUT_FOLDER";
	public static final String PROPERTY_NAME_CLOUDML_WEBSOCKET_IP = "MODACLOUDS_AR_CLOUDML_IP";
	public static final String PROPERTY_NAME_CLOUDML_WEBSOCKET_PORT = "MODACLOUDS_AR_CLOUDML_PORT";
	public static final String PROPERTY_NAME_MONITORING_PLATFORM_IP = "MODACLOUDS_TOWER4CLOUDS_MANAGER_IP";
	public static final String PROPERTY_NAME_MONITORING_PLATFORM_PORT = "MODACLOUDS_TOWER4CLOUDS_MANAGER_PORT";
	public static final String PROPERTY_NAME_DEFAULT_DEMAND = "MODACLOUDS_AR_DEF_DEMAND";
	public static final String PROPERTY_NAME_OBJECT_STORE_IP = "MODACLOUDS_AR_OBJ_STORE_IP";
	public static final String PROPERTY_NAME_OBJECT_STORE_PORT = "MODACLOUDS_AR_OBJ_STORE_PORT";
	public static final String PROPERTY_NAME_OBJECT_STORE_MODEL_PATH = "MODACLOUDS_AR_OBJ_STORE_PATH";

	public static void setFromEnrivonmentVariables() throws ConfigurationFileException {
		setPropertyIfNotNull("OWN_IP", System.getenv(PROPERTY_NAME_OWN_IP));
		setPropertyIfNotNull("LISTENING_PORT", System.getenv(PROPERTY_NAME_LISTENING_PORT));
		setPropertyIfNotNull("PATH_TO_DESIGN_TIME_MODEL", System.getenv(PROPERTY_NAME_PATH_TO_DESIGN_TIME_MODEL));
		setPropertyIfNotNull("SSH_USER_NAME", System.getenv(PROPERTY_NAME_SSH_USER_NAME));
		setPropertyIfNotNull("SSH_HOST", System.getenv(PROPERTY_NAME_SSH_HOST));
		setPropertyIfNotNull("SSH_PASSWORD", System.getenv(PROPERTY_NAME_SSH_PASSWORD));
		setPropertyIfNotNull("OPTIMIZATION_LAUNCHER", System.getenv(PROPERTY_NAME_OPTIMIZATION_LAUNCHER));
		setPropertyIfNotNull("OPTIMIZATION_INPUT_FOLDER", System.getenv(PROPERTY_NAME_OPTIMIZATION_INPUT_FOLDER));
		setPropertyIfNotNull("OPTIMIZATION_OUTPUT_FILE", System.getenv(PROPERTY_NAME_OPTIMIZATION_OUTPUT_FILE));
		setPropertyIfNotNull("CLOUDML_WEBSOCKET_IP", System.getenv(PROPERTY_NAME_CLOUDML_WEBSOCKET_IP));
		setPropertyIfNotNull("CLOUDML_WEBSOCKET_PORT", System.getenv(PROPERTY_NAME_CLOUDML_WEBSOCKET_PORT));
		setPropertyIfNotNull("MONITORING_PLATFORM_IP", System.getenv(PROPERTY_NAME_MONITORING_PLATFORM_IP));
		setPropertyIfNotNull("MONITORING_PLATFORM_PORT", System.getenv(PROPERTY_NAME_MONITORING_PLATFORM_PORT));
		setPropertyIfNotNull("DEFAULT_DEMAND", System.getenv(PROPERTY_NAME_DEFAULT_DEMAND));
		setPropertyIfNotNull("OBJECT_STORE_IP", System.getenv(PROPERTY_NAME_OBJECT_STORE_IP));
		setPropertyIfNotNull("OBJECT_STORE_PORT", System.getenv(PROPERTY_NAME_OBJECT_STORE_PORT));
		setPropertyIfNotNull("OBJECT_STORE_MODEL_PATH", System.getenv(PROPERTY_NAME_OBJECT_STORE_MODEL_PATH));
	}
	
	public static void setFromSystemProperties() throws ConfigurationFileException {
		setPropertyIfNotNull("OWN_IP", System.getProperty(PROPERTY_NAME_OWN_IP));
		setPropertyIfNotNull("LISTENING_PORT", System.getProperty(PROPERTY_NAME_LISTENING_PORT));
		setPropertyIfNotNull("PATH_TO_DESIGN_TIME_MODEL", System.getProperty(PROPERTY_NAME_PATH_TO_DESIGN_TIME_MODEL));
		setPropertyIfNotNull("SSH_USER_NAME", System.getProperty(PROPERTY_NAME_SSH_USER_NAME));
		setPropertyIfNotNull("SSH_HOST", System.getProperty(PROPERTY_NAME_SSH_HOST));
		setPropertyIfNotNull("SSH_PASSWORD", System.getProperty(PROPERTY_NAME_SSH_PASSWORD));
		setPropertyIfNotNull("OPTIMIZATION_LAUNCHER", System.getProperty(PROPERTY_NAME_OPTIMIZATION_LAUNCHER));
		setPropertyIfNotNull("OPTIMIZATION_INPUT_FOLDER", System.getProperty(PROPERTY_NAME_OPTIMIZATION_INPUT_FOLDER));
		setPropertyIfNotNull("OPTIMIZATION_OUTPUT_FILE", System.getProperty(PROPERTY_NAME_OPTIMIZATION_OUTPUT_FILE));
		setPropertyIfNotNull("CLOUDML_WEBSOCKET_IP", System.getProperty(PROPERTY_NAME_CLOUDML_WEBSOCKET_IP));
		setPropertyIfNotNull("CLOUDML_WEBSOCKET_PORT", System.getProperty(PROPERTY_NAME_CLOUDML_WEBSOCKET_PORT));
		setPropertyIfNotNull("MONITORING_PLATFORM_IP", System.getProperty(PROPERTY_NAME_MONITORING_PLATFORM_IP));
		setPropertyIfNotNull("MONITORING_PLATFORM_PORT", System.getProperty(PROPERTY_NAME_MONITORING_PLATFORM_PORT));
		setPropertyIfNotNull("DEFAULT_DEMAND", System.getProperty(PROPERTY_NAME_DEFAULT_DEMAND));
		setPropertyIfNotNull("OBJECT_STORE_IP", System.getProperty(PROPERTY_NAME_OBJECT_STORE_IP));
		setPropertyIfNotNull("OBJECT_STORE_PORT", System.getProperty(PROPERTY_NAME_OBJECT_STORE_PORT));
		setPropertyIfNotNull("OBJECT_STORE_MODEL_PATH", System.getProperty(PROPERTY_NAME_OBJECT_STORE_MODEL_PATH));
	}

	public static void setFromArguments(Map<String, String> paramsMap) throws ConfigurationFileException {
		setPropertyIfNotNull("OWN_IP", paramsMap.get("ownIp"));
		setPropertyIfNotNull("LISTENING_PORT", paramsMap.get("listeningPort"));
		setPropertyIfNotNull("PATH_TO_DESIGN_TIME_MODEL", paramsMap.get("pathToDesignAdaptationModel"));
		setPropertyIfNotNull("SSH_USER_NAME", paramsMap.get("sshUser"));
		setPropertyIfNotNull("SSH_HOST", paramsMap.get("sshHost"));
		setPropertyIfNotNull("SSH_PASSWORD", paramsMap.get("sshPass"));
		setPropertyIfNotNull("OPTIMIZATION_LAUNCHER", paramsMap.get("pathToOptLauncher"));
		setPropertyIfNotNull("OPTIMIZATION_INPUT_FOLDER", paramsMap.get("pathToOptInputFolder"));
		setPropertyIfNotNull("OPTIMIZATION_OUTPUT_FILE", paramsMap.get("pathToOptOutputFile"));
		setPropertyIfNotNull("CLOUDML_WEBSOCKET_IP", paramsMap.get("cloudMLIp"));
		setPropertyIfNotNull("CLOUDML_WEBSOCKET_PORT", paramsMap.get("cloudMLPort"));
		setPropertyIfNotNull("MONITORING_PLATFORM_IP", paramsMap.get("t4cIp"));
		setPropertyIfNotNull("MONITORING_PLATFORM_PORT", paramsMap.get("t4cPort"));
		setPropertyIfNotNull("DEFAULT_DEMAND", paramsMap.get("defaultDemand"));
		setPropertyIfNotNull("OBJECT_STORE_IP", paramsMap.get("objStorIp"));
		setPropertyIfNotNull("OBJECT_STORE_PORT", paramsMap.get("objStorePort"));
		setPropertyIfNotNull("OBJECT_STORE_MODEL_PATH", paramsMap.get("objStorePathToModel"));
	}

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
				journal.error("Error while deleting the directory.", e);
			}
		}
	}
	
	public static void loadConfiguration() throws ConfigurationFileException {
		loadConfiguration(null);
	}
	
	public static void loadConfiguration(Map<String, String> paramsMap) throws ConfigurationFileException {
		if (!isAlreadySet || paramsMap != null) {
			setFromFile();
			setFromEnrivonmentVariables();
			setFromSystemProperties();
			if (paramsMap != null) {
				setFromArguments(paramsMap);
			}
			isAlreadySet = true;
		}
	}
	
	public static InputStream getInputStream(String filePath) {
		File f = new File(filePath);
		if (f.exists())
			try {
				return new FileInputStream(f);
			} catch (Exception e) { }
		
		InputStream is = ConfigManager.class.getResourceAsStream(filePath);
		if (is == null)
			is = ConfigManager.class.getResourceAsStream("/" + filePath);
		return is;
	}
	
	private static InputStream findConfigFile() {
		InputStream toReturn = getInputStream("config.xml");
		
		if(toReturn==null){
			journal.warn("input stream null");
		}
		
		return toReturn;
	}
	
	private static void setPropertyIfNotNull(String key, String value){
		if (key != null && value != null)
			setProperty(key, value);
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
			
		case "OBJECT_STORE_IP":
				OBJECT_STORE_IP=value;
			break;
			
		case "OBJECT_STORE_PORT":
			OBJECT_STORE_PORT=value;
			break;
		
		case "OBJECT_STORE_MODEL_PATH":
			OBJECT_STORE_MODEL_PATH=value;
			break;

		default:
			break;
		}
	}
	
	public static void printConfig(){
		
		journal.info("ownIp={}", OWN_IP);
		journal.info("t4cIp={}", MONITORING_PLATFORM_IP);
		journal.info("cloudMLIp={}", CLOUDML_WEBSOCKET_IP);
		journal.info("listeningPort={}", LISTENING_PORT);
		journal.info("t4cPort={}", MONITORING_PLATFORM_PORT);
		journal.info("cloudMLPort={}", CLOUDML_WEBSOCKET_PORT);
		journal.info("pathToDesignAdapatationModel={}", PATH_TO_DESIGN_TIME_MODEL);
		journal.info("sshUser={}", SSH_USER_NAME);
		journal.info("sshPass={}", SSH_PASSWORD);
		journal.info("sshHost={}", SSH_HOST);
		journal.info("pathToOptInputFolder={}", OPTIMIZATION_INPUT_FOLDER);
		journal.info("pathToOptLauncher={}", OPTIMIZATION_LAUNCHER);
		journal.info("pathToOptOutputFile={}", OPTIMIZATION_OUTPUT_FILE);
		journal.info("defaultDemand={}", DEFAULT_DEMAND);
		journal.info("objStoreIp={}", OBJECT_STORE_IP);
		journal.info("objStorePort={}", OBJECT_STORE_PORT);
		journal.info("objSotreModelPath={}", OBJECT_STORE_MODEL_PATH);
	}
	
	public static boolean isRunningLocally() {
		return (SSH_HOST.equals("localhost") || SSH_HOST.equals("127.0.0.1"));
	}
	
}
