package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

import java.util.HashMap;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterDescription;

public class Main {

	private static final Logger journal = LoggerFactory
			.getLogger(Main.class);
	
	@Parameter(names = { "-h", "--help", "-help" }, help = true, description = "Shows this help")
	private boolean help = false;
	
	@Parameter(names = "-sshUser", description = "The user name that will be used to connect to the SSH server")
	public String sshUser = null;
	
	@Parameter(names = "-sshPass", description = "The password for the user on the SSH server")
	public String sshPass = null;
	
	@Parameter(names = "-sshHost", description = "The IP of the machine with the AMPL/CMPL binaries")
	public String sshHost = ConfigManager.DEFAULT_IP;
	
	@Parameter(names = "-objStorIp", description = "The IP of the object store")
	public String objStorIp = ConfigManager.DEFAULT_IP;
	
	@Parameter(names = "-objStorePort", description = "The port of the object store")
	public String objStorePort = Integer.toString(ConfigManager.DEFAULT_OBJECT_STORE_PORT);
	
	@Parameter(names = "-objStorePathToModel", description = "The path in the object store where the data will be saved")
	public String objStorePathToModel = ConfigManager.DEFAULT_OBJECT_STORE_MODEL_PATH;
	
	@Parameter(names = "-ownIp", description = "The IP of this machine")
	public String ownIp = ConfigManager.DEFAULT_IP;
	
	@Parameter(names = "-t4cIp", description = "The IP of the monitoring platform")
	public String t4cIp = ConfigManager.DEFAULT_IP;
	
	@Parameter(names = "-cloudMLIp", description = "The IP of the CloudML daemon")
	public String cloudMLIp = ConfigManager.DEFAULT_IP;
	
	@Parameter(names = "-listeningPort", description = "The port to which this program is going to listen")
	public String listeningPort = Integer.toString(ConfigManager.DEFAULT_LISTENING_PORT);
	
	@Parameter(names = "-t4cPort", description = "The port of the monitoring platform")
	public String t4cPort = Integer.toString(ConfigManager.DEFAULT_MONITORING_PLATFORM_PORT);
	
	@Parameter(names = "-cloudMLPort", description = "The port of the CloudML daemon")
	public String cloudMLPort = Integer.toString(ConfigManager.DEFAULT_CLOUDML_WEBSOCKET_PORT);
	
	@Parameter(names = "-pathToDesignAdaptationModel")
	public String pathToDesignAdaptationModel = null;
	
	@Parameter(names = "-defaultDemand")
	public String defaultDemand = Float.toString(ConfigManager.DEFAULT_DEFAULT_DEMAND);
	
	@Parameter(names = "-solverName", description = "The name of the math solver that will be used (either AMPL or CMPL)")
	public String solverName = ConfigManager.Solver.DEFAULT.getName();
	
	@Parameter(names = "-solverSolver", description = "The solver for the math solver used")
	public String solverSolver = ConfigManager.Solver.DEFAULT == ConfigManager.Solver.AMPL ? ConfigManager.DEFAULT_RUN_AMPL_SOLVER : ConfigManager.DEFAULT_RUN_CMPL_SOLVER;
	
	@Parameter(names = "-solverExecutable", description = "The executable for the math solver used")
	public String solverExecutable = ConfigManager.Solver.DEFAULT == ConfigManager.Solver.AMPL ? ConfigManager.DEFAULT_RUN_AMPL_EXECUTABLE : ConfigManager.DEFAULT_RUN_CMPL_EXECUTABLE;
	
	@Parameter(names = "-solverThreads", description = "The threads used by the math solver (it only make sense when running CMPL remotely)")
	public String solverThreads = Integer.toString(ConfigManager.DEFAULT_CMPL_THREADS);
	
	@Parameter(names = "-observerPort", description = "The other observer port")
	public String observerPort = Integer.toString(ConfigManager.DEFAULT_OTHER_OBSERVER_PORT);
	
	public static String APP_NAME;
	public static String APP_FILE_NAME;
	public static String APP_VERSION;

	static {
		// Optionally remove existing handlers attached to j.u.l root logger
		SLF4JBridgeHandler.removeHandlersForRootLogger();  // (since SLF4J 1.6.5)

		// add SLF4JBridgeHandler to j.u.l's root logger, should be done once during
		// the initialization phase of your application
		SLF4JBridgeHandler.install();
	}
	 
	public static void main(String[] args) {
		
		PropertiesConfiguration releaseProperties = null;
		try {
			releaseProperties = new PropertiesConfiguration("release.properties");
		} catch (ConfigurationException e) {
			journal.error("Internal error", e);
			System.exit(1);
		}
		APP_NAME = releaseProperties.getString("application.name");
		APP_FILE_NAME = releaseProperties.getString("dist.file.name");
		APP_VERSION = releaseProperties.getString("release.version");
		
		Main m = new Main();
		JCommander jc = new JCommander(m, args);
		
		if (m.help) {
			jc.setProgramName(APP_FILE_NAME);
			jc.usage();
			System.exit(0);
		}
		
		journal.info("{} {}", APP_NAME, APP_VERSION);
		
		HashMap<String, String> paramsMap = new HashMap<String, String>();

		for (ParameterDescription param : jc.getParameters())
			if (param.isAssigned()) {
				String name = param.getLongestName().replaceAll("-", "");
				String value = null;
				try {
					value = Main.class.getField(name).get(m).toString();
				} catch (Exception e) {
				}

				paramsMap.put(name, value);
			}
		
		try {
			ConfigManager.loadConfiguration(paramsMap);
		} catch (Exception e) {
			journal.error("Error while parsing the configuration. Exiting.", e);
			System.exit(-1);
		}
		
		journal.info("Autoscaling Reasoner started");
		journal.info("Starting the initialization phase");
		AdaptationInitializer initializer=new AdaptationInitializer();
		initializer.initialize();
		
	}

}
