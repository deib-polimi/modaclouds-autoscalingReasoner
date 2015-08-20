package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import java.io.File;
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
						
		try {
			ConfigManager.loadConfiguration();
		} catch (ConfigurationFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ModelManager.loadModel();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		ModelManager.initializeUsedForScale();
		ModelManager.printCurrentModel();
		
		try {
			ConfigManager.inizializeFileSystem();
		} catch (ProjectFileSystemException e1) {
			e1.printStackTrace();
		}
		
		MonitoringConnector monitor=new MonitoringConnector();
		JAXBContext context;
		try {
			context = JAXBContext.newInstance("it.polimi.tower4clouds.rules");
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
			File rules = Paths.get("sarBuildingRulesTest.xml").toFile();
			OutputStream out = new FileOutputStream(rules);
			marshaller.marshal(monitor.buildRequiredRules(),out);
			System.out.println("Rules file "+ rules.toString()+" created!");

			
		} catch (JAXBException | IOException e) {
			e.printStackTrace();
		}
		
		//to install created rules
		
		//to attach required observers
		
		OptimizationInputWriter siw= new OptimizationInputWriter();
		siw.writeStaticInput(ModelManager.getModel());
		
		Clock clock=new Clock(ModelManager.getTimestepDuration());
		
		/*
					
			try {
				mp.attachObserver("EstimatedDemand", ConfigManager.OWN_IP, "8179");
				MainObserver.startServer("8179");

				
				for(int i=1; i<=ModelManager.getOptimizationWindow();i++){
					//MonitoringRules workloadRules=mp.buildWorkloadForecastRule(ModelManager.getModel(), i);
					//mp.installRules(workloadRules);
					mp.attachObserver("ForecastedWorkload"+i, ConfigManager.OWN_IP, Integer.toString(8180+i-1));
					MainObserver.startServer(Integer.toString(8180+i-1));

				}
			} catch (NotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		*/
		
		}
}
