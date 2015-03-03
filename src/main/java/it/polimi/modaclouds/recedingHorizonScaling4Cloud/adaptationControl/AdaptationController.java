package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;




import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import it.polimi.modaclouds.qos_models.schema.MonitoringRule;
import it.polimi.modaclouds.qos_models.schema.MonitoringRules;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.ScaleOutSocket;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ConfigurationFileException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTier;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.Container;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ModelManager;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.monitoringPlatformConnector.MonitoringPlatformAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.observers.FakeObserver;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.observers.Observer;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizerOutputParser;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizerInputWriter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.sshConnector.SshAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;
import it.polimi.modaclouds.space4cloud.milp.ssh.SshConnector;



public class AdaptationController {

	
	public static void main(String[] args) {
		
			Thread thread;
			ModelManager mm=ModelManager.getInstance();
			
			
			//path al modello di design dato in input come argomento/prelevato dall'object store

			mm.loadModel("/home/mik/workspace/recedingHorizonScaling4Cloud/resource/outputModelExample.xml");
		
			
			/*
			//orizzonta predittivo da considerare dato in inpout come argomento
			mm.setOptimizationHorizon(1);

			//IP della MP dato in input come argomento
			MonitoringPlatformAdapter mp=new MonitoringPlatformAdapter("54.154.45.180");
			
			Observer tempObs;
			
			
			ConfigManager cm=new ConfigManager();
			
			try {
				
				cm.loadConfiguration();
				cm.inizializeFileSystem(mm.getModel());
			} catch (ConfigurationFileException | ProjectFileSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		

			
			MonitoringRules demandRules=mp.buildDemandRule(mm.getModel());
			
			
			//costruisce ed installa le regole necessari per monitorare la demand a livello di tier applicativo
			//mp.installRules(demandRules);
	
			
			//installa gli observer necessari per ricevere la demand stimaTA e lancia lobserver relativo
			//mp.attachObserver("EstimatedDemand", "131.175.135.243", "8179");
			tempObs=new Observer(8179);
			thread = new Thread(tempObs);
			thread.start();

			
			
			//costruisce ed installa le regole per il monitoraggio del workload a livello di tier applicativo
			for(int i=1; i<=mm.getOptimizationHorizon();i++){
				//MonitoringRules workloadRules=mp.buildWorkloadForecastRule(mm.getModel(), i);
					//mp.installRules(workloadRules);
		
				
				//installa gli observer necessari per ricevere il workload monitoraton e lancia l'observer relativo
				//mp.attachObserver("ForecastedWorkload"+i, "131.175.135.243", Integer.toString(8180+i-1));
				tempObs=new Observer(8180+i);
				thread = new Thread(tempObs);
				thread.start();

			}

			//costruisce i file di tipo statico per molinari
			OptimizerInputWriter siw= new OptimizerInputWriter();
			siw.writeStaticInput(mm.getModel());
			
			
			//test observer
			//va tutto parametrizzato compresi i nomi delle metriche scelti, l'ip della macchia su cui si trova il componente e òle porte da utilizzare
			//mp.attachObserver("FrontendCPUUtilization", "131.175.135.243", "8177");
			tempObs=new Observer(8177);
			thread = new Thread(tempObs);
			thread.start();
			
			System.out.println("sto per lanciare fake");
			FakeObserver fake=new FakeObserver();
			fake.startCollectData(mm.getModel(), mm.getOptimizationHorizon());
		
		
			*/
			
			//working with CloudML
			
			applyAdaptation(mm.getModel().getContainer().get(0));
		
	
		}
	
	
		
		public static void applyAdaptation(Container toAdapt){
			
			/*
			SshAdapter ssh=new SshAdapter();
			
			ssh.run(toAdapt);
			
			
			OptimizerOutputParser outputParser= new OptimizerOutputParser();
			
			
			
			outputParser.parseExecutionOutput("executions/execution_"+toAdapt.getId()+"/IaaS_1/output.out", toAdapt);
			*/
			
			
			// working with cloudml
			scaleOut(toAdapt.getApplicationTier().get(0));

			

			
		}
	
		private static void scaleOut(ApplicationTier toScaleOut){
			
			
			
			//CLoutML IP and port required
			String destUri = "ws://127.0.0.1:9000";
			
			//retreive instance toscale asking CLoudML model via web socket
			
			ScaleOutSocket.instanceToScale="eu-west-1/i-8b891f6c";

			
			//run scaling
	        WebSocketClient client = new WebSocketClient();
	        ScaleOutSocket socket = new ScaleOutSocket();
	        try {
	            client.start();
	            URI echoUri = new URI(destUri);
	            ClientUpgradeRequest request = new ClientUpgradeRequest();
	            client.connect(socket, echoUri, request);
	            System.out.printf("Connecting to : %s%n", echoUri);
	            socket.awaitClose(5, TimeUnit.SECONDS);
	        } catch (Throwable t) {
	            t.printStackTrace();
	        } finally {
	            try {
	                client.stop();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    
			
		}
		
		
		public static void printObserved(String toPrint){
			System.out.println(toPrint);
		}
		
		
		
		
		
		
		
	

}
