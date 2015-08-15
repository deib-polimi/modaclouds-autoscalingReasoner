package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import it.polimi.tower4clouds.manager.api.NotFoundException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.CloudMLAdapter;
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
				e.printStackTrace();
			}
						
			ModelManager.loadModel();
			
			CloudMLAdapter cloudml=new CloudMLAdapter();
			
			cloudml.getDeploymentModel();
			
			try {
				Thread.sleep(5000);
			
			/*
			for(ApplicationTierAtRuntime tier: ModelManager.getRuntimeEnv()){
				Set<String> instances=tier.getInstancesStartTimes().keySet();
				
				for(String i: instances){
					cloudml.getInstanceInfo(i);
					Thread.sleep(5000);
				}
			}
			*/
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			ModelManager.printCurrentModel();
	
			try {
				ConfigManager.inizializeFileSystem(ModelManager.getModel());
			} catch (ProjectFileSystemException e) {
				e.printStackTrace();
			}
			
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			
			MonitoringConnector mp=new MonitoringConnector();				
					
			//MonitoringRules demandRules=mp.buildDemandRule(ModelManager.getModel());				
			//mp.installRules(demandRules);
			//journal.log(Level.INFO, "Monitoring rule for demand monitoring successfull installed");
					
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

			
			//journal.log(Level.INFO, "Monitoring rule for workload forecasts monitoring successfull installed");
			

			OptimizationInputWriter siw= new OptimizationInputWriter();
			siw.writeStaticInput(ModelManager.getModel());
			
			
			//using locally a fake observer
			
			/*
			FakeObserver fake=new FakeObserver();
			fake.model=ModelManager.getModel();
			fake.optimizationHorizon=ModelManager.getOptimizationWindow();
			Thread toRun=new Thread(fake);
			toRun.start();
			*/
		
		}

		
		public static void printObserved(String s){
			System.out.println(s);
		}
	
}
