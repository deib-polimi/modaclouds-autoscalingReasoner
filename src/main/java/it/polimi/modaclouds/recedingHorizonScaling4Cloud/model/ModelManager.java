package it.polimi.modaclouds.recedingHorizonScaling4Cloud.model;


import it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl.AdaptationController;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl.AdaptationInitializer;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.WSClient;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.TierNotFoudException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizationInputWriter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.GenericXMLHelper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;

public class ModelManager {
	private static final Logger journal = Logger
			.getLogger(WSClient.class.getName());
	
	private static Containers model;
	private static List<ApplicationTierAtRuntime> runtimeEnv;

	private static int optimizationWindow;
	private static int timestepDuration;

	
	private static Map<String,TierTempRuntimeData> tempMonitoringData;

	public void setOptimizationWindow(int horizon){
		optimizationWindow=horizon;
	}
	
	public static int getOptimizationWindow(){
		return optimizationWindow;
	}
	
	public static Containers getModel(){
		if(model!=null){
			return model;
		}
		
		return null;
	}
	
	public static void loadModel(String pathToSourceModel, String window, String timestep){
		
		journal.log(Level.INFO, "loading model");
		
		tempMonitoringData=new HashMap<String, TierTempRuntimeData>();

		
		ObjectFactory factory=new ObjectFactory();
		model=factory.createContainers();
		GenericXMLHelper xmlHelper= new GenericXMLHelper(pathToSourceModel);	
		runtimeEnv=new ArrayList<ApplicationTierAtRuntime>();

		optimizationWindow=Integer.parseInt(window);
		timestepDuration=Integer.parseInt(timestep);
		
		for(Element c: xmlHelper.getElements("container")){
			Container toAdd=factory.createContainer();
			toAdd.setCapacity(Float.parseFloat(c.getAttribute("capacity")));
			toAdd.setMaxReserved(Integer.parseInt(c.getAttribute("maxReserved")));
			toAdd.setOnDemandCost(Float.parseFloat(c.getAttribute("onDemandCost")));
			toAdd.setReservedCost(Float.parseFloat(c.getAttribute("reservedCost")));
			toAdd.setId(UUID.randomUUID().toString()+"_capacity="+toAdd.getCapacity());
						
			ApplicationTier tier;
			ApplicationTierAtRuntime runtimeTier;
			
			List<Element> tiers=xmlHelper.getElements(c, "applicationTier");
			int index=1;
			
			for(Element t: tiers){
				runtimeTier=new ApplicationTierAtRuntime();
				
				tier= factory.createApplicationTier();
				tier.setId(t.getAttribute("id"));
				tier.setInitialNumberOfVMs(Integer.parseInt(t.getAttribute("initialNumberOfVMs")));
				
				Functionality tempFunc;
				
				for(Element f: xmlHelper.getElements(t, "functionality")){
					tempFunc=new Functionality();
					tempFunc.setId(f.getAttribute("id"));
					
					tier.getFunctionality().add(tempFunc);
				}
				
				ResponseTimeThreshold tempThreshold;
				
				for(Element rtt:xmlHelper.getElements(t, "responseTimeThreshold")){
					tempThreshold=new ResponseTimeThreshold();
					
					tempThreshold.setHour(Integer.parseInt(rtt.getAttribute("hour")));
					tempThreshold.setValue(Float.parseFloat(rtt.getAttribute("value")));
					tier.getResponseTimeThreshold().add(tempThreshold);
				}
				
				toAdd.getApplicationTier().add(tier);
				
				tempMonitoringData.put(tier.getId(), new TierTempRuntimeData(tier.getFunctionality().size(), optimizationWindow));
				
				runtimeTier.setTierId(tier.getId());
				runtimeTier.setAlgorithmIndex(index);
				index++;
				runtimeEnv.add(runtimeTier);
			}
			
			
			model.getContainer().add(toAdd);
			

		}
		
	}
	
	public static void flushTemporaryMonitoringData(Container c){
			for(ApplicationTier t: c.getApplicationTier()){
				TierTempRuntimeData temp=tempMonitoringData.get(t.getId());
				temp.refreshBuffers(optimizationWindow);
			}
		
	}
	
