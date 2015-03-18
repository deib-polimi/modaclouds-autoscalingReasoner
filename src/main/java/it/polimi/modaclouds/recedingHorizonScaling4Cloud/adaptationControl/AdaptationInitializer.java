package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cloudml.facade.commands.ScaleOut;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.CloudMLAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.WSClient;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ConfigurationFileException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.TierNotFoudException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTier;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.observers.FakeObserver;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizationOutputParser;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizationInputWriter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.sshConnector.SshAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigDictionary;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

public class AdaptationInitializer {
	private static final Logger journal = Logger
			.getLogger(WSClient.class.getName());
	
	public void initialize() {
		
			journal.log(Level.INFO, "Initializing Modaclouds Autoscaling Reasoner");
			Thread thread;
			
			
			
			ConfigManager cm=new ConfigManager();
			
			try {
				
				cm.loadConfiguration();

			} catch (ConfigurationFileException e) {
				e.printStackTrace();
			}
						
			
			journal.log(Level.INFO, "Connected to CloudML");
			
			

			ModelManager.loadModel(ConfigManager.getConfig(ConfigDictionary.pathToDesignAdaptationModel), 
					ConfigManager.getConfig(ConfigDictionary.OptimizationWindow),
					ConfigManager.getConfig(ConfigDictionary.TimeStepDuration));
			
			
			CloudMLAdapter cloudml=new CloudMLAdapter("ws://"+ConfigManager.getConfig(ConfigDictionary.CloudMLWebSocketIP)+":"+
					ConfigManager.getConfig(ConfigDictionary.CloudMLWebSocketPort)+"");
			
			cloudml.getDeploymentModel();
			
			ModelManager.printModel();

			
			try {
				cm.inizializeFileSystem(ModelManager.getModel());
			} catch (ProjectFileSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
			journal.log(Level.INFO,"Model initialized");
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			//MonitoringConnector mp=new MonitoringConnector(cm.getConfig(ConfigDictionary.MonitoringPlatformIP));				
			//Observer tempObs;
					
			//MonitoringRules demandRules=mp.buildDemandRule(mm.getModel());				
			//mp.installRules(demandRules);
			journal.log(Level.INFO, "Monitoring rule for demand monitoring successfull installed");
					
			//mp.attachObserver("EstimatedDemand", "131.175.135.243", "8179");
			//tempObs=new Observer(8179);
			//thread = new Thread(tempObs);
			//thread.start();
	
			//for(int i=1; i<=mm.getOptimizationWindow();i++){
				//MonitoringRules workloadRules=mp.buildWorkloadForecastRule(mm.getModel(), i);
				//mp.installRules(workloadRules);
				//mp.attachObserver("ForecastedWorkload"+i, "131.175.135.243", Integer.toString(8180+i-1));
				//tempObs=new Observer(8180+i);
				//thread = new Thread(tempObs);
				//thread.start();
			//}
			
			journal.log(Level.INFO, "Monitoring rule for workload forecasts monitoring successfull installed");

			OptimizationInputWriter siw= new OptimizationInputWriter();
			siw.writeStaticInput(ModelManager.getModel());
			journal.log(Level.INFO, "Static input files initialized");
		
			//mp.attachObserver("FrontendCPUUtilization", "131.175.135.243", "8177");
			//tempObs=new Observer(8177);
			//thread = new Thread(tempObs);
			//thread.start();
					
			//using locally a fake observer
			FakeObserver fake=new FakeObserver();
			fake.startCollectData(ModelManager.getModel(), ModelManager.getOptimizationWindow());
		
		}

		
		public static void printObserved(String s){
			System.out.println(s);
		}
	
}
