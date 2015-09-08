package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Main {

	private static final Logger journal = LoggerFactory
			.getLogger(Main.class);
	
	@Parameter(names = { "-h", "--help", "-help" }, help = true, description = "Shows this help")
	private boolean help = false;
	
	@Parameter(names = "-sshUser", description = "The user name that will be used to connect to the SSH server")
	private String sshUser = null;
	
	@Parameter(names = "-sshPass", description = "The password for the user on the SSH server")
	private String sshPass = null;
	
	@Parameter(names = "-sshHost", description = "The IP of the machine with the AMPL/CMPL binaries")
	private String sshHost = null;
	
	@Parameter(names = "-pathToOptInputFolder")
	private String pathToOptInputFolder = null;
	
	@Parameter(names = "-pathToOptLauncher")
	private String pathToOptLauncher = null;
	
	@Parameter(names = "-pathToOptOutputFile")
	private String pathToOptOutputFile = null;
	
	@Parameter(names = "-objStorIp", description = "The IP of the object store")
	private String objStorIp = "127.0.0.1";
	
	@Parameter(names = "-pbjStorePort", description = "The port of the object store")
	private String objStorePort = "20622";
	
	@Parameter(names = "-objStorePathToModel", description = "The path in the object store where the data will be saved")
	private String objStorePathToModel = "/v1/collections/S4C/objects/OpsConfig/data";
	
	@Parameter(names = "-ownIp", description = "The IP of this machine")
	private String ownIp = "127.0.0.1";
	
	@Parameter(names = "-t4cIp", description = "The IP of the monitoring platform")
	private String t4cIp = "127.0.0.1";
	
	@Parameter(names = "-cloudMLIp", description = "The IP of the CloudML daemon")
	private String cloudMLIp = "127.0.0.1";
	
	@Parameter(names = "-listeningPort", description = "The port to which this program is going to listen")
	private String listeningPort = "8179";
	
	@Parameter(names = "-t4cPort", description = "The port of the monitoring platform")
	private String t4cPort = "8170";
	
	@Parameter(names = "-cloudMLPort", description = "The port of the CloudML daemon")
	private String cloudMLPort = "9000";
	
	@Parameter(names = "-pathToDesignAdapatationModel")
	private String pathToDesignAdapatationModel = null;
	
	@Parameter(names = "-defaultDemand")
	private String defaultDemand = "0";
	
	public static final String APP_TITLE = "\nAutoscaling Reasoner\n";

	static {
		// Optionally remove existing handlers attached to j.u.l root logger
		SLF4JBridgeHandler.removeHandlersForRootLogger();  // (since SLF4J 1.6.5)

		// add SLF4JBridgeHandler to j.u.l's root logger, should be done once during
		// the initialization phase of your application
		SLF4JBridgeHandler.install();
	}
	 
	public static void main(String[] args) {
		
		Main m = new Main();
		JCommander jc = new JCommander(m, args);
		
		System.out.println(APP_TITLE);
		
		if (m.help) {
			jc.usage();
			System.exit(0);
		}
		
		if(m.sshHost!=null & m.sshPass!=null & m.sshUser!=null 
				& m.pathToOptInputFolder!=null & m.pathToOptLauncher!=null & m.pathToOptOutputFile!=null){
			
			journal.info("All the mandatory configurations are given as arguments; setting configuration from arguments");
			
			ConfigManager.setFromArguments(m.ownIp, m.t4cIp, m.cloudMLIp, m.listeningPort, m.t4cPort, m.cloudMLPort, 
					m.objStorIp, m.objStorePort, m.objStorePathToModel,
					m.pathToDesignAdapatationModel, m.sshUser, m.sshPass, m.sshHost, m.pathToOptInputFolder, m.pathToOptLauncher,
					m.pathToOptOutputFile, m.defaultDemand);
			
		}else{
			journal.info("Not all the mandatory configurations are given as arguments; trying to load configuration from config file");

		}
		
		journal.info("Autoscaling Reasoner started");
		journal.info("Starting the initialization phase");
		AdaptationInitializer initializer=new AdaptationInitializer();
		initializer.initialize();
		
	}

}
