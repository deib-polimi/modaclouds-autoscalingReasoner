package it.polimi.modaclouds.recedingHorizonScaling4Cloud.model;


import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.GenericXMLHelper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

public class ModelManager {
	
	
	private static ModelManager instance=null;
	private Containers model;
	
	private Map<String,TierTempRuntimeData> tempData;
	
	private ModelManager(){
		this.tempData=new HashMap<String, TierTempRuntimeData>();
	}
	
	public static ModelManager getInstance() {
		if (instance == null) {
			
			instance=new ModelManager();
			

		}
		return instance;
	}
	
	public Containers getModel(){
		if(this.model!=null){
			return this.model;
		}
		
		return null;
	}
	
	public void loadModel(String pathToSourceModel){
		
		ObjectFactory factory=new ObjectFactory();
		this.model=factory.createContainers();
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
	
	public void updateDemand(){
	}
	
	public void updateDelay(){
		
	}
	
	public void updateWorkloadPrediction(){
		
	}
	
	public void updateNumberOfVmInstances(){
		
	}

}
