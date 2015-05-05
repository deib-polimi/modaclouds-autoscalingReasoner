package it.polimi.modaclouds.recedingHorizonScaling4Cloud.model;


import it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl.AdaptationController;
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
			.getLogger(ModelManager.class.getName());
	
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
				
				tempMonitoringData.put(tier.getId(), new TierTempRuntimeData(tier.getFunctionality(), optimizationWindow));
				
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
				temp.refreshBuffers(t.getFunctionality(),optimizationWindow);
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
						if(rt.getInstancesStartTimes().get(instance)!=null){
							journal.log(Level.INFO, instance+ "started at "+rt.getInstancesStartTimes().get(instance));
						}else{
							journal.log(Level.INFO, instance+ "actually stopped");
						}
					}
					journal.log(Level.INFO, "hosted functionalities");

					for(Functionality f: t.functionality){
						journal.log(Level.INFO, f.getId());
					}
				} catch (TierNotFoudException e) {
					e.printStackTrace();
				}
				

			}
		}
	}
	
	
	
	public static void updateDemand(String monitoredResource, Float monitoredValue, String functionality) {
		
		System.out.println("Updating demand for resource "+monitoredResource+" with value: "+monitoredValue
				+"relative to functionality "+ functionality);
		
		for(String key: tempMonitoringData.keySet() ){			
			if(key.equals(monitoredResource)){
				System.out.println("updating...");
				tempMonitoringData.get(key).addDemandValue(monitoredValue, functionality);	
			}
		}
		
		ModelManager.checkContainerReadyForOptimization("MIC");
		
		
		
	}
	
	public static void updateWorkloadPrediction(String monitoredResource, String monitoredMetric, Float monitoredValue, String functionality) {
		

		System.out.println("updating workload forecast for resource "+monitoredResource+" and for time step ahead "+monitoredMetric+" with value: "+monitoredValue
				+"relative to functionality "+ functionality);
		for(String key: tempMonitoringData.keySet() ){
			

			if(key.equals(monitoredResource)){
				
				for(int i=1; i<=optimizationWindow; i++){
					if(monitoredMetric.contains(Integer.toString(i))){	
						System.out.println("updating...");
						tempMonitoringData.get(key).addWorkloadForecastValue(monitoredValue, i, functionality);	
					}
				}
			}		
			
			
		}
		
		ModelManager.checkContainerReadyForOptimization("MIC");
		
	}
	
	public static String getLastInstanceCreated(String tierId) throws TierNotFoudException{
		String toReturn=null;
		Date maxDate=null;

		ApplicationTierAtRuntime tier=getApplicationTierAtRuntime(tierId);
		Map<String,Date> instances=tier.getInstancesStartTimes();
		
		for(String instance: instances.keySet()){
			if(maxDate==null && instances.get(instance)!=null){
				maxDate=instances.get(instance);
				toReturn=instance;
			}
			else{
				if(instances.get(instance)!=null){
					if(instances.get(instance).after(maxDate)){
						maxDate=instances.get(instance);
						toReturn=instance;
					}
				}
			}
			
		}
		
		return toReturn;
		
		
	
	}
	

	
	public static void checkContainerReadyForOptimization(String lastUpdatedTierId) {
		
		
		Container toCheck=getContainerByTierId(lastUpdatedTierId);
		
		System.out.println("checking container "+toCheck.getId()+" ready for optimization");
		
		boolean allReady=true;
		
		for(ApplicationTier t: toCheck.getApplicationTier()){
			if(!tempMonitoringData.get(t.getId()).getReady()){
				allReady=false;
			}
		}
		
		if(allReady){
			
			System.out.println("container is ready!");
			int index;
			for(ApplicationTier t: toCheck.getApplicationTier()){
				try {
					index=getApplicationTierAtRuntime(t.getId()).getAlgorithmIndex();
					t.setDemand(tempMonitoringData.get(t.getId()).getTierCurrentDemand());
					
					t.getWorkloadForecast().clear();
					
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
					

				} catch (TierNotFoudException e) {
					e.printStackTrace();
				}
				
				
				
				

			}
			flushTemporaryMonitoringData(toCheck);
			AdaptationController controller=new AdaptationController();
			controller.setToAdapt(toCheck);
			Thread toRun=new Thread(controller);
			toRun.start();

			
		}else{
			System.out.println("container not ready");
		}
	}
	
	public static void updateRuntimeEnv(JSONArray instances) throws JSONException, TierNotFoudException{
		ApplicationTierAtRuntime temp;
		System.out.println("updating runtime environment");

		for(int i=0; i<instances.length(); i++){
			JSONObject instance=instances.getJSONObject(i);
			if(instance.get("id")!=null){
				System.out.println("checking instance "+instance.get("id")+" on tier "+instance.get("type").toString().substring(4, instance.get("type").toString().length()-1));
				temp=getApplicationTierAtRuntime(instance.get("type").toString().substring(4, instance.get("type").toString().length()-1));
				if(!containInstance(temp, instance.get("id").toString())){
					temp.addNewInstance(instance.get("id").toString());
					
					if(temp.getInstanceToScale()==null){
					//if(instance.getString("id").toString().equals("eu-west-1/i-6c307c8b")){
						temp.setInstanceToScale(instance.getString("id").toString());
					}
				}
			}
		}		
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


	public static List<ApplicationTierAtRuntime> getRuntimeEnv() {
		return runtimeEnv;
	}

	public static void setRuntimeEnv(List<ApplicationTierAtRuntime> runtimeModel) {
		runtimeEnv = runtimeModel;
	}
	
	public static  ApplicationTierAtRuntime getApplicationTierAtRuntime(String tierId) throws TierNotFoudException{
		for(ApplicationTierAtRuntime t: runtimeEnv){
			if(t.getTierId().equals(tierId) |tierId.startsWith(t.getTierId())){
				return t;
			}
		}
		
		throw new TierNotFoudException("in the runtime model there is no tier with the specified id");
	}
	
	public static List<String> getExpiringInstances(ApplicationTier toCheck,  int lookAhead) {
		List<String> toReturn=new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		
		System.out.println("START LOOKING FOR EXPIRING INSTANCES");
		
			ApplicationTierAtRuntime tier;
			try {
				tier = getApplicationTierAtRuntime(toCheck.getId());
				Map<String,Date> instancesStartTimes=tier.getInstancesStartTimes();
				Date actual=cal.getTime();

				
				cal.add(Calendar.MINUTE, -(60-timestepDuration*lookAhead));
				Date oneHourBack = cal.getTime();

				
				for(String instance: instancesStartTimes.keySet()){		
					//if the 'instance' is not stopped
					if(instancesStartTimes.get(instance)!=null){
						//and if the instance will be automatically recharged within the next 'lookAhead' timesteps
						if(instancesStartTimes.get(instance).before(oneHourBack)){
							System.out.println("expiring instance found within "+lookAhead+" timesteps! id: "+instance+" instanceStartTime: "+instancesStartTimes.get(instance).toString()+""
									+ " one hour bofere it is: "+oneHourBack.toString()+"actual checking time: "+actual);
							toReturn.add(instance);
						}
					}
				}
			} catch (TierNotFoudException e) {
				e.printStackTrace();
			}
			
		
		
		return toReturn;
	}
	
	public static List<String> getAvailableInstances(ApplicationTier toCheck, int lookAhead) {
		List<String> toReturn=new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		
		System.out.println("START LOOKING FOR AVAILABLE INSTANCES");

		
			ApplicationTierAtRuntime tier;
			try {
				tier = getApplicationTierAtRuntime(toCheck.getId());
				Map<String,Date> instancesStartTimes=tier.getInstancesStartTimes();
				Date actual=cal.getTime();
				cal.add(Calendar.MINUTE, -(60-timestepDuration*lookAhead));
				Date oneHourBack = cal.getTime();

				
				for(String instance: instancesStartTimes.keySet()){	
					//if the 'instance' is not stopped
					if(instancesStartTimes.get(instance)!=null){
						//and if the instance doesn't need to be recharged within the next 'lookAhead' timesteps
						if(instancesStartTimes.get(instance).after(oneHourBack)){
							System.out.println("available instance found within "+lookAhead+" timesteps! id: "+instance+" instanceStartTime: "+instancesStartTimes.get(instance).toString()+""
									+ " one hour bofere it is: "+oneHourBack.toString()+"actual checking time: "+actual);
							toReturn.add(instance);
						}
					}
				}
			} catch (TierNotFoudException e) {
				e.printStackTrace();
			}
			
			
			
		
		
		return toReturn;
	}
	
	public static List<String> getStoppedInstances(ApplicationTier toCheck){
		List<String> toReturn=new ArrayList<String>();
		
		System.out.println("START LOOKING FOR STOPPED INSTANCES");

		
			ApplicationTierAtRuntime tier;
			try {
				tier = getApplicationTierAtRuntime(toCheck.getId());
				Map<String,Date> instancesStartTimes=tier.getInstancesStartTimes();

				
				for(String instance: instancesStartTimes.keySet()){	
					//if the 'instance' is stopped
					if(instancesStartTimes.get(instance)==null){
							System.out.println("stopped instance found: "+instance);
							toReturn.add(instance);
					}
				}
			} catch (TierNotFoudException e) {
				e.printStackTrace();
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
	
	public static void stopInstance(String instanceId, String tierId) throws TierNotFoudException{
			ApplicationTierAtRuntime toUpdate=getApplicationTierAtRuntime(tierId);
			toUpdate.deleteInstance(instanceId);
	}
	
	
	
	public static void addInstance(String instanceId, String tierId) throws TierNotFoudException{
		ApplicationTierAtRuntime toUpdate=getApplicationTierAtRuntime(tierId);
		toUpdate.addNewInstance(instanceId);
	}

	
	public static String getTierIdByInstanceId(String instanceId){
		
		for(ApplicationTierAtRuntime tier:runtimeEnv){
			for(String instance: tier.getInstancesStartTimes().keySet()){
				if(instance.equals(instanceId)){
					return tier.getTierId();
				}
			}
		}
		
		return null;
		
	}
}
