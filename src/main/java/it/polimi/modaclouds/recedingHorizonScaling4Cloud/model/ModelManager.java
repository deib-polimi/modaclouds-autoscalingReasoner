package it.polimi.modaclouds.recedingHorizonScaling4Cloud.model;


import it.polimi.modaclouds.recedingHorizonScaling4Cloud.staticInputProcessing.StaticInputWriter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.GenericXMLHelper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

public class ModelManager {
	
	
	private static ModelManager instance=null;
	private static Containers model;
	
	private static int optimizatiopnHorizon;
	
	private static Map<String,TierTempRuntimeData> tempMonitoringData;
	
	private ModelManager(){
		tempMonitoringData=new HashMap<String, TierTempRuntimeData>();
	}
	
	public void setOptimizationHorizon(int horizon){
		this.optimizatiopnHorizon=horizon;
	}
	
	public int getOptimizationHorizon(){
		return this.optimizatiopnHorizon;
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
				
				for(int i=1; i<=optimizatiopnHorizon; i++){
					if(monitoredMetric.contains(Integer.toString(i))){
						tempMonitoringData.get(key).addWorkloadForecastValuie(monitoredValue, i);	
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
	
	private static ApplicationTier getTierById(String id){
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
		//controlla dopo ogni update di un dato dinamico relativo ad un tier se il tier è pronto e se lo è se sono pronti 
		//anche tutti gli altri tier coinvolti nell stessa ottimizzazione
		//solo in caso positivo scrive i file mancanti per l'ottimizzazione (tutti in una volta) e lancia l'ottimizzazione
	}

}
