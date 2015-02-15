package it.polimi.modaclouds.recedingHorizonScaling4Cloud.model;


import java.nio.file.Path;

import org.w3c.dom.Element;

import util.GenericXMLHelper;

public class ModelManager {
	
	
	private static ModelManager instance=null;
	private Containers model;
	

	
	public static ModelManager getModel() {
		if (instance == null) {
			
			instance=new ModelManager();

		}
		return instance;
	}
	
	private void loadModel(String pathToSourceModel){
		
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
				}
				
				
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
