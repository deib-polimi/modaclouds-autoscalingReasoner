package it.polimi.modaclouds.recedingHorizonScaling4Cloud.model;


import it.polimi.modaclouds.recedingHorizonScaling4Cloud.adaptationControl.AdaptationController;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.optimizerFileProcessing.OptimizerInputWriter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.GenericXMLHelper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.w3c.dom.Element;

public class ModelManager {
	
	
	private static ModelManager instance=null;
	private static Containers model;
	
	private static int optimizationHorizon;
	
	private static Map<String,TierTempRuntimeData> tempMonitoringData;
	
	private ModelManager(){
		tempMonitoringData=new HashMap<String, TierTempRuntimeData>();
		
	}
	
	public void setOptimizationHorizon(int horizon){
		optimizationHorizon=horizon;
	}
	
	public int getOptimizationHorizon(){
		return optimizationHorizon;
	}
	
	public static ModelManager getInstance() {
		if (instance == null) {
			
			instance=new ModelManager();
			

		}
		return instance;
	}
	
	public Containers getModel(){
		if(model!=null){
			return model;
		}
		
		return null;
	}
	
	public void loadModel(String pathToSourceModel){
		
		ObjectFactory factory=new ObjectFactory();
		model=factory.createContainers();
		GenericXMLHelper xmlHelper= new GenericXMLHelper(pathToSourceModel);
		
		for(Element c: xmlHelper.getElements("container")){
			Container toAdd=factory.createContainer();
			toAdd.setCapacity(Float.parseFloat(c.getAttribute("capacity")));
			toAdd.setMaxReserved(Integer.parseInt(c.getAttribute("maxReserved")));
			toAdd.setOnDemandCost(Float.parseFloat(c.getAttribute("onDemandCost")));
			toAdd.setReservedCost(Float.parseFloat(c.getAttribute("reservedCost")));
			toAdd.setId(UUID.randomUUID().toString());
			
			ApplicationTier tempTier;
			
			for(Element t: xmlHelper.getElements(c, "applicationTier")){
				tempTier= factory.createApplicationTier();
				tempTier.setId(t.getAttribute("id"));
				tempTier.setInitialNumberOfVMs(Integer.parseInt(t.getAttribute("initialNumberOfVMs")));
				
				Functionality tempFunc;
				
				for(Element f: xmlHelper.getElements(t, "functionality")){
					tempFunc=new Functionality();
					tempFunc.setId(f.getAttribute("id"));
					
					tempTier.getFunctionality().add(tempFunc);
				}
				
				ResponseTimeThreshold tempThreshold;
				
				for(Element rtt:xmlHelper.getElements(t, "responseTimeThreshold")){
					tempThreshold=new ResponseTimeThreshold();
					
					tempThreshold.setHour(Integer.parseInt(rtt.getAttribute("hour")));
					tempThreshold.setValue(Float.parseFloat(rtt.getAttribute("value")));
					tempTier.getResponseTimeThreshold().add(tempThreshold);
				}
				
				toAdd.getApplicationTier().add(tempTier);
				
				tempMonitoringData.put(tempTier.getId(), new TierTempRuntimeData(tempTier.getFunctionality().size(), optimizationHorizon));
			}
			
			
			this.model.getContainer().add(toAdd);
			

		}
	}
	
	public static void updateDemand(String monitoredResource, Float monitoredValue){
		
		for(String key: tempMonitoringData.keySet() ){			
			
			if(key.equals(monitoredResource)){
				tempMonitoringData.get(key).addDemandValue(monitoredValue);	
				checkContainerReadyForOptimization(monitoredResource);

			}
		}
		
		
		
	}
	
	public static void updateWorkloadPrediction(String monitoredResource, String monitoredMetric, Float monitoredValue){
		

		for(String key: tempMonitoringData.keySet() ){
			

			if(key.equals(monitoredResource)){
				
				for(int i=1; i<=optimizationHorizon; i++){
					if(monitoredMetric.contains(Integer.toString(i))){
						tempMonitoringData.get(key).addWorkloadForecastValue(monitoredValue, i);	
						checkContainerReadyForOptimization(monitoredResource);

					}
				}
			}		
			
			
		}
		
	}
	
	
	public static void updateDelay(){
		
	}
	
	public static void updateNumberOfVmInstances(){
		
	}
	
	public static ApplicationTier getTierById(String id){
		for(Container c: model.getContainer()){
			for(ApplicationTier t: c.getApplicationTier()){
				if(t.getId().equals(id)){
					return t;
				}
			}
		}
		
		return null;
	}
	
	private static void checkContainerReadyForOptimization(String lastUpdatedTierId){
		
		
		Container toCheck=getContainerByTierId(lastUpdatedTierId);
		
		
		boolean allReady=true;
		
		for(ApplicationTier t: toCheck.getApplicationTier()){
			if(!tempMonitoringData.get(t.getId()).getReady()){
				allReady=false;
			}
		}
		
		if(allReady){
			//aggrega le demand e i workload ricevuti per ogni tier contenuto nel container pronto per l'ottimizzazione
			//scrive i file mancanti chiamando statiINputWrite
			//lancia l'ottimizzazione per il container corente
			
			int cont=1;
			for(ApplicationTier t: toCheck.getApplicationTier()){
				t.setDemand(tempMonitoringData.get(t.getId()).getTierCurrentDemand());
				
				for(int i=1; i<=optimizationHorizon; i++){
					WorkloadForecast tempForecast= new WorkloadForecast();
					tempForecast.setTimeStepAhead(i);
					tempForecast.setValue(tempMonitoringData.get(t.id).getTierCurrentWorkloadPredictions()[i-1]);
					t.getWorkloadForecast().add(tempForecast);
				}
				OptimizerInputWriter.writeFile("mu.dat", toCheck.getId(), "let mu["+cont+"]:=\n"+(1/t.getDemand())+"\n;");
				OptimizerInputWriter.writeFile("Delay.dat", toCheck.getId(), "let D["+cont+"]:=\n"+0+"\n;");

				
				for(WorkloadForecast wf:t.getWorkloadForecast()){
					OptimizerInputWriter.writeFile("workload_class"+cont+".dat", toCheck.getId(), "let Lambda["+cont+","+wf.getTimeStepAhead()+"]:=\n"+wf.getValue()+"\n;");
				}
				
				//necessary to write also the initialVM.dat file
				
				cont++;
			}
			
			AdaptationController.applyAdaptation(toCheck);
		}
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
	
	public static Container getContainerById(String containerId){
		for(Container c:model.getContainer()){
			if(c.getId().equals(containerId)){
				return c;
			}
		}
		
		return null;
	}

}
