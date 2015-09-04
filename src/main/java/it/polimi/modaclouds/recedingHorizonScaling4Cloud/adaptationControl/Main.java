package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.osgi.service.log.LogEntry;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Main {

	private static final Logger journal = Logger
			.getLogger(Main.class.getName());
	
	@Parameter(names = "-sshUser")
	private String sshUser = null;
	
	@Parameter(names = "-sshPass")
	private String sshPass = null;
	
	@Parameter(names = "-sshHost")
	private String sshHost = null;
	
	@Parameter(names = "-pathToOptInputFolder")
	private String pathToOptInputFolder = null;
	
	@Parameter(names = "-pathToOptLauncher")
	private String pathToOptLauncher = null;
	
	@Parameter(names = "-pathToOptOutputFile")
	private String pathToOptOutputFile = null;
	
	@Parameter(names = "-ownIp")
	private String ownIp = "127.0.0.1";
	
	@Parameter(names = "-t4cIp")
	private String t4cIp = "127.0.0.1";
	
	@Parameter(names = "-cloudMLIp")
	private String cloudMLIp = "127.0.0.1";
	
	@Parameter(names = "-listeningPort")
	private String listeningPort = "8179";
	
	@Parameter(names = "-t4cPort")
	private String t4cPort = "8170";
	
	@Parameter(names = "-cloudMLPort")
	private String cloudMLPort = "9000";
	
	@Parameter(names = "-pathToDesignAdapatationModel")
	private String pathToDesignAdapatationModel = "./";
	
	@Parameter(names = "-defaultDemand")
	private String defaultDemand = "0";
	
	 
	public static void main(String[] args) {

		
		Main m = new Main();
		JCommander jc = new JCommander(m, args);
		
		
		
		if(m.sshHost!=null & m.sshPass!=null & m.sshUser!=null 
				& m.pathToOptInputFolder!=null & m.pathToOptLauncher!=null & m.pathToOptOutputFile!=null){
			
			journal.log(Level.INFO,"All the mandatory configurations are given as arguments; setting configuration from arguments");
			
			ConfigManager.setFromArguments(m.ownIp, m.t4cIp, m.cloudMLIp, m.listeningPort, m.t4cPort, m.cloudMLPort, 
					m.pathToDesignAdapatationModel, m.sshUser, m.sshPass, m.sshHost, m.pathToOptInputFolder, m.pathToOptLauncher,
					m.pathToOptOutputFile, m.defaultDemand);
			
		}else{
			journal.log(Level.INFO,"Not all the mandatory configurations are given as arguments; it will try to load configuration from config file");

		}
		
		journal.log(Level.INFO, "Autoscaling Reasoner started");
		journal.log(Level.INFO, "Starting the initialization phase");
		AdaptationInitializer initializer=new AdaptationInitializer();
		initializer.initialize();
		
	}

}
