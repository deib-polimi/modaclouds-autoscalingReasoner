package it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl;




import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.cloudml.facade.commands.ScaleOut;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.polimi.modaclouds.qos_models.schema.MonitoringRule;
import it.polimi.modaclouds.qos_models.schema.MonitoringRules;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.CloudMLAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ConfigurationFileException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ProjectFileSystemException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.TierNotFoudException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTier;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.model.ApplicationTierAtRuntime;
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

	private static CloudMLAdapter cloudml;
	private static List<ApplicationTierAtRuntime> runtimeEnv;
	private static ModelManager mm;
	private static String lastCommand;
	private static int timestepDuration;
	
	public void start() {
		
			//initialize timestep duration (should come from config)
			timestepDuration=5;
		
			Thread thread;
			
			//inizialize cloudml connector
			cloudml=new CloudMLAdapter("ws://127.0.0.1:9000");
			
			//create tha model manager
			mm=ModelManager.getInstance();
			
			//create the model at runtime
			runtimeEnv=new ArrayList<ApplicationTierAtRuntime>();
			
			
			//orizzonta predittivo da considerare dato in inpout come argomento
			mm.setOptimizationHorizon(1);
			
			//path al modello di design dato in input come argomento/prelevato dall'object store

			mm.loadModel("/home/mik/workspace/recedingHorizonScaling4Cloud/resource/outputModelExample.xml");
			
			//initialize model at runtime
			ApplicationTierAtRuntime temp;
			for(Container c:mm.getModel().getContainer()){
				for(ApplicationTier t: c.getApplicationTier()){
					temp=new ApplicationTierAtRuntime();
					temp.setTierId(t.getId());
					runtimeEnv.add(temp);
				}
			}
			
			this.setLastCommand("initializeRuntimeEnv");
			
			cloudml.getDeploymentModel();
			
			
			
			
			
		

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
			siw.writeStaticInput(mm.getModel(), mm.getAlgorithmTierIndexes());
			
			

			
			
			
			//test observer
			//va tutto parametrizzato compresi i nomi delle metriche scelti, l'ip della macchia su cui si trova il componente e òle porte da utilizzare
			//mp.attachObserver("FrontendCPUUtilization", "131.175.135.243", "8177");
			tempObs=new Observer(8177);
			thread = new Thread(tempObs);
			thread.start();
			
			for(ApplicationTierAtRuntime t:runtimeEnv){
				System.out.println(t.getTierId());
				for(String k:t.getInstancesStartTimes().keySet())
					System.out.println(k+" "+t.getInstancesStartTimes().get(k));
			}
			
			
			//using locally a fake observer
			//FakeObserver fake=new FakeObserver();
			//fake.startCollectData(mm.getModel(), mm.getOptimizationHorizon());
		
		
			
			
			//temporary working with CloudML
			
			//applyAdaptation(mm.getModel().getContainer().get(0));
		
	
		}
	
		public static void setLastCommand(String command){
			lastCommand=command;
		}
	
		
		public static void applyAdaptation(Container toAdapt){
			
			
			
			try {
				//for each tier in the container retrieve the number of instances that will be available at the next time step
				Map<String,List<String>> exipiring=getNextTimestepExpiringInstances(toAdapt.getApplicationTier());
				//retrieve for each tier in the container the number of instaces that expires at the end of the current timestep
				Map<String,List<String>> available=getNextTimestepAvailableInstances(toAdapt.getApplicationTier());
				
				//write the initialVM.dat file for each tier
				for(ApplicationTier t:toAdapt.getApplicationTier()){
					int index=mm.getAlgorithmTierIndexes().get(t.getId()).intValue();
					for(int j=1; j<=mm.getOptimizationHorizon();j++)
						OptimizerInputWriter.writeFile("initialVM.dat", toAdapt.getId(), "let Nond["+index+",j]:=\n"+available.get(t.getId()).size()+"\n;");
				}
				
				//call the capacity allocation algorithm
				//SshAdapter ssh=new SshAdapter();
				
				//ssh.run(toAdapt);
				
				
				
				//parse the algorithm output and update the model and get the result in terms of an array with the new instances to instantiate 
				//for each tier (intexed)
				//OptimizerOutputParser outputParser= new OptimizerOutputParser();
				//int[] algorithmResult=outputParser.parseExecutionOutput("executions/execution_"+toAdapt.getId()+"/IaaS_1/output.out", toAdapt);
				
				
				//run the scaling for each tier
				
				
				
				
				// working with cloudml
				//scaleOut(toAdapt.getApplicationTier().get(0));
			} catch (TierNotFoudException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			


			
			

			

			
		}
	
		private static void scaleOut(ApplicationTier toScaleOut){
			
			
			/*
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
	        */
			
			cloudml.getDeploymentModel();
	    
			
		}
		
		
		public static void handleCloudMLResponse(String response){
			
			
			switch(lastCommand){
			case "addNewInstances":
				
				try {  
					
					JSONObject jsonObject = new JSONObject(response.substring(27));
					JSONArray instances=jsonObject.getJSONArray("vmInstances");
					addNewInstancesToRuntimeModelHandler(instances);
					//ScaleOut scale=new ScaleOut(id);
					//scaleOut(scale);
					

					} catch (JSONException | TierNotFoudException e) {
						e.printStackTrace();
					}
				
				
			case "initializeRuntimeEnv":	
				
				try {  
				
				JSONObject jsonObject = new JSONObject(response.substring(27));
				JSONArray instances=jsonObject.getJSONArray("vmInstances");
				initializeRuntimeEnvHandler(instances);
				//ScaleOut scale=new ScaleOut(id);
				//scaleOut(scale);

				} catch (JSONException | TierNotFoudException e) {
					e.printStackTrace();
				}
				
			default : System.out.println("no handlers finded for the last command issued");
			
			}
			

		}
		
		
		private static void addNewInstancesToRuntimeModelHandler(JSONArray instances) throws JSONException, TierNotFoudException{
			JSONObject tempInstance;
			String tempId;
			
			for(int i=0; i<instances.length(); i++){
				tempInstance=instances.getJSONObject(i);
				tempId=tempInstance.getString("id");
				if(!existsInstanceInRuntimeModel(tempId)){
					ApplicationTierAtRuntime toUpdate=getApplicationTierAtRuntime(tempInstance.getString("type"));
					
					if(toUpdate!=null){
						toUpdate.addNewInstance(tempId);
					}else{
						throw new TierNotFoudException("in the runtime model there is no tier with the specified id");
					}
					
				}
			}
			
			setLastCommand("");
			
			
		}
		
		private static boolean existsInstanceInRuntimeModel(String instance){
			
			for(ApplicationTierAtRuntime tier: runtimeEnv){
				for(String inst: tier.getInstancesStartTimes().keySet()){
					if(inst.equals(instance))
						return true;
				}
			}
			
			return false;
		}
		
		
		private static void initializeRuntimeEnvHandler(JSONArray instances) throws JSONException, TierNotFoudException{
			ApplicationTierAtRuntime temp;

			for(int i=0; i<instances.length(); i++){
				JSONObject instance=instances.getJSONObject(i);
				temp=getApplicationTierAtRuntime(instance.get("type").toString().substring(4, instance.get("type").toString().length()-1));
				temp.addNewInstance(instance.get("id").toString());
			}
			
			setLastCommand("");

		}
		
		
		private static ApplicationTierAtRuntime getApplicationTierAtRuntime(String tierId) throws TierNotFoudException{
			for(ApplicationTierAtRuntime t: runtimeEnv){
				if(t.getTierId().equals(tierId)){
					return t;
				}
			}
			
			throw new TierNotFoudException("in the runtime model there is no tier with the specified id");
		}
		
		private static Map<String,List<String>> getNextTimestepExpiringInstances(List<ApplicationTier> toCheck) throws TierNotFoudException{
			Map<String,List<String>> toReturn=new HashMap<String,List<String>>();
			Calendar cal = Calendar.getInstance();
			
			for(ApplicationTier t: toCheck){
				toReturn.put(t.getId(), new ArrayList<String>());
				ApplicationTierAtRuntime tier=getApplicationTierAtRuntime(t.getId());
				Map<String,Date> instancesStartTimes=tier.getInstancesStartTimes();
				
				
				cal.add(Calendar.MINUTE, -(55-timestepDuration));
				Date oneHourBack = cal.getTime();
	
				
				for(String instance: instancesStartTimes.keySet()){		
					if(instancesStartTimes.get(instance).before(oneHourBack)){
						toReturn.get(t.getId()).add(instance);
					}
				}
			}
			
			return toReturn;
		}
		
		private static Map<String,List<String>> getNextTimestepAvailableInstances(List<ApplicationTier> toCheck) throws TierNotFoudException{
			Map<String,List<String>> toReturn=new HashMap<String,List<String>>();
			Calendar cal = Calendar.getInstance();
			
			for(ApplicationTier t: toCheck){
				toReturn.put(t.getId(), new ArrayList<String>());
				ApplicationTierAtRuntime tier=getApplicationTierAtRuntime(t.getId());
				Map<String,Date> instancesStartTimes=tier.getInstancesStartTimes();
				
				
				cal.add(Calendar.MINUTE, -(55-timestepDuration));
				Date oneHourBack = cal.getTime();
	
				
				for(String instance: instancesStartTimes.keySet()){		
					if(instancesStartTimes.get(instance).after(oneHourBack)){
						toReturn.get(t.getId()).add(instance);
					}
				}
			}
			
			return toReturn;
		}
		
		

		
		public static void printObserved(String s){
			System.out.println(s);
		}
		
		
		
		
		
		
		
	

}
