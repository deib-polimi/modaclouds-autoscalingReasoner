package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import it.polimi.tower4clouds.rules.MonitoringRules;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ConfigurationFileException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector.FakeObserver;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector.MainObserver;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector.MonitoringConnector;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizationInputWriter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

public class AdaptationInitializer {
	private static final Logger journal = Logger
			.getLogger(AdaptationInitializer.class.getName());
	
	public void initialize() {
						
		//loading the config.xml file
		journal.log(Level.INFO, "Loading the configuration");

		try {
			ConfigManager.loadConfiguration();
			ConfigManager.printConfig();
		} catch (ConfigurationFileException e) {
			e.printStackTrace();
		}

		
		//loading the internal model
		journal.log(Level.INFO, "Loading the model");
		ModelManager.loadModel();
		
		journal.log(Level.INFO, "Waiting for model initialization to end");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		
		//initializing the instances used for scale for each application tier
		journal.log(Level.INFO, "Initializing the instances used for scale (the newest one) for each application tier");
		ModelManager.initializeUsedForScale();
		journal.log(Level.INFO, "Printing the initial model");
		journal.log(Level.INFO, ModelManager.printCurrentModel());

		
		//initialize the local file system
		journal.log(Level.INFO, "Initializing the internal file system");
		try {
			ConfigManager.inizializeFileSystem();
		} catch (ProjectFileSystemException e1) {
			e1.printStackTrace();
		}
		
		
		MonitoringConnector monitor=new MonitoringConnector();
		JAXBContext context;

				
		try {
			//getting required monitoring rules
			journal.log(Level.INFO, "Building the required monitoring rules");
			MonitoringRules toInstall=monitor.buildRequiredRules();

			//serializing built rules
			context = JAXBContext.newInstance("it.polimi.tower4clouds.rules");
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
			File rules = Paths.get("sarBuildingRulesTest.xml").toFile();
			OutputStream out = new FileOutputStream(rules);
			marshaller.marshal(monitor.buildRequiredRules(),out);
			
			
			//installing required rules
			journal.log(Level.INFO, "Installing monitoring rules");
			monitor.installRules(toInstall);
			
			//attaching required observers
			//journal.log(Level.INFO, "Attaching required observers on port "+ConfigManager.LISTENING_PORT);
			//monitor.attachRequiredObservers();

			//starting observer
			journal.log(Level.INFO, "Starting the observer");
			MainObserver.startServer(ConfigManager.LISTENING_PORT);

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		
		
		//writing the static input files for each container
		journal.log(Level.INFO, "Writing the static input files");
		OptimizationInputWriter siw= new OptimizationInputWriter();
		siw.writeStaticInput(ModelManager.getModel());
		journal.log(Level.INFO, "Static input files wrote");
		
		//starting a clock changing the response time thresholds for each application tier every hour
		journal.log(Level.INFO, "Starting a clock changing the threshold in the model every hour for each managed application tier");
		@SuppressWarnings("unused")
		HourClock hourClock=new HourClock();
		
		//starting a fake workload observer
		//@SuppressWarnings("unused")
		//FakeObserver obs=new FakeObserver(ModelManager.getTimestepDuration());
		
		//starting the adaptation clock running the adaptation every timestepDuration minutes
		journal.log(Level.INFO, "Starting the adaptation clock to run an adaptation step every "+ModelManager.getTimestepDuration()+" minutes");
		@SuppressWarnings("unused")
		AdapatationClock clock=new AdapatationClock(ModelManager.getTimestepDuration());
		

		

		}
}
