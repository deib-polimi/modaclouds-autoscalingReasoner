package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector.MainObserver;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector.MonitoringConnector;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizationInputWriter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.sshConnector.SshAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ModelManager;
import it.polimi.tower4clouds.rules.MonitoringRules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdaptationInitializer {
	private static final Logger journal = LoggerFactory
			.getLogger(AdaptationInitializer.class);
	
	public void initialize() {
						
		//printing the configuration
		ConfigManager.printConfig();
		
		//loading the internal model
		journal.info("Loading the model");
		ModelManager.loadModel();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			journal.error("Error while waiting.", e1);
		}
		
		
		//initializing the instances used for scale for each application tier
		journal.info("Initializing the instances used for scale (the newest one) for each application tier");
		ModelManager.initializeUsedForScale();
		journal.info("Initial model:");
		journal.info(ModelManager.printCurrentModel());

		
		//initialize the local file system
		journal.info("Initializing the internal file system");
		try {
			ConfigManager.inizializeFileSystem();
		} catch (ProjectFileSystemException e1) {
			journal.error("Error while inizializing the file system.", e1);
		}
		
		
		MonitoringConnector monitor=new MonitoringConnector();
		JAXBContext context;

				
		try {
			//getting required monitoring rules
			journal.info("Building the required monitoring rules");
			MonitoringRules toInstall=monitor.buildRequiredRules();

			//serializing built rules
			context = JAXBContext.newInstance("it.polimi.tower4clouds.rules");
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
			File rules = Paths.get("sarBuildingRulesTest.xml").toFile();
			OutputStream out = new FileOutputStream(rules);
			marshaller.marshal(monitor.buildRequiredRules(),out);
			
			
			//installing required rules
			journal.info("Installing monitoring rules");
			monitor.installRules(toInstall);
			
			//attaching required observers
			//journal.info("Attaching required observers on port {}", ConfigManager.LISTENING_PORT);
			//monitor.attachRequiredObservers();

			//starting observer
			journal.info("Starting the observer");
			MainObserver.startServer(ConfigManager.LISTENING_PORT);

		} catch (JAXBException e) {
			journal.error("JAXB error.", e);
		} catch (FileNotFoundException e) {
			journal.error("File not found!", e);
		} 
		
		
		//writing the static input files for each container
		journal.info("Writing the static input files");
		OptimizationInputWriter siw= new OptimizationInputWriter();
		siw.writeStaticInput(ModelManager.getModel());
		journal.info("Static input files wrote");
		
		try {
			SshAdapter.sendFixedFiles();
			journal.info("Models and static files sent");
		} catch (Exception e) {
			journal.error("Error while generating and sending the fixed files.", e);
		}
		
		//starting a clock changing the response time thresholds for each application tier every hour
		journal.info("Starting a clock changing the threshold in the model every hour for each managed application tier");
		@SuppressWarnings("unused")
		HourClock hourClock=new HourClock();
		
		//starting a fake workload observer
		//@SuppressWarnings("unused")
		//FakeObserver obs=new FakeObserver(ModelManager.getTimestepDuration());
		
		//starting the adaptation clock running the adaptation every timestepDuration minutes
		journal.info("Starting the adaptation clock to run an adaptation step every {} minutes", ModelManager.getTimestepDuration());
		@SuppressWarnings("unused")
		AdaptationClock clock=new AdaptationClock(ModelManager.getTimestepDuration());
		

		

		}
}
