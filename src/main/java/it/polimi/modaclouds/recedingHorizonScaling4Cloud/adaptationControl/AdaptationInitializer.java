package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.cloudml.facade.commands.ScaleOut;

import it.polimi.modaclouds.qos_models.schema.MonitoringRules;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.CloudMLAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.WSClient;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ConfigurationFileException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.TierNotFoudException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTier;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTierAtRuntime;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector.MonitoringConnector;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.observers.FakeObserver;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.observers.Observer;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizationOutputParser;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizationInputWriter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.sshConnector.SshAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigDictionary;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

public class AdaptationInitializer {
	private static final Logger journal = Logger
			.getLogger(AdaptationInitializer.class.getName());
	
	public void initialize() {
						
			ConfigManager cm=new ConfigManager();
			try {
				
				cm.loadConfiguration();
			} catch (ConfigurationFileException e) {
				e.printStackTrace();
			}
						
			ModelManager.loadModel(ConfigManager.getConfig(ConfigDictionary.pathToDesignAdaptationModel), 
					ConfigManager.getConfig(ConfigDictionary.OptimizationWindow),
					ConfigManager.getConfig(ConfigDictionary.TimeStepDuration));
			
			CloudMLAdapter cloudml=new CloudMLAdapter("ws://"+ConfigManager.getConfig(ConfigDictionary.CloudMLWebSocketIP)+":"+
					ConfigManager.getConfig(ConfigDictionary.CloudMLWebSocketPort)+"");
			
			cloudml.getDeploymentModel();
			
			try {
				Thread.sleep(5000);
			
			
			for(ApplicationTierAtRuntime tier: ModelManager.getRuntimeEnv()){
				Set<String> instances=tier.getInstancesStartTimes().keySet();
				
				for(String i: instances){
					cloudml.getInstanceInfo(i);
					Thread.sleep(5000);
				}
			}
			
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			ModelManager.printModel();
	
			try {
				cm.inizializeFileSystem(ModelManager.getModel());
			} catch (ProjectFileSystemException e) {
				e.printStackTrace();
			}
			
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			
			MonitoringConnector mp=new MonitoringConnector(ConfigManager.getConfig(ConfigDictionary.MonitoringPlatformIP));				
			Observer tempObs;
			Thread thread;
					
			//MonitoringRules demandRules=mp.buildDemandRule(ModelManager.getModel());				
			//mp.installRules(demandRules);
			//journal.log(Level.INFO, "Monitoring rule for demand monitoring successfull installed");
					
			mp.attachObserver("EstimatedDemand", ConfigManager.getConfig(ConfigDictionary.OWNIP), "8179");
			tempObs=new Observer(8179);
			thread = new Thread(tempObs);
			thread.start();
	
			for(int i=1; i<=ModelManager.getOptimizationWindow();i++){
				//MonitoringRules workloadRules=mp.buildWorkloadForecastRule(ModelManager.getModel(), i);
				//mp.installRules(workloadRules);
				mp.attachObserver("ForecastedWorkload"+i, ConfigManager.getConfig(ConfigDictionary.OWNIP), Integer.toString(8180+i-1));
				tempObs=new Observer(8180+i-1);
				thread = new Thread(tempObs);
				thread.start();
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
