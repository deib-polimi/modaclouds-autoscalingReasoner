package it.polimi.modaclouds.recedingHorizonScaling4Cloud.util;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ConfigurationFileException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
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

	public static final String DEFAULT_IP = "127.0.0.1";
	
	public static String OWN_IP = DEFAULT_IP;
	
	public static final int DEFAULT_LISTENING_PORT = 81790;
	public static String LISTENING_PORT = Integer.toString(DEFAULT_LISTENING_PORT);
	
	public static final int DEFAULT_OTHER_OBSERVER_PORT = 8001;
	public static String OTHER_OBSERVER_PORT = Integer.toString(DEFAULT_OTHER_OBSERVER_PORT);
	
	public static String PATH_TO_DESIGN_TIME_MODEL;
	
	public static String SSH_USER_NAME;
	public static String SSH_HOST;
	public static String SSH_PASSWORD;
	
	public static String CLOUDML_WEBSOCKET_IP = DEFAULT_IP;
	
	public static final int DEFAULT_CLOUDML_WEBSOCKET_PORT = 9030;
	public static String CLOUDML_WEBSOCKET_PORT = Integer.toString(DEFAULT_CLOUDML_WEBSOCKET_PORT);
	
	public static String MONITORING_PLATFORM_IP = DEFAULT_IP;
	
	public static final int DEFAULT_MONITORING_PLATFORM_PORT = 8170;
	public static String MONITORING_PLATFORM_PORT = Integer.toString(DEFAULT_MONITORING_PLATFORM_PORT);
	
	public static final float DEFAULT_DEFAULT_DEMAND = 0f;
	public static String DEFAULT_DEMAND = Float.toString(DEFAULT_DEFAULT_DEMAND);
	
	public static String OBJECT_STORE_IP = DEFAULT_IP;
	
	public static final int DEFAULT_OBJECT_STORE_PORT = 20622;
	public static String OBJECT_STORE_PORT = Integer.toString(DEFAULT_OBJECT_STORE_PORT);
	
	public static final String DEFAULT_OBJECT_STORE_MODEL_PATH = "/v1/collections/S4C/objects/OpsConfig/data";
	public static String OBJECT_STORE_MODEL_PATH = DEFAULT_OBJECT_STORE_MODEL_PATH;
	
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
			RUN_WORKING_DIRECTORY = getLocalTmp().toString();
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
	
	public static Path getLocalTmp() {
		if (LOCAL_TEMPORARY_FOLDER == null)
			createNewLocalTmp();
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
	
	public static final String DEFAULT_RUN_AMPL_SOLVER = "/usr/optimization/CPLEX_Studio_Preview126/cplex/bin/x86-64_linux/cplexamp"; 
	public static String RUN_AMPL_SOLVER = DEFAULT_RUN_AMPL_SOLVER;
	
	public static final String DEFAULT_RUN_AMPL_EXECUTABLE = "/usr/optimization/ILOG/ampl20060626.cplex101/ampl";
	public static String RUN_AMPL_EXECUTABLE = DEFAULT_RUN_AMPL_EXECUTABLE;
	
	public static final String DEFAULT_RUN_CMPL_SOLVER = "cbc"; // glpk, cbc, scip, gurobi, cplex
	public static String RUN_CMPL_SOLVER = DEFAULT_RUN_CMPL_SOLVER;
	
	public static final String DEFAULT_RUN_CMPL_EXECUTABLE = "/usr/share/Cmpl/cmpl";
	public static String RUN_CMPL_EXECUTABLE = DEFAULT_RUN_CMPL_EXECUTABLE;
	
	public static final int DEFAULT_CMPL_THREADS = 4;
	public static int CMPL_THREADS = DEFAULT_CMPL_THREADS;
	
	public static int getCMPLThreads() {
		int threads = CMPL_THREADS;
		if (isRunningLocally()) {
			threads = Runtime.getRuntime().availableProcessors() - 1;
			if (threads <= 0)
				threads = 1;
		}
		return threads;
	}

	public static Solver MATH_SOLVER = Solver.DEFAULT;
	
	public static enum Solver {
		AMPL("AMPL"), CMPL("CMPL");
		
		public static Solver DEFAULT = AMPL;

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
			return DEFAULT;
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
								setPropertyIfNotNull(key, value);
							}		
		}
	}
	
	public static final String PROPERTY_NAME_OWN_IP = "MODACLOUDS_AR_OWN_IP";
	public static final String PROPERTY_NAME_LISTENING_PORT = "MODACLOUDS_AR_LISTENER_PORT";
	public static final String PROPERTY_NAME_PATH_TO_DESIGN_TIME_MODEL = "MODACLOUDS_AR_PATH_TO_MODEL";
	public static final String PROPERTY_NAME_SSH_USER_NAME = "MODACLOUDS_AR_SSH_USER";
	public static final String PROPERTY_NAME_SSH_HOST = "MODACLOUDS_AR_SSH_HOST";
	public static final String PROPERTY_NAME_SSH_PASSWORD = "MODACLOUDS_AR_SSH_PASS";
	public static final String PROPERTY_NAME_CLOUDML_WEBSOCKET_IP = "MODACLOUDS_AR_CLOUDML_IP";
	public static final String PROPERTY_NAME_CLOUDML_WEBSOCKET_PORT = "MODACLOUDS_AR_CLOUDML_PORT";
	public static final String PROPERTY_NAME_MONITORING_PLATFORM_IP = "MODACLOUDS_TOWER4CLOUDS_MANAGER_IP";
	public static final String PROPERTY_NAME_MONITORING_PLATFORM_PORT = "MODACLOUDS_TOWER4CLOUDS_MANAGER_PORT";
	public static final String PROPERTY_NAME_DEFAULT_DEMAND = "MODACLOUDS_AR_DEF_DEMAND";
	public static final String PROPERTY_NAME_OBJECT_STORE_IP = "MODACLOUDS_AR_OBJ_STORE_IP";
	public static final String PROPERTY_NAME_OBJECT_STORE_PORT = "MODACLOUDS_AR_OBJ_STORE_PORT";
	public static final String PROPERTY_NAME_OBJECT_STORE_MODEL_PATH = "MODACLOUDS_AR_OBJ_STORE_PATH";
	public static final String PROPERTY_NAME_SOLVER_NAME = "MODACLOUDS_AR_SOLVER_NAME";
	public static final String PROPERTY_NAME_SOLVER_SOLVER = "MODACLOUDS_AR_SOLVER_SOLVER";
	public static final String PROPERTY_NAME_SOLVER_EXECUTABLE = "MODACLOUDS_AR_SOLVER_EXECUTABLE";
	public static final String PROPERTY_NAME_SOLVER_THREADS = "MODACLOUDS_AR_SOLVER_THREADS";

	public static void setFromEnrivonmentVariables() throws ConfigurationFileException {
		setPropertyIfNotNull("OWN_IP", System.getenv(PROPERTY_NAME_OWN_IP));
		setPropertyIfNotNull("LISTENING_PORT", System.getenv(PROPERTY_NAME_LISTENING_PORT));
		setPropertyIfNotNull("PATH_TO_DESIGN_TIME_MODEL", System.getenv(PROPERTY_NAME_PATH_TO_DESIGN_TIME_MODEL));
		setPropertyIfNotNull("SSH_USER_NAME", System.getenv(PROPERTY_NAME_SSH_USER_NAME));
		setPropertyIfNotNull("SSH_HOST", System.getenv(PROPERTY_NAME_SSH_HOST));
		setPropertyIfNotNull("SSH_PASSWORD", System.getenv(PROPERTY_NAME_SSH_PASSWORD));
		setPropertyIfNotNull("CLOUDML_WEBSOCKET_IP", System.getenv(PROPERTY_NAME_CLOUDML_WEBSOCKET_IP));
		setPropertyIfNotNull("CLOUDML_WEBSOCKET_PORT", System.getenv(PROPERTY_NAME_CLOUDML_WEBSOCKET_PORT));
		setPropertyIfNotNull("MONITORING_PLATFORM_IP", System.getenv(PROPERTY_NAME_MONITORING_PLATFORM_IP));
		setPropertyIfNotNull("MONITORING_PLATFORM_PORT", System.getenv(PROPERTY_NAME_MONITORING_PLATFORM_PORT));
		setPropertyIfNotNull("DEFAULT_DEMAND", System.getenv(PROPERTY_NAME_DEFAULT_DEMAND));
		setPropertyIfNotNull("OBJECT_STORE_IP", System.getenv(PROPERTY_NAME_OBJECT_STORE_IP));
		setPropertyIfNotNull("OBJECT_STORE_PORT", System.getenv(PROPERTY_NAME_OBJECT_STORE_PORT));
		setPropertyIfNotNull("OBJECT_STORE_MODEL_PATH", System.getenv(PROPERTY_NAME_OBJECT_STORE_MODEL_PATH));
		
		setSolver(
				System.getenv(PROPERTY_NAME_SOLVER_NAME),
				System.getenv(PROPERTY_NAME_SOLVER_SOLVER),
				System.getenv(PROPERTY_NAME_SOLVER_EXECUTABLE),
				System.getenv(PROPERTY_NAME_SOLVER_THREADS));
	}
	
	public static void setFromSystemProperties() throws ConfigurationFileException {
		setPropertyIfNotNull("OWN_IP", System.getProperty(PROPERTY_NAME_OWN_IP));
		setPropertyIfNotNull("LISTENING_PORT", System.getProperty(PROPERTY_NAME_LISTENING_PORT));
		setPropertyIfNotNull("PATH_TO_DESIGN_TIME_MODEL", System.getProperty(PROPERTY_NAME_PATH_TO_DESIGN_TIME_MODEL));
		setPropertyIfNotNull("SSH_USER_NAME", System.getProperty(PROPERTY_NAME_SSH_USER_NAME));
		setPropertyIfNotNull("SSH_HOST", System.getProperty(PROPERTY_NAME_SSH_HOST));
		setPropertyIfNotNull("SSH_PASSWORD", System.getProperty(PROPERTY_NAME_SSH_PASSWORD));
		setPropertyIfNotNull("CLOUDML_WEBSOCKET_IP", System.getProperty(PROPERTY_NAME_CLOUDML_WEBSOCKET_IP));
		setPropertyIfNotNull("CLOUDML_WEBSOCKET_PORT", System.getProperty(PROPERTY_NAME_CLOUDML_WEBSOCKET_PORT));
		setPropertyIfNotNull("MONITORING_PLATFORM_IP", System.getProperty(PROPERTY_NAME_MONITORING_PLATFORM_IP));
		setPropertyIfNotNull("MONITORING_PLATFORM_PORT", System.getProperty(PROPERTY_NAME_MONITORING_PLATFORM_PORT));
		setPropertyIfNotNull("DEFAULT_DEMAND", System.getProperty(PROPERTY_NAME_DEFAULT_DEMAND));
		setPropertyIfNotNull("OBJECT_STORE_IP", System.getProperty(PROPERTY_NAME_OBJECT_STORE_IP));
		setPropertyIfNotNull("OBJECT_STORE_PORT", System.getProperty(PROPERTY_NAME_OBJECT_STORE_PORT));
		setPropertyIfNotNull("OBJECT_STORE_MODEL_PATH", System.getProperty(PROPERTY_NAME_OBJECT_STORE_MODEL_PATH));
		
		setSolver(
				System.getProperty(PROPERTY_NAME_SOLVER_NAME),
				System.getProperty(PROPERTY_NAME_SOLVER_SOLVER),
				System.getProperty(PROPERTY_NAME_SOLVER_EXECUTABLE),
				System.getProperty(PROPERTY_NAME_SOLVER_THREADS));
	}

	public static void setFromArguments(Map<String, String> paramsMap) throws ConfigurationFileException {
		setPropertyIfNotNull("OWN_IP", paramsMap.get("ownIp"));
		setPropertyIfNotNull("LISTENING_PORT", paramsMap.get("listeningPort"));
		setPropertyIfNotNull("PATH_TO_DESIGN_TIME_MODEL", paramsMap.get("pathToDesignAdaptationModel"));
		setPropertyIfNotNull("SSH_USER_NAME", paramsMap.get("sshUser"));
		setPropertyIfNotNull("SSH_HOST", paramsMap.get("sshHost"));
		setPropertyIfNotNull("SSH_PASSWORD", paramsMap.get("sshPass"));
		setPropertyIfNotNull("CLOUDML_WEBSOCKET_IP", paramsMap.get("cloudMLIp"));
		setPropertyIfNotNull("CLOUDML_WEBSOCKET_PORT", paramsMap.get("cloudMLPort"));
		setPropertyIfNotNull("MONITORING_PLATFORM_IP", paramsMap.get("t4cIp"));
		setPropertyIfNotNull("MONITORING_PLATFORM_PORT", paramsMap.get("t4cPort"));
		setPropertyIfNotNull("DEFAULT_DEMAND", paramsMap.get("defaultDemand"));
		setPropertyIfNotNull("OBJECT_STORE_IP", paramsMap.get("objStorIp"));
		setPropertyIfNotNull("OBJECT_STORE_PORT", paramsMap.get("objStorePort"));
		setPropertyIfNotNull("OBJECT_STORE_MODEL_PATH", paramsMap.get("objStorePathToModel"));
		setPropertyIfNotNull("MATH_SOLVER", paramsMap.get("objStorePathToModel"));
		
		setSolver(paramsMap.get("solverName"), paramsMap.get("solverSolver"), paramsMap.get("solverExecutable"), paramsMap.get("solverThreads"));
	}
	
	public static void setSolver(String name, String solver, String executable, String threads) {
		Solver newSolver = Solver.DEFAULT;
		if (name != null && name.length() > 0) {
			newSolver = Solver.getByName(name);
		}
		
		if ((solver != null && solver.length() > 0) || (executable != null && executable.length() > 0) || (threads != null && threads.length() > 0))
			MATH_SOLVER = newSolver;
		
		switch (newSolver) {
		case AMPL:
			setPropertyIfNotNull("RUN_AMPL_SOLVER", solver);
			setPropertyIfNotNull("RUN_AMPL_EXECUTABLE", executable);
			break;
		case CMPL:
			setPropertyIfNotNull("RUN_CMPL_SOLVER", solver);
			setPropertyIfNotNull("RUN_CMPL_EXECUTABLE", executable);
			if (threads != null && threads.length() > 0) {
				try {
					CMPL_THREADS = Integer.parseInt(threads);
				} catch (Exception e) {
					journal.error("Error while parsing the number of threads parameter.", e);
				}
			}
			break;
		}
	}

	public static void inizializeFileSystem() throws ProjectFileSystemException{

		File file = null;
		
		
		clearFileSystem();
		
		for(Container c: ModelManager.getModel().getContainer()){
			file = Paths.get(getLocalTmp().toString(), "executions", "execution_"+c.getId(), "IaaS_1").toFile();
			boolean success=file.mkdirs();
			
			if(!success)
				throw new ProjectFileSystemException("Error initializing the project file system: the following folder cannot be created: "
						+ "executions/execution_"+c.getId()+"/IaaS_1");


		}
		
	}
	
	private static void clearFileSystem(){
		File file = Paths.get(getLocalTmp().toString(), "executions").toFile();
		
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
			try {
				setFromFile();
			} catch (Exception e) {
				journal.warn("Error while setting from the file, maybe it doesn't exist.", e);
			}
			setFromEnrivonmentVariables();
			setFromSystemProperties();
			if (paramsMap != null) {
				setFromArguments(paramsMap);
			}
			isAlreadySet = true;
			
			initFolders();
		}
	}
	
	public static InputStream getInputStream(String filePath) {
		Path p = getPathToFile(filePath);
		if (p != null)
			try {
				return new FileInputStream(p.toFile());
			} catch (Exception e) { }
		
		return null;
	}
	
	public static Path getPathToFile(String filePath) {
		File f = new File(filePath);
		if (f.exists())
			try {
				return f.toPath();
			} catch (Exception e) { }
		
		URL url = ConfigManager.class.getResource(filePath);
		if (url == null)
			url = ConfigManager.class.getResource("/" + filePath);
		if (url == null)
			return null;
		else
			return Paths.get(url.getPath());
	}
	
	private static InputStream findConfigFile() {
		InputStream toReturn = getInputStream("config.xml");
		
		if(toReturn==null){
			journal.warn("input stream null");
		}
		
		return toReturn;
	}
	
	private static void setPropertyIfNotNull(String key, String value){
		if (key != null && key.length() > 0 && value != null && value.length() > 0)
			setProperty(key, value);
	}
	
	private static void setProperty(String key, String value){
		try {
			Field f = ConfigManager.class.getField(key);
			f.set(null, value);
		} catch (Exception e) {
			journal.error("Error while setting the value of the property.", e);
		}
	}
	
	public static void printConfig(){
		try {
			Field[] fs = ConfigManager.class.getFields();
			for (Field f : fs) {
				if (Modifier.isFinal(f.getModifiers()))
					continue;
				journal.info("{} = {}", f.getName(), f.get(null));
			}
		} catch (Exception e) {
			journal.error("Error while getting the value of the properties.", e);
		}
	}
	
	public static boolean isRunningLocally() {
		return (SSH_HOST.equals("localhost") || SSH_HOST.equals("127.0.0.1"));
	}
	
}
