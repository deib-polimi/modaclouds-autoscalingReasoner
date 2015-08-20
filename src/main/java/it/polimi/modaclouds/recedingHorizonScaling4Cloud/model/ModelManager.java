package it.polimi.modaclouds.recedingHorizonScaling4Cloud.model;


import it.polimi.modaclouds.recedingHorizonScaling4Cloud.cloudMLConnector.CloudMLAdapter;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.CloudMLReturnedModelException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.ConfigurationFileException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.exceptions.TierNotFoudException;
import it.polimi.modaclouds.recedingHorizonScaling4Cloud.util.ConfigManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.research.ws.wadl.Application;

public class ModelManager {
	private static final Logger journal = Logger
			.getLogger(ModelManager.class.getName());
	private static Containers model;

	
	public static int getOptimizationWindow(){
		return model.optimizationWindowsLenght;
	}
	
	public static Containers getModel(){
		if(model!=null){
			return model;
		}
		
		return null;
	}
	
	public static void loadModel(){
		
		try {
			JAXBContext jc;

			jc = JAXBContext.newInstance(Containers.class);
	        Unmarshaller unmarshaller = jc.createUnmarshaller();
	        File xml = new File(ConfigManager.PATH_TO_DESIGN_TIME_MODEL);
	        model = (Containers) unmarshaller.unmarshal(xml);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		//setting the class idexes
		
		for(Container c: model.getContainer()){
			int index=1;
			for(ApplicationTier t: c.getApplicationTier()){
				t.setClassIndex(index);
				index++;
			}
		}
		
		CloudMLAdapter cloudml=new CloudMLAdapter();
		cloudml.getDeploymentModel();
		
	}
	
	public static void printCurrentModel() {
		
	}
		
	public static void updateServiceDemand(String monitoredResource, Double monitoredValue) {
		
		for(Container c: model.getContainer()){
			for(ApplicationTier t: c.getApplicationTier()){
				for(Functionality f: t.getFunctionality()){
					if(f.getId().equals(monitoredResource)){
						f.setDemand(monitoredValue);
					}
				}
			}
		}
	}
	
	public static void updateServiceWorkloadPrediction(String monitoredResource, String monitoredMetric, Double monitoredValue) {
		
		for(Container c: model.getContainer()){
			for(ApplicationTier t: c.getApplicationTier()){
				for(Functionality f: t.getFunctionality()){
					if(f.getId().equals(monitoredResource)){
						WorkloadForecast toUpdate=null;
						for(WorkloadForecast wf: f.getWorkloadForecast()){
							if(wf.getTimeStepAhead()==Integer.parseInt(monitoredMetric.substring(monitoredMetric.length() - 1))){
								toUpdate=wf;
							}
						}
						if(toUpdate!=null){
							toUpdate.setValue(monitoredValue);
						}else{
							toUpdate=new WorkloadForecast();
							toUpdate.setTimeStepAhead(Integer.parseInt(monitoredMetric.substring(monitoredMetric.length() - 1)));
							toUpdate.setValue(monitoredValue);
							f.getWorkloadForecast().add(toUpdate);
						}
					}
				}
			}
		}
		
		
	}
	
	public static String getNewestRunningInstance(String tierId){

		List<Date> startTimes= new ArrayList<Date>();
		Instance toReturn=null;
		
		ApplicationTier tier=getTier(tierId);

		for(Instance i: tier.instances){
			if(i.getStatus().equals("RUNNING")){
				startTimes.add(i.getStartTime().toGregorianCalendar().getTime());
			}
		}
		
		
		Calendar max= Calendar.getInstance();
		max.setTime(Collections.max(startTimes));
				
		Calendar temp=Calendar.getInstance();
		for(Instance i: tier.instances){
			temp.setTime(i.getStartTime().toGregorianCalendar().getTime());
			if(max.equals(temp)){
				toReturn=i;
			}
		}
		
		return toReturn.getId();

	}
	
	public static String getOldestRunningInstance(String tierId){
		List<Date> startTimes= new ArrayList<Date>();
		Instance toReturn=null;
		
		ApplicationTier tier=getTier(tierId);

		for(Instance i: tier.instances){
			if(i.getStatus().equals("RUNNING")){
				startTimes.add(i.getStartTime().toGregorianCalendar().getTime());
			}
		}
		
		
		Calendar min= Calendar.getInstance();
		min.setTime(Collections.max(startTimes));
				
		Calendar temp=Calendar.getInstance();
		for(Instance i: tier.instances){
			temp.setTime(i.getStartTime().toGregorianCalendar().getTime());
			if(min.equals(temp)){
				toReturn=i;
			}
		}
		
		return toReturn.getId();

	}

	public static void updateDeploymentInfo(JSONArray instances) throws JSONException, TierNotFoudException, CloudMLReturnedModelException{

		for(int i=0; i<instances.length(); i++){
			JSONObject instance=instances.getJSONObject(i);
			String instanceId=instance.get("id").toString();
			String instanceType=instance.getString("type").toString().substring(4, instance.get("type").toString().length()-1);
			String tierId=getHostedTier(instanceId).getId();
			String instanceStatus=instance.get("status").toString();
			
			if(instanceId!=null && instanceType != null && instanceStatus !=null){
				if(tierId!=null){
					if(instanceType.equals(tierId)){
						//CHECK IF THE STATUS OF THE INSTANCE AS REPORTED BY CLOUDML CORRESPONDS TO THE INTERNAL STORED STATUS
						if(!instanceStatus.equals(getInstance(instanceId).getStatus())){
							//RAISE ERROR SINCE INSTANCE STATUS AS REPORTED BY CLOUDML
							//DOES NOT CORRESPOND TO THE INTERNALLY STORED STATUS
							throw new CloudMLReturnedModelException("CloudML reported status does not correspond "
									+ "to the internally stored status for instance: "+instanceId);
						}
					}else{
						//RAISE ERROR SINCE THE VM INSTANCE TYPE HAS REPORTED FROM CLOUDML IS NOT EQUAL TO 
						//THE TIER ID TO WHICH THE INSTANCE BELONGS TO WITHIN THE INTERNAL MODEL
						throw new CloudMLReturnedModelException("CloudML reported instance type does not correspond "
								+ "to the internally stored instance type for instance: "+instanceId);
					}
				}else{
					//THE INSTANCE IS NOT ASSOCIATED TO ANY MANAGED APPLICATION TIER WITHIN THE INTERNAL MODEL AND NEEDS TO BE ADDED
					addInstance(instanceId, instanceType, instanceStatus);
				}
			}else{
				//RAISE ERROR SINCE A VM INSTANCE FROM CLOUDML RESPONSE HAS A NULL ID OR A NULL TYPE OR A NULL STATUS
				throw new CloudMLReturnedModelException("CloudML reported instance with a null type or status or Id for instance: "+instanceId);
			}
		}		
	}
	
	public static Instance getInstance(String instanceId){
		for(Container c: model.getContainer()){
			for(ApplicationTier t:c.getApplicationTier()){
				for(Instance i: t.getInstances()){
					if(i.getId().equals(instanceId)){
						return i;
					}
				}
			}
		}
		
		return null;
	}
	
	public static ApplicationTier getTier(String tierId){
		for(Container c: model.getContainer()){
			for(ApplicationTier t: c.getApplicationTier()){
				if(t.getId().equals(tierId)){
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
	
	public static List<String> getExpiringInstances(String tierId,  int lookAhead) {
		List<String> toReturn=new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
			
				ApplicationTier tier = getTier(tierId);
				
				cal.add(Calendar.MINUTE, -(60-model.getTimestepDuration()*lookAhead));

				
				for(Instance instance: tier.getInstances()){		
					//if the 'instance' is not stopped
					if(instance.getStatus().equals("RUNNING")){
						//and if the instance will be automatically recharged within the next 'lookAhead' timesteps
						if(instance.getStartTime().toGregorianCalendar().getTime().before(cal.getTime())){
							toReturn.add(instance.getId());
						}
					}
				}	
		
		return toReturn;
	}
	
	public static List<String> getAvailableInstances(String tierId, int lookAhead) {
		List<String> toReturn=new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
			
				ApplicationTier tier = getTier(tierId);
				
				cal.add(Calendar.MINUTE, -(60-model.getTimestepDuration()*lookAhead));

				
				for(Instance instance: tier.getInstances()){		
					//if the 'instance' is not stopped
					if(instance.getStatus().equals("RUNNING")){
						//and if the instance will be automatically recharged within the next 'lookAhead' timesteps
						if(instance.getStartTime().toGregorianCalendar().getTime().after(cal.getTime())){
							toReturn.add(instance.getId());
						}
					}
				}	
		
		return toReturn;
	}
	
	public static List<String> getRunningInstances(String tierId){
		List<String> toReturn=new ArrayList<String>();
		
		ApplicationTier tier = getTier(tierId);
		
		for(Instance instance: tier.getInstances()){		
			if(instance.getStatus().equals("RUNNING")){
					toReturn.add(instance.getId());
				
			}
		}	
		
		return toReturn;
	}
	
	public static List<String> getStoppedInstances(String tierId){
		List<String> toReturn=new ArrayList<String>();
		
		ApplicationTier tier = getTier(tierId);
		
		for(Instance instance: tier.getInstances()){		
			if(instance.getStatus().equals("STOPPED")){
					toReturn.add(instance.getId());
				
			}
		}	
		
		return toReturn;
	}
	
	public static int getTimestepDuration() {
		return model.timestepDuration;
	}

	
	private static boolean containInstance(String tierId, String instanceId){
		
		
		ApplicationTier tier = getTier(tierId);

		for(Instance instance: tier.getInstances()){
			if(instance.getId().equals(instanceId)){
				return true;
			}
		}
		
		
		return false;
	}
	
	public static void stopInstances(List<String> instances) throws TierNotFoudException{
		
		
		CloudMLAdapter cloudml=new CloudMLAdapter();
	 	String toSend="";

		for(String instanceId: instances){
			
	 		if(toSend.equals("")){
	 			toSend+=instanceId;
	 		}else{
	 			toSend+=","+instanceId;
	 		}
		}
		
		cloudml.stopInstances(toSend);
		for(String instanceId: instances){

			Instance stopped=getInstance(instanceId);
			stopped.setStatus("STOPPED");
			stopped.setStartTime(null);
			
			System.out.println("Instance "+instanceId+" successfully stopped");

		}
	}
	
	public static void startInstance(List<String> instances) throws TierNotFoudException{
		
		CloudMLAdapter cloudml=new CloudMLAdapter();
	 	String toSend="";

		for(String instanceId: instances){
			
	 		if(toSend.equals("")){
	 			toSend+=instanceId;
	 		}else{
	 			toSend+=","+instanceId;
	 		}
		}
		
		cloudml.startInstances(toSend);
		
		for(String instanceId: instances){

			Instance started=getInstance(instanceId);
			started.setStatus("RUNNING");
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(new Date());
			XMLGregorianCalendar date2;
			try {
				date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
				started.setStartTime(date2);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}		
			System.out.println("Instance "+instanceId+" successfully restarted");
		}
		

	}
	
	public static void renewRunningInstance(String toRenew){
		Instance instance=getInstance(toRenew);
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(new Date());
		XMLGregorianCalendar date2;
		try {
			date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			instance.setStartTime(date2);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}		
		System.out.println("Instance "+toRenew+" successfully restarted");
	}
	
	public static void addInstance(String instanceId, String tierId, String status) throws TierNotFoudException{
		Instance toAdd=new Instance();
		toAdd.setId(instanceId);
		toAdd.setStatus(status);
		toAdd.setUsedForScale(false);
		
		if(status.equals("RUNNING")){
			GregorianCalendar c = new GregorianCalendar();
			c.setTime(new Date());
			XMLGregorianCalendar date2;
			try {
				date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
				toAdd.setStartTime(date2);
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
		}else{
			toAdd.setStartTime(null);
		}
		
		
		getTier(tierId).getInstances().add(toAdd);
	}
	
	public static ApplicationTier getHostedTier(String instanceId){
		for(Container c: model.getContainer()){
			for(ApplicationTier t: c.getApplicationTier()){
				for(Instance i: t.getInstances()){
					if(i.getId().equals(instanceId)){
						return t;
					}
				}
			}
		}
		
		return null;
	}
	
	public static ApplicationTier getContainingTier(String functionalityId){	
		for(Container c: model.getContainer()){
			for(ApplicationTier t: c.getApplicationTier()){
				for(Functionality f: t.getFunctionality()){
					if(f.getId().equals(functionalityId)){
						return t;
					}
				}
			}
		}
		
		return null;
	}
	
	public static List<String> getExposedFunctionalities(String tierId){	
		List<String> toReturn = new ArrayList<String>();
		
		for(Functionality f: getTier(tierId).getFunctionality()){
			toReturn.add(f.getId());
		}
		
		return toReturn;
	}
	
	public static List<String> getHostingInstances(String tierId){	
		
		List<String> toReturn = new ArrayList<String>();
		
		for(Instance i: getTier(tierId).getInstances()){
			toReturn.add(i.getId());
		}
		
		return toReturn;	
	}
	
	public static double getDemand(String tierId){

		ApplicationTier tier=getTier(tierId);
		double toReturn=0;
		for(Functionality f:tier.getFunctionality()){
			if(f.getDemand()!= null){
				toReturn=toReturn+f.getDemand();
			}
		}
		tier.setDemand(toReturn);
		return toReturn;
	}
	
	public static double getWorkloadPrediction(String tierId, int lookAhead){
		ApplicationTier tier=getTier(tierId);
		double toReturn=0;
		for(Functionality f:tier.getFunctionality()){
			for(WorkloadForecast wf: f.getWorkloadForecast()){
				if(wf.getTimeStepAhead()==lookAhead){
					toReturn=toReturn+wf.getValue();
				}
			}
		}
		
		WorkloadForecast toUpdate=null;
		for(WorkloadForecast wf: tier.getWorkloadForecast()){
			if(wf.getTimeStepAhead()==lookAhead){
				toUpdate=wf;
			}
		}
		if(toUpdate!=null){
			toUpdate.setValue(toReturn);
		}else{
			toUpdate=new WorkloadForecast();
			toUpdate.setTimeStepAhead(lookAhead);
			toUpdate.setValue(toReturn);
			tier.getWorkloadForecast().add(toUpdate);
		}
		
		return toReturn;
	}
	
	public static String getInstanceToScale(String tierId){
		
		
		for(Instance i: getTier(tierId).getInstances()){
			if(i.isUsedForScale()){
				return i.getId();
			}
		}
		
		return null;
	}
	
	public static void setInstanceToScale(String instanceId){
		getInstance(instanceId).setUsedForScale(true);
	}
	
	public static void initializeUsedForScale(){

		for(Container c: model.getContainer()){
			for(ApplicationTier t: c.getApplicationTier()){
				String toSet=getNewestRunningInstance(t.getId());
				for(Instance i: t.getInstances()){
					if(i.getId().equals(toSet)){
						i.setUsedForScale(true);
					}else{
						i.setUsedForScale(false);
					}
				}
			}
		}
	}
	
	public static void initializeUsedForScale(String tierId){

		ApplicationTier t=getTier(tierId);
		String toSet=getNewestRunningInstance(t.getId());
		for(Instance i: t.getInstances()){
			if(i.getId().equals(toSet)){
				i.setUsedForScale(true);
			}else{
				i.setUsedForScale(false);
			}
		}
		
	}
}
