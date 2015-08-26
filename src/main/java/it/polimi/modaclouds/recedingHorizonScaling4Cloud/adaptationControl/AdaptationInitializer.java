package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import it.polimi.tower4clouds.manager.api.NotFoundException;
import it.polimi.tower4clouds.rules.MonitoringRules;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ConfigurationFileException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector.MainObserver;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector.MonitoringConnector;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizationInputWriter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

public class AdaptationInitializer {
	private static final Logger journal = Logger
			.getLogger(AdaptationInitializer.class.getName());
	
	public void initialize() {
						
		//loading the config.xml file
		try {
			ConfigManager.loadConfiguration();
		} catch (ConfigurationFileException e) {
			e.printStackTrace();
		}

		
		//loading the internal model
		ModelManager.loadModel();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		
		//initializing the instances used for scale for each application tier
		ModelManager.initializeUsedForScale();
		ModelManager.printCurrentModel();
		
		
		//initialize the local file system
		try {
			ConfigManager.inizializeFileSystem();
		} catch (ProjectFileSystemException e1) {
			e1.printStackTrace();
		}
		

		MonitoringConnector monitor=new MonitoringConnector();
		JAXBContext context;

				
		try {
			//getting required monitoring rules
			MonitoringRules toInstall=monitor.buildRequiredRules();

			//serializing built rules
			context = JAXBContext.newInstance("it.polimi.tower4clouds.rules");
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
			File rules = Paths.get("sarBuildingRulesTest.xml").toFile();
			OutputStream out = new FileOutputStream(rules);
			marshaller.marshal(monitor.buildRequiredRules(),out);
			
			
			//installing required rules
			monitor.installRules(toInstall);
			
			//attaching required observers
			monitor.attachRequiredObservers();

			//starting observer
			MainObserver.startServer(ConfigManager.LISTENING_PORT);

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		
		
		//writing the static input files for each container
		OptimizationInputWriter siw= new OptimizationInputWriter();
		siw.writeStaticInput(ModelManager.getModel());
		
		//starting the adaptation clock running the adaptation every timestepDuration minutes
		AdapatationClock clock=new AdapatationClock(ModelManager.getTimestepDuration());
		
		//starting a clock changing the response time thresholds for each application tier every hour
		HourClock hourClock=new HourClock();
		
		}
}