	public static void printModel() {
		
		journal.log(Level.INFO, "actual model...");
		
		for(Container c: model.getContainer()){
			journal.log(Level.INFO, "Container:"+c.getId());
			for(ApplicationTier t: c.getApplicationTier()){
				ApplicationTierAtRuntime rt;
				try {
					rt = getApplicationTierAtRuntime(t.getId());
					journal.log(Level.INFO, "Tier: "+t.getId());
					journal.log(Level.INFO, "Actual instance pointed for scale out: "+rt.getInstanceToScale());
					journal.log(Level.INFO, "Tier index in the optimization problem: "+rt.getAlgorithmIndex());
					
					journal.log(Level.INFO, "running tier instances...");
					
					for(String instance: rt.getInstancesStartTimes().keySet()){
						journal.log(Level.INFO, instance+ "started at "+rt.getInstancesStartTimes().get(instance));
					}
				} catch (TierNotFoudException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			}
		}
	}
	
	private static void printTemporaryMonitoringInfo(){
		
		journal.log(Level.INFO, "printing monitoring data received so far");
		
		for(String tier: tempMonitoringData.keySet()){
			TierTempRuntimeData temp=tempMonitoringData.get(tier);

			journal.log(Level.INFO, "tier: "+tier);
			journal.log(Level.INFO, " number of functionalities hoste: "+temp.getnFunct());
			
			journal.log(Level.INFO, "received functionality demands:");
			
			for(Float f: temp.getDemands()){
				journal.log(Level.INFO, f.toString());
			}
			
			journal.log(Level.INFO, "actual tier level demand"+temp.getTierCurrentDemand());
			
			journal.log(Level.INFO, "received functionality workload forecasts");
			
			for(Integer i: temp.getFunctWorkloadForecast().keySet()){
				journal.log(Level.INFO, "forecasts "+i.toString()+" timestep in ahead");
				
				for(Float f: temp.getFunctWorkloadForecast().get(i)){
					journal.log(Level.INFO, f.toString());
				}
			}
			
			journal.log(Level.INFO, "actual tier level worklaod forecasts");
			float[] forecasts=temp.getTierCurrentWorkloadPredictions();
			for (int i=0; i<forecasts.length; i++){
				
				journal.log(Level.INFO, (i+1)+" timestep ahead: "+forecasts[i]);
			}
			
			
		}
	}
	
	public static void updateDemand(String monitoredResource, Float monitoredValue) {
		
		for(String key: tempMonitoringData.keySet() ){			
			
			if(key.equals(monitoredResource)){
				tempMonitoringData.get(key).addDemandValue(monitoredValue);	
				checkContainerReadyForOptimization(monitoredResource);

			}
		}
		
		
		
	}
	
	public static void updateWorkloadPrediction(String monitoredResource, String monitoredMetric, Float monitoredValue) {
		

		for(String key: tempMonitoringData.keySet() ){
			

			if(key.equals(monitoredResource)){
				
				for(int i=1; i<=optimizationWindow; i++){
					if(monitoredMetric.contains(Integer.toString(i))){						
						tempMonitoringData.get(key).addWorkloadForecastValue(monitoredValue, i);	
						checkContainerReadyForOptimization(monitoredResource);

					}
				}
			}		
			
			
		}
		
	}
	

	
	private static void checkContainerReadyForOptimization(String lastUpdatedTierId) {
		
		journal.log(Level.INFO, "checking container ready for optimization after new monitoring data received for tier: "+lastUpdatedTierId);
		
		printTemporaryMonitoringInfo();
		
		Container toCheck=getContainerByTierId(lastUpdatedTierId);
		
		
		boolean allReady=true;
		
		for(ApplicationTier t: toCheck.getApplicationTier()){
			if(!tempMonitoringData.get(t.getId()).getReady()){
				allReady=false;
			}
		}
		
		if(allReady){

			journal.log(Level.INFO, "container "+toCheck.getId()+ " ready for optimization");
			//aggrega le demand e i workload ricevuti per ogni tier contenuto nel container pronto per l'ottimizzazione
			//scrive i file mancanti chiamando statiINputWrite
			//lancia l'ottimizzazione per il container corente
			int index;
			for(ApplicationTier t: toCheck.getApplicationTier()){
				try {
					index=getApplicationTierAtRuntime(t.getId()).getAlgorithmIndex();
					t.setDemand(tempMonitoringData.get(t.getId()).getTierCurrentDemand());
					
					for(int i=1; i<=optimizationWindow; i++){
						WorkloadForecast tempForecast= new WorkloadForecast();
						tempForecast.setTimeStepAhead(i);
						tempForecast.setValue(tempMonitoringData.get(t.id).getTierCurrentWorkloadPredictions()[i-1]);
						t.getWorkloadForecast().add(tempForecast);
					}
					OptimizationInputWriter.writeFile("mu.dat", toCheck.getId(), "let mu["+index+"]:=\n"+(1/t.getDemand())+"\n;");
					OptimizationInputWriter.writeFile("Delay.dat", toCheck.getId(), "let D["+index+"]:=\n"+0+"\n;");

					
					for(WorkloadForecast wf:t.getWorkloadForecast()){
						OptimizationInputWriter.writeFile("workload_class"+index+".dat", toCheck.getId(), "let Lambda["+index+","+wf.getTimeStepAhead()+"]:=\n"+wf.getValue()+"\n;");
					}
					
					journal.log(Level.INFO, "demand delay and workload files written");

				} catch (TierNotFoudException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				

			}
			
			AdaptationController controller=new AdaptationController();
			controller.setToAdapt(toCheck);
			Thread toRun=new Thread(controller);
			toRun.start();

			
		}
	}
	
	public static void updateRuntimeEnv(JSONArray instances) throws JSONException, TierNotFoudException{
		ApplicationTierAtRuntime temp;
		System.out.println("updating runtime environment");

		for(int i=0; i<instances.length(); i++){
			JSONObject instance=instances.getJSONObject(i);
			System.out.println("checking instance "+instance.get("id")+" on tier "+instance.get("type").toString().substring(4, instance.get("type").toString().length()-1));
			temp=getApplicationTierAtRuntime(instance.get("type").toString().substring(4, instance.get("type").toString().length()-1));
			if(!containInstance(temp, instance.get("id").toString())){
				temp.addNewInstance(instance.get("id").toString());
				if(temp.getInstanceToScale()==null){
					temp.setInstanceToScale(instance.getString("id").toString());
				}
			}
		}
		
		printModel();
		
	}
	
	public ApplicationTier getTierById(String id){
		for(Container c: model.getContainer()){
			for(ApplicationTier t: c.getApplicationTier()){
				if(t.getId().equals(id)){
					return t;
				}
			}
		}
		
		return null;
	}
	
	public static Container getContainerByTierId(String tierId){
		for(Container c: model.getContainer()){
			for(ApplicationTier t: c.getApplicationTier()){
				if(t.getId().equals(tierId)){
					return c;
				}
			}
		}
		
		return null;
	}
	
	public Container getContainerById(String containerId){
		for(Container c:model.getContainer()){
			if(c.getId().equals(containerId)){
				return c;
			}
		}
		
		return null;
	}


	public List<ApplicationTierAtRuntime> getRuntimeEnv() {
		return runtimeEnv;
	}

	public static void setRuntimeEnv(List<ApplicationTierAtRuntime> runtimeEnv) {
		runtimeEnv = runtimeEnv;
	}
	
	public static  ApplicationTierAtRuntime getApplicationTierAtRuntime(String tierId) throws TierNotFoudException{
		for(ApplicationTierAtRuntime t: runtimeEnv){
			if(t.getTierId().equals(tierId) |tierId.startsWith(t.getTierId())){
				return t;
			}
		}
		
		throw new TierNotFoudException("in the runtime model there is no tier with the specified id");
	}
	
	public static Map<String,List<String>> getNextTimestepExpiringInstances(List<ApplicationTier> toCheck) {
		Map<String,List<String>> toReturn=new HashMap<String,List<String>>();
		Calendar cal = Calendar.getInstance();
		
		for(ApplicationTier t: toCheck){
			toReturn.put(t.getId(), new ArrayList<String>());
			ApplicationTierAtRuntime tier;
			try {
				tier = getApplicationTierAtRuntime(t.getId());
				Map<String,Date> instancesStartTimes=tier.getInstancesStartTimes();
				
				
				cal.add(Calendar.MINUTE, -(55-timestepDuration));
				Date oneHourBack = cal.getTime();

				
				for(String instance: instancesStartTimes.keySet()){		
					if(instancesStartTimes.get(instance).before(oneHourBack)){
						toReturn.get(t.getId()).add(instance);
					}
				}
			} catch (TierNotFoudException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return toReturn;
	}
	
	public static Map<String,List<String>> getNextTimestepAvailableInstances(List<ApplicationTier> toCheck) {
		Map<String,List<String>> toReturn=new HashMap<String,List<String>>();
		Calendar cal = Calendar.getInstance();
		
		for(ApplicationTier t: toCheck){
			toReturn.put(t.getId(), new ArrayList<String>());
			ApplicationTierAtRuntime tier;
			try {
				tier = getApplicationTierAtRuntime(t.getId());
				Map<String,Date> instancesStartTimes=tier.getInstancesStartTimes();
				cal.add(Calendar.MINUTE, -(55-timestepDuration));
				Date oneHourBack = cal.getTime();

				
				for(String instance: instancesStartTimes.keySet()){		
					if(instancesStartTimes.get(instance).after(oneHourBack)){
						toReturn.get(t.getId()).add(instance);
					}
				}
			} catch (TierNotFoudException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		
		return toReturn;
	}
	
	public static int getTimestepDuration() {
		return timestepDuration;
	}

	public static void setTimestepDuration(int timestepDuration) {
		ModelManager.timestepDuration = timestepDuration;
	}
	
	private static boolean containInstance(ApplicationTierAtRuntime toCheck, String toFind){
		
		for(String instance: toCheck.getInstancesStartTimes().keySet()){
			if(instance.equals(toFind)){
				return true;
			}
		}
		
		
		return false;
	}

}
